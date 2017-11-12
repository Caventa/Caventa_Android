package caventa.ansheer.ndk.caventa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Work_Advance;

/**
 * Created by prism on 01-11-2017.
 */
public class Work_Advances_View_Adapter extends RecyclerView.Adapter<Work_Advances_View_Adapter.ViewHolder> {

    private Context mContext;
    private List<Work_Advance> work_advances;
    public static boolean delete_status=false;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        public TextView mTextView;
//        public ViewHolder(TextView v) {
//            super(v);
//            mTextView = v;
//        }

        public TextView description_amount;
//        public ImageView delete_icon;

        public ViewHolder(View view) {
            super(view);
            description_amount = view.findViewById(R.id.description_amount);
//            count = (TextView) view.findViewById(R.id.count);
//            delete_icon = view.findViewById(R.id.delete_icon);
//            edit_icon = (ImageView) view.findViewById(R.id.edit_icon);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Work_Advances_View_Adapter(Context mContext, List<Work_Advance> work_advances) {
        this.mContext = mContext;
        this.work_advances = work_advances;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Work_Advances_View_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

//        ViewHolder vh = new ViewHolder(v);
//        return vh;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_work_advance_view, parent, false);

        return new Work_Advances_View_Adapter.ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.mTextView.setText(mDataset[position]);

        Work_Advance work_advance = work_advances.get(position);
        holder.description_amount.setText(work_advance.getDescription() + " - " + work_advance.getAmount());
//        holder.count.setText(album.getNumOfSongs() + " songs");

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

//        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(General_Data.TAG, "Delete");
//                work_advances.remove(position);
//                notifyItemRemoved(position);
//
//            }
//        });

//        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(General_Data.TAG,"Delete");
//                delete_status=true;
//            }
//        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return work_advances.size();
    }
}
