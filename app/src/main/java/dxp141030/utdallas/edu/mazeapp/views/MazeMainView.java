package dxp141030.utdallas.edu.mazeapp.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.TextView;

import dxp141030.utdallas.edu.mazeapp.FailureActivity;
import dxp141030.utdallas.edu.mazeapp.R;
import dxp141030.utdallas.edu.mazeapp.SuccessActivity;
import dxp141030.utdallas.edu.mazeapp.models.Border;
import dxp141030.utdallas.edu.mazeapp.models.Ball;
import dxp141030.utdallas.edu.mazeapp.util.TimerUtil;

/**
 * @author Durga Sai Preetham Palagummi
 * NetID dxp141030
 * Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 24th April 2015
 */

/* MazeMainView class is utilized as the Main Game View that extends SurfaceView,
 * implements Runnable for Thread, SensorEvents, Callback, etc...
 * */
public class MazeMainView extends SurfaceView implements Runnable, SensorEventListener, Callback {

    // Variable that helps in communication with sensorManager object
    private SensorManager sensorManager;

    // Main thread for the game
    Thread t = null;
    SurfaceHolder holder;

    // Flag to maintain the thread state
    boolean isThreadRunning = true;

    private Context context;

    // Game View Objects
    private Canvas canvas;
    private Border border1, border2, border3, border4, border5;
    private Ball ball;

    // X and Y positions of the ball
    private float x, y;
    // Height and Width of the Screen/Canvas
    private float height, width;
    // Acceleration from the Sensor data
    private float accelerationX, accelerationY;
    // Entry point for the ball
    private float gateGap; // Will be helpful in defining the exit point as well
    // Define margins and start, stop of X and Y for border positions
    private float leftMargin, rightMargin, topMargin, bottomMargin;
    private float border1StartX, border1StartY, border1StopX, border1StopY;
    private float border2StartX, border2StartY, border2StopX, border2StopY;
    private float border3StartX, border3StartY, border3StopX, border3StopY;
    private float border4StartX, border4StartY, border4StopX, border4StopY;
    private float border5StartX, border5StartY, border5StopX, border5StopY;
    // Ball radius
    private float ballRadius;
    // Start Position of the ball
    private float[] ballStartPosition;

    // Variable that helps to move the ball faster, accelerationX and accelerationY times this value
    private float accelerationRate;
    // Various Flags to use in Game Logic
    private boolean hasEntered = false;
    private boolean isGameFinished = false;
    private boolean isPenaltyReduced = false;
    private boolean isTimerStarted = false;
    private boolean isSoundEnabled = false;
    private boolean isInterrupted = false;
    // Game Difficulty from SharedPreferences
    private String gameDifficulty;

    // Synchronized(Object) to send
    private Object TIMER_LOCK = new Object();

    // Values for timer. Initialize to 0
    private long startTime = 0L, timeDiffMil = 0L, tempTime = 0L;

    // Initialize objects for vibration, game sounds, timer, etc...
    private Vibrator vibrator;
    private MediaPlayer wallMedia, borderMedia;
    private TextView textViewTime;
    private Handler handler;
    private TimerUtil timerUtil;

    private SharedPreferences sharedPreferences;

    // Constructor
    public MazeMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Get the Holder and addCallback on start
        getHolder().addCallback(this);
    }

    // Initialize various objects, timer textView and get height and width
    public void initialize(Context context, int width, int height, TextView textViewTime) {
        this.context = context;
        this.height = height;
        this.width = width;
        holder = getHolder();
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        initializeCoordinates(); //Initialize Object Coordinates
        initializeObjects(); //Initialize Game View Objects
        initializeMedia(); //Initialize Media and Vibrator
        initializeGamePrefs(); //Fetch Game Preferences from SharedPreferences. Will be useful while setting the game speed based on the difficulty selected
        this.textViewTime = textViewTime;
    }

    // Fetch Game Preferences from SharedPreferences
    private void initializeGamePrefs() {
        sharedPreferences = context.getSharedPreferences("GAME_PREFS", Context.MODE_PRIVATE);
        isSoundEnabled = sharedPreferences.getBoolean("sound", true);
        gameDifficulty = sharedPreferences.getString("difficulty", "normal"); // Default game speed will be normal

        // Check game difficulty from SharedPreferences to set accelerationRate
        if (gameDifficulty.equals("easy")) {
            accelerationRate = 1.45F;
        } else if (gameDifficulty.equals("hard")) {
            accelerationRate = 2.90F;
        } else {
            accelerationRate = 1.80F;
        }
    }

    // Start Timer when start button is pressed
    public void startTimer() {
        startTime = SystemClock.uptimeMillis(); // Get Start Time and store it for later use
        handler = new Handler(); // Handler to update TextView
        timerUtil = new TimerUtil();
        handler.postDelayed(updateTimer, 0); // Start Handler to update timer textView
        isTimerStarted = true; // Set Timer started to true
    }

    // Initialize Media and Vibrator
    private void initializeMedia() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        wallMedia = MediaPlayer.create(context, R.raw.wall_hit);
        borderMedia = MediaPlayer.create(context, R.raw.border_hit);
    }

    // Initialize Object Coordinates
    private void initializeCoordinates() {

        // Set ballStartPosition as [x,y]
        ballStartPosition = new float[]{width / 2, height - 50};

        x = ballStartPosition[0];
        y = ballStartPosition[1];

        // Calculate ballRadius using width of Screen
        ballRadius = width / 45;

        // Calculate gateGap based on the ballRadius
        gateGap = (float) (ballRadius * 2.5);

        // Margins for the borders
        leftMargin = rightMargin = width / 7;
        topMargin = height / 10;
        bottomMargin = height / 7;

        // Set border1 StartX, StartY, StopX and StopY
        border1StartX = leftMargin;
        border1StartY = topMargin;
        border1StopX = width - rightMargin - gateGap;
        border1StopY = topMargin;

        // Set border2 StartX, StartY, StopX and StopY
        border2StartX = leftMargin;
        border2StartY = topMargin;
        border2StopX = leftMargin;
        border2StopY = height - bottomMargin;

        // Set border3 StartX, StartY, StopX and StopY
        border3StartX = width - rightMargin;
        border3StartY = topMargin;
        border3StopX = width - rightMargin;
        border3StopY = height - bottomMargin;

        // Set border4 StartX, StartY, StopX and StopY
        border4StartX = leftMargin;
        border4StartY = height - bottomMargin;
        border4StopX = (width / 2) - gateGap / 2;
        border4StopY = height - bottomMargin;

        // Set border5 StartX, StartY, StopX and StopY*/
        border5StartX = width - rightMargin;
        border5StartY = height - bottomMargin;
        border5StopX = (width / 2) + gateGap / 2;
        border5StopY = height - bottomMargin;

    }

    // Initialize the various Game View objects
    private void initializeObjects() {
        ball = new Ball(context);

        // Draw borders based on (x1,y1) and (x2,y2) points
        border1 = new Border(context, border1StartX, border1StartY, border1StopX, border1StopY);
        border2 = new Border(context, border2StartX, border2StartY, border2StopX, border2StopY);
        border3 = new Border(context, border3StartX, border3StartY, border3StopX, border3StopY);
        border4 = new Border(context, border4StartX, border4StartY, border4StopX, border4StopY);
        border5 = new Border(context, border5StartX, border5StartY, border5StopX, border5StopY);

    }

    // Thread run()
    @Override
    public void run() {
        while (isThreadRunning) {
            // Check if holder Surface is valid so that we can proceed
            if (holder.getSurface().isValid()) {
                // Get Lock on Canvas for drawing
                canvas = holder.lockCanvas();
                // Set Canvas Background
                canvas.drawColor(context.getResources().getColor(R.color.bg_color));

                // Draw borders
                border1.draw(canvas);
                border2.draw(canvas);
                border3.draw(canvas);
                border4.draw(canvas);
                border5.draw(canvas);

                // Create paint object to draw the game walls. We will hard code the walls for now
                Paint mazePaint = new Paint();
                mazePaint.setColor(context.getResources().getColor(R.color.wall_color));
                mazePaint.setStrokeWidth(5);
                canvas.drawLine(700F, 700F, 1400F, 700F, mazePaint);
                canvas.drawLine(700F, 700F, 700F, 925F, mazePaint);
                canvas.drawLine(500F, 500F, 1535F, 500F, mazePaint);
                canvas.drawLine(500F, 500F, 500F, 800F, mazePaint);
                canvas.drawLine(255F, 300F, 800F, 300F, mazePaint);
                canvas.drawLine(900F, 200F, 900F, 400F, mazePaint);
                canvas.drawLine(1000F, 300F, 1535F, 300F, mazePaint);

                // Change X and Y of the ball
                x += accelerationX;
                y += accelerationY;

                // Don't Let the ball go down the screen, Set base value
                if (y > ballStartPosition[1]) {
                    y = ballStartPosition[1];
                }

                // Draw the ball
                ball.draw(x, y, ballRadius - 5, canvas);

                // Check if the ball has entered the maze area
                if (y <= height - bottomMargin + 20 - ballRadius) {
                    hasEntered = true;
                } else {
                    hasEntered = false;
                }

                // Check collision with the walls
                if (collides()) {
                    handleCollision();
                }

                // Check Collision with the borders
                if (collidesBorder()) {
                    handleCollisionWithBorder();
                } else {
                    isPenaltyReduced = false;
                }

                /*
                Can use the following commented code to check the position of the ball for cross-verification
                 */
//                System.out.println("The position of ball is " + x + "The value of finish x is " + (width-rightMargin-gateGap));
//                System.out.println("The position of ball is " + y + "The value of finish y is " + topMargin);
                if (x >= (width - rightMargin - gateGap) && (y <= 150)) {
                    // Set thread state to false
                    isThreadRunning = false;
                    // Interrupt thread
                    t.interrupt();
                    if (!isGameFinished) {
                        // Start SuccessActivity, send elapsed time with Intent
                        Intent toSuccessActivity = new Intent(context, SuccessActivity.class);
                        toSuccessActivity.putExtra("timeElapsed", timeDiffMil);
                        context.startActivity(toSuccessActivity);
                        ((Activity) context).finish();
                        // Set GameFinished to true
                        isGameFinished = true;
                    }
                }
                // Unlock the canvas
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    // Handle what happens when ball collides with the border
    private void handleCollisionWithBorder() {
        if (!isPenaltyReduced) {
            if (isSoundEnabled) {
                // If Sound is Enabled in Preferences play sound on collision
                borderMedia.start();
            }

            if (vibrator.hasVibrator()) {
                // If device has vibrator vibrate for 100ms
                vibrator.vibrate(50);
            }


            // Reduce startTime by 5000ms to add penalty
            synchronized (TIMER_LOCK) {
                startTime -= 5000;
            }
            // Set penalty reduced to true
            isPenaltyReduced = true;
        }
    }

    // Check Collision with borders
    private boolean collidesBorder() {
        boolean collidesBorder = false;

        // Check if it collides with left border, if true then readjust ball position
        if (x <= (ballRadius + border2StartX)) {
            x = border2StartX + ballRadius;
            collidesBorder = true;
        }

        // Check if it collides with right border, if true then readjust ball position
        if (x >= (border3StartX - ballRadius)) {
            x = border3StartX - ballRadius;
            collidesBorder = true;
        }

        // Check if it collides with top border, if true then readjust ball position
        if (y <= (ballRadius + border1StartY)) {
            y = ballRadius + border1StartY;
            collidesBorder = true;
        }

        /*
         Check if it collides with left border, if true then readjust ball position.
         Check if the ball is not within the gateGap entry point. Check if ball is inside the area
         if true then readjust ball position
        */
        if (y >= border4StartY - ballRadius) {
            // Check if the ball is not within the gateGap entry point
            if (x >= border4StopX + 10 && x <= border5StopX - 10) {
                collidesBorder = false;
            } else {
                if (hasEntered) {
                    // Readjust ball if inside area
                    y = border4StartY - ballRadius;
                    collidesBorder = true;
                } else {
                    // If outside Readjust ball to initial value
                    // y = ballStartPosition[1]; Need not implement this. The other logic works well
                    y = border4StartY - ballRadius;
                    collidesBorder = false;
                }
            }
        }

        return collidesBorder;
    }

    // Handle collision with the walls
    private void handleCollision() {

        // Stop the Game by setting game state to false and interrupt thread
        isThreadRunning = false;
        t.interrupt();

        if (!isGameFinished) {
            if (isSoundEnabled) {
                // Play sound if sound is enabled in preferences
                wallMedia.start();
            }
            if (vibrator.hasVibrator()) {
                // Vibrate if device has a vibrator
                vibrator.vibrate(100);
            }
            // Go to failure activity to stop the game
            Intent toFailureActivity = new Intent(context, FailureActivity.class);
            context.startActivity(toFailureActivity);
            ((Activity) context).finish();
            // Set Game finished to true
            isGameFinished = true;
        }
    }

    // Check if ball collides with the borders
    private boolean collides() {
        if ((x >= 665 && x <= 1435) && (y >= 665 && y <= 735)) {
            return true;
        } else if ((x >= 665 && x <= 735) && (y >= 665 && y <= 960)) {
            return true;
        } else if ((x >= 465 && x <= 1570) && (y >= 465 && y <= 535)) {
            return true;
        } else if ((x >= 465 && x <= 535) && (y >= 465 && y <= 835)) {
            return true;
        } else if ((x >= 220 && x <= 835) && (y >= 265 && y <= 335)) {
            return true;
        } else if ((x >= 865 && x <= 935) && (y >= 165 && y <= 435)) {
            return true;
        } else if ((x >= 965 && x <= 1570) && (y >= 265 && y <= 335)) {
            return true;
        } else {
            return false;
        }
    }

    /*Pause the Game Thread on Game Activity pause*/
    public void pause() {
        tempTime += SystemClock.uptimeMillis() - startTime;
        isThreadRunning = false;
        isInterrupted = true;
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                // Logger class will be implemented if the time permits. Let us print the stackTrace for now
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    /*Resume Game Thread on Game Activity Resume*/
    public void resume() {
        if (isInterrupted) {
            startTime = SystemClock.uptimeMillis();
            handler = new Handler(); //Handler to update TextView
            timerUtil = new TimerUtil();
            handler.postDelayed(updateTimer, 0); //Start Handler to update timer textview
            isInterrupted = false;
        }
        isThreadRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    // updateTimer runnable to update the values in textView
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {

            if (isThreadRunning) {
                timeDiffMil = (SystemClock.uptimeMillis() - startTime) + tempTime;

                // Format time and set it to textView
                textViewTime.setText(timerUtil.formatTime(timeDiffMil));
                handler.postDelayed(this, 0);
            }
        }

    };

    // Check if the Motion Sensor has a change in X,Y or Z position
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check if the timer has been started
        if (isTimerStarted) {
            // get X and Y acceleration from Sensor and multiply with accelerationRate
            accelerationX = (float) (event.values[1] * accelerationRate);
            accelerationY = (float) (event.values[0] * (accelerationRate * 1.5));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
