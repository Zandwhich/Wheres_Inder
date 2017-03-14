package com.example.alex.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.example.alex.myapplication.R.drawable.amazon_prime_app_logo;
import static com.example.alex.myapplication.R.drawable.imdb_app_logo;
import static com.example.alex.myapplication.R.drawable.netflix_app_logo;

public class MainActivity extends AppCompatActivity {

    String movieID = "";
    Random generator = new Random();

    ArrayList<String> allIDs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allIDs.add("tt2109248");
        allIDs.add("tt0068646");
        allIDs.add("tt0107290");
        allIDs.add("tt0369610");
        allIDs.add("tt0000001");
        allIDs.add("tt0078748");
        allIDs.add("tt0211668");
        allIDs.add("tt1739212");
        allIDs.add("tt1316037");
        allIDs.add("tt1674047");
        allIDs.add("tt1612774");
        allIDs.add("tt0109830");
        allIDs.add("tt0076759");
        allIDs.add("tt0080684");
        allIDs.add("tt0086190");
        allIDs.add("tt0120915");
        allIDs.add("tt0121765");
        allIDs.add("tt0121766");
        allIDs.add("tt5613052");
        allIDs.add("tt0120737");

        movieID = allIDs.get(generator.nextInt(allIDs.size()));


        JSONApiWrapper.get(("http://www.omdbapi.com/?i=" + movieID), new JSONObjLambda() {
            @Override
            public void onData(JSONObject o) {
                try {
                    //Toast.makeText(getApplicationContext(), o.getString("Title"), Toast.LENGTH_LONG).show();

                    TextView name = (TextView) findViewById(R.id.MovieName);
                    name.setText(o.getString("Title"));

                    TextView genre = (TextView) findViewById(R.id.Genre);
                    genre.setText(o.getString("Genre"));

                    TextView summary = (TextView) findViewById(R.id.Summary);
                    summary.setText(o.getString("Plot"));

                    TextView starring = (TextView) findViewById(R.id.Starring);
                    String actor = o.getString("Actors");
                    for (int i = 0; i < actor.length() - 1; i++) {
                        if (actor.substring(i, i+1).equals(",")){
                            actor = actor.substring(0, i);
                        }//end if
                    }//end for i
                    starring.setText(actor);

                    TextView rating = (TextView) findViewById(R.id.Rating);
                    rating.setText(o.getString("imdbRating"));

                    JSONApiWrapper.getImage(o.getString("Poster"), new ImageLambda() {
                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getApplicationContext(), "Couldn't get poster", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onData(Drawable img) {
                            ImageView poster = (ImageView) findViewById(R.id.Poster);
                            poster.setImageDrawable(img);
                            poster.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e){

            }
        });
    }

    public void goToImdb(View view){
        Uri webpage = Uri.parse("http://imdb.com/title/" + movieID);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    public void onClick(View view){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
