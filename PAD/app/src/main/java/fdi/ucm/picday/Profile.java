package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Activity {

    private TextView name;
    private PictureAdapter pic_adapter;
    private List<Picture> myPics;
    private RecyclerView list_pics;
    //Receiving extra
    private final String EXTRA_USER = "username";

    //Sending extra
    private final String EXTRA_PID = "picid";
    private final String EXTRA_OWNER = "username";
    private final String EXTRA_SCORE = "score";
    private final String EXTRA_TIMES = "timesscored";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String username = intent.getStringExtra(EXTRA_USER);

        name = (TextView) findViewById(R.id.prof_name);
        name.setText(username);

        //initiate the RecyclerView and the adapter
        list_pics = (RecyclerView) findViewById(R.id.rv_pics);

        myPics = new ArrayList<>();
        pic_adapter = new PictureAdapter(this, myPics);

        list_pics.setLayoutManager(new GridLayoutManager(this, 2));
        list_pics.addItemDecoration(new Profile.GridSpacingItemDecoration(2, dpToPx(10), true));
        list_pics.setItemAnimator(new DefaultItemAnimator());
        list_pics.setAdapter(pic_adapter);

        list_pics.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), list_pics, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Picture p = myPics.get(position);
                Intent picIntent = new Intent(Profile.this, PicturePage.class);
                picIntent.putExtra(EXTRA_PID, p.getId());
                picIntent.putExtra(EXTRA_OWNER, p.getOwner());
                picIntent.putExtra(EXTRA_SCORE, p.getScore());
                picIntent.putExtra(EXTRA_TIMES, p.getTimes_scored());
                startActivity(picIntent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Picture p = myPics.get(position);
                Intent picIntent = new Intent(Profile.this, PicturePage.class);
                picIntent.putExtra(EXTRA_PID, p.getId());
                picIntent.putExtra(EXTRA_OWNER, p.getOwner());
                picIntent.putExtra(EXTRA_SCORE, p.getScore());
                picIntent.putExtra(EXTRA_TIMES, p.getTimes_scored());
                startActivity(picIntent);
            }
        }));

        loadPics(username);
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
        if (name.equals("dokrai")) {
            int[] pics = new int[]{R.drawable.jobs3, R.drawable.pr1,R.drawable.pr3,R.drawable.dist5,R.drawable.cute2,R.drawable.hobby1};
            bMap = BitmapFactory.decodeResource(res,R.drawable.jobs3);
            Picture p1 = new Picture(pics[0], bMap, "dokrai", "MyJob", 5, 4);
            myPics.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.pr1);
            Picture p2 = new Picture(pics[1], bMap, "dokrai", "MyFav T-Shirt", 5, 5);
            myPics.add(p2);
            bMap = BitmapFactory.decodeResource(res,R.drawable.pr3);
            Picture p3 = new Picture(pics[2], bMap, "dokrai", "MyPet", 4, 4);
            myPics.add(p3);
            bMap = BitmapFactory.decodeResource(res,R.drawable.dist5);
            Picture p4 = new Picture(pics[3], bMap, "dokrai", "Distopia", 4, 6);
            myPics.add(p4);
            bMap = BitmapFactory.decodeResource(res,R.drawable.cute2);
            Picture p5 = new Picture(pics[4], bMap, "dokrai", "Cuteness", 5, 6);
            myPics.add(p5);
            bMap = BitmapFactory.decodeResource(res,R.drawable.hobby1);
            Picture p6 = new Picture(pics[5], bMap, "dokrai", "MyHobby", 3, 1);
            myPics.add(p6);
        }
        else {
            int[] pics = new int[]{R.drawable.pr4, R.drawable.pr5};
            bMap = BitmapFactory.decodeResource(res,R.drawable.pr4);
            Picture p1 = new Picture(pics[0], bMap, "admin", "MyJob", 5, 4);
            myPics.add(p1);
            bMap = BitmapFactory.decodeResource(res,R.drawable.pr5);
            Picture p2 = new Picture(pics[1], bMap, "admin", "MyFav T-Shirt", 5, 5);
            myPics.add(p2);
        }

        pic_adapter.notifyDataSetChanged();
    }
}
