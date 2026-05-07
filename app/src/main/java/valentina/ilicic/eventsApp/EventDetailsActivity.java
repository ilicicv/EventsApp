package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgEvent;
    private TextView txtName, txtCategory, txtLocation, txtDateTime, txtDesc, txtCapacity, txtRating;
    private Button btnInterested, btnAttending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        imgEvent = findViewById(R.id.imageEvents);
        txtName = findViewById(R.id.txtName);
        txtCategory = findViewById(R.id.txtCategory);
        txtLocation = findViewById(R.id.txtLocation);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtDesc = findViewById(R.id.txtDesc);
        txtCapacity = findViewById(R.id.txtCapacity);
        txtRating = findViewById(R.id.txtRating);
        btnInterested = findViewById(R.id.btnInterested);
        btnAttending = findViewById(R.id.btnAttending);

        String eventName = getIntent().getStringExtra("event_name");
        Event event = AppData.findByName(eventName);

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
        if (view.getId() == R.id.btnInterested) {
            Toast.makeText(this, "Dodat u zainteresovane.", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btnAttending) {
            Toast.makeText(this, "Prijavili ste se na događaj.", Toast.LENGTH_SHORT).show();
        }
    }
}