package dropNumber;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Game extends JFrame {
    //JFrame frame = new JFrame();
    //JPanel panel = new JPanel();
    JButton button = new JButton("Inizio");
    int score=0;
    JTextArea scoreLabel= new JTextArea();
    JLabel prossimoValore= new JLabel("Prossimo valore: ");
    int cols = 6;
    int rows= 5;
    int[][] matrix = new int[cols][rows];
    int counter = 4;
    //LinkedList<Integer> possibleValues = new LinkedList<>();
    int[] possibleValues = {2, 4, 8, 16, 32};
    int[] values= {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
    Random random = new Random();
    int colonnaScelta=0;
    int colonnaPrecedente= 2;
    Timer timer;


    void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Game() {
        getContentPane().setBackground(new Color (35,45,55,255));
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
        scoreLabel.setBackground(new Color(35,45,55,255));
        scoreLabel.setBorder(new LineBorder(new Color(24,115,120,255), 1));
        scoreLabel.setText("Score:\n" + score);
        scoreLabel.setEditable(false);

        add(prossimoValore);
        prossimoValore.setBounds(450, 20, 1000, 40);
        prossimoValore.setForeground(Color.white);
        prossimoValore.setVisible(true);
        prossimoValore.setFont(new Font("SansSerif", Font.BOLD, 20));
    }
    int genereteBlock(){
        //int[] possibleValues= {2, 4, 8, 16, 32};
        int valore= possibleValues[random.nextInt(possibleValues.length)];
        prossimoValore.setText("Prossimo valore: "+ valore);
        return valore;
    }
    public void actionPerformed(ActionEvent e) {
        /*int value= genereteBlock();
        String colonnaStr = JOptionPane.showInputDialog(this, "inserire colonna: " );
        //int colonnaScelta= Integer.parseInt(colonnaStr);
        colonnaScelta= Integer.parseInt(colonnaStr);
        for(int i = cols -1; i>=0; i--){
            if (matrix[i][colonnaScelta] == 0) {
                matrix[i][colonnaScelta] = value;
                //score += value;
                break;
            }
        }
        scoreLabel.setText("Score: \n"+ score);
        repaint();
        blockOnTop();
        merge();*/
        int value = genereteBlock();

        // Timer per far cadere automaticamente il blocco dopo 10 secondi
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Se il tempo scade, scegli una colonna random e posiziona il blocco
                //colonnaScelta = random.nextInt(rows);  // scegli una colonna casuale
                colonnaScelta = colonnaPrecedente;
                for (int i = cols - 1; i >= 0; i--) {
                    if (matrix[i][colonnaScelta] == 0) {
                        matrix[i][colonnaScelta] = value;
                        break;
                    }
                }
                scoreLabel.setText("Score: \n" + score);
                repaint();
                blockOnTop();
                merge();
                timer.stop();  // Ferma il timer dopo che il blocco è stato posizionato automaticamente
            }
        });

        timer.setRepeats(false);  // Il timer non si ripete
        timer.start();  // Avvia il timer

        // Chiedi all'utente di inserire la colonna
        String colonnaStr = JOptionPane.showInputDialog(this, "Inserire colonna: ");

        // Se l'utente inserisce la colonna, ferma il timer
        if (colonnaStr != null) {
            colonnaScelta = Integer.parseInt(colonnaStr);
            colonnaPrecedente = colonnaScelta;
            timer.stop();  // Ferma il timer dato che l'utente ha scelto la colonna

            // Posiziona il blocco nella colonna scelta
            for (int i = cols - 1; i >= 0; i--) {
                if (matrix[i][colonnaScelta] == 0) {
                    matrix[i][colonnaScelta] = value;
                    break;
                }
            }
        }

        scoreLabel.setText("Score: \n" + score);
        repaint();
        blockOnTop();
        merge();

    }
    void merge() { //TODO non funziona il caso in cui ho un tre blocchi ad angolo, fa il merge sotto e basta
        System.out.println("colonna "+colonnaScelta);
        boolean merge = true;
        while (merge){
            merge = false;
            for(int i= cols - 1; i>=0; i--){
                if(matrix[i][colonnaScelta]!=0){
                    int valore = matrix[i][colonnaScelta];
                    System.out.println("valore: "+ valore);
                    if (colonnaScelta-1 >= 0 && matrix[i][colonnaScelta - 1]==valore){
                        System.out.println("merge sinistro "+matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta]*=2;
                        matrix[i][colonnaScelta - 1]=0;
                        score+=valore*2;
                        merge= true;
                    }
                    else{
                        System.out.println("NO MERGE");
                    }
                    if (colonnaScelta + 1 < rows && matrix[i][colonnaScelta+1] == valore){
                        System.out.println("merge destro "+matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta]*=2;
                        matrix[i][colonnaScelta + 1]=0;
                        score+=valore*2;
                        merge= true;
                    }
                    else{
                        System.out.println("NO MERGE");
                    }
                    if (i- 1 >=0 && matrix[i - 1][colonnaScelta]==valore){
                        System.out.println("merge sotto "+matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta]*=2;
                        matrix[i -1][colonnaScelta]=0;
                        score+=valore*2;
                        merge = true;
                    }
                    if (i + 1 < cols && matrix[i + 1][colonnaScelta]==valore){
                        System.out.println("merge sopra "+matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta]*=2;
                        matrix[i + 1][colonnaScelta]=0;
                        score+=valore*2;
                        merge= true;
                    }
                }
            }
            collapseColumns();
            scoreLabel.setText("Score: \n"+ score);
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
    void aggiorna(int nuovoValore){  //TODO da modificare il modo, non aggiuge appena scopre nuovo valore ma dopo un po che c'è
        if(nuovoValore>32){
            System.out.println("nuovo valore" + nuovoValore);
            for(int i=0; i<values.length; i++){
                if (values[i] <= nuovoValore){
                    counter+=1;
                }
            }
            possibleValues= new int[counter];
            for(int i=0; i<counter; i++){
                possibleValues[i]= values[i];
            }
        }
    }
    void collapseColumns(){
        for(int i=cols-1; i>0; i--){
            for(int j=0; j<rows; j++){
                if(matrix[i][j]==0){
                    matrix[i][j]=matrix[i-1][j];
                    matrix[i-1][j]=0;
                }
            }
        }
    }
    void blockOnTop(){
        for(int i=0; i<rows; i++){
            if(matrix[0][i]!=0){
                JOptionPane.showMessageDialog(this, "Game Over", "Warning", JOptionPane.WARNING_MESSAGE);
                button.setEnabled(false);
                break;
            }
        }
    }
    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
            }
        }
    }
    void fillMatrix(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows ; j++) {
                if (matrix[i][j] != 0) {
                    switch (matrix[i][j]) {
                        case 2:
                            g2d.setColor(new Color(222,107,145,255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2", j * 50 + 170, i * 50 + 90);
                            break;
                        case 4:
                            g2d.setColor(new Color(4,198,82));
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
                            g2d.setColor(new Color(0,146,234,255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16", j * 50 + 170, i * 50 + 90);
                            break;
                        case 32:
                            g2d.setColor(new Color(255,109,0,255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32", j * 50 + 165, i * 50 + 90);
                            break;
                        case 64:
                            g2d.setColor(new Color(117,80,245,255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 60, 50, 50);
                            g2d.setColor(Color.black);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("64", j * 50 + 165, i * 50 + 90);
                            break;
                        case 128:
                            g2d.setColor(new Color(116,87,73,255));
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