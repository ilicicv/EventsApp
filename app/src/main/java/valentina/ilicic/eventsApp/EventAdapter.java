package valentina.ilicic.eventsApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Event> events;
    private int layoutId;
    private DatabaseHelper dbHelper;
    private DatabaseRepository repo;
    private String currentUser = "";
    private int userId = -1;

    public EventAdapter(Context context) {
        this.context = context;
        this.events = new ArrayList<>();
        this.layoutId = R.layout.list_row;
        this.dbHelper = new DatabaseHelper(context);
        this.repo = new DatabaseRepository(dbHelper);
    }

    public EventAdapter(Context context, int layoutId, String currentUser) {
        this.context = context;
        this.events = new ArrayList<>();
        this.layoutId = layoutId;
        this.currentUser = currentUser;
        this.dbHelper = new DatabaseHelper(context);
        this.repo = new DatabaseRepository(dbHelper);
        this.userId = repo.getLocalUserId(currentUser);
    }

    public void setUsername(String username) {
        this.currentUser = username;
        if (dbHelper != null) {
            this.userId = repo.getLocalUserId(username);
        }
        notifyDataSetChanged();
    }

    public void setEvents(ArrayList<Event> eventsList) {
        this.events.clear();
        if (eventsList != null) {
            this.events.addAll(eventsList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int pos) {
        return events.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);

            vh = new ViewHolder();
            vh.layoutList = convertView.findViewById(R.id.layoutList);
            vh.txtFeatured = convertView.findViewById(R.id.txtFeatured);
            vh.imageEvents = convertView.findViewById(R.id.imageEvents);
            vh.txtName = convertView.findViewById(R.id.txtName);
            vh.txtCategory = convertView.findViewById(R.id.txtCategory);
            vh.txtLocation = convertView.findViewById(R.id.txtLocation);
            vh.txtDateTime = convertView.findViewById(R.id.txtDateTime);
            vh.txtCapacity = convertView.findViewById(R.id.txtCapacity);
            vh.btnRate = convertView.findViewById(R.id.btnRate);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Event currentEvent = (Event) getItem(position);

        if (currentEvent != null) {
            bindBasicData(vh, currentEvent);
            formatAndSetDate(vh, currentEvent.getDateTime());
            updateRowAppereance(vh, currentEvent);
            handleRatingButton(vh, currentEvent);
        }
        return convertView;
    }

    private void bindBasicData(ViewHolder vh, Event event) {
        if (vh.txtName != null) vh.txtName.setText(event.getName());
        if (vh.txtCategory != null) vh.txtCategory.setText(event.getCategory());
        if (vh.txtLocation != null) vh.txtLocation.setText(event.getLocation());
        if (vh.imageEvents != null) vh.imageEvents.setImageResource(event.getImageResId());
    }

    private void formatAndSetDate(ViewHolder vh, String dateTimeFromDb) {
        if (vh.txtDateTime == null || dateTimeFromDb == null) return;

        SimpleDateFormat inputFormat = new SimpleDateFormat("d.M.yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateTimeFromDb);
            String formattedDateTime = outputFormat.format(date);
            vh.txtDateTime.setText(formattedDateTime);
        } catch (ParseException e) {
            vh.txtDateTime.setText(dateTimeFromDb);
        }
    }

    private void updateRowAppereance(ViewHolder vh, Event currentEvent) {
        SharedPreferences prefs = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        long timeLeft = prefs.getLong("expiration_time", 0);
        String savedExclusiveName = prefs.getString("exclusive_event_name", "");

        boolean isExclusiveActive = !savedExclusiveName.isEmpty() &&
                currentEvent.getName().equals(savedExclusiveName) &&
                (System.currentTimeMillis() < timeLeft);

        if (isExclusiveActive) {
            if (vh.txtFeatured != null) {
                vh.txtFeatured.setVisibility(View.VISIBLE);
                vh.txtFeatured.setText("EXCLUSIVE");
                vh.txtFeatured.setBackgroundColor(context.getColor(R.color.red));
            }
            if (vh.txtCapacity != null) vh.txtCapacity.setVisibility(View.GONE);
            if (vh.layoutList != null) vh.layoutList.setBackgroundColor(context.getColor(R.color.light_red));

        } else if (currentEvent.isPromoted() && !currentEvent.isPast()) {
            if (vh.txtFeatured != null) {
                vh.txtFeatured.setVisibility(View.VISIBLE);
                vh.txtFeatured.setText(R.string.featured);
                vh.txtFeatured.setBackgroundColor(context.getColor(R.color.orange));
            }
            if (vh.txtCapacity != null) {
                vh.txtCapacity.setVisibility(View.VISIBLE);
                int freeSeats = currentEvent.getCapacity() - currentEvent.getAttendingCount();
                vh.txtCapacity.setText(context.getString(R.string.slobodnih_mesta_x_y, freeSeats, currentEvent.getCapacity()));
                vh.txtCapacity.setTextColor(context.getColor(R.color.orange));
            }
            if (vh.layoutList != null) vh.layoutList.setBackgroundColor(context.getColor(R.color.orange_light));

        } else {
            if (vh.txtFeatured != null) vh.txtFeatured.setVisibility(View.INVISIBLE);
            if (vh.txtCapacity != null) vh.txtCapacity.setVisibility(View.GONE);
            if (vh.layoutList != null) vh.layoutList.setBackgroundColor(context.getColor(android.R.color.transparent));
        }
    }

    private void handleRatingButton(ViewHolder vh, Event currentEvent) {
        if (vh.btnRate == null) return;

        if (currentEvent.isPast()) {
            vh.btnRate.setVisibility(View.VISIBLE);

            int eventId = repo.getLocalEventId(currentEvent.getName());
            boolean rated = false;
            if (userId != -1 && eventId != -1) {
                rated = repo.eventRated(userId, eventId);
            }

            if (!rated) {
                vh.btnRate.setEnabled(true);
                vh.btnRate.setOnClickListener(v -> {
                    Intent intent = new Intent(context, RatingActivity.class);
                    intent.putExtra("event_name", currentEvent.getName());
                    intent.putExtra("username", currentUser);
                    context.startActivity(intent);
                });
            } else {
                vh.btnRate.setEnabled(false);
                vh.btnRate.setOnClickListener(null);
            }
        } else {
            vh.btnRate.setVisibility(View.GONE);
            vh.btnRate.setOnClickListener(null);
        }
    }

    private static class ViewHolder {
        LinearLayout layoutList;
        TextView txtFeatured;
        ImageView imageEvents;
        TextView txtName;
        TextView txtCategory;
        TextView txtLocation;
        TextView txtDateTime;
        TextView txtCapacity;
        Button btnRate;
    }
}