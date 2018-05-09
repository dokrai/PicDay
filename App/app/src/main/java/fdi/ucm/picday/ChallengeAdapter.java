package fdi.ucm.picday;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {

    private List<Challenge> challengeList; //our list of challenges

    //references to the visual components
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView challenge,desc; // only a TextView is shown in this case

        public ViewHolder(View view) {
            super(view);
            challenge = (TextView) view.findViewById(R.id.chal_name);
            desc = (TextView) view.findViewById(R.id.chal_desc);
        }
    }

    public ChallengeAdapter(List<Challenge> cDataSet) {
        this.challengeList = cDataSet;
    }

    public ChallengeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //creating the new list
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_list_row, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        Challenge c = challengeList.get(position);
        holder.challenge.setText(c.getName());
        holder.desc.setText(c.getDescription());
    }

    public int getItemCount() {return challengeList.size();}
}
