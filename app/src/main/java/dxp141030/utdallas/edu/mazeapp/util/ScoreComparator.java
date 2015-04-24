package dxp141030.utdallas.edu.mazeapp.util;

import java.util.Comparator;

import dxp141030.utdallas.edu.mazeapp.models.Score;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


/**
 * Class used to compare two score objects
 */
public class ScoreComparator implements Comparator<Score>{

    @Override
    public int compare(Score s1, Score s2) {
        return Long.compare(s1.getScoreTime(), s2.getScoreTime());
    }

}
