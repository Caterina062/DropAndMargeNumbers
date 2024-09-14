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

    public void updateScore(int score){;
        getHighScores();  // Leggi i punteggi dal file
        for(int i = 0; i < highScore.size(); i++){
            if(score > Integer.parseInt(highScore.get(i))){
                highScore.add(i, "" + score);
                highScore.remove(highScore.size()-1);
                break;
            }
        }
        writeScoresToFile();  // Riscrivi il file con i nuovi punteggi
    }
    private void writeScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String score : highScore) {
                writer.write(score);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
