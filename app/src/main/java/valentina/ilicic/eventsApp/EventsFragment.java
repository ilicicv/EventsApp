package valentina.ilicic.eventsApp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView list;
    private EventAdapter adapter;

    private Button btnAddEvent, btnFilterAll, btnFilterParty, btnFilterFestival, btnFilterTheater, btnFilterConcert, btnFilterExhibition;

    public EventsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        list = view.findViewById(R.id.listEvents);
        list.setEmptyView(view.findViewById(R.id.txtNoEvents));

        adapter = new EventAdapter(getActivity());
        adapter.setEvents(AppData.getSortedEvents());
        list.setAdapter(adapter);

        btnAddEvent = view.findViewById(R.id.btnAddEvent);
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterParty = view.findViewById(R.id.btnFilterParty);
        btnFilterFestival = view.findViewById(R.id.btnFilterFestival);
        btnFilterTheater = view.findViewById(R.id.btnFilterTheater);
        btnFilterConcert = view.findViewById(R.id.btnFilterConcert);
        btnFilterExhibition = view.findViewById(R.id.btnFilterExhibition);

        btnAddEvent.setOnClickListener(this);
        btnFilterAll.setOnClickListener(this);
        btnFilterParty.setOnClickListener(this);
        btnFilterFestival.setOnClickListener(this);
        btnFilterTheater.setOnClickListener(this);
        btnFilterConcert.setOnClickListener(this);
        btnFilterExhibition.setOnClickListener(this);

        list.setOnItemClickListener(this);

        btnFilterAll.setBackgroundColor(getContext().getColor(R.color.purple_500));

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAddEvent) {
            Intent intent = new Intent(getActivity(), CreateEventActivity.class);
            startActivity(intent);
        }
        else if (view instanceof Button) {
            Button b = (Button) view;

            btnFilterAll.setBackgroundColor(getContext().getColor(R.color.grey));
            btnFilterParty.setBackgroundColor(getContext().getColor(R.color.grey));
            btnFilterFestival.setBackgroundColor(getContext().getColor(R.color.grey));
            btnFilterTheater.setBackgroundColor(getContext().getColor(R.color.grey));
            btnFilterConcert.setBackgroundColor(getContext().getColor(R.color.grey));
            btnFilterExhibition.setBackgroundColor(getContext().getColor(R.color.grey));

            b.setBackgroundColor(getContext().getColor(R.color.purple_500));

            String category = b.getText().toString();
            if (category.equalsIgnoreCase(getString(R.string.sve)) || category.equalsIgnoreCase("Sve")) {
                adapter.setEvents(AppData.getSortedEvents());
            } else {
                ArrayList<Event> events = AppData.getSortedEventsByCategory(category);
                adapter.setEvents(events);
            }
        }
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
}