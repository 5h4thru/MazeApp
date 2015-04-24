package dxp141030.utdallas.edu.mazeapp.util;

import android.os.Environment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dxp141030.utdallas.edu.mazeapp.models.Score;

/**
 * @author Durga Sai Preetham Palagummi
 * @NetID dxp141030
 * @Course: CS6301 UI Design - Spring 2015
 * Date 17th April 2015
 * Modified on 22nd April 2015
 */


/**
 * Class to write and read from a file
 * In this application, the file will typically contain the name of the player and time the player took to complete the game
 */
public class FileUtil {
    private FileOutputStream fileOutputStream;
    private BufferedReader bufferedReader;

    // Write to file
    public boolean writeToFile(String stringToWrite) {
        boolean success = false;

        if (isWritableToStorage()) {
            File fileToWrite = new File(Environment.getExternalStorageDirectory(), "highscores.txt");
            try {
                if (!fileToWrite.exists()) {
                    fileToWrite.createNewFile();
                }
                fileOutputStream = new FileOutputStream(fileToWrite);
                fileOutputStream.write(stringToWrite.getBytes());
                fileOutputStream.close();
                success = true;
            } catch (Exception e) {
                // Logger class will be implemented if the time permits. Let us print the stackTrace for now
                e.printStackTrace();
            }
        }

        return success;
    }

    // Read from File
    public ArrayList<Score> fetchHighScores() {
        String str;

        ArrayList<Score> resultArray = new ArrayList<Score>();
        if (isWritableToStorage()) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), "highscores.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((str = bufferedReader.readLine()) != null) {
                    String[] tempStr = str.split("\t");
                    resultArray.add(new Score(tempStr[0], Long.parseLong(tempStr[1])));
                }
            } catch (IOException e) {
                // Logger class will be implemented if the time permits. Let us print the stackTrace for now
                e.printStackTrace();
            }
        }

        return resultArray;
    }

    // Check whether the storage media for the phone is mounted
    private boolean isWritableToStorage() {
        String storageState = Environment.getExternalStorageState();

        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }
}
