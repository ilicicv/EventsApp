package valentina.ilicic.eventsApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyEventsFragment extends Fragment {

    Button btnInterestedEvents, btnAttendingEvents, btnMyProfile;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);

        btnInterestedEvents = v.findViewById(R.id.btnInterestedEvents);
        btnAttendingEvents = v.findViewById(R.id.btnAttendingEvents);
        btnMyProfile = v.findViewById(R.id.btnMyProfile);

        btnInterestedEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InterestedEventsActivity.class);
                startActivity(intent);
            }
        });

        btnAttendingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AttendingEventsActivity.class);
                startActivity(intent);
            }
        });

        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    Bundle loginData = getActivity().getIntent().getExtras();

                    if (loginData != null) {

                        String user = loginData.getString("username");
                        String email = loginData.getString("email");

                        intent.putExtra("profileUsername", user);
                        intent.putExtra("profileEmail", email);
                    }
                    startActivity(intent);
            }
        });
        return v;
    }
}