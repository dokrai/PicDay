package fdi.ucm.picday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    private Context myContext;
    private List<Picture> pictureList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, rate;
        public ImageView pic;

        public ViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.user_name);
            rate = (TextView) view.findViewById(R.id.rate);
            pic = (ImageView) view.findViewById(R.id.pic);
        }
    }


    public PictureAdapter(Context myContext, List<Picture> pictures) {
        this.myContext = myContext;
        this.pictureList = pictures;
    }

    @Override
    public PictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picture pic = pictureList.get(position);
        holder.user_name.setText(pic.getOwner());
        holder.rate.setText(pic.getScore() + "/5.0");

      final ImageRequest imageRequest = new ImageRequest(pic.getDir(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        holder.pic.setImageBitmap(response);

                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(myContext).addToRequestQueue(imageRequest);
    }


    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
