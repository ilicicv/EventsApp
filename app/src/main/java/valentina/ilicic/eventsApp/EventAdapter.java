package valentina.ilicic.eventsApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context){
        this.context = context;
        events = new ArrayList<Event>();
    }

    public void setEvents(ArrayList<Event> eventsList){
        this.events.clear();
        if (eventsList != null) {
            this.events.addAll(eventsList);
        }
        notifyDataSetChanged();
    }

    public void deleteAllEvents(){
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row, parent, false);

            vh = new ViewHolder();
            vh.layoutList = convertView.findViewById(R.id.layoutList);
            vh.txtFeatured = convertView.findViewById(R.id.txtFeatured);
            vh.imageEvents = convertView.findViewById(R.id.imageEvents);
            vh.txtName = convertView.findViewById(R.id.txtName);
            vh.txtCategory = convertView.findViewById(R.id.txtCategory);
            vh.txtLocation = convertView.findViewById(R.id.txtLocation);
            vh.txtDateTime = convertView.findViewById(R.id.txtDateTime);
            vh.capacityLayout = (LinearLayout) convertView.findViewById(R.id.txtCapacity).getParent();
            vh.txtCapacityNumX = convertView.findViewById(R.id.txtCapacityNumX);
            vh.txtCapacityNumY = convertView.findViewById(R.id.txtCapacityNumY);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Event currentEvent = (Event) getItem(position);

        if (currentEvent != null) {
            vh.txtName.setText(currentEvent.getName());
            vh.txtCategory.setText(currentEvent.getCategory());
            vh.txtLocation.setText(currentEvent.getLocation());
            vh.txtDateTime.setText(currentEvent.getDateTime());
            vh.imageEvents.setImageResource(currentEvent.getImageResId());

            if (currentEvent.isPromoted()) {
                vh.txtFeatured.setVisibility(View.VISIBLE);
                vh.capacityLayout.setVisibility(View.VISIBLE);

                int freeSeats = currentEvent.getCapacity() - currentEvent.getAttendingCount();
                vh.txtCapacityNumX.setText(String.valueOf(freeSeats));
                vh.txtCapacityNumY.setText(String.valueOf(currentEvent.getCapacity()));

                vh.layoutList.setBackgroundColor(context.getColor(R.color.orange_light));
            } else {
                vh.txtFeatured.setVisibility(View.GONE);
                vh.capacityLayout.setVisibility(View.GONE);
                vh.layoutList.setBackgroundColor(context.getColor(android.R.color.transparent));
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
        LinearLayout capacityLayout;
        TextView txtCapacityNumX;
        TextView txtCapacityNumY;
    }

}
