package com.moviedirectory.devsoftware.moviedirectory.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moviedirectory.devsoftware.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.moviedirectory.devsoftware.moviedirectory.Model.Movie;
import com.moviedirectory.devsoftware.moviedirectory.R;
import com.moviedirectory.devsoftware.moviedirectory.Util.Constants;
import com.moviedirectory.devsoftware.moviedirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialog ;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        movieList = new ArrayList<>();
        //getMovies(search);
        movieList = getMovies(search);
           movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
            recyclerView.setAdapter(movieRecyclerViewAdapter);
            movieRecyclerViewAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog()
    {
        alertDialog = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialogueview, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialog.setView(view);
        dialog = alertDialog.create();

        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs prefs = new Prefs(MainActivity.this);

                if(!newSearchEdt.getText().toString().isEmpty())
                {
                    String search = newSearchEdt.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();
                    dialog.dismiss();
                    getMovies(search);
                }

            }
        });

    }

    //Get Movies

    public List<Movie> getMovies(String searchTerm)
    {
        movieList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_Right,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {

                            JSONArray movieArray = response.getJSONArray("Search");

                            for(int i = 0 ; i < movieArray.length(); i++)
                            {
                                JSONObject movieObject = movieArray.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setTitle(movieObject.getString("Title"));
                                movie.setYear("Year Released: " + movieObject.getString("Year"));
                                movie.setMovieType("Type: " + movieObject.getString("Type"));
                                movie.setPoster(movieObject.getString("Poster"));
                                movie.setImdbId(movieObject.getString("imdbID"));

                                Log.d("MovieList: " , movie.getTitle());
                                movieList.add(movie);
                            }/*
                            *
                            * Important Very Important Very Very Important!! Otherwise we wont see anything displayed.
                            * */

                            movieRecyclerViewAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
        return movieList;
    }
}
