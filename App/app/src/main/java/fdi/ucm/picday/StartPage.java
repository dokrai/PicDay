package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartPage extends Activity {

    private TextView name;

    private final String EXTRA_USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USER);
        name = (TextView) findViewById(R.id.pene);
        name.setText(userName);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
            Login.database.close_connection();
            Toast.makeText(this,"Thanks for using application!!",Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
