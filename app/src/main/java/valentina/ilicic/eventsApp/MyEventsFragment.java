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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    Button btnInterestedEvents, btnAttendingEvents, btnMyProfile;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    public static MyEventsFragment newInstance(String param1, String param2) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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