package dxp141030.utdallas.edu.mazeapp.util;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


// Format Time (to a readable display) based on the elapsedTime value
public class TimerUtil {
        
    public String formatTime(long timeElapsed) {
        int timeDiff = (int) (timeElapsed/1000);
        int minutes = timeDiff/60;
        int seconds = timeDiff % 60;
        int milliseconds = (int) (timeElapsed % 1000);

        return minutes + ":" + String.format("%02d", seconds) 
                + ":" + String.format("%03d", milliseconds);
    }
}
