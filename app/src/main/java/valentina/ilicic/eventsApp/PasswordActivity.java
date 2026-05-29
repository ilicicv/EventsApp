package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    DatabaseRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        EditText editCurrPass = findViewById(R.id.editCurrPass);
        EditText editNewPass = findViewById(R.id.editNewPass);
        Button btnSavePass = findViewById(R.id.btnSavePass);

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                String user = prefs.getString("username", "");

                String oldPass = editCurrPass.getText().toString().trim();
                String newPass = editNewPass.getText().toString().trim();

                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    editCurrPass.setError(getString(R.string.errorRegister));
                    return;
                }
                if (oldPass.equals(newPass)) {
                    editNewPass.setError(getString(R.string.nova_lozinka));
                    return;
                }
                if (newPass.length() < 6) {
                    editNewPass.setError(getString(R.string.msg_pass));
                    return;
                }

                JSONObject passwordData = new JSONObject();
                try {
                    passwordData.put("username", user);
                    passwordData.put("oldPassword", oldPass);
                    passwordData.put("newPassword", newPass);
                } catch (JSONException e) { e.printStackTrace(); }

                new Thread(() -> {
                    try {
                        HTTPHelper helper = new HTTPHelper();
                        JSONObject updatedUser = helper.putJSONObjectFromURL(HTTPHelper.BASE_URL + "/password", passwordData);

                        runOnUiThread(() -> {
                            if (updatedUser != null) {
                                repo.updatePassword(user, newPass);
                                Toast.makeText(PasswordActivity.this, R.string.lozinka_usp, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(PasswordActivity.this, R.string.err_server_lozinka, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(PasswordActivity.this, R.string.err_server, Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });
    }
}