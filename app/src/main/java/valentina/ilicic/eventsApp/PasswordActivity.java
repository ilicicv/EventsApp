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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        editCurrPass = findViewById(R.id.editCurrPass);
        editNewPass = findViewById(R.id.editNewPass);
        btnSavePass = findViewById(R.id.btnSavePass);

        btnSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPass = editCurrPass.getText().toString();
                String newPass = editNewPass.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty()) {
                    editCurrPass.setError(getString(R.string.errorRegister));
                    return;
                }

                if (oldPass.equals(newPass)) {
                    editNewPass.setError(getString(R.string.samePass));
                    return;
                }

                Toast.makeText(PasswordActivity.this, getString(R.string.passwordChanged), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}