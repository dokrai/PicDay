package fdi.ucm.picday;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Register extends Activity {

    private Button submit;
    private Button cancel;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView registration_error;


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
                //create_user();
                RegistrarUserBd();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return_to_login();
            }
        });
    }

    private void RegistrarUserBd(){
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            //la url tiene que tener la ip del host, en este caso tiene la de mi ordena
            String url = "http://192.168.1.40/bdRemota/registroUsuario.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            registration_error.setText("Registrado con exito");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    registration_error.setText("Hubo un error al registrar" + error);
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError{
                    Map<String,String> params = new HashMap<String,String>();
                    String name = inputUserName.getText().toString();
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
                    params.put("name",name);
                    params.put("email",email);
                    params.put("password",password);
                    return params;
                }
            };

            queue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*private void create_user() {
        String user_name = inputUserName.getText().toString();
        String user_email = inputEmail.getText().toString();
        String user_password = inputPassword.getText().toString();
        User user = new User(user_name,user_email,user_password);
        int correct_registration = user.register(this);
        if (correct_registration == 0) {
            registration_error.setText("");
            Intent startIntent = new Intent(Register.this, StartPage.class);
            startActivity(startIntent);
        }
        else if (correct_registration == 1) {
            registration_error.setText("Given username is not available!");
        }
        else {
            registration_error.setText("Password should have at least 6 characters");
        }
    }*/

    private void return_to_login() {
        Intent retIntent = new Intent(Register.this, Login.class);
        startActivity(retIntent);
    }
}
