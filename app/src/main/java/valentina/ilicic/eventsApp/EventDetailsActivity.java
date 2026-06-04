package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String eventName;
    private Event event;
    private TextView txtCapacity;
    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private int userId = -1;
    private int eventId = -1;
    private String mongoUserId;
    String currentUser = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        SharedPreferences prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        currentUser = prefs.getString("username", "");
        mongoUserId = getSharedPreferences("UserData", MODE_PRIVATE).getString("mongoUserId", "");
        userId = repo.getLocalUserId(currentUser);

        eventName = getIntent().getStringExtra("event_name");
        event = repo.getEventByName(eventName);
        eventId = repo.getLocalEventId(eventName);

        ImageView imgEvent = findViewById(R.id.imageEvents);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView txtDateTime = findViewById(R.id.txtDateTime);
        TextView txtDesc = findViewById(R.id.txtDesc);
        TextView txtRating = findViewById(R.id.txtRating);
        txtCapacity = findViewById(R.id.txtCapacity);

        Button btnInterested = findViewById(R.id.btnInterested);
        Button btnAttending = findViewById(R.id.btnAttending);

        if (event != null) {
            txtName.setText(event.getName());
            txtCategory.setText(event.getCategory());
            txtLocation.setText(event.getLocation());
            txtDateTime.setText(event.getDateTime());
            txtDesc.setText(event.getDescription());
            imgEvent.setImageResource(event.getImageResId());

            if (event.isPromoted()) {
                txtCapacity.setVisibility(View.VISIBLE);
                int freeSeats = event.getCapacity() - event.getAttendingCount();
                txtCapacity.setText(getString(R.string.slobodnih_mesta_x_y, freeSeats, event.getCapacity()));
            } else {
                txtCapacity.setVisibility(View.GONE);
            }

            int count = event.getRatingCount();
            double average = event.getAverageRating();

            if (count > 0) {
                txtRating.setText(getString(R.string.prose_na_ocena_0_0_0_ocena, average, count));
            } else {
                txtRating.setText(getString(R.string.jo_uvek_nema_ocena));
            }
        }

        btnInterested.setOnClickListener(this);
        btnAttending.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (event == null || userId == -1 || eventId == -1) {
            return;
        }

        if (event.isPast()) {
            Toast.makeText(this, R.string.msg_prosao_dogadjaj, Toast.LENGTH_SHORT).show();
            return;
        }

        String currentStatus = repo.getAttendanceStatus(userId, eventId);

        if (id == R.id.btnInterested) {
            if ("PRISUSTVUJE".equals(currentStatus)) {
                Toast.makeText(this, R.string.msg_vec_prijavljen, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        new Thread(() -> {
            if (isFinishing() || isDestroyed()) return;
            String currentMongoEventId = repo.getMongoEventId(eventName);

            if (currentMongoEventId == null || mongoUserId == null) {
                runOnUiThread(() -> Toast.makeText(this, R.string.err_server, Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("userId", mongoUserId);
                json.put("eventId", currentMongoEventId);

                HTTPHelper helper = new HTTPHelper();
                if (id == R.id.btnInterested) {
                    json.put("commitment", "ZAINTERESOVAN");
                    JSONObject response = helper.postJSONObjectFromURL(HTTPHelper.BASE_URL + "/attendance", json);
                    if (response != null) {
                        runOnUiThread(() -> {
                            int resInterested = repo.setInterested(userId, eventId);
                            if (resInterested == 1) {
                                Toast.makeText(this, R.string.added_to_interested, Toast.LENGTH_SHORT).show();
                            } else if (resInterested == 0) {
                                Toast.makeText(this, R.string.msg_vec_dodat, Toast.LENGTH_SHORT).show();
                            } else if (resInterested == -1) {
                                Toast.makeText(this, R.string.msg_vec_prijavljen, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else if (id == R.id.btnAttending) {
                    json.put("commitment", "PRISUSTVUJE");
                    JSONObject response = helper.postJSONObjectFromURL(HTTPHelper.BASE_URL + "/attendance", json);
                    if (response != null) {
                        runOnUiThread(() -> {
                            int resAttending = repo.setAttending(userId, eventId, event.isPromoted(), event.getCapacity(), event.getAttendingCount());
                            if (resAttending == 1) {
                                Toast.makeText(this, R.string.signed_up, Toast.LENGTH_SHORT).show();
                                if (event.isPromoted()) {
                                    event.setAttendingCount(event.getAttendingCount() + 1);
                                    int newFreeSeats = event.getCapacity() - event.getAttendingCount();
                                    txtCapacity.setText(getString(R.string.slobodnih_mesta_x_y, newFreeSeats, event.getCapacity()));
                                }
                            } else if (resAttending == 0) {
                                Toast.makeText(this, R.string.msg_vec_prijavljen, Toast.LENGTH_SHORT).show();
                            } else if (resAttending == -1) {
                                Toast.makeText(this, R.string.msg_nema_slobodnih, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}