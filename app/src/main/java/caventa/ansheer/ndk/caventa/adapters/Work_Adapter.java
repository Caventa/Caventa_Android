package caventa.ansheer.ndk.caventa.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Work;
import ndk.utils.Date_Utils;


public class Work_Adapter extends RecyclerView.Adapter<Work_Adapter.MyViewHolder> {

    private List<Work> work_List;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            genre = view.findViewById(R.id.genre);
            year = view.findViewById(R.id.year);
        }
    }


    public Work_Adapter(List<Work> work_List) {
        this.work_List = work_List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Work work = work_List.get(position);
        holder.title.setText(work.getWork_name());
        holder.genre.setText(Date_Utils.normal_Date_Format_words.format(work.getWork_date()));
        holder.year.setText("1995");
    }

    @Override
    public int getItemCount() {
        return work_List.size();
    }

//    @Override
//    public int getItemCount() {
//        return work_List.size();
//    }
}