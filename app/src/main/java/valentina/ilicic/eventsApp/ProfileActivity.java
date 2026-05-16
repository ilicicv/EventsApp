package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textUser, textEmail;
    Button btnPassProfile, btnEndSessionProfile;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textUser = findViewById(R.id.textUser);
        textEmail = findViewById(R.id.textEmail);
        btnPassProfile = findViewById(R.id.btnPassProfile);
        btnEndSessionProfile = findViewById(R.id.btnEndSessionProfile);

        btnPassProfile.setOnClickListener(this);
        btnEndSessionProfile.setOnClickListener(this);

        Bundle loginData = getIntent().getExtras();
        if (loginData != null) {
            String user = loginData.getString("profileUsername");
            String email = loginData.getString("profileEmail");

            if (user != null) {
                textUser.setText(user);
                username = user;
            }

            if (email != null) {
                textEmail.setText(email);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnEndSessionProfile) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.btnPassProfile) {
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }
}