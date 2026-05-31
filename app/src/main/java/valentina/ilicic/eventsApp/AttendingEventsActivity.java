package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class AttendingEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listUpcoming, listPast;
    private TextView empty;
    private LinearLayout layoutUpcoming, layoutPast;
    private EventAdapter adapterUpcoming, adapterPast;

    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private String currentUsername = "";
    private String mongoUserId = "";
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "");
        mongoUserId = prefs.getString("mongoUserId", "");
        userId = repo.getLocalUserId(currentUsername);

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
        listPast.setOnItemClickListener(this);

        loadData();
    }

    private void loadData() {
        if (mongoUserId == null || mongoUserId.isEmpty()) return;

        new Thread(() -> {
            ArrayList<Event> upcomingList = new ArrayList<>();
            ArrayList<Event> pastList = new ArrayList<>();

            try {
                HTTPHelper helper = new HTTPHelper();
                String url = HTTPHelper.BASE_URL + "/attendance/" + mongoUserId;
                JSONArray array = helper.getJSONArrayFromURL(url);

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String commitment = obj.optString("commitment", "");
                        JSONObject e = obj.optJSONObject("event");

                        if (e != null && "PRISUSTVUJE".equals(commitment)) {
                            Event event = EventFactory.createRegularEvent(
                                    e.optString("name", "Nepoznato"),
                                    e.optString("description", ""),
                                    e.optString("location", "Nema lokacije"),
                                    e.optString("eventTime", ""),
                                    e.optString("category", ""),
                                    e.optInt("numberOfAttendees", 0),
                                    e.optDouble("avgRating", 0.0),
                                    e.optInt("numberOfRatings", 0)
                            );

                            if (event.isPast()) {
                                pastList.add(event);
                            } else {
                                upcomingList.add(event);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> updateUI(upcomingList, pastList));
        }).start();
    }

    private void updateUI(ArrayList<Event> upcoming, ArrayList<Event> past) {
        if (upcoming.isEmpty() && past.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            layoutUpcoming.setVisibility(View.GONE);
            layoutPast.setVisibility(View.GONE);

            adapterUpcoming.setEvents(new ArrayList<>());
            adapterPast.setEvents(new ArrayList<>());
        } else {
            empty.setVisibility(View.GONE);

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
        adapterUpcoming.notifyDataSetChanged();
        adapterPast.notifyDataSetChanged();
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
    protected void onResume() {
        super.onResume();
        loadData();
    }
}