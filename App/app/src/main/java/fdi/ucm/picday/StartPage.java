package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class StartPage extends Activity {

    private List<Challenge> challengeList = new ArrayList<>();
    private RecyclerView list_challenges;
    private ChallengeAdapter cAdapter;

    private final String EXTRA_USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USER);

        cAdapter = new ChallengeAdapter(challengeList);
        list_challenges = (RecyclerView) findViewById(R.id.rv_challenges);
        //list_challenges.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_challenges.setLayoutManager(layoutManager);
        list_challenges.setItemAnimator(new DefaultItemAnimator());
        list_challenges.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL)); //line between the challenges

        //set the adapter
        list_challenges.setAdapter(cAdapter);

        //add click listener
        list_challenges.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), list_challenges, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Challenge c = challengeList.get(position);
                Toast.makeText(getApplicationContext(), c.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Challenge c = challengeList.get(position);
                Toast.makeText(getApplicationContext(), c.getName() + " is selected!", Toast.LENGTH_SHORT).show();

            }
        }));
        loadChallenges();

    }

    private void loadChallenges() {
        Challenge ht = new Challenge("The Handmaid's Tale","Fotitos distopicas");
        challengeList.add(ht);

        Challenge groot = new Challenge("Groot","Fotitos de tus bichos favoritos");
        challengeList.add(groot);

        cAdapter.notifyDataSetChanged();
    }
}
