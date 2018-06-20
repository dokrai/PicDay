package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChallengeActivity extends Activity {

    private RecyclerView list_pics;
    private TextView challenge_name;
    private ImageView add;
    private PictureAdapter pic_adapter;
    private List<Picture> picsList;
    private final int IMG_REQUEST = 1;

    private File picFile;
    private Uri picUri;

    //Receiving extra
    private final String EXTRA_CID = "challid";
    private final String EXTRA_CNAME = "challname";

    //Sending extra
    private final String EXTRA_PID = "picid";
    private final String EXTRA_OWNER = "username";
    private final String EXTRA_SCORE = "score";
    private final String EXTRA_TIMES = "timesscored";
    private final String EXTRA_IMAGE = "imageDir";


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        /*
         * Receiving the challenge data from the previous form
         */
        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_CID,0);
        String name = intent.getStringExtra(EXTRA_CNAME);

        //initiate the RecyclerView and the adapter
        list_pics = (RecyclerView) findViewById(R.id.rv_pics);
        picsList = new ArrayList<>();
        pic_adapter = new PictureAdapter(this, picsList);

        //create and configure the layout which will contains the pictures
        list_pics.setLayoutManager(new GridLayoutManager(this, 2));
        list_pics.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        list_pics.setItemAnimator(new DefaultItemAnimator());
        list_pics.setAdapter(pic_adapter);

        //shows the challenge name
        challenge_name = (TextView) findViewById(R.id.chal_name);
        challenge_name.setText(name);

        //when selecting a picture
        list_pics.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), list_pics, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Picture p = picsList.get(position);
                Intent picIntent = new Intent(ChallengeActivity.this, PicturePage.class);
                picIntent.putExtra(EXTRA_PID, p.getId());
                picIntent.putExtra(EXTRA_OWNER, p.getOwner());
                picIntent.putExtra(EXTRA_SCORE, p.getScore());
                picIntent.putExtra(EXTRA_TIMES, p.getTimes_scored());
                picIntent.putExtra(EXTRA_IMAGE,p.getDir());
                startActivity(picIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Picture p = picsList.get(position);
                Intent picIntent = new Intent(ChallengeActivity.this, PicturePage.class);
                picIntent.putExtra(EXTRA_PID, p.getId());
                picIntent.putExtra(EXTRA_OWNER, p.getOwner());
                picIntent.putExtra(EXTRA_SCORE, p.getScore());
                picIntent.putExtra(EXTRA_TIMES, p.getTimes_scored());
                picIntent.putExtra(EXTRA_IMAGE,p.getDir());
                startActivity(picIntent);
            }
        }));

        //add picture button configuration
        add = (ImageView) findViewById(R.id.add_pic);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Opening Camera", Toast.LENGTH_SHORT).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getOutputMediaFileUri();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                // start the image capture Intent
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(), "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG).show();
        }

        loadPics(name);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadPics(final String name) {
        try{
        RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://192.168.1.40/bdRemota/loadPics.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray array = obj.getJSONArray("data");
                                for(int i = 0; i < array.length();i++){
                                    JSONObject o = array.getJSONObject(i);
                                    Picture pic = new Picture(o.getInt("foto_id"),o.getString("img"), o.getString("user_name"),o.getString("challenge_name"),o.getInt("score"),o.getInt("times_scored"));
                                    picsList.add(pic);
                                }
                                pic_adapter.notifyDataSetChanged();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("challenge",name);
                    return params;
                }
            };
            queue.add(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(createImageFile());
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String picFileName = "ftvi-" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File pic = new File(storageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return pic;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void previewCapturedImage() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(picFile.getAbsolutePath(), opt);
        Picture p = new Picture(15555,bitmap,"dokrai",challenge_name.getText().toString(),0,0);
        picsList.add(p);
    }
}


