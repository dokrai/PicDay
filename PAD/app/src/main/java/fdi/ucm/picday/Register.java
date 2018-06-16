package fdi.ucm.picday;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends Activity {

    private Button submit;
    private Button cancel;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView registration_error;

    private final String EXTRA_USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUserName = (EditText) findViewById(R.id.regUserName);
        inputEmail = (EditText) findViewById(R.id.regEmail);
        inputPassword = (EditText) findViewById(R.id.regPassword);
        submit = (Button) findViewById(R.id.btnSubmit);
        cancel = (Button) findViewById(R.id.btnCancelReg);
        registration_error = (TextView) findViewById(R.id.reg_error);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_user();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return_to_login();
            }
        });
    }

    private void create_user() {
        String user_name = inputUserName.getText().toString();
        String user_email = inputEmail.getText().toString();
        String user_password = inputPassword.getText().toString();
        User user = new User(user_name,user_email,user_password);
        int correct_registration = user.register(this);
        if (correct_registration == 0) {
            registration_error.setText("");
            Intent startIntent = new Intent(Register.this, StartPage.class);
            startIntent.putExtra(EXTRA_USER,user_name);
            startActivity(startIntent);
        }
        else if (correct_registration == 1) {
            registration_error.setText("Given username is not available!");
        }
        else {
            registration_error.setText("Password should have at least 6 characters");
        }
    }

    private void return_to_login() {
        Intent retIntent = new Intent(Register.this, Login.class);
        startActivity(retIntent);
    }
}
