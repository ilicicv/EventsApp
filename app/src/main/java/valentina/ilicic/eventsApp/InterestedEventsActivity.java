package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class InterestedEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listInterested;
    private TextView empty;
    private EventAdapter adapter;
    private DatabaseHelper dbHelper;
    private String currentUsername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        dbHelper = new DatabaseHelper(this);

        Bundle loginData = getIntent().getExtras();
        if (loginData != null) {
            currentUsername = loginData.getString("username", "");
        }

        empty = findViewById(R.id.txtNoInterestedEvents);
        listInterested = findViewById(R.id.listInterestedEvents);
        listInterested.setEmptyView(empty);

        adapter = new EventAdapter(this);
        listInterested.setAdapter(adapter);

        listInterested.setOnItemClickListener(this);
        listInterested.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListFromDatabase();
    }

    private void refreshListFromDatabase() {
        int userId = dbHelper.getUserIdByUsername(currentUsername);

        if (userId != -1) {
            ArrayList<Event> interestedEvents = dbHelper.getInterestedEvents(userId, "ZAINTERESOVAN");
            adapter.setEvents(interestedEvents);
        } else {
            adapter.setEvents(new ArrayList<>());
        }
        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapter.getItem(i);

        if (event != null) {
            int userId = dbHelper.getUserIdByUsername(currentUsername);
            String eventName = event.getName();

            if (userId != -1) {
                boolean isRemoved = dbHelper.removeInterested(userId, eventName);

                if (isRemoved) {
                    refreshListFromDatabase();
                    Toast.makeText(this, R.string.msg_uklonjeno_zainteresovani, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.msg_dberror, Toast.LENGTH_SHORT).show();
                }
            }
        }
        return true;
    }
}