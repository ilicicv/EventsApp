package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editName, editDescription, editLocation, editDateTime, editCapacity;
    private Spinner spinnerCategory;
    private CheckBox cbPromoted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editLocation = findViewById(R.id.editLocation);
        editDateTime = findViewById(R.id.editDateTime);
        editCapacity = findViewById(R.id.editCapacity);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        cbPromoted = findViewById(R.id.cbPromoted);
        Button btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(this);
        cbPromoted.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cbPromoted) {
            if (cbPromoted.isChecked()) {
                editCapacity.setVisibility(View.VISIBLE);
            } else {
                editCapacity.setVisibility(View.GONE);
                editCapacity.setText("");
            }
        }

        if (view.getId() == R.id.btnCreateEvent) {
            String name = editName.getText().toString().trim();
            String desc = editDescription.getText().toString().trim();
            String loc = editLocation.getText().toString().trim();
            String dt = editDateTime.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            boolean isPromoted = cbPromoted.isChecked();
            int imageResId = getIconByCategory(category);
            int capacity = 0;

            if (name.isEmpty() || loc.isEmpty() || dt.isEmpty()) {
                Toast.makeText(this, R.string.popunite_polja, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidDateTime(dt)) {
                Toast.makeText(this, R.string.msg_format_datuma, Toast.LENGTH_LONG).show();
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
                    if (capacity <= 0) {
                        Toast.makeText(this, R.string.msg_ispravan_kapacitet, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, R.string.msg_ispravan_kapacitet, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Event newEvent;
            if (isPromoted) {
                newEvent = EventFactory.createPromotedEvent(name, desc, loc, dt, category, imageResId, capacity);
            } else {
                newEvent = EventFactory.createRegularEvent(name, desc, loc, dt, category, imageResId);
            }

            AppData.allEvents.add(newEvent);
            Toast.makeText(CreateEventActivity.this, R.string.msg_kreiran_dogadjaj, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private int getIconByCategory(String category) {
        switch (category) {
            case "Party":
                return android.R.drawable.ic_menu_camera;
            case "Stand-Up & Theater":
                return android.R.drawable.ic_menu_info_details;
            case "Exhibition":
                return android.R.drawable.ic_menu_gallery;
            case "Festival":
                return android.R.drawable.ic_menu_slideshow;
            default:
                return android.R.drawable.ic_media_play;
        }
    }

    private boolean isValidDateTime(String dateTimeStr) {
        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm");
        date.setLenient(false);
        try {
            date.parse(dateTimeStr.trim());
        } catch (java.text.ParseException e) {
            return false;
        }
        return true;
    }
}