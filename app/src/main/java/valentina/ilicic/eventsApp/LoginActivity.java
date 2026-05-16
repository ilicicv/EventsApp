package valentina.ilicic.eventsApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layoutStart, layoutLogin, layoutRegister;
    Button btnLogin, btnRegister, btnLoginForm, btnRegisterForm;
    EditText editUsername, editPassword, editUsernameReg, editPasswordReg, editEmailReg;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginForm = findViewById(R.id.btnLoginForm);
        btnRegisterForm = findViewById(R.id.btnRegisterForm);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editUsernameReg = findViewById(R.id.editUsernameReg);
        editPasswordReg = findViewById(R.id.editPassReg);
        editEmailReg = findViewById(R.id.editEmailReg);

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
        }
        else if (id == R.id.btnRegister) {
            layoutRegister.setVisibility(View.VISIBLE);
            layoutStart.setVisibility(View.GONE);
        }
        else if (id == R.id.btnLoginForm) {
            String user = editUsername.getText().toString().trim();
            String pass = editPassword.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                editUsername.setError(getString(R.string.errorLogin));
                return;
            }

            if (dbHelper.loginUser(user, pass)) {
                Intent intent = new Intent(this, EventsActivity.class);
                Bundle login = new Bundle();
                login.putString("username", user);
                intent.putExtras(login);
                startActivity(intent);
            } else {
                editUsername.setError(getString(R.string.errorLogin));
            }

        }
        else if (id == R.id.btnRegisterForm) {
            String user = editUsernameReg.getText().toString().trim();
            String pass = editPasswordReg.getText().toString().trim();
            String email = editEmailReg.getText().toString().trim();

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                editUsernameReg.setError(getString(R.string.errorRegister));
            } else if (pass.length()<6){
                editPasswordReg.setError(getString(R.string.msg_pass));
            } else {
                if (dbHelper.checkUsername(user)) {
                    editUsernameReg.setError(getString(R.string.msg_user));
                    return;
                }

                if (dbHelper.checkEmail(email)) {
                    editEmailReg.setError(getString(R.string.msg_mail));
                    return;
                }

                if (dbHelper.registerUser(user, email, pass)) {
                    Intent intent = new Intent(this, EventsActivity.class);
                    Bundle register = new Bundle();
                    register.putString("username", user);
                    register.putString("email", email);
                    intent.putExtras(register);
                    startActivity(intent);
                } else {
                    editUsernameReg.setError(getString(R.string.msg_dberror));
                }
            }
        }
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