package dropNumber;

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();

        int[][] matrixOrder = {{0, 2}, {3, 2}, {1, 4}, {2, 2}, {4, 4},
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

        for (int i = 0; i < 31; i++) {
            list.add(matrixOrder[i][1], matrixOrder[i][0]);

            list.fillMatrix();

            list.printMatrix();
            System.out.println("");

        }

    }

}
