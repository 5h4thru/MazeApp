package dxp141030.utdallas.edu.mazeapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import dxp141030.utdallas.edu.mazeapp.models.Score;
import dxp141030.utdallas.edu.mazeapp.util.CustomAdapter;
import dxp141030.utdallas.edu.mazeapp.util.FileUtil;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */

// This activity class will be launched for viewing the high scores
public class ViewScoresActivity extends Activity {

    // Variables to display the necessary details in the hall-of-fame page
    private ListView listViewScores;
    private ProgressBar progressBar;
    private TextView textViewNoScores;
    private TextView textViewTitle;
    private Typeface titleFont;
    private ArrayList<Score> scoresList;
    private CustomAdapter adapter;
    private FileUtil fileUtil;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        listViewScores = (ListView) findViewById(R.id.listViewScores);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewNoScores = (TextView) findViewById(R.id.textViewNoScores);
        
        // Set Custom Typeface to the title text
        titleFont = Typeface.createFromAsset(getAssets(), "fonts/title_font.ttf");
        textViewTitle.setTypeface(titleFont);
        
        scoresList = new ArrayList<Score>();
        fileUtil = new FileUtil();
    
        // Fetch from file in an AsyncTask
        new FetchScoresTask().execute();
    }

    // AsyncTask to fetch values from file
    private class FetchScoresTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // Fetch High scores and set it to CustomAdapter
            scoresList = fileUtil.fetchHighScores();
            adapter = new CustomAdapter(ViewScoresActivity.this, scoresList);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Hide the ProgressBar
            progressBar.setVisibility(View.GONE);
            // If no scores are available, display a nice message
            if(scoresList.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Scores Available", Toast.LENGTH_SHORT).show();
                textViewNoScores.setVisibility(View.VISIBLE);
            } else {
                // Set list adapter
                listViewScores.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
