package fdi.ucm.picday;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends Activity {

    private RecyclerView list_pics;
    private TextView challenge_name;
    private FloatingActionButton fab;
    private PictureAdapter pic_adapter;
    private List<Picture> picsList;

    //Receiving extra
    private final String EXTRA_CID = "challid";
    private final String EXTRA_CNAME = "challname";
    private final String EXTRA_DESC = "description";

    //Sending extra
    private final String EXTRA_PID = "picid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        Intent intent = getIntent();
        int id = intent.getIntExtra(EXTRA_CID,0);
        String name = intent.getStringExtra(EXTRA_CNAME);
        String desc = intent.getStringExtra(EXTRA_DESC);

        //initiate the RecyclerView and the adapter
        list_pics = (RecyclerView) findViewById(R.id.rv_pics);

        picsList = new ArrayList<>();
        pic_adapter = new PictureAdapter(this, picsList);

        list_pics.setLayoutManager(new GridLayoutManager(this, 2));
        list_pics.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        list_pics.setItemAnimator(new DefaultItemAnimator());
        list_pics.setAdapter(pic_adapter);


        challenge_name = (TextView) findViewById(R.id.chal_name);
        challenge_name.setText(name);
        /*
        challenge_desc = (TextView) findViewById(R.id.chal_desc);
        challenge_desc.setText(desc);
        */
        list_pics.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), list_pics, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Picture p = picsList.get(position);
                Toast.makeText(getApplicationContext(), "This pis was done by: " + p.getOwner(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Picture p = picsList.get(position);
                Toast.makeText(getApplicationContext(), "This pis was done by: " + p.getOwner(), Toast.LENGTH_SHORT).show();
            }
        }));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Camera openinig", Toast.LENGTH_SHORT).show();
            }
        });

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
        if (name.equals("The Handmaid's Tale")) {
            int[] pics = new int[]{R.drawable.ht1,R.drawable.ht2,R.drawable.ht3,R.drawable.ht4,R.drawable.ht5};
            Picture p1 = new Picture(pics[0], "ht1", "miamigo", "The Handmain's Tale", 0, 0);
            picsList.add(p1);
            Picture p2 = new Picture(pics[1], "ht2", "yo", "The Handmain's Tale", 2.4, 5);
            picsList.add(p2);
            Picture p3 = new Picture(pics[2], "ht3", "tumama", "The Handmain's Tale", 5, 4);
            picsList.add(p3);
            Picture p4 = new Picture(pics[3], "ht4", "iueputa", "The Handmain's Tale", 3, 5);
            picsList.add(p4);
            Picture p5 = new Picture(pics[4], "ht5", "miamigo", "The Handmain's Tale", 4.6, 6);
            picsList.add(p5);
        }
        else {
            int[] pics = new int[]{R.drawable.gr1,R.drawable.gr2,R.drawable.gr3,R.drawable.gr4};
            Picture p1 = new Picture(pics[0], "gr1", "starlord", "Groot", 5, 3);
            picsList.add(p1);
            Picture p2 = new Picture(pics[1], "gr2", "gamora", "Groot", 2, 5);
            picsList.add(p2);
            Picture p3 = new Picture(pics[2], "gr3", "thanos", "Groot", 4, 4);
            picsList.add(p3);
            Picture p4 = new Picture(pics[3], "gr4", "rocket", "Groot", 3, 4);
            picsList.add(p4);
        }

        pic_adapter.notifyDataSetChanged();
    }
}


