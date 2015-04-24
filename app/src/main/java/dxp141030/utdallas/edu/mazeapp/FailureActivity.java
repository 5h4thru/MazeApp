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

// This activity class will be launched when Game Fails to be completed
public class FailureActivity extends Activity implements OnClickListener {

    // Variables to display options to the user once the game fails
    private Button buttonTryAgain, buttonHome;
    private TextView textViewTitle;
    private Typeface titleFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure); // Bring forth the layout designed to handle failure of the game

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        buttonTryAgain = (Button) findViewById(R.id.buttonTryAgain);
        buttonHome = (Button) findViewById(R.id.buttonHome);
    
        // Set Custom Typeface to the title
        titleFont = Typeface.createFromAsset(getAssets(), "fonts/title_font.ttf"); //Bringing the fonts as a part of the code will help in future modifications. Will be followed everywhere
        textViewTitle.setTypeface(titleFont);

        buttonTryAgain.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
    }

    // Handle button Onclick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonTryAgain:
                Intent toGameActivity = new Intent(this, GameActivity.class);
                startActivity(toGameActivity);
                break;
            case R.id.buttonHome:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
