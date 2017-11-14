package caventa.ansheer.ndk.caventa.adapters.extra;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.extra.Commision;


public class Commision_Adapter extends RecyclerView.Adapter<Commision_Adapter.ViewHolder> {

    private List<Commision> commisions;

    public Commision_Adapter(List<Commision> commisions) {
        this.commisions = commisions;

    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.tv_work_amount.setText(commisions.get(position).getWork()+" : "+ commisions.get(position).getCommision());
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return commisions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_work_amount;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tv_work_amount = (TextView) itemLayoutView.findViewById(R.id.tvName);
        }

    }

    // method to access in activity after updating selection
    public List<Commision> getStudentist() {
        return commisions;
    }

}