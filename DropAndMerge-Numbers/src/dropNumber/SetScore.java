package dropNumber;

import java.io.*;
import java.util.ArrayList;

public class SetScore {
    private String fileName = "DropAndMerge-Numbers/score.txt";

    public void saveScore(String playerName, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(playerName + ":" + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public java.util.List<String> getHighScores() {
        java.util.List<String> highScore = new ArrayList<>();
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
