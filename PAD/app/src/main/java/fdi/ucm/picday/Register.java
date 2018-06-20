package fdi.ucm.picday;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.40/bdRemota/registroUsuario.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean success = obj.getBoolean("success");
                                if (success) {
                                    Intent startIntent = new Intent(Register.this, StartPage.class);
                                    startActivity(startIntent);
                                } else {
                                    registration_error.setText("Given username is not available!");
                                }
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    registration_error.setText("Hubo un error al registrar" + error);
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
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

    private void return_to_login() {
        Intent retIntent = new Intent(Register.this, Login.class);
        startActivity(retIntent);
    }
}
