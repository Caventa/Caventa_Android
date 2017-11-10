package caventa.ansheer.ndk.caventa.adapters;

/**
 * Created by prism on 24-10-2017.
 */

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import caventa.ansheer.ndk.caventa.R;
import caventa.ansheer.ndk.caventa.models.Movie;

public class MoviesAdapter_Color extends RecyclerView.Adapter<MoviesAdapter_Color.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public MoviesAdapter_Color(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(movie.getYear());
        if(position %2 == 1)
        {
            // Set a background color for ListView regular row/item
            holder.itemView.setBackgroundColor(Color.RED);
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}