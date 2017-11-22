package caventa.ansheer.ndk.caventa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Work_Overview;


public class Work_Overview_Adapter extends RecyclerView.Adapter<Work_Overview_Adapter.ViewHolder> {

    private List<Work_Overview> work_overviews;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView work_address, work_profit;
        public TextView work_name;

        public ViewHolder(View view) {
            super(view);
            work_name = view.findViewById(R.id.textView_work_name);
            work_address = view.findViewById(R.id.textView_work_address);
            work_profit = view.findViewById(R.id.textView_work_profit);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Work_Overview_Adapter(Context mContext, List<Work_Overview> work_overviews) {
        this.work_overviews = work_overviews;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Work_Overview_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View item_View = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_work_overview, parent, false);

        return new Work_Overview_Adapter.ViewHolder(item_View);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position

        Work_Overview work_overview = work_overviews.get(position);
        holder.work_name.setText(work_overview.getWork_name());
        holder.work_address.setText(work_overview.getWork_address());
        holder.work_profit.setText("Pro. "+String.valueOf(work_overview.getTotal_advance())+String.valueOf(work_overview.getTotal_expense())+String.valueOf(work_overview.getTotal_advance()-work_overview.getTotal_expense()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return work_overviews.size();
    }
}
