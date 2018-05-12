package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;


public class Profile extends Activity {

    private TextView name;
    private PictureAdapter pic_adapter;
    private List<Picture> myPics;
    private RecyclerView list_pics;
    //Receiving extra
    private final String EXTRA_USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String username = intent.getStringExtra(EXTRA_USER);

        name = (TextView) findViewById(R.id.prof_name);
        name.setText(username);

    }
}
