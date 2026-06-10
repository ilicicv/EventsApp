package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutStart, layoutLogin, layoutRegister;
    Button btnLogin, btnRegister, btnLoginForm, btnRegisterForm;
    EditText editUsername, editPassword, editUsernameReg, editPasswordReg, editEmailReg;
    CheckBox isAdmin;
    DatabaseHelper dbHelper;
    DatabaseRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginForm = findViewById(R.id.btnLoginForm);
        btnRegisterForm = findViewById(R.id.btnRegisterForm);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editUsernameReg = findViewById(R.id.editUsernameReg);
        editPasswordReg = findViewById(R.id.editPassReg);
        editEmailReg = findViewById(R.id.editEmailReg);

        isAdmin = findViewById(R.id.cbIsAdmin);

        layoutStart = findViewById(R.id.formStart);
        layoutLogin = findViewById(R.id.formLogin);
        layoutRegister = findViewById(R.id.formRegister);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLoginForm.setOnClickListener(this);
        btnRegisterForm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnLogin) {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutStart.setVisibility(View.GONE);
        } else if (id == R.id.btnRegister) {
            layoutRegister.setVisibility(View.VISIBLE);
            layoutStart.setVisibility(View.GONE);
        } else if (id == R.id.btnLoginForm) {
            login();
        } else if (id == R.id.btnRegisterForm) {
            register();
        }
    }

    private void login() {
        String user = editUsername.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) return;

        new Thread(() -> {
            try {
                HTTPHelper helper = new HTTPHelper();
                JSONObject loginJson = new JSONObject();
                loginJson.put("username", user);
                loginJson.put("password", pass);

                JSONObject response = helper.postJSONObjectFromURL(HTTPHelper.BASE_URL + "/login", loginJson);

                if (response != null && response.has("username")) {
                    String mongoId = response.optString("_id", "");
                    boolean isAdmin = response.optBoolean("isAdmin", false);

                    if (repo.getLocalUserId(user) == -1) {
                        repo.registerUser(user, response.optString("email", ""), pass, isAdmin, mongoId);
                    }

                    getSharedPreferences("UserData", MODE_PRIVATE).edit()
                            .putString("username", user)
                            .putBoolean("isAdmin", isAdmin)
                            .putString("mongoUserId", mongoId)
                            .apply();

                    startService(new Intent(this, ExclusiveEventService.class));
                    runOnUiThread(() -> {
                        startActivity(new Intent(this, EventsActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, R.string.err_server, Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void register() {
        String user = editUsernameReg.getText().toString().trim();
        String pass = editPasswordReg.getText().toString().trim();
        String email = editEmailReg.getText().toString().trim();
        boolean isUserAdmin = isAdmin.isChecked();

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            editUsernameReg.setError(getString(R.string.errorRegister));
            return;
        }

        JSONObject regJson = new JSONObject();
        try {
            regJson.put("username", user);
            regJson.put("email", email);
            regJson.put("password", pass);
            regJson.put("isAdmin", isUserAdmin ? 1 : 0);
        } catch (JSONException e) { e.printStackTrace(); }

        new Thread(() -> {
            try {
                HTTPHelper helper = new HTTPHelper();
                JSONObject response = helper.postJSONObjectFromURL(HTTPHelper.BASE_URL + "/users", regJson);

                if (response != null && response.has("_id")) {
                    String mongoUserId = response.optString("_id", "");

                    repo.registerUser(user, email, pass, isUserAdmin, mongoUserId);

                    getSharedPreferences("UserData", MODE_PRIVATE).edit()
                            .putString("username", user)
                            .putBoolean("isAdmin", isUserAdmin)
                            .putString("mongoUserId", mongoUserId)
                            .apply();

                    Intent serviceIntent = new Intent(LoginActivity.this, ExclusiveEventService.class);
                    startService(serviceIntent);

                    runOnUiThread(() -> {
                        startActivity(new Intent(this, EventsActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, R.string.server, Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, R.string.err_server_konekc, Toast.LENGTH_LONG).show();
                });            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (layoutLogin.getVisibility() == View.VISIBLE || layoutRegister.getVisibility() == View.VISIBLE) {
            layoutLogin.setVisibility(View.GONE);
            layoutRegister.setVisibility(View.GONE);
            layoutStart.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}