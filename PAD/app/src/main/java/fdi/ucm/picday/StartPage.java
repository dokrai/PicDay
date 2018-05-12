package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
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
    private BottomNavigationView menu;

    //Receiving extra
    private final String EXTRA_USER = "username";

    //Sending extra
    private final String EXTRA_CID = "challid";
    private final String EXTRA_CNAME = "challname";
    private final String EXTRA_DESC = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra(EXTRA_USER);

        menu = (BottomNavigationView) findViewById(R.id.menu);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profBtn:
                        Intent profIntent = new Intent(StartPage.this, Profile.class);
                        profIntent.putExtra(EXTRA_USER, userName);
                        startActivity(profIntent);
                        break;
                    case R.id.findBtn:
                        Intent findIntent = new Intent(StartPage.this, FindPics.class);
                        startActivity(findIntent);
                        break;
                    case R.id.rankBtn:
                        Intent rankIntent = new Intent(StartPage.this, Ranking.class);
                        rankIntent.putExtra(EXTRA_USER, userName);
                        startActivity(rankIntent);
                        break;
                }

                return true;
            }
        });

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
                Intent cIntent = new Intent(StartPage.this, ChallengeActivity.class);
                cIntent.putExtra(EXTRA_CID, c.getId());
                cIntent.putExtra(EXTRA_CNAME, c.getName());
                cIntent.putExtra(EXTRA_DESC, c.getDescription());
                startActivity(cIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Challenge c = challengeList.get(position);
                Intent cIntent = new Intent(StartPage.this, ChallengeActivity.class);
                cIntent.putExtra(EXTRA_CID, c.getId());
                cIntent.putExtra(EXTRA_CNAME, c.getName());
                cIntent.putExtra(EXTRA_DESC, c.getDescription());
                startActivity(cIntent);
            }
        }));
        loadChallenges();

    }

    private void loadChallenges() {
        Challenge ht = new Challenge("The Handmaid's Tale","Fotitos distopicas");
        ht.setId(1);
        challengeList.add(ht);

        Challenge groot = new Challenge("Groot","Fotitos de tus bichos favoritos");
        ht.setId(2);
        challengeList.add(groot);

        cAdapter.notifyDataSetChanged();
    }
}
