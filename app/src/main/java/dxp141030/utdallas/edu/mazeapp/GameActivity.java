package dxp141030.utdallas.edu.mazeapp;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import dxp141030.utdallas.edu.mazeapp.views.MazeMainView;


/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


// Main Game Activity
public class GameActivity extends Activity implements OnClickListener {

    // Initialize the MazeMainView that handles the logic for the game
    private MazeMainView mazeMainView;
    private TextView textViewTime;
    private Button buttonStart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        // Get width and height of the Screen using point
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        
        mazeMainView = (MazeMainView) findViewById(R.id.mazeMainView);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        mazeMainView.initialize(this, point.x, point.y, textViewTime);
    
        buttonStart.setOnClickListener(this);
    }

    // Handle onResume
    @Override
    protected void onResume() {
        super.onResume();
        mazeMainView.resume();
    }

    // Handle onPause
    @Override
    protected void onPause() {
        super.onPause();
        mazeMainView.pause();
    }

    // Handle start button onClick
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.buttonStart:
            // Start Timer on button click
            mazeMainView.startTimer();
            buttonStart.setEnabled(false); //Disable the button once you start the game
            break;
        }
    }
    
}
