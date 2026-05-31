package valentina.ilicic.eventsApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class EventsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView list;
    private EventAdapter adapter;
    private Button btnAddEvent, btnFilterAll, btnFilterParty, btnFilterFestival, btnFilterTheater, btnFilterConcert, btnFilterExhibition;
    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private String currentCategoryFilter = null;

    public EventsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        repo = new DatabaseRepository(dbHelper);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String user = prefs.getString("username", "");
        boolean isAdmin = prefs.getBoolean("isAdmin", false);

        list = view.findViewById(R.id.listEvents);
        list.setEmptyView(view.findViewById(R.id.txtNoEvents));

        adapter = new EventAdapter(getActivity(), R.layout.list_row, user);
        list.setAdapter(adapter);

        btnAddEvent = view.findViewById(R.id.btnAddEvent);
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterParty = view.findViewById(R.id.btnFilterParty);
        btnFilterFestival = view.findViewById(R.id.btnFilterFestival);
        btnFilterTheater = view.findViewById(R.id.btnFilterTheater);
        btnFilterConcert = view.findViewById(R.id.btnFilterConcert);
        btnFilterExhibition = view.findViewById(R.id.btnFilterExhibition);

        btnFilterAll.setOnClickListener(this);
        btnFilterParty.setOnClickListener(this);
        btnFilterFestival.setOnClickListener(this);
        btnFilterTheater.setOnClickListener(this);
        btnFilterConcert.setOnClickListener(this);
        btnFilterExhibition.setOnClickListener(this);
        list.setOnItemClickListener(this);

        if (isAdmin) {
            btnAddEvent.setVisibility(View.VISIBLE);
            btnAddEvent.setOnClickListener(this);
        } else {
            btnAddEvent.setVisibility(View.GONE);
        }

        updateButtonColors(btnFilterAll);
        loadEvents(null);
        syncEventsWithServer(null);

        return view;
    }

    @Override
    public void onClick(View view) {
        boolean isAdmin = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                .getBoolean("isAdmin", false);

        if (view.getId() == R.id.btnAddEvent) {
            if (isAdmin) {
                startActivity(new Intent(getActivity(), CreateEventActivity.class));
            } else {
                Toast.makeText(getActivity(), R.string.samo_admin, Toast.LENGTH_SHORT).show();
            }
        } else if (view instanceof Button) {
            updateButtonColors(view);
            Button b = (Button) view;
            String category = b.getText().toString();
            currentCategoryFilter = (category.equalsIgnoreCase(getString(R.string.sve)) || category.equalsIgnoreCase("Sve")) ? null : category;

            loadEvents(currentCategoryFilter);
            syncEventsWithServer(currentCategoryFilter);
        }
    }

    private void loadEvents(String category) {
        if (category == null) adapter.setEvents(new ArrayList<>(repo.getAllEventsList()));
        else adapter.setEvents(new ArrayList<>(repo.getEventsByCategoryList(category)));
        adapter.notifyDataSetChanged();
    }

    private void syncEventsWithServer(String category) {
        String route = (category == null) ? "/events" : "/events/" + category;

        new Thread(() -> {
            try {
                HTTPHelper helper = new HTTPHelper();
                JSONArray array = helper.getJSONArrayFromURL(HTTPHelper.BASE_URL + route);

                if (array != null) {
                    if (category == null) {
                        repo.deleteAllEvents();
                    }

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String name = obj.getString("name");
                        String mongoId = obj.getString("_id");

                        if (!repo.eventExists(name)) {
                            repo.insertEvent(
                                    mongoId,
                                    name,
                                    obj.getString("description"),
                                    obj.getString("location"),
                                    obj.getString("eventTime"),
                                    obj.getString("category"),
                                    obj.getBoolean("promoted") ? 1 : 0,
                                    obj.getInt("capacity")
                            );
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> loadEvents(currentCategoryFilter));
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents(currentCategoryFilter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event selectedEvent = (Event) adapter.getItem(i);
        if (selectedEvent != null) {
            Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
            intent.putExtra("event_name", selectedEvent.getName());
            startActivity(intent);
        }
    }

    private void updateButtonColors(View selectedView) {
        Button[] buttons = {btnFilterAll, btnFilterParty, btnFilterFestival, btnFilterTheater, btnFilterConcert, btnFilterExhibition};

        for (Button b : buttons)
            b.setBackgroundColor(getContext().getColor(R.color.grey));

        if (selectedView instanceof Button)
            selectedView.setBackgroundColor(getContext().getColor(R.color.purple_500));
    }
}