package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnEvents, btnMyEvents, btnFriends;
    TextView textUsername;
    EventsFragment eventsFragm;
    MyEventsFragment myEventsFragm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        btnEvents = findViewById(R.id.btnEvents);
        btnMyEvents = findViewById(R.id.btnMyEvents);
        btnFriends = findViewById(R.id.btnFriends);

        textUsername = findViewById(R.id.textUsername);

        eventsFragm = new EventsFragment();
        myEventsFragm = new MyEventsFragment();

        Bundle user = getIntent().getExtras();
        if(user != null){
            String username = user.getString("username");
            textUsername.setText(username);
        }

        btnEvents.setOnClickListener(this);
        btnMyEvents.setOnClickListener(this);
        btnFriends.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentLayout, eventsFragm)
                .commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnMyEvents) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, myEventsFragm)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.btnEvents) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, eventsFragm)
                    .addToBackStack(null)
                    .commit();
        }
    }
}