package caventa.ansheer.ndk.caventa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Work_Advance;

public class Work_Advances_Adapter extends RecyclerView.Adapter<Work_Advances_Adapter.ViewHolder> {

    private List<Work_Advance> work_advances;
    public static boolean delete_status=false;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description_amount;
        ImageView delete_icon, edit_icon;

        ViewHolder(View view) {
            super(view);
            description_amount = view.findViewById(R.id.description_amount);
            delete_icon = view.findViewById(R.id.delete_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Work_Advances_Adapter(Context mContext, List<Work_Advance> work_advances) {
        this.work_advances = work_advances;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Work_Advances_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_work_advance, parent, false);

        return new Work_Advances_Adapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Work_Advance work_advance = work_advances.get(position);
        holder.description_amount.setText(work_advance.getDescription() + " - " + work_advance.getAmount());
//        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(General_Data.TAG,"Delete");
//                delete_status=true;
//            }
//        });

    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return work_advances.size();
    }
}
