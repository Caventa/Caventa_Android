package caventa.ansheer.ndk.caventa.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.constants.General_Data;
import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Sales_Person;

import static com.koushikdutta.ion.Ion.with;

public class Sales_Person_Adapter extends RecyclerView.Adapter<Sales_Person_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Sales_Person> sales_persons;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.thumbnail);

        }
    }


    public Sales_Person_Adapter(Context mContext, List<Sales_Person> sales_persons) {
        this.mContext = mContext;
        this.sales_persons = sales_persons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_sales_person, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Sales_Person album = sales_persons.get(position);
        holder.title.setText(album.getName());

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        // This is the "long" way to do build an ImageView request... it allows you to set headers, etc.
        with(mContext)
                .load("http://" + General_Data.SERVER_IP_ADDRESS + "/icons/" + album.getId() + ".jpg")
                .withBitmap()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
//                .animateLoad(spinAnimation)
//                .animateIn(fadeInAnimation)
                .intoImageView(holder.thumbnail);


    }


    @Override
    public int getItemCount() {
        return sales_persons.size();
    }
}
