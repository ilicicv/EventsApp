package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listUpcoming, listPast;
    private TextView empty;
    private LinearLayout layoutUpcoming, layoutPast;
    private EventAdapter adapterUpcoming, adapterPast;

    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private String currentUsername = "";
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        currentUsername = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("username", "");
        userId = repo.getUserIdByUsername(currentUsername);

        empty = findViewById(R.id.txtNoAttendingEvents);
        listUpcoming = findViewById(R.id.listUpcoming);
        listPast = findViewById(R.id.listPast);
        layoutUpcoming = findViewById(R.id.layoutUpcoming);
        layoutPast = findViewById(R.id.layoutPast);

        adapterUpcoming = new EventAdapter(this, R.layout.list_row_attending, currentUsername);
        adapterPast = new EventAdapter(this, R.layout.list_row_attending, currentUsername);

        listUpcoming.setAdapter(adapterUpcoming);
        listPast.setAdapter(adapterPast);

        listUpcoming.setOnItemClickListener(this);
        listUpcoming.setOnItemLongClickListener(this);
        listPast.setOnItemClickListener(this);

        loadData();
    }

    private void loadData() {
        if (userId == -1) return;

        ArrayList<Event> allEvents = repo.getAttendingEvents(userId);

        if (allEvents == null || allEvents.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            layoutUpcoming.setVisibility(View.GONE);
            layoutPast.setVisibility(View.GONE);

            adapterUpcoming.setEvents(new ArrayList<>());
            adapterPast.setEvents(new ArrayList<>());
        } else {
            empty.setVisibility(View.GONE);

            ArrayList<Event> upcoming = new ArrayList<>();
            ArrayList<Event> past = new ArrayList<>();

            for (Event e : allEvents) {
                if (e.isPast()) {
                    past.add(e);
                } else {
                    upcoming.add(e);
                }
            }

            if (!upcoming.isEmpty()) {
                layoutUpcoming.setVisibility(View.VISIBLE);
                adapterUpcoming.setEvents(upcoming);
            } else {
                layoutUpcoming.setVisibility(View.GONE);
                adapterUpcoming.setEvents(new ArrayList<>());
            }

            if (!past.isEmpty()) {
                layoutPast.setVisibility(View.VISIBLE);
                adapterPast.setEvents(past);
            } else {
                layoutPast.setVisibility(View.GONE);
                adapterPast.setEvents(new ArrayList<>());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapterView.getItemAtPosition(i);
        if (event != null) {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("event_name", event.getName());
            intent.putExtra("username", currentUsername);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapterView.getItemAtPosition(i);

        if (event != null && userId != -1) {
            boolean deleted = repo.removeAttending(userId, event.getName());

            if (deleted) {
                loadData();
                Toast.makeText(this, R.string.msg_otkazano, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.msg_dberror, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }


    protected  void onResume(){
        super.onResume();
        loadData();
    }
}