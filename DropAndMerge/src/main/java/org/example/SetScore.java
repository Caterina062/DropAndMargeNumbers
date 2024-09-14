package org.example;

import java.io.*;
import java.util.ArrayList;

public class SetScore {
    private String fileName = "DropAndMerge/src/main/java/score.txt";
    public java.util.List<String> highScore = new ArrayList<>();


    public java.util.List<String> getHighScores() {
        highScore.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScore.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }


}
