package valentina.ilicic.eventsApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> activityList;

    public FriendsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        list = view.findViewById(R.id.listFriends);

        TextView emptyView = view.findViewById(R.id.txtNone);
        list.setEmptyView(emptyView);

        activityList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, activityList);
        list.setAdapter(adapter);

        loadFriendsActivity();

        return view;
    }

    private void loadFriendsActivity() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String mongoUserId = prefs.getString("mongoUserId", "");

        if (mongoUserId.isEmpty()) return;

        new Thread(() -> {
            try {
                HTTPHelper helper = new HTTPHelper();
                String url = HTTPHelper.BASE_URL + "/friends-activity/" + mongoUserId;
                JSONArray array = helper.getJSONArrayFromURL(url);

                if (array != null) {
                    activityList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);

                        String username = obj.optString("username", "Unknown");
                        String eventName = obj.optString("eventName", "Unknown Event");
                        String commitment = obj.optString("commitment", "");

                        String action = commitment.equals("PRISUSTVUJE") ? "is also attending" : "is also interested in";
                        activityList.add(username + " " + action + " " + eventName);
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFriendsActivity();
    }
}