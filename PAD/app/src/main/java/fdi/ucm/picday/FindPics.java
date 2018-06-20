package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FindPics extends Activity {

    private List<Challenge> challengeList = new ArrayList<>();
    private RecyclerView list_challenges;
    private ChallengeAdapter cAdapter;
    private BottomNavigationView menu;
    private ImageView help;

    private final double MAX_DISTANCE = 500; //meters
    private double longitude;
    private double latitude;

    //Receiving extra
    private final String EXTRA_USER = "username";
    private final String EXTRA_DESC = "description";
    private final String EXTRA_LONG = "longitude";
    private final String EXTRA_LAT = "latitude";

    //Sending extra
    private final String EXTRA_CID = "challid";
    private final String EXTRA_CNAME = "challname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra(EXTRA_USER);
        longitude = intent.getDoubleExtra(EXTRA_LONG,0);
        latitude = intent.getDoubleExtra(EXTRA_LAT,0);

        menu = (BottomNavigationView) findViewById(R.id.menu);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profBtn:
                        Intent profIntent = new Intent(FindPics.this, Profile.class);
                        profIntent.putExtra(EXTRA_USER, userName);
                        startActivity(profIntent);
                        break;
                    case R.id.findBtn:
                        break;
                    case R.id.rankBtn:
                        Intent rankIntent = new Intent(FindPics.this, Ranking.class);
                        rankIntent.putExtra(EXTRA_USER, userName);
                        startActivity(rankIntent);
                        break;
                }

                return true;
            }
        });

        help = (ImageView) findViewById(R.id.help_pic);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Contact Us!",
                        Toast.LENGTH_SHORT).show();
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
                LocatedChallenge c = (LocatedChallenge) challengeList.get(position);
                Intent cIntent = new Intent(FindPics.this, ChallengeActivity.class);
                cIntent.putExtra(EXTRA_CID, c.getId());
                cIntent.putExtra(EXTRA_CNAME, c.getName());
                cIntent.putExtra(EXTRA_DESC, c.getDescription());
                startActivity(cIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                LocatedChallenge c = (LocatedChallenge) challengeList.get(position);
                Intent cIntent = new Intent(FindPics.this, ChallengeActivity.class);
                cIntent.putExtra(EXTRA_CID, c.getId());
                cIntent.putExtra(EXTRA_CNAME, c.getName());
                cIntent.putExtra(EXTRA_DESC, c.getDescription());
                startActivity(cIntent);
            }
        }));
        loadChallenges();

        if (challengeList.size() < 0) {
            Toast.makeText(getApplicationContext(), "There is no special challenges around by, keep looking!", Toast.LENGTH_LONG).show();
        }
    }

    private void loadChallenges() {

        LocatedChallenge tech = new LocatedChallenge(501, "FDI", "Show us your TechSide", -110,37);
        if (isClose(longitude,tech.getLongitude(),latitude,tech.getLatitude())) {
            challengeList.add(tech);
        }

        LocatedChallenge retiro = new LocatedChallenge(502, "ElRetiro", "Let's enjoy the nature together", 100.5,200.4);
        if (isClose(longitude,retiro.getLongitude(),latitude,retiro.getLatitude())) {
            challengeList.add(retiro);
        }

        LocatedChallenge sol = new LocatedChallenge(503, "Sol", "Explore Madrid's Downtown and share you favourite view.", 100.5,200.4);
        if (isClose(longitude,sol.getLongitude(),latitude,sol.getLatitude())) {
            challengeList.add(sol);
        }

        LocatedChallenge fdi = new LocatedChallenge(504, "FDI", "Which is your favourite part of the school?", -122,40);
        if (isClose(longitude,fdi.getLongitude(),latitude,fdi.getLatitude())) {
            challengeList.add(fdi);
        }

        cAdapter.notifyDataSetChanged();
    }

    private boolean isClose(double x1, double x2, double y1, double y2) {
        double longit = Math.pow(2,x2-x1);
        double latit = Math.pow(2,y2-y1);
        double dist = Math.sqrt(longit+latit);
        return dist<MAX_DISTANCE;
    }
}
