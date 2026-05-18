package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String eventName;
    private Event event;
    private TextView txtCapacity;

    private DatabaseHelper dbHelper;
    private String currentUser = "";
    private int userId = -1;
    private int eventId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        dbHelper = new DatabaseHelper(this);

        Bundle loginData = getIntent().getExtras();
        if (loginData != null) {
            currentUser = loginData.getString("username", "");
        }

        eventName = getIntent().getStringExtra("event_name");
        userId = dbHelper.getUserIdByUsername(currentUser);
        event = dbHelper.getEventByName(eventName);
        eventId = dbHelper.getEventId(eventName);

        ImageView imgEvent = findViewById(R.id.imageEvents);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView txtDateTime = findViewById(R.id.txtDateTime);
        TextView txtDesc = findViewById(R.id.txtDesc);
        txtCapacity = findViewById(R.id.txtCapacity);
        TextView txtRating = findViewById(R.id.txtRating);
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
        if (event == null || userId == -1 || eventId == -1) return;

//        if (event.isPast()) {
//            Toast.makeText(this, R.string.msg_prosao_dogadjaj, Toast.LENGTH_SHORT).show();
//            return;
//        }
        int id = view.getId();

        if (id == R.id.btnInterested) {
            int resInterested = dbHelper.setInterested(userId, eventId);

            switch (resInterested) {
                case 1:
                    Toast.makeText(this, R.string.added_to_interested, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(this, R.string.msg_vec_dodat, Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(this, R.string.msg_vec_prijavljen, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, R.string.msg_dberror, Toast.LENGTH_SHORT).show();
                    break;
            }

        } else if (id == R.id.btnAttending) {
            int resAttending = dbHelper.setAttending(userId, eventId, event.isPromoted(), event.getCapacity(), event.getAttendingCount());

            switch (resAttending) {
                case 1:
                    Toast.makeText(this, R.string.signed_up, Toast.LENGTH_SHORT).show();

                    if (event.isPromoted()) {
                        event.setAttendingCount(event.getAttendingCount() + 1);
                        int newFreeSeats = event.getCapacity() - event.getAttendingCount();
                        txtCapacity.setText(getString(R.string.slobodnih_mesta_x_y, newFreeSeats, event.getCapacity()));
                    }
                    break;
                case 0:
                    Toast.makeText(this, R.string.msg_vec_prijavljen, Toast.LENGTH_SHORT).show();
                    break;
                case -1:
                    Toast.makeText(this, R.string.msg_nema_slobodnih, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, R.string.msg_dberror, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}