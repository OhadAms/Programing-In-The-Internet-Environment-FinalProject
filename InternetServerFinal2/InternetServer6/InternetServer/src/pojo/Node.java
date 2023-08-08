package pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * The Node class represents a node in a graph or tree, containing data of type T.
 * @param <T> The type of data stored in the node.
 */

public class Node<T> implements Serializable {
    private static final Long serialVersionUID = 1L;

    private T data;
    private Node<T> parent;

    /**
     * Constructs a Node with the specified data, parent, weight, and visited status.
     * @param data      The data stored in the node.
     * @param parent    The parent node of the current node.
     */

    public Node(T data, Node<T> parent) {
        setData(data);
        setParent(parent);
    }

    /**
     * Constructs a Node with the specified data and default values for parent, weight, and visited status.
     * @param data The data stored in the node.
     */

    public Node(T data) {
        this(data, null);
    }

    public void setData(T data) {
        this.data = data;
    }
    /**
     * Returns the data stored in the node.
     * @return The data stored in the node.
     */

    public T getData() {
        return data;
    }


    /**
     * Returns the parent node of the current node.
     * @return The parent node.
     */

    public Node<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent node of the current node.
     * @param parent The parent node to set.
     */

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    /**
     * Checks if the current node is equal to the specified object.
     * @param o The object to compare.
     * @return true if the nodes are equal, false otherwise.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node<?> node)) return false;
        return Objects.equals(getData(), node.getData());
    }

    /**
     * Returns the hash code value for the node.
     * @return The hash code value.
     */

    @Override
    public int hashCode() {
        return Objects.hash(getData());
    }

    /**
     * Returns a string representation of the node.
     * @return The string representation of the node.
     */

    @Override
    public String toString() {
        return String.valueOf(getData());
    }

}