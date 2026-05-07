package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listUpcoming, listPast;
    private TextView empty;
    private LinearLayout layoutUpcoming, layoutPast;
    private EventAdapter adapterUpcoming, adapterPast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        empty = findViewById(R.id.txtNoAttendingEvents);
        listUpcoming = findViewById(R.id.listUpcoming);
        listPast = findViewById(R.id.listPast);
        layoutUpcoming = findViewById(R.id.layoutUpcoming);
        layoutPast = findViewById(R.id.layoutPast);

        ArrayList<Event> allEvents = AppData.attendingEvents;

        if (allEvents == null || allEvents.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            layoutUpcoming.setVisibility(View.GONE);
            layoutPast.setVisibility(View.GONE);
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
                adapterUpcoming = new EventAdapter(this, R.layout.list_row_attending);
                adapterUpcoming.setEvents(upcoming);
                listUpcoming.setAdapter(adapterUpcoming);
                listUpcoming.setOnItemClickListener(this);
            } else {
                layoutUpcoming.setVisibility(View.GONE);
            }

            if (!past.isEmpty()) {
                layoutPast.setVisibility(View.VISIBLE);
                adapterPast = new EventAdapter(this, R.layout.list_row_attending);
                adapterPast.setEvents(past);
                listPast.setAdapter(adapterPast);
                listPast.setOnItemClickListener(this);
            } else {
                layoutPast.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapterView.getItemAtPosition(i);
        if (event != null) {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("event_name", event.getName());
            startActivity(intent);
        }
    }
}