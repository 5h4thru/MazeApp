package dxp141030.utdallas.edu.mazeapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import dxp141030.utdallas.edu.mazeapp.models.Score;
import dxp141030.utdallas.edu.mazeapp.util.FileUtil;
import dxp141030.utdallas.edu.mazeapp.util.ScoreComparator;
import dxp141030.utdallas.edu.mazeapp.util.TimerUtil;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */

// This activity class will be launched when Game is successfully completed
public class SuccessActivity extends Activity implements OnClickListener {

    // Variables to hold the results to display when the game is completed
    private Button buttonHome;
    private TextView textViewTime;
    private TextView textViewTitle;
    private Typeface titleFont;
    private EditText editTextName;
    private Long timeElapsed = 0L;

    private FileUtil fileUtil;
    private ArrayList<Score> scoresList;
    private StringBuilder strBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        buttonHome = (Button) findViewById(R.id.buttonHome);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonHome.setOnClickListener(this);

        // FileUtil to write the values to a file. The values written to the file are the name and time taken to complete the game
        fileUtil = new FileUtil();
        scoresList = new ArrayList<Score>();
        strBuilder = new StringBuilder();

        // Set Custom Typeface to the title
        titleFont = Typeface.createFromAsset(getAssets(), "fonts/title_font.ttf");
        textViewTitle.setTypeface(titleFont);

        // Show the formatted timeElapsed value in textView
        try {
            timeElapsed = getIntent().getLongExtra("timeElapsed", 0L);
            textViewTime.setText(new TimerUtil().formatTime(timeElapsed));
        } catch (Exception e) {
            // Logger class will be implemented if the time permits. Let us print the stackTrace for now
            e.printStackTrace();
        }
    }

    /*Handle Button Onclick*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonHome:
                if (editTextName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Name to submit", Toast.LENGTH_SHORT).show();
                } else {
                    scoresList = fileUtil.fetchHighScores();
                    scoresList.add(new Score(editTextName.getText().toString(), timeElapsed));

                /*Sort the List before writing*/
                    Collections.sort(scoresList, new ScoreComparator());

                    int size = 10;
                /*Write only 10 top scores*/
                    if (scoresList.size() < 10) {
                        size = scoresList.size();
                    }

                    for (int i = 0; i < size; i++) {
                        strBuilder.append(scoresList.get(i).getName());
                        strBuilder.append("\t");
                        strBuilder.append(scoresList.get(i).getScoreTime());
                        strBuilder.append(System.lineSeparator());
                    }

                /*Write to file*/
                    fileUtil.writeToFile(strBuilder.toString());
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
