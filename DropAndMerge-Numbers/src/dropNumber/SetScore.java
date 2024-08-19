package dropNumber;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SetScore {
    private String fileName= "DropAndMerge-Numbers/src/dropNumber/scores.txt";
    public void saveScore(String playerName, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(playerName + ":" + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getHighScores() {
        List<String> highScore = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                highScore.add(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }
    public static void main(String[] args){
        SetScore setScore = new SetScore();
        setScore.saveScore("1. ", 100);
        List<String> scores = setScore.getHighScores();
        for(String score : scores){
            System.out.println(score);
        }
    }
}
