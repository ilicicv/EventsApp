package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener {

    private String eventName;
    private int selected = 0;
    private ImageButton[] stars = new ImageButton[5];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        TextView txtEventName = findViewById(R.id.txtName);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        eventName = getIntent().getStringExtra("event_name");
        if (eventName != null) {
            txtEventName.setText(eventName);
        }

        for (ImageButton s: stars) {
            s.setOnClickListener(this);
        }

        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnConfirm) {
            if (selected == 0) {
                Toast.makeText(RatingActivity.this, R.string.error_rating, Toast.LENGTH_SHORT).show();
            } else {
                for (Event e : AppData.allEvents) {
                    if (e.getName().equals(eventName)) {
                        e.addRating(selected);

                        if (!AppData.ratedEvents.contains(eventName)) {
                            AppData.ratedEvents.add(eventName);
                        }

                        break;
                    }
                }
                Toast.makeText(RatingActivity.this, R.string.rating_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            for (int i = 0; i < stars.length; i++) {
                if (id == stars[i].getId()) {
                    updateRating(i + 1);
                    break;
                }
            }
        }
    }

    private void updateRating(int rating) {
        selected = rating;
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                stars[i].setImageResource(android.R.drawable.btn_star);
            }
        }
    }
}