package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

import static com.example.flixster.MainActivity.NOW_PLAYING_URL;

public class MovieDetailsActivity extends AppCompatActivity {

    //the movie to display
    Movie movie;

    //the view objects
//    TextView tvTitle;
//    TextView tvOverview;
//    RatingBar rbVoteAverage;

    ActivityMovieDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //setContentView(R.layout.activity_movie_details);
//        tvTitle = (TextView)findViewById(R.id.tvTitle);
//        tvOverview = (TextView)findViewById(R.id.tvOverview);
//        rbVoteAverage = (RatingBar)findViewById(R.id.rbVoteAverage);


        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage / 2.0f);

        Glide.with(this)
                .load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(10, 9))
                .into(binding.imageView);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("youtube", "inside");
                AsyncHttpClient client = new AsyncHttpClient();
                String NOW_MOVIE_URL = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=4dd9f6daa6f53fbe4c664ee15bdbee3e&language=en-US";
                client.get(NOW_MOVIE_URL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d("MovieDetailsActivity", "onSuccess");

                        JSONObject jsonObject = json.jsonObject;

                        try {
                            JSONArray arrayM = jsonObject.getJSONArray("results");
                            String valKey = arrayM.getJSONObject(0).getString("key");
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("key", valKey);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d("MovieDetailsActivity", "onFailure");
                    }
                });

            }

        });
    }


}