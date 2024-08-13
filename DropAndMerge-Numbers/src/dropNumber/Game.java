package dropNumber;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Game extends JFrame {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton button = new JButton("Inizio");
    int score=0;

    JLabel scoreLabel= new JLabel("Score: \n"+ score);
    JLabel prossimoValore= new JLabel("Prossimo valore: ");
    int colonna = 6;
    int riga= 5;
    int[][] matrix = new int[colonna][riga];
    int counter = 0;
    LinkedList<Integer> list = new LinkedList<>();
   /* int[][] matrixOrder = {{0, 2}, {3, 2}, {1, 4}, {2, 2}, {4, 4},
            {1, 2}, {4, 4}, {0, 8},
            {0, 8}, {1, 32},
            {2, 2}, {2, 64}, {3, 16},
            {1, 64}, {2, 32}, {0, 16},
            {4, 16}, {2, 32},
            {1, 64}, {3, 8}, {3, 4},
            {3, 2}, {3, 2},
            {1, 2}, {2, 64}, {2, 32},
            {2, 16}, {2, 8}, {2, 8},
            {1, 4}, {1, 8}, {0, 0}};

    */

    Random random = new Random();
    int colonnaScelta=0;


    void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Game() {

        getContentPane().setBackground(new Color (253, 253, 150));
        setSize(700, 500);
        setTitle("Drop Number dropNumber.Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.setVisible(true);
        button.setBounds(500, 190, 100, 40);
        setLayout(null);
        add(button);
        setVisible(true);
        setLocationRelativeTo(null);

        add(scoreLabel);
        scoreLabel.setBounds(500, 120, 1000, 50);
        scoreLabel.setVisible(true);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        scoreLabel.setForeground(Color.black);
        scoreLabel.setBorder(new LineBorder(new Color(246, 103, 24), 1));

        add(prossimoValore);
        prossimoValore.setBounds(450, 20, 1000, 40);
        prossimoValore.setVisible(true);
        prossimoValore.setFont(new Font("SansSerif", Font.BOLD, 20));
    }
    int genereteBlock(){
        int[] possibleValues= {2, 4, 8, 16, 32};
        int valore= possibleValues[random.nextInt(possibleValues.length)];
        prossimoValore.setText("Prossimo valore: "+ valore);
        return valore;
    }


    public void actionPerformed(ActionEvent e) {
        int value= genereteBlock();
        /*if (counter != 31) {
            list.add(matrixOrder[counter][1], matrixOrder[counter][0]);
            /*score+=matrix[counter][1];
            scoreLabel.setText("Score: "+ score);*/
            /*list.fillMatrix();
            list.sumTiles();
            counter++;
            setMatrix(list.getMatrix());
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "dropNumber.Game has finished", "Warning", JOptionPane.WARNING_MESSAGE);
            button.setEnabled(false);
        }*/
        String colonnaStr = JOptionPane.showInputDialog(this, "inserire colonna: " );
        //int colonnaScelta= Integer.parseInt(colonnaStr);
        colonnaScelta= Integer.parseInt(colonnaStr);
        for(int i = colonna-1; i>=0; i--){
            if (matrix[i][colonnaScelta] == 0) {
                matrix[i][colonnaScelta] = value;
                score += value;
                break;
            }
        }

        scoreLabel.setText("Score: \n"+ score);
        //list.add(matrixOrder[counter][1], matrixOrder[counter][0]);
        //list.fillMatrix();
        //list.sumTiles();
        //counter++;
        //setMatrix(list.getMatrix());

        repaint();
        blockOnTop();
        merge();
    }
    void merge() {
        for (int i = colonna - 1; i > 0; i--) {
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

                    if (valore == matrix[i][colonnaScelta + 1] && colonnaScelta != colonna - 1) { //merge a destra
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta + 1] = 0;
                        score += matrix[i][colonnaScelta];
                        merge=true;
                    }
                    scendi();
                }
            }
        }
        scoreLabel.setText("Score: \n" + score);
        repaint();
    }

    void scendi(){
        for(int i=colonna-1; i>0; i--){
            for(int j=0; j<riga; j++){
                if(matrix[i][j]==0){
                    matrix[i][j]=matrix[i-1][j];
                    matrix[i-1][j]=0;
                }
            }
        }
    }


    void blockOnTop(){
        for(int i=0; i<riga; i++){
            if(matrix[0][i]!=0){
                JOptionPane.showMessageDialog(this, "Game Over", "Warning", JOptionPane.WARNING_MESSAGE);
                button.setEnabled(false);
                break;
            }

        }
    }

    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < colonna; i++) {
            for (int j = 0; j < riga; j++) {
                g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
            }
        }

    }

    void fillMatrix(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < colonna; i++) {
            for (int j = 0; j < riga; j++) {
                if (matrix[i][j] != 0) {
                    switch (matrix[i][j]) {
                        case 2:
                            g2d.setColor(new Color(218, 238, 218));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2", j * 50 + 170, i * 50 + 90);
                            break;
                        case 4:
                            g2d.setColor(new Color(237, 224, 200));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("4", j * 50 + 170, i * 50 + 90);
                            break;
                        case 8:
                            g2d.setColor(new Color(242, 177, 121));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("8", j * 50 + 170, i * 50 + 90);
                            break;
                        case 16:
                            g2d.setColor(new Color(245, 149, 99));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16", j * 50 + 170, i * 50 + 90);
                            break;
                        case 32:
                            g2d.setColor(new Color(246, 124, 95));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32", j * 50 + 165, i * 50 + 90);
                            break;
                        case 64:
                            g2d.setColor(new Color(246, 94, 59));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("64", j * 50 + 165, i * 50 + 90);
                            break;
                        case 128:
                            g2d.setColor(new Color(237, 207, 114));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("128", j * 50 + 160, i * 50 + 90);
                            break;
                        case 256:
                            g2d.setColor(new Color(237, 204, 97));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("256", j * 50 + 160, i * 50 + 90);
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