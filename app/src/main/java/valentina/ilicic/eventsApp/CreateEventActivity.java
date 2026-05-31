package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName, editDescription, editLocation, editDateTime, editCapacity;
    private ImageButton btnCalendarIcon;
    private Spinner spinnerCategory;
    private CheckBox cbPromoted;
    private Button btnCreateEvent;
    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private boolean isPastDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editLocation = findViewById(R.id.editLocation);
        editDateTime = findViewById(R.id.editDateTime);
        editCapacity = findViewById(R.id.editCapacity);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        cbPromoted = findViewById(R.id.cbPromoted);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);
        btnCalendarIcon = findViewById(R.id.btnCalendarIcon);

        btnCreateEvent.setOnClickListener(this);
        cbPromoted.setOnClickListener(this);
        btnCalendarIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cbPromoted) {
            if (cbPromoted.isChecked()) {
                editCapacity.setVisibility(View.VISIBLE);
            } else {
                editCapacity.setVisibility(View.GONE);
                editCapacity.setText("");
            }
        }

        if (id == R.id.btnCalendarIcon) {
            showDateTimePicker();
        }

        if (id == R.id.btnCreateEvent) {
            String name = editName.getText().toString().trim();
            String desc = editDescription.getText().toString().trim();
            String loc = editLocation.getText().toString().trim();
            String dt = editDateTime.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            boolean isPromoted = cbPromoted.isChecked();
            int capacity = 0;

            if (name.isEmpty() || loc.isEmpty() || dt.isEmpty()) {
                Toast.makeText(this, R.string.popunite_polja, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDateTime(dt)) {
                if (isPastDate) {
                    Toast.makeText(this, R.string.msg_prosao_datum, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.msg_format_datuma, Toast.LENGTH_LONG).show();
                }
                return;
            }

            if (isPromoted) {
                String cap = editCapacity.getText().toString().trim();
                if (cap.isEmpty()) {
                    Toast.makeText(this, R.string.msg_ispravan_kapacitet, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    capacity = Integer.parseInt(cap);
                    if (capacity <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, R.string.msg_ispravan_kapacitet, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            final int finalCapacity = capacity;
            int promotedValue = isPromoted ? 1 : 0;
            JSONObject eventJSON = new JSONObject();
            try {
                eventJSON.put("name", name);
                eventJSON.put("description", desc);
                eventJSON.put("location", loc);
                eventJSON.put("eventTime", dt);
                eventJSON.put("category", category);
                eventJSON.put("promoted", promotedValue);
                eventJSON.put("capacity", capacity);
            } catch (JSONException e) { e.printStackTrace(); }

            new Thread(() -> {
                try {
                    HTTPHelper helper = new HTTPHelper();
                    JSONObject response = helper.postJSONObjectFromURL(HTTPHelper.BASE_URL + "/events", eventJSON);

                    if (response != null && response.has("_id")) {
                        String mongoEventId = response.getString("_id");

                        if (repo.insertEvent(mongoEventId, name, desc, loc, dt, category, promotedValue, finalCapacity)) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, R.string.msg_kreiran_dogadjaj, Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(this, R.string.msg_dberror, Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, R.string.server, Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, R.string.err_server, Toast.LENGTH_SHORT).show());
                }
            }).start();
        }
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, selectedHour, selectedMinute) -> {

                String formattedDateTime = String.format(Locale.getDefault(), "%02d.%02d.%d %02d:%02d",
                        selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute);

                editDateTime.setText(formattedDateTime);

            }, hour, minute, true);

            timePickerDialog.show();
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private boolean isValidDateTime(String dateTime) {

        String regex = "^\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.? \\d{1,2}:\\d{2}$";
        String trimmed = dateTime.trim();

        if (!trimmed.matches(regex)) {
            return false;
        }

        if (trimmed.contains(". ")) {
            trimmed = trimmed.replace(". ", " ");
        }

        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        date.setLenient(false);

        try {
            Date inputDate = date.parse(trimmed);
            Date now = new Date();

            if (inputDate.before(now)) {
                isPastDate = true;
                return false;
            }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}