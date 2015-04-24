package dxp141030.utdallas.edu.mazeapp.models;


/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


/**
 * Class used to track the score of the game
 * Will be helpful while creating the hall-of-fame
 */
//Note: This class even though may at first glance may not be fit for models part of the code, I feel that this is apt and better than plugging it somewhere else
public class Score {

    // Variables to track the time taken to finish the game and the name of the player who finished
    private long scoreTime;
    private String name;
    
    // Constructor
    public Score(String name, long scoreTime) {
        this.name = name;
        this.scoreTime = scoreTime;
    }

    // Getters and Setters
    public long getScoreTime() {
        return scoreTime;
    }
    
    public void setScoreTime(long scoreTime) {
        this.scoreTime = scoreTime;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
