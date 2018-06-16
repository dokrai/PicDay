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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChallengeActivity extends Activity {

    private RecyclerView list_pics;
    private TextView challenge_name;
    private ImageView add;
    private PictureAdapter pic_adapter;
    private List<Picture> picsList;

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

    private void loadPics(String name) {
        Resources res = getApplicationContext().getResources();
        Bitmap bMap;
        if (name.equals("MyJob")) {
            Toast.makeText(getApplicationContext(), "Daily Challenge, it would disappear tomorrow!", Toast.LENGTH_LONG).show();
            int[] pics = new int[]{R.drawable.jobs1,R.drawable.jobs2,R.drawable.jobs3,R.drawable.jobs4,R.drawable.jobs5};
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs1);
            Picture p1 = new Picture(pics[0], bMap, "awar11", "MyJob", 0, 0);
            picsList.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs2);
            Picture p2 = new Picture(pics[1], bMap, "offred", "MyJob", 2, 5);
            picsList.add(p2);
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs3);
            Picture p3 = new Picture(pics[2], bMap, "dokrai", "MyJob", 5, 4);
            picsList.add(p3);
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs4);
            Picture p4 = new Picture(pics[3], bMap, "dewitt", "MyJob", 3, 5);
            picsList.add(p4);
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs5);
            Picture p5 = new Picture(pics[4], bMap, "dres", "MyJob", 4, 6);
            picsList.add(p5);
        }
        else if (name.equals("Distopia")) {
            int[] pics = new int[]{R.drawable.dist1,R.drawable.dist2,R.drawable.dist3,R.drawable.dist4,R.drawable.dist5};
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist1);
            Picture p1 = new Picture(pics[0], bMap, "dres", "Distopia", 3, 1);
            picsList.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist2);
            Picture p2 = new Picture(pics[1], bMap, "offred", "Distopia", 5, 5);
            picsList.add(p2);
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist3);
            Picture p3 = new Picture(pics[2], bMap, "dewitt", "Distopia", 5, 4);
            picsList.add(p3);
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist4);
            Picture p4 = new Picture(pics[3], bMap, "awar11", "Distopia", 3, 5);
            picsList.add(p4);
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist5);
            Picture p5 = new Picture(pics[4], bMap, "dokrai", "Distopia", 4, 6);
            picsList.add(p5);
        }
        else if (name.equals("Cuteness")) {
            int[] pics = new int[]{R.drawable.cute1,R.drawable.cute2,R.drawable.cute3,R.drawable.cute4,R.drawable.cute5};
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute1);
            Picture p1 = new Picture(pics[0], bMap, "dewitt", "Cuteness", 3, 1);
            picsList.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute2);
            Picture p2 = new Picture(pics[1], bMap, "dokrai", "Cuteness", 5, 5);
            picsList.add(p2);
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute3);
            Picture p3 = new Picture(pics[2], bMap, "awar11", "Cuteness", 5, 4);
            picsList.add(p3);
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute4);
            Picture p4 = new Picture(pics[3], bMap, "dres", "Cuteness", 3, 5);
            picsList.add(p4);
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute5);
            Picture p5 = new Picture(pics[4], bMap, "offred", "Cuteness", 4, 6);
            picsList.add(p5);
        }
        else {
            int[] pics = new int[]{R.drawable.hobby1,R.drawable.hobby2,R.drawable.hobby3,R.drawable.hobby4,R.drawable.hobby5};
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby1);
            Picture p1 = new Picture(pics[0], bMap, "dokrai", "MyHobby", 3, 1);
            picsList.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby2);
            Picture p2 = new Picture(pics[1], bMap, "awar11", "MyHobby", 5, 5);
            picsList.add(p2);
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby3);
            Picture p3 = new Picture(pics[2], bMap, "offred", "MyHobby", 5, 4);
            picsList.add(p3);
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby4);
            Picture p4 = new Picture(pics[3], bMap, "dres", "MyHobby", 3, 5);
            picsList.add(p4);
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby5);
            Picture p5 = new Picture(pics[4], bMap, "dewitt", "MyHobby", 4, 6);
            picsList.add(p5);
        }

        pic_adapter.notifyDataSetChanged();
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


