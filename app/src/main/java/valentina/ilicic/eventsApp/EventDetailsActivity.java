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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ImageView imgEvent = findViewById(R.id.imageEvents);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtCategory = findViewById(R.id.txtCategory);
        TextView txtLocation = findViewById(R.id.txtLocation);
        TextView txtDateTime = findViewById(R.id.txtDateTime);
        TextView txtDesc = findViewById(R.id.txtDesc);
        TextView txtCapacity = findViewById(R.id.txtCapacity);
        TextView txtRating = findViewById(R.id.txtRating);
        Button btnInterested = findViewById(R.id.btnInterested);
        Button btnAttending = findViewById(R.id.btnAttending);

        eventName = getIntent().getStringExtra("event_name");
        event = AppData.findByName(eventName);

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
        if (event == null) return;

        int id = view.getId();

        if (id == R.id.btnInterested) {
            if (!AppData.interestedEvents.contains(event)) {
                AppData.interestedEvents.add(event);
                Toast.makeText(this, R.string.added_to_interested, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.msg_vec_dodat, Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.btnAttending) {
            if (!AppData.attendingEvents.contains(event)) {
                AppData.attendingEvents.add(event);
                Toast.makeText(this, R.string.signed_up, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.msg_vec_attending, Toast.LENGTH_SHORT).show();
            }
        }
    }
}