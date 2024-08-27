package org.example;

import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Game extends JFrame {
    JButton button = new JButton("Inizio");
    JButton inizioPartita = new JButton("Nuova Partita");
    int attualScore = 0;
    JTextArea scoreLabel = new JTextArea();
    JLabel prossimoValore = new JLabel();
    JLabel countdownLabel = new JLabel();  // Nuova JLabel per il countdown
    int rows = 6;
    int cols = 5;
    int[][] matrix = new int[rows][cols];
    //int counter = 4;
    int countdownTime = 10;  // Variabile per il conto alla rovescia
    //LinkedList<Integer> possibleValues= new LinkedList<Integer>();
    int[] possibleValues = {2, 4, 8, 16, 32};
    int[] values = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};
    Random random = new Random();
    int colonnaScelta = 0;
    int colonnaPrecedente = 2;
    Timer timer;
    Timer countdownTimer;  // Nuovo timer per il conto alla rovescia
    static SetScore setScore = new SetScore();
    int value;

    ///
    JTextArea finalScore = new JTextArea();
    JTextArea nextValueShow = new JTextArea();

    void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Game() {
        getContentPane().setBackground(new Color(35, 45, 55, 255));
        setSize(730, 510);
        setTitle("DROP & MERGE NUMBERS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.setVisible(true);
        button.setBounds(450, 230, 100, 40);
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        //button.setForeground(Color.white);
        //button.setBackground(new Color(205, 90, 213));

        setLayout(null);
        add(button);
        setVisible(true);
        setLocationRelativeTo(null);

        add(scoreLabel);
        scoreLabel.setBounds(450, 140, 200, 70);
        scoreLabel.setVisible(true);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        scoreLabel.setForeground(Color.white);
        scoreLabel.setBackground(new Color(35, 45, 55, 255));
        //scoreLabel.setBorder(new LineBorder(new Color(24, 115, 120, 255), 1));
        scoreLabel.setText("Score:\n" + attualScore);
        scoreLabel.setForeground(new Color(231, 202, 51));
        scoreLabel.setEditable(false);

        add(prossimoValore);
        prossimoValore.setBounds(450, 60, 1000, 40);
        prossimoValore.setForeground(Color.white);
        prossimoValore.setVisible(true);
        prossimoValore.setFont(new Font("SansSerif", Font.BOLD, 20));
        prossimoValore.setText("Prossimo valore: " + genereteBlock());


        add(countdownLabel);  // Aggiungi la countdownLabel al frame
        countdownLabel.setBounds(450, 86, 200, 40);
        countdownLabel.setForeground(Color.white);
        countdownLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(finalScore);
        finalScore.setEditable(false);
        finalScore.setBounds(450, 100, 400, 200);
        finalScore.setVisible(false);
        finalScore.setFont(new Font("SansSerif", Font.BOLD, 20));
        finalScore.setForeground(Color.white);
        finalScore.setBackground(new Color(35, 45, 55, 255));

        add(inizioPartita);
        inizioPartita.setVisible(false);
        inizioPartita.setBounds(450, 300, 200, 40);

    }

    int genereteBlock() {
        //int[] possibleValues= {2, 4, 8, 16, 32};
        int valore = possibleValues[random.nextInt(possibleValues.length)];
        //prossimoValore.setText("Prossimo valore: " + valore);
        return valore;
    }

    public void actionPerformed(ActionEvent e) throws Exception {
        //value = genereteBlock();
        passInputToOracle("DropAndMerge/encoding.asp");


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


        colonnaScelta = getOutputFromOracle();
        colonnaPrecedente = colonnaScelta;
        countdownTimer.stop();  // Ferma il countdown
        timer.stop();  // Ferma il timer dato che l'utente ha scelto la colonna

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
    }
    void stampa(){
        for (int i = 0; i < rows; i++) {
            System.out.println("indice riga: "+i);
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }

    void merge() {
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
                        stampa();
                    }
                    if (colonnaScelta + 1 < cols && matrix[i][colonnaScelta + 1] == valore) {
                        System.out.println("merge destro " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i][colonnaScelta + 1] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                    System.out.println(i+1);
                    if(i+1<6){
                        System.out.print(matrix[i+1][colonnaScelta]);}
                    if (i + 1 < rows && matrix[i + 1][colonnaScelta] == valore) {
                        System.out.println("merge sotto " + matrix[i][colonnaScelta]);
                        matrix[i][colonnaScelta] *= 2;
                        matrix[i + 1][colonnaScelta] = 0;
                        attualScore += valore * 2;
                        merge = true;
                    }
                }
            }
            //nel faccio per sicurezza u merge generale per risolvere il problema del nonMerge dei blocchi che si uniscono su colonne diverse da quelle in cui cade l'ultimo blocco
            if (merge == false) {
                for (int i = rows - 1; i >= 0; i--) {
                    for (int j = 0; j < cols; j++) {
                        if (matrix[i][j] != 0) {
                            int valore = matrix[i][j];
                            if (j - 1 >= 0 && matrix[i][j - 1] == valore) {
                                matrix[i][j] *= 2;
                                matrix[i][j - 1] = 0;
                                attualScore += valore * 2;
                                merge = true;
                            }
                            if (j + 1 < cols && matrix[i][j + 1] == valore) {
                                matrix[i][j] *= 2;
                                matrix[i][j + 1] = 0;
                                attualScore += valore * 2;
                                merge = true;
                            }
                            if (i + 1 < rows && matrix[i + 1][j] == valore) {
                                matrix[i][j] *= 2;
                                matrix[i + 1][j] = 0;
                                attualScore += valore * 2;
                                merge = true;
                            }
                        }
                    }
                }
            }
            updateValues();
            collapseColumns();
            scoreLabel.setText("Score: \n" + attualScore);
        }
    }

    void updateValues() {
        int counter=0;
        //LinkedList<Integer> ValueBlock = new LinkedList<>();
        for(int i=0; i<rows-1; i++){
            for(int j=0; j<cols; j++){
                if(matrix[i][j]>possibleValues[possibleValues.length-1]){
                    counter+=1;
                }
            }
        }
        if(counter>=2) {
            int [] newPossibleValues= new int[possibleValues.length+1];
            System.arraycopy(possibleValues, 0, newPossibleValues, 0, possibleValues.length);
            newPossibleValues[newPossibleValues.length-1]=values[newPossibleValues.length-1];
            possibleValues=newPossibleValues;
        }
        for(int i=0; i<possibleValues.length; i++){
            System.out.println(possibleValues[i]);
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
                //per mostrare i migliori punteggi
                showScore();
                break;
            }
        }
    }
    void showScore(){
        button.setVisible(false);
        scoreLabel.setVisible(false);
        prossimoValore.setVisible(false);
        countdownLabel.setVisible(false);

        inizioPartita.setVisible(true);

        //il bottone inizioPartita chiama il metodo nuovaPartita
        inizioPartita.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuovaPartita(e);
            }
        });


        finalScore.setVisible(true);
        finalScore.setText("Youre Score: " + attualScore + "\n"+"\n Best Scores: \n"+"1. "+ setScore.getHighScores().get(0)+
                "\n"+ "2. "+ setScore.getHighScores().get(1)+ "\n"+ "3. "+ setScore.getHighScores().get(2));

    }

    void nuovaPartita(ActionEvent e){
        finalScore.setVisible(false);
        inizioPartita.setVisible(false);
        button.setVisible(true);
        button.setEnabled(true);
        scoreLabel.setVisible(true);
        prossimoValore.setVisible(true);
        countdownLabel.setVisible(true);
        attualScore=0;

        possibleValues = new int[]{2, 4, 8, 16, 32};
        //richiamare la matrice da disegnare da zero
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++){
                matrix[i][j]=0;
            }
        }
        scoreLabel.setText("Score: \n" + attualScore);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = 0;
            }
        }

        repaint();
    }

    void drawRectangles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
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
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2", j * 50 + 170, i * 50 + 120);

                            /*//bordo rettangolo
                            g2d.setColor(Color.white);
                            g2d.setStroke(new BasicStroke(2));
                            // Disegna il contorno del rettangolo
                            g2d.drawRect(j * 50 + 150, i * 50 + 60, 50, 50);*/
                            break;
                        case 4:
                            g2d.setColor(new Color(4, 198, 82));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("4", j * 50 + 170, i * 50 + 120);
                            break;
                        case 8:
                            g2d.setColor(new Color(116, 205, 222));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("8", j * 50 + 170, i * 50 + 120);
                            break;
                        case 16:
                            g2d.setColor(new Color(0, 146, 234, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16", j * 50 + 165, i * 50 +120);
                            break;
                        case 32:
                            g2d.setColor(new Color(255, 109, 0, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32", j * 50 + 165, i * 50 + 120);
                            break;
                        case 64:
                            g2d.setColor(new Color(117, 80, 245, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("64", j * 50 + 165, i * 50 + 120);
                            break;
                        case 128:
                            g2d.setColor(new Color(116, 87, 73, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("128", j * 50 + 157, i * 50 + 120);
                            break;
                        case 256:
                            g2d.setColor(new Color(128, 128, 128));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("256", j * 50 + 159, i * 50 + 120);
                            break;
                        case 512:
                            g2d.setColor(new Color(249, 168, 37, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("512", j * 50 + 158, i * 50 + 120);
                            break;
                        case 1024:
                            g2d.setColor(new Color(198, 40, 40, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("1024", j * 50 + 152, i * 50 + 120);
                            break;
                        case 2048:
                            g2d.setColor(new Color(40, 53, 147, 255));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("2048", j * 50 + 153, i * 50 + 120);
                            break;
                        case 4096:
                            g2d.setColor(new Color(151, 57, 227));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("4096", j * 50 + 153, i * 50 + 120);
                            break;
                        case 8192:
                            g2d.setColor(new Color(46, 220, 54));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("8192", j * 50 + 155, i * 50 + 120);
                            break;
                        case 16384:
                            g2d.setColor(new Color(108, 86, 21));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("16384", j * 50 + 155, i * 50 + 120);
                            break;
                        case 32768:
                            g2d.setColor(new Color(145, 76, 155));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("32768", j * 50 + 155, i * 50 + 120);
                            break;
                        case 65536:
                            g2d.setColor(new Color(29, 86, 35));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("65536", j * 50 + 150, i * 50 + 120);
                            break;
                        case 131072:
                            g2d.setColor(new Color(106, 176, 31));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
                            g2d.drawString("131072", j * 50 + 150, i * 50 + 120);
                            break;
                        case 262144:
                            g2d.setColor(new Color(45, 7, 61));
                            g2d.drawRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.fillRect(j * 50 + 150, i * 50 + 90, 50, 50);
                            g2d.setColor(Color.white);
                            g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
                            g2d.drawString("131072", j * 50 + 150, i * 50 + 120);
                            break;
                    }
                }
            }
        }
    }

    private String readEncoding(String filePath) { //metodo per leggere un file
        String encoding = "";
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                encoding += line + "\n";
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoding;
    }

    private void passInputToOracle(String filePath) throws Exception {
        EmbASPManager.getInstance().reset();
        String encoding = readEncoding(filePath); //legge il file di encoding
        EmbASPManager.getInstance().getProgram().addProgram(encoding);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                EmbASPManager.getInstance().getProgram().addProgram("cell(" + i + "," + j + "," + matrix[i][j] + ").");
            }
        }

        EmbASPManager.getInstance().getProgram().addProgram("block(" + value + ").");
        EmbASPManager.getInstance().getProgram().addProgram("maxValue("+possibleValues[possibleValues.length-1]+").");

        //System.out.println(EmbASPManager.getInstance().getProgram().getPrograms());
        EmbASPManager.getInstance().getHandler().addProgram(EmbASPManager.getInstance().getProgram());
    }


    private int getOutputFromOracle() throws ObjectNotValidException, IllegalAnnotationException {
        // GET OUTPUT
        Output output = EmbASPManager.getInstance().getHandler().startSync(); //avvia dlv
        ASPMapper.getInstance().registerClass(Move.class);

        int result = -1;
        AnswerSets answersets = (AnswerSets) output; //risultato di ASP
        for(AnswerSet a: answersets.getOptimalAnswerSets()) {
            System.out.println(a.toString());
            //trova move nell'answersets
            try {
                for (Object obj : a.getAtoms()) {
                    if (!(obj instanceof Move)){
                        continue; //se non è un Move
                    }
                    Move move = (Move) obj;
                    result=move.getCol();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Only the first answerSet is needed
            break;
        }
        //EmbASPManager.getInstance().reset();
        if(result==-1){ //se non ha generato nessuna mossa
            System.out.println("colonna precedente: " + colonnaPrecedente);
            result=colonnaPrecedente;
        }
        return result;
    }
    void prossimoValore(){ //TODO da modificare
        value=genereteBlock();
        nextValueShow.setText(Integer.toString(value));
        nextValueShow.setBounds(615, 45, 50, 50);
        nextValueShow.setForeground(Color.white);
        nextValueShow.setFont(new Font("SansSerif", Font.BOLD, 20));
        nextValueShow.setEditable(false);
        //centrare la stringa
        //i margini vanno cambiati nello switch
        nextValueShow.setMargin(new Insets(10, 16, 10, 25));

        add(nextValueShow);

        switch (value) {
            case 2:
                nextValueShow.setBackground(new Color(222, 107, 145, 255));
                break;
            case 4:
                nextValueShow.setBackground(new Color(4, 198, 82));
                break;
            case 8:
                nextValueShow.setBackground(new Color(116, 205, 222));
                break;
            case 16:
                nextValueShow.setBackground(new Color(0, 146, 234, 255));
                break;
            case 32:
                nextValueShow.setBackground(new Color(255, 109, 0, 255));
                break;
            case 64:
                nextValueShow.setForeground(new Color(117, 80, 245, 255));
                break;
            case 128:
                nextValueShow.setForeground(new Color(116, 87, 73, 255));
                break;
            case 256:
                nextValueShow.setForeground(new Color(128, 128, 128));
                break;
            case 512:
                nextValueShow.setForeground(new Color(249, 168, 37, 255));
                break;
            case 1024:
                nextValueShow.setForeground(new Color(198, 40, 40, 255));
                break;
            case 2048:
                nextValueShow.setForeground(new Color(40, 53, 147, 255));
                break;
            case 4096:
                nextValueShow.setForeground(new Color(151, 57, 227));
                break;
            case 8192:
                nextValueShow.setForeground(new Color(46, 220, 54));
                break;
            case 16384:
                nextValueShow.setForeground(new Color(108, 86, 21));
                break;
            case 32768:
                nextValueShow.setForeground(new Color(145, 76, 155));
                break;
            case 65536:
                nextValueShow.setForeground(new Color(29, 86, 35));
                break;
            case 131072:
                nextValueShow.setForeground(new Color(106, 176, 31));
                break;
            case 262144:
                nextValueShow.setForeground(new Color(45, 7, 61));
                break;
        }

        prossimoValore.setText("Prossimo valore: ");
        //prossimoValore.setText("Prossimo valore: " + value);
    }
    @Override
    public void paint(Graphics g) {
        prossimoValore();

        super.paint(g);
        drawRectangles(g);
        fillMatrix(g);
    }
    public static void main(String[] args) {
        SetScore getScoreInstance = new SetScore();
        java.util.List<String> scores = setScore.getHighScores();
        for(String score : scores){
            System.out.println(score);
        }
        Game gFrame = new Game();

        gFrame.button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gFrame.actionPerformed(e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        gFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}