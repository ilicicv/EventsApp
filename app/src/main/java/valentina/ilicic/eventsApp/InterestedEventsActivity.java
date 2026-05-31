package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class InterestedEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listInterested;
    private TextView empty;
    private EventAdapter adapter;
    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private String currentUsername = "";
    private String mongoUserId = "";
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "");
        mongoUserId = prefs.getString("mongoUserId", "");
        userId = repo.getLocalUserId(currentUsername);

        empty = findViewById(R.id.txtNoInterestedEvents);
        listInterested = findViewById(R.id.listInterestedEvents);
        listInterested.setEmptyView(empty);

        adapter = new EventAdapter(this);
        listInterested.setAdapter(adapter);

        listInterested.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchInterestedEventsFromServer(mongoUserId);
    }

    private void fetchInterestedEventsFromServer(String urlUserId) {
        if (urlUserId == null || urlUserId.isEmpty()) return;

        new Thread(() -> {
            ArrayList<Event> interestedEvents = new ArrayList<>();
            try {
                HTTPHelper helper = new HTTPHelper();
                String url = HTTPHelper.BASE_URL + "/attendance/" + urlUserId;
                JSONArray array = helper.getJSONArrayFromURL(url);

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String commitment = obj.optString("commitment", "");

                        if ("ZAINTERESOVAN".equals(commitment)) {
                            JSONObject e = obj.optJSONObject("event");

                            if (e != null) {
                                boolean isPromoted = e.optBoolean("promoted", false);
                                Event event;

                                if (isPromoted) {
                                    event = EventFactory.createPromotedEvent(
                                            e.optString("name", "Nepoznato"),
                                            e.optString("description", ""),
                                            e.optString("location", "Nema lokacije"),
                                            e.optString("eventTime", ""),
                                            e.optString("category", ""),
                                            e.optInt("capacity", 0),
                                            e.optInt("numberOfAttendees", 0),
                                            e.optDouble("avgRating", 0.0),
                                            e.optInt("numberOfRatings", 0)
                                    );
                                } else {
                                    event = EventFactory.createRegularEvent(
                                            e.optString("name", "Nepoznato"),
                                            e.optString("description", ""),
                                            e.optString("location", "Nema lokacije"),
                                            e.optString("eventTime", ""),
                                            e.optString("category", ""),
                                            e.optInt("numberOfAttendees", 0),
                                            e.optDouble("avgRating", 0.0),
                                            e.optInt("numberOfRatings", 0)
                                    );
                                }
                                interestedEvents.add(event);
                            }
                        }
                    }
                }

                EventsTable.sortEventsByPromotedAndDate(interestedEvents);

            } catch (Exception e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                adapter.setEvents(interestedEvents);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapter.getItem(i);

        if (event != null) {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("event_name", event.getName());

            Bundle loginData = getIntent().getExtras();
            if (loginData != null) {
                intent.putExtras(loginData);
            }

            startActivity(intent);
        }
    }
}