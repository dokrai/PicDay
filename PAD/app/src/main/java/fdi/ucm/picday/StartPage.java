package fdi.ucm.picday;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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

public class StartPage extends Activity {

    private List<Challenge> challengeList = new ArrayList<>();
    private RecyclerView list_challenges;
    private ChallengeAdapter cAdapter;
    private BottomNavigationView menu;
    private ImageView help;
    //Receiving extra
    private final String EXTRA_USER = "username";

    //Sending extra
    private final String EXTRA_CID = "challid";
    private final String EXTRA_CNAME = "challname";
    private final String EXTRA_DESC = "description";
    private final String EXTRA_LONG = "longitude";
    private final String EXTRA_LAT = "latitude";

    //geolocation
    protected LocationManager locationManager;

    private static final long DISTANCE_UPDATE = 100; //metres
    private static final long TIME_UPDATE = 1000; //ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra(EXTRA_USER);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Deactivated geolocation!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DISTANCE_UPDATE, TIME_UPDATE, new MyLocationListener());
        }


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
                        Location here = getCurrentLocation();
                        Intent findIntent = new Intent(StartPage.this, FindPics.class);
                        findIntent.putExtra(EXTRA_LONG,here.getLongitude());
                        findIntent.putExtra(EXTRA_LAT,here.getLatitude());
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

        help = (ImageView) findViewById(R.id.help_pic);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(StartPage.this, AboutUs.class);
                startActivity(aboutIntent);

            }
        });

        cAdapter = new ChallengeAdapter(challengeList);
        list_challenges = (RecyclerView) findViewById(R.id.rv_challenges);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_challenges.setLayoutManager(layoutManager);
        list_challenges.setItemAnimator(new DefaultItemAnimator());
        list_challenges.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); //line between the challenges

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

    private Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(), "Deactivated geolocation!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return null;
        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this, "You gave us permission to access device location", Toast.LENGTH_LONG).show();
            } else {
                // User refused to grant permission.
                Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadChallenges() {
        Challenge daily = new Challenge(1,"MyJob","Show everybody your workstation");
        challengeList.add(daily);

        Challenge dist = new Challenge(1,"Distopia","Share a picture of your favourite distopic world");
        challengeList.add(dist);

        Challenge cute = new Challenge(2,"Cuteness","Welcome to the world of the cutest creatures!");
        challengeList.add(cute);

        Challenge hobby = new Challenge(3,"MyHobby","Let us see how you enjoy spending your free time");
        challengeList.add(hobby);

        cAdapter.notifyDataSetChanged();
    }

    private class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());
            Toast.makeText(StartPage.this, message, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Toast.makeText(StartPage.this, "Status changed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(StartPage.this, "GPS OFF!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(StartPage.this, "GPS ON!", Toast.LENGTH_LONG).show();
        }
    }
}
