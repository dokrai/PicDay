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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ranking extends Activity {

    private List<User> userList = new ArrayList<>();
    private RecyclerView list_users;
    private UserAdapter user_adapter;
    private BottomNavigationView menu;
    private ImageView help;
    //Receiving extra
    private final String EXTRA_USER = "username";

    //Sending extra
    private final String EXTRA_LONG = "longitude";
    private final String EXTRA_LAT = "latitude";

    //geolocation
    protected LocationManager locationManager;
    private static final long DISTANCE_UPDATE = 100; //metres
    private static final long TIME_UPDATE = 1000; //ms


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra(EXTRA_USER);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Deactivated geolocation!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, DISTANCE_UPDATE, TIME_UPDATE, new Ranking.MyLocationListener());
        }

        menu = (BottomNavigationView) findViewById(R.id.menu);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profBtn:
                        Intent profIntent = new Intent(Ranking.this, Profile.class);
                        profIntent.putExtra(EXTRA_USER, userName);
                        startActivity(profIntent);
                        break;
                    case R.id.findBtn:
                        Location here = getCurrentLocation();
                        Intent findIntent = new Intent(Ranking.this, FindPics.class);
                        findIntent.putExtra(EXTRA_LONG,here.getLongitude());
                        findIntent.putExtra(EXTRA_LAT,here.getLatitude());
                        startActivity(findIntent);
                        break;
                    case R.id.rankBtn:
                        break;
                }

                return true;
            }
        });

        help = (ImageView) findViewById(R.id.help_pic);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(Ranking.this, AboutUs.class);
                startActivity(aboutIntent);

            }
        });

        user_adapter = new UserAdapter(userList);
        list_users = (RecyclerView) findViewById(R.id.rv_users);
        list_users.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        list_users.setLayoutManager(layoutManager);
        list_users.setItemAnimator(new DefaultItemAnimator());
        list_users.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); //line between the challenges

        //set the adapter
        list_users.setAdapter(user_adapter);

        loadUsers();

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
            String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s", location.getLongitude(), location.getLatitude());
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                // User refused to grant permission. You can add AlertDialog here
                Toast.makeText(this, "You didn't give permission to access device location", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadUsers() {

        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.40/bdRemota/ranking.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("data");
                                for(int i = 0; i < array.length();i++){
                                    JSONObject o = array.getJSONObject(i);
                                    User userScore = new User(o.getString("user_name"),o.getString("email"),o.getString("password"),o.getInt("SUM(score)"));
                                    userList.add(userScore);
                                }
                                user_adapter.notifyDataSetChanged();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude());
            Toast.makeText(Ranking.this, message, Toast.LENGTH_LONG).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //Toast.makeText(Ranking.this, "Status changed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(Ranking.this, "GPS OFF!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(Ranking.this, "GPS ON!", Toast.LENGTH_LONG).show();
        }
    }
}
