package valentina.ilicic.eventsApp;

import android.content.Context;
import android.content.Intent;
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

    public void deleteAllEvents() {
        this.events.clear();
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
            if (vh.txtName != null) vh.txtName.setText(currentEvent.getName());
            if (vh.txtCategory != null) vh.txtCategory.setText(currentEvent.getCategory());
            if (vh.txtLocation != null) vh.txtLocation.setText(currentEvent.getLocation());
            if (vh.imageEvents != null) vh.imageEvents.setImageResource(currentEvent.getImageResId());

            if (vh.txtDateTime != null) {
                String dateTimeFromDb = currentEvent.getDateTime();


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

            if (vh.txtFeatured != null && vh.txtCapacity != null) {
                if (currentEvent.isPromoted() && !currentEvent.isPast()) {
                    vh.txtFeatured.setVisibility(View.VISIBLE);
                    vh.txtCapacity.setVisibility(View.VISIBLE);

                    int freeSeats = currentEvent.getCapacity() - currentEvent.getAttendingCount();
                    vh.txtCapacity.setText(context.getString(R.string.slobodnih_mesta_x_y, freeSeats, currentEvent.getCapacity()));

                    if (vh.layoutList != null) {
                        vh.layoutList.setBackgroundColor(context.getColor(R.color.orange_light));
                    }
                } else {
                    vh.txtFeatured.setVisibility(View.GONE);
                    vh.txtCapacity.setVisibility(View.GONE);
                    if (vh.layoutList != null) {
                        vh.layoutList.setBackgroundColor(context.getColor(android.R.color.transparent));
                    }
                }
            }

            if (vh.btnRate != null) {
                if (currentEvent.isPast()) {
                    vh.btnRate.setVisibility(View.VISIBLE);

                    int eventId = repo.getLocalEventId(currentEvent.getName());
                    boolean rated = false;
                    if (userId != -1 && eventId != -1) {
                        rated = repo.eventRated(userId, eventId);
                    }

                    if (!rated) {
                        vh.btnRate.setEnabled(true);
                        vh.btnRate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, RatingActivity.class);
                                intent.putExtra("event_name", currentEvent.getName());
                                intent.putExtra("username", currentUser);
                                context.startActivity(intent);
                            }
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
        }
        return convertView;
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