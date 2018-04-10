package com.moviedirectory.devsoftware.moviedirectory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moviedirectory.devsoftware.moviedirectory.Model.Movie;
import com.moviedirectory.devsoftware.moviedirectory.R;
import com.moviedirectory.devsoftware.moviedirectory.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {
    private Movie movie;
    private TextView movieTitle, movieYear, director, actors, category, rating, writers, plot, boxoffice, runTime;
    private ImageView moviePoster;
    private RequestQueue queue;
    private String movieID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        queue = Volley.newRequestQueue(this);
        
        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieID = movie.getImdbId();

        setUpUi();
        getMovieDetails(movieID);


    }

    private void getMovieDetails(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL + id + Constants.URL_Right,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            if(response.has("Ratings"))
                            {
                                JSONArray ratings = response.getJSONArray("Ratings");

                                String source  = null;
                                String value = null;
                                if(ratings.length() > 0)
                                {
                                    JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);
                                    source = mRatings.getString("Source");
                                    value = mRatings.getString("Value");
                                    rating.setText(source + ": " + value);
                                }
                                else
                                {
                                    rating.setText("Ratings: Not Applicable");
                                }

                                movieTitle.setText("Movie Title: "+response.getString("Title"));
                                movieYear.setText("Release Year: "+response.getString("Year"));
                                director.setText("Director: "+response.getString("Director"));
                                writers.setText("Writer: "+response.getString("Writer"));
                                plot.setText("Plot: "+response.getString("Plot"));
                                runTime.setText("Runtime: "+ response.getString("Runtime"));
                                actors.setText("Actors: "+ response.getString("Actors"));

                                Picasso.with(getApplicationContext())
                                        .load(response.getString("Poster"))
                                        .into(moviePoster);

                                boxoffice.setText("Box Office Collection :" + response.getString("BoxOffice"));
                            }

                        }
                        catch (JSONException e)
                        {
                            VolleyLog.d("Error: ", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        queue.add(jsonObjectRequest);

    }

    private void setUpUi() {
        movieTitle = (TextView) findViewById(R.id.movieTitleDets);
        movieYear = (TextView) findViewById(R.id.movieReleaseDets);
        director = (TextView) findViewById(R.id.movieDirectedDets);
        actors = (TextView) findViewById(R.id.actorsDets);
        category = (TextView) findViewById(R.id.movieCatDets);
        rating = (TextView) findViewById(R.id.movieRatingDets);
        writers = (TextView) findViewById(R.id.writersDets);
        plot = (TextView) findViewById(R.id.ploatDets);
        boxoffice = (TextView) findViewById(R.id.boxOfficeDets);
        runTime = (TextView) findViewById(R.id.movieRuntimeDets);
        moviePoster = (ImageView) findViewById(R.id.imagedet);
    }
}
