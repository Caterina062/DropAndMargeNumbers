
package org.example;

public class NodeClass<T extends Number> {
    NodeClass up;
    NodeClass down;
    NodeClass left;
    NodeClass right;
    T value;
    int column;

    public NodeClass(T value, int column){
        this.value = value;
        this.right = null;
        this.up = null;
        this.down = null;
        this.left = null;
        this.column = column;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
