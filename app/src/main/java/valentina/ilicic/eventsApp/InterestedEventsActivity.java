package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class InterestedEventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listInterested;
    private TextView empty;
    private EventAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_events);

        empty = findViewById(R.id.txtNoInterestedEvents);
        listInterested = findViewById(R.id.listInterestedEvents);
        listInterested.setEmptyView(empty);

        adapter = new EventAdapter(this);
        adapter.setEvents(AppData.getSorted(AppData.interestedEvents));
        listInterested.setAdapter(adapter);

        listInterested.setOnItemClickListener(this);
        listInterested.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapter.getItem(i);

        if (event != null) {
            Intent intent = new Intent(this, EventDetailsActivity.class);
            intent.putExtra("event_name", event.getName());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = (Event) adapter.getItem(i);

        if (event != null) {
            AppData.interestedEvents.remove(event);
            adapter.setEvents(AppData.getSorted(AppData.interestedEvents));
            adapter.notifyDataSetChanged();

            Toast.makeText(this, R.string.msg_uklonjeno_zainteresovani, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}