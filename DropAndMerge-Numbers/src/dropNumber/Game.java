package dropNumber;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Game extends JFrame {
    JButton button = new JButton("Inizio");
    int attualScore = 0;
    JTextArea scoreLabel = new JTextArea();
    JLabel prossimoValore = new JLabel("Prossimo valore: ");
    JLabel countdownLabel = new JLabel();  // Nuova JLabel per il countdown
    int rows = 6;
    int cols = 5;
    int[][] matrix = new int[rows][cols];
    int counter = 4;
    int countdownTime = 10;  // Variabile per il conto alla rovescia
    int[] possibleValues = {2, 4, 8, 16, 32};
    int[] values = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
    Random random = new Random();
    int colonnaScelta = 0;
    int colonnaPrecedente = 2;
    Timer timer;
    Timer countdownTimer;  // Nuovo timer per il conto alla rovescia
    static SetScore setScore = new SetScore();

    void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Game() {
        getContentPane().setBackground(new Color(35, 45, 55, 255));
        setSize(700, 500);
        setTitle("DROP & MERGE NUMBERS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.setVisible(true);
        button.setBounds(450, 190, 100, 40);
        setLayout(null);
        add(button);
        setVisible(true);
        setLocationRelativeTo(null);

        add(scoreLabel);
        scoreLabel.setBounds(450, 100, 200, 70);
        scoreLabel.setVisible(true);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        scoreLabel.setForeground(Color.white);
        scoreLabel.setBackground(new Color(35, 45, 55, 255));
        scoreLabel.setBorder(new LineBorder(new Color(24, 115, 120, 255), 1));
        scoreLabel.setText("Score:\n" + attualScore);
        scoreLabel.setEditable(false);

        add(prossimoValore);
        prossimoValore.setBounds(450, 20, 1000, 40);
        prossimoValore.setForeground(Color.white);
        prossimoValore.setVisible(true);
        prossimoValore.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(countdownLabel);  // Aggiungi la countdownLabel al frame
        countdownLabel.setBounds(450, 40, 200, 40);
        countdownLabel.setForeground(Color.white);
        countdownLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

    }

    int genereteBlock() {
        //int[] possibleValues= {2, 4, 8, 16, 32};
        int valore = possibleValues[random.nextInt(possibleValues.length)];
        prossimoValore.setText("Prossimo valore: " + valore);
        return valore;
    }

    public void actionPerformed(ActionEvent e) {
        int value = genereteBlock();

        // Timer per il countdown da 10 a 1
        countdownTime = 10;
        countdownLabel.setText("Tempo rimanente: " + countdownTime);

        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownTime--;
                countdownLabel.setText("Tempo rimanente: " + countdownTime);
                if (countdownTime <= 0) {
                    countdownTimer.stop();
                    timer.start();  // Avvia il timer per posizionare il blocco automaticamente
                }
            }
        });

        countdownTimer.start();
        // Timer per far cadere automaticamente il blocco dopo il countdown
        timer = new Timer(1000, new ActionListener() {  // Cambiato a 1000 ms per allinearsi col countdown
            @Override
            public void actionPerformed(ActionEvent e) {
                colonnaScelta = colonnaPrecedente;
                for (int i = rows - 1; i >= 0; i--) {
                    if (matrix[i][colonnaScelta] == 0) {
                        matrix[i][colonnaScelta] = value;
                        break;
                    }
                }
                scoreLabel.setText("Score: \n" + attualScore);
                repaint();
                merge();
                blockOnTop();
                timer.stop();
            }
        });

        timer.setRepeats(false);
        timer.setInitialDelay(countdownTime * 1000);  // Imposta il timer per iniziare dopo il countdown
        timer.start();

        String colonnaStr = JOptionPane.showInputDialog(this, "Inserire colonna: ");

        if (colonnaStr != null) {
            colonnaScelta = Integer.parseInt(colonnaStr);
            colonnaPrecedente = colonnaScelta;
            countdownTimer.stop();  // Ferma il countdown
            timer.stop();  // Ferma il timer dato che l'utente ha scelto la colonna

            for (int i = rows - 1; i >= 0; i--) {
                if (matrix[i][colonnaScelta] == 0) {
                    matrix[i][colonnaScelta] = value;
                    break;
                }
            }
        }

        scoreLabel.setText("Score: \n" + attualScore);
        repaint();
        merge();
        blockOnTop();
    }

    void merge() { //TODO non funziona il caso in cui ho un tre blocchi ad angolo, fa il merge sotto e basta
        System.out.println("colonna " + colonnaScelta);
        boolean merge = true;
        while (merge) {
            merge = false;
            for (int i = rows - 1; i >= 0; i--) {
                if (matrix[i][colonnaScelta] != 0) {
                    int valore = matrix[i][colonnaScelta];
                    System.out.println("valore: " + valore);
                    if (colonnaScelta - 1 >= 0 && matrix[i][colonnaScelta - 1] == valore) {
                        System.out.println("merge sinistro " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta - 1] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                    if (colonnaScelta + 1 < cols && matrix[i][colonnaScelta + 1] == valore) {
                        System.out.println("merge destro " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta + 1] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                    if (i - 1 >= 0 && matrix[i - 1][colonnaScelta] == valore) {
                        System.out.println("merge sotto " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i - 1][colonnaScelta] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                    if (i + 1 < rows && matrix[i + 1][colonnaScelta] == valore) {
                        System.out.println("merge sopra " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i + 1][colonnaScelta] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                }
            }
            collapseColumns();
            scoreLabel.setText("Score: \n" + attualScore);
        }
        /*for (int i = cols - 1; i > 0; i--) {
            boolean merge=true;
            while (merge) {
                merge = false;
                if (matrix[i][colonnaScelta] != 0) {
                    int valore = matrix[i][colonnaScelta];

                    if (valore == matrix[i - 1][colonnaScelta]) {  //merge sotto
                        matrix[i][colonnaScelta] *= 2;  // Unisci i blocchi
                        matrix[i - 1][colonnaScelta] = 0; // Rendi la casella sopra vuota
                        score += matrix[i][colonnaScelta];  // Aggiorna il punteggio
                        merge=true;
                    }

                    if (valore == matrix[i][colonnaScelta - 1] && colonnaScelta != 0) { //merge a sinistra
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta - 1] = 0;
                        score += matrix[i][colonnaScelta];
                        merge=true;
                    }

                    if (valore == matrix[i][colonnaScelta + 1] && colonnaScelta < cols - 1) { //merge a destra
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta + 1] = 0;
                        score += matrix[i][colonnaScelta];
                        merge=true;
                    }
                    scendi();
                    aggiorna(matrix[i][colonnaScelta]);
                }
            }
        }
        scoreLabel.setText("Score: \n" + score);
        repaint();*/
    }

    void aggiorna(int nuovoValore) {  //TODO da modificare il modo, non aggiuge appena scopre nuovo valore ma dopo un po che c'è
        if (nuovoValore > 32) {
            System.out.println("nuovo valore" + nuovoValore);
            for (int i = 0; i < values.length; i++) {
                if (values[i] <= nuovoValore) {
                    counter += 1;
                }
            }
            possibleValues = new int[counter];
            for (int i = 0; i < counter; i++) {
                possibleValues[i] = values[i];
            }
        }
    }

    void collapseColumns() {
        for (int i = rows - 1; i > 0; i--) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][j] = matrix[i - 1][j];
                    matrix[i - 1][j] = 0;
                }
            }
        }
    }

    void blockOnTop() {
        for (int i = 0; i < cols; i++) {
            if (matrix[0][i] != 0) {
                JOptionPane.showMessageDialog(this, "Game Over", "Warning", JOptionPane.WARNING_MESSAGE);
                button.setEnabled(false);
                setScore.updateScore(attualScore);
                countdownTimer.stop();
                timer.stop();
                break;
            }
        }
    }

    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
            }
        }
    }

    void fillMatrix(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != 0) {
                    switch (matrix[i][j]) {
                        case 2:
                            g2d.setColor(new Color(222, 107, 145, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2", j * 50 + 170, i * 50 + 90);
                            break;
                        case 4:
                            g2d.setColor(new Color(4, 198, 82));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("4", j * 50 + 170, i * 50 + 90);
                            break;
                        case 8:
                            g2d.setColor(new Color(116, 205, 222));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("8", j * 50 + 170, i * 50 + 90);
                            break;
                        case 16:
                            g2d.setColor(new Color(0, 146, 234, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16", j * 50 + 170, i * 50 + 90);
                            break;
                        case 32:
                            g2d.setColor(new Color(255, 109, 0, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32", j * 50 + 165, i * 50 + 90);
                            break;
                        case 64:
                            g2d.setColor(new Color(117, 80, 245, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("64", j * 50 + 165, i * 50 + 90);
                            break;
                        case 128:
                            g2d.setColor(new Color(116, 87, 73, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("128", j * 50 + 160, i * 50 + 90);
                            break;
                        case 256:
                            g2d.setColor(new Color(128, 128, 128));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("256", j * 50 + 160, i * 50 + 90);
                            break;
                        case 512:
                            g2d.setColor(new Color(249, 168, 37, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("512", j * 50 + 160, i * 50 + 90);
                            break;
                        case 1024:
                            g2d.setColor(new Color(198, 40, 40, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("1024", j * 50 + 160, i * 50 + 90);
                            break;
                        case 2048:
                            g2d.setColor(new Color(40, 53, 147, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2048", j * 50 + 160, i * 50 + 90);
                            break;
                        case 4096:
                            g2d.setColor(new Color(151, 57, 227));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("4096", j * 50 + 160, i * 50 + 90);
                            break;
                        case 8192:
                            g2d.setColor(new Color(46, 220, 54));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("8192", j * 50 + 160, i * 50 + 90);
                            break;
                        case 16384:
                            g2d.setColor(new Color(108, 86, 21));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16384", j * 50 + 160, i * 50 + 90);
                            break;
                        case 32768:
                            g2d.setColor(new Color(145, 76, 155));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32768", j * 50 + 160, i * 50 + 90);
                            break;
                        case 65536:
                            g2d.setColor(new Color(29, 86, 35));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("65536", j * 50 + 160, i * 50 + 90);
                            break;
                        case 131072:
                            g2d.setColor(new Color(106, 176, 31));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("131072", j * 50 + 160, i * 50 + 90);
                            break;
                        case 262144:
                            g2d.setColor(new Color(36, 63, 9));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("131072", j * 50 + 160, i * 50 + 90);
                            break;
                    }
                }
            }
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawRectangles(g);
        fillMatrix(g);
    }
    public static void main(String[] args) {


        //SetScore getScoreInstance = new SetScore();
        //setScore.saveScore("1. ", 100);
        //setScore.saveScore("2. ", 200);
        //setScore.saveScore("3. ", 300);
        java.util.List<String> scores = setScore.getHighScores();
        for(String score : scores){
            System.out.println(score);
        }


        Game gFrame = new Game();
        gFrame.button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gFrame.actionPerformed(e);
            }

        });

        gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}