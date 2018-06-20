package fdi.ucm.picday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private List<User> usersList; //our list of challenges

    //references to the visual components
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name,points; // only a TextView is shown in this case

        public ViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.usname);
            points = (TextView) view.findViewById(R.id.uspoints);
        }
    }

    public UserAdapter(List<User> cDataSet) {
        this.usersList = cDataSet;
    }

    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //creating the new list
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);

        UserAdapter.ViewHolder vh = new UserAdapter.ViewHolder(itemView);
        return vh;
    }

    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        User u = usersList.get(position);
        holder.user_name.setText(u.getUser_name());
        holder.points.setText(Integer.toString(u.getPoints())+ " points");
}

    public int getItemCount() {return usersList.size();}
}
