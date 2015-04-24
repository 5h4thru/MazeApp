package dxp141030.utdallas.edu.mazeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */

// Main Launcher Activity
public class MainActivity extends Activity implements OnClickListener {

    // Variables used to display the options in the beginning of the game
    private Button buttonPlay, buttonScores, buttonSettings;
    private TextView textViewTitle;
    private Typeface titleFont;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonScores = (Button) findViewById(R.id.buttonScores);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        
        // Set Custom Typeface to the TitleText
        titleFont = Typeface.createFromAsset(getAssets(), "fonts/title_font.ttf");
        textViewTitle.setTypeface(titleFont);
        
        // Handle button Clicks
        buttonPlay.setOnClickListener(this);
        buttonScores.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
    }

    // On Button click start activities
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()) {
        case R.id.buttonPlay:
            Intent toGameActivity = new Intent(this, GameActivity.class);
            startActivity(toGameActivity);
            break;
        case R.id.buttonScores:
            Intent toViewScoresActivity = new Intent(this, ViewScoresActivity.class);
            startActivity(toViewScoresActivity);
            break;
        case R.id.buttonSettings:
            Intent toSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(toSettingsActivity);
            break;
        }
    }
}
