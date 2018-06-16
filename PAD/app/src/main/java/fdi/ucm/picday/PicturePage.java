package fdi.ucm.picday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicturePage extends Activity {

    private ImageView picture;
    private TextView user;
    private RatingBar rate;
    private ImageView save;

    //Receiving extra
    private final String EXTRA_PID = "picid";
    private final String EXTRA_OWNER = "username";
    private final String EXTRA_SCORE = "score";
    private final String EXTRA_TIMES = "timesscored";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picpage);

        Intent intent = getIntent();
        final int pid = intent.getIntExtra(EXTRA_PID,0);
        String username = intent.getStringExtra(EXTRA_OWNER);
        final float score = intent.getFloatExtra(EXTRA_SCORE, 0);
        final int times_scored = intent.getIntExtra(EXTRA_TIMES,0);

        Resources res = getApplicationContext().getResources();
        picture = (ImageView) findViewById(R.id.pic);
        final Bitmap bMap = BitmapFactory.decodeResource(res,pid);
        picture.setImageBitmap(bMap);

        user = (TextView) findViewById(R.id.doneBy);
        user.setText(username);

        rate = (RatingBar) findViewById(R.id.score);
        rate.setRating(score);
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                float new_score = (score + rating)/(times_scored+1);
                //update database
                rate.setIsIndicator(true);
            }
        });

        save = (ImageView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bMap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    FileOutputStream outputStream = getApplicationContext().openFileOutput(Integer.toString(pid)+".png", Context.MODE_PRIVATE);
                    outputStream.write(byteArray);
                    outputStream.close();
                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
    }
}
