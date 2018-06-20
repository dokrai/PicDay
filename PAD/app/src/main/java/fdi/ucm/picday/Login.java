package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Login extends Activity {

    /*
        Declaring the atributes.
     */
    private TextView to_register;
    private TextView login_error;
    private Button login;
    private EditText inputUserName;
    private EditText inputPassword;
    //Connecting to the database
    //public final static DatabaseConnection database = new DatabaseConnection();
    //public final static Connection conn_db = database.connect_db(); // connection with out database

    private final String EXTRA_USER = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing the atributes.
        inputUserName = (EditText) findViewById(R.id.txtUserName);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        login = (Button) findViewById(R.id.btnLogin);
        login_error = (TextView) findViewById(R.id.log_error);
        to_register = (TextView) findViewById(R.id.linkToReg);

        //configuring the listener of the login button
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = inputUserName.getText().toString();
                String password = inputPassword.getText().toString();
                log_in(user_name,password);
            }
        });

        //configuring the listener of the to_register text so it leads to a register form
        to_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    /*
        Checks the given name and password and, if correct, leads to the start page. If the given
        data is not right an error message is shown.
     */
     private void log_in(final String user_name, String password) {
        try {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.40/bdRemota/loginUser.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean success= obj.getBoolean("success");
                                if(success){
                                    login_error.setText("");
                                    Intent logIntent = new Intent(Login.this, StartPage.class);
                                    logIntent.putExtra(EXTRA_USER,user_name);
                                    startActivity(logIntent);
                                }else{
                                    login_error.setText("Invalid UserName or Password!");
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    login_error.setText("Hubo un error al registrar" + error);
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    String name = inputUserName.getText().toString();
                    String password = inputPassword.getText().toString();
                    params.put("name",name);
                    params.put("password",password);
                    return params;
                }
            };
            queue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        Just leads to the register form.
     */
    private void register() {
        Intent regIntent = new Intent(Login.this, Register.class);
        startActivity(regIntent);
    }
}
