package pojo;

import java.io.Serializable;
import java.util.Objects;


/**
 * Index is a class that represents the index of a node in a data structure.
 * It provides methods for equality comparison, hashing, and string representation.
 */

public class Index implements Serializable {

    int row, column;

    /**
     * Constructs a new Index object with the specified row and column.
     * @param row    The row index.
     * @param column The column index.
     */

    public Index(final int row, final int column) {
        setRow(row);
        setColumn(column);
    }

    /**
     * Compares this Index object to another object for equality.
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return getRow() == index.getRow() &&
                getColumn() == index.getColumn();
    }

    /**
     * Computes the hash code for this Index object.
     * @return The hash code value.
     */

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }

    /**
     * Returns a string representation of this Index object.
     * @return The string representation.
     */

    @Override
    public String toString() {
        return "("+getRow() +
                "," + getColumn() +
                ')';
    }

    /**
     * Sets the row value for this object.
     *
     * @param row The value to set as the row.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column value for this object.
     *
     * @param column The value to set as the column.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Returns the row index of this Index object.
     * @return The row index.
     */

    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this Index object.
     * @return The column index.
     */

    public int getColumn() {
        return column;
    }


    /**
     * A sample usage of the Index class.
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        Index index = new Index(2,3);
        System.out.println(index);
    }
}
