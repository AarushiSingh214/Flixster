package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.GlideApp;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{


    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    //Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateView");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false );
        return new ViewHolder(movieView);

    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        //Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview  =itemView.findViewById(R.id.tvOverview);
            ivPoster  =itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {
           //gets item position
           //int position = getAbsoluteAdapterPosition();
           int position = getAdapterPosition();

           //make sure the position is valid, i.e. actually exists in the view
            if(position!= RecyclerView.NO_POSITION){
                //get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                //create intent for the new activity
                Intent intent  = new Intent(context, MovieDetailsActivity.class);
                //serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //show activity
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int picture;

            //if phone is in landscape
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                picture = R.drawable.flicks_backdrop_placeholder;
            }else{
                imageUrl = movie.getPosterPath();
                picture = R.drawable.flicks_movie_placeholder;

            }

            //Glide.with(context).load(imageUrl).into(ivPoster);

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10; // crop margin, set to 0 for corners with no crop

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(picture)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);

        }
    }
}
