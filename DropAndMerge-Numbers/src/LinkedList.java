
package com.dropandmerge.numbers;


public class LinkedList<T extends Number> {

    com.dropandmerge.numbers.NodeClass<T> head;
    int size;
    int[][] matrix = new int[7][5];

    public boolean check(int column) { //checks whether a node with that column number already exists in the LinkedList
        com.dropandmerge.numbers.NodeClass<T> temp = head;
        while (temp != null) {
            if (temp.column == column) {
                return true;
            } else {
                temp = temp.right;
            }
        }
        return false;

    }


    public void add(T value, int column) { //adds a new node with value and column number to the LinkedList.
        NodeClass<T> newNode = new NodeClass<>(value, column);
        NodeClass<T> temp = head;
        if (head == null) { // if the linked list is empty, set the head to the new node
            head = newNode;
        } else {
            if (check(column)) { // if a node with the same column already exists
                while (temp != null) { // traverse the linked list
                    while (temp.right != null) { // traverse to the rightmost node in the column
                        if (temp.column == column) { // if a node with the same column is found, break the loop
                            break;
                        } else {
                            temp = temp.right;
                        }

                    }
                    if (temp.up == null) {  // if the upper node of the rightmost node is empty, set the new node as the upper node
                        temp.up = newNode;
                        newNode.down = temp;
                        sumTiles();
                        break;
                    } else {
                        temp = temp.up;
                    }

                }
            } else { // if a node with the same column does not exis
                while (temp != null) { // traverse to the rightmost node
                    if (temp.right == null) { // if the right node is empty, set the new node as the right node
                        temp.right = newNode;
                        newNode.left = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }

                }
            }
        }
    }

    public void sumTiles() {
        NodeClass<T> temp = head;
        NodeClass<T> tempHead = head; //i wanted to keep a reference to the head of the row
        for (int i = 0; i < 5; i++) { //column
            for (int j = 0; j < 7; j++) { //row

                if (temp.up != null) { // if the current node has an upper tile
                    temp = temp.up; //move up
                } else { //we've reached the top row
                    continue; //continur from next row
                }
            }
            while (temp.down != null) { // while the current node has a lower tile
                if ((int) temp.value == (int) temp.down.value) { //if values are equal
                    temp = temp.down;
                    temp.setValue((T) (Integer.valueOf(temp.value.intValue() * 2))); //summing the same values
                    temp.up = null; // remove the upper node
                } else { //nodes don't have the same value
                    break; //finishing the loop
                }
            }
            if (tempHead.right != null) { // if the current node has a right tile
                tempHead = tempHead.right;
                temp = tempHead; //set the current node to the head of the row
            } else { //we've reached the end of the row
                continue;
            }
        }

    }

    public void fillMatrix() { //fills the matrix with the values in the LinkedList
        NodeClass<T> temp = head;
        NodeClass<T> tempHead = head;
        matrix = new int[7][5];
        int row = 6;
        while (tempHead != null) {
            while (temp != null) {
                matrix[row][temp.column] = (int) temp.value;
                row--; //move up one row
                temp = temp.up; //move up one node
            }
            tempHead = tempHead.right;
            temp = tempHead;
            row = 6; //we should reset the row value when we move to a new row
        }
    }

    public void printMatrix() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    int[][] getMatrix() {

        return matrix;

    }

}
