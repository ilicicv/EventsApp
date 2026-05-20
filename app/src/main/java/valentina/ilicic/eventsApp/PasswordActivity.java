package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    EditText editCurrPass, editNewPass;
    Button btnSavePass;
    String user;
    DatabaseHelper dbHelper;
    DatabaseRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        dbHelper = new DatabaseHelper(this);
        repo = new DatabaseRepository(dbHelper);

        editCurrPass = findViewById(R.id.editCurrPass);
        editNewPass = findViewById(R.id.editNewPass);
        btnSavePass = findViewById(R.id.btnSavePass);

        if (getIntent().getExtras() != null) {
            user = getIntent().getExtras().getString("username");
        }

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = editCurrPass.getText().toString().trim();
                String newPass = editNewPass.getText().toString().trim();

                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    editCurrPass.setError(getString(R.string.errorRegister));
                    return;
                }

                if (oldPass.equals(newPass)) {
                    editNewPass.setError(getString(R.string.samePass));
                    return;
                }

                if (newPass.length() < 6) {
                    editNewPass.setError(getString(R.string.msg_pass));
                    return;
                }

                if (!repo.loginUser(user, oldPass)) {
                    editCurrPass.setError(getString(R.string.msg_wrongCurrPass));
                    return;
                }

                if(repo.updatePassword(user, newPass)){
                    Toast.makeText(PasswordActivity.this, getString(R.string.passwordChanged), Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(PasswordActivity.this, getString(R.string.msg_dberror), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}