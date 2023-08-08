package pojo;

import java.io.Serializable;
import java.util.*;

/**
 Matrix is a class that represents a 2D matrix of integers.
 It provides methods for matrix creation, manipulation, and retrieval of values and neighbors.
 */

public class Matrix implements Serializable {

    int[][] primitiveMatrix;

    /**
     Constructs a Matrix object from a 2D array of integers.
     Each row of the array is cloned to ensure immutability.
     @param oArray The 2D array representing the matrix.
     */

    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        setPrimitiveMatrix(list.toArray(new int[0][]));
    }

    /**
     Constructs a Matrix object with the specified number of rows and columns.
     The matrix is filled with random binary values (0 or 1).
     @param numOfRows The number of rows in the matrix.
     @param numOfColumns The number of columns in the matrix.
     */

    public Matrix(int numOfRows, int numOfColumns) {
        Random r = new Random();
        setPrimitiveMatrix(new int[numOfRows][numOfColumns]);
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                getPrimitiveMatrix()[i][j] = r.nextInt(2);
            }
        }
    }

    /**
     Returns a string representation of the matrix.
     Each row is converted to a string and appended with a newline character.
     @return The string representation of the matrix.
     */

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : getPrimitiveMatrix()) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


    /**
     * Retrieves the neighboring indices of a given index in a grid-like structure.
     *
     * @param index The index for which neighbors are to be retrieved.
     * @return A collection of neighboring indices.
     */

    /*
     * The algorithm getNeighbors iterates over predefined offsets to determine the neighboring indices of a
     *  given index within a grid-like structure, taking into account the boundaries of the grid. */
    public Collection<Index> getNeighbors(final Index index) {

        // Collection to store the neighboring indices
        Collection<Index> neighbors = new ArrayList<>();

        // Define the offsets for neighboring cells in rows and columns
        int[] rowOffsets = {1, 0, -1, 0};
        int[] colOffsets = {0, 1, 0, -1};

        // Get the dimensions of the grid
        int numRows = getPrimitiveMatrix().length;
        int numCols = getPrimitiveMatrix()[0].length;

        // Iterate over the offsets to calculate the neighboring indices
        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = index.getRow() + rowOffsets[i];
            int newCol = index.getColumn() + colOffsets[i];

            // Check if the new indices are within the valid range of the grid
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                // Create a new Index object with the new indices and add it to the neighbors collection
                neighbors.add(new Index(newRow, newCol));
            }
        }

        // Return the collection of neighboring indices
        return neighbors;
    }


    /**
     * Retrieves all neighboring indices, including diagonals, of a given index in a grid-like structure.
     *
     * @param index The index for which neighbors are to be retrieved.
     * @return A collection of neighboring indices, including diagonals.
     */

    /*
     * The algorithm getNeighbors iterates over predefined offsets to determine the neighboring indices of a
     *  given index within a grid-like structure, taking into account the boundaries of the grid. */
    public Collection<Index> getAllNeighborsIncludingDiagonals(final Index index) {

        // Define the offsets for neighboring cells in rows and columns, including diagonals
        int[] rowOffsets = {1, 0, -1, 0, 1, 1, -1, -1};
        int[] colOffsets = {0, 1, 0, -1, -1, 1, -1, 1};

        // Collection to store the neighboring indices, including diagonals
        Collection<Index> neighbors = new ArrayList<>();

        // Get the dimensions of the grid
        int numRows = getPrimitiveMatrix().length;
        int numCols = getPrimitiveMatrix()[0].length;

        // Iterate over the offsets to calculate the neighboring indices, including diagonals
        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = index.getRow() + rowOffsets[i];
            int newCol = index.getColumn() + colOffsets[i];

            // Check if the new indices are within the valid range of the grid
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                // Create a new Index object with the new indices and add it to the neighbors collection
                neighbors.add(new Index(newRow, newCol));
            }
        }

        // Return the collection of neighboring indices, including diagonals
        return neighbors;
    }

    /**
     Returns the value at the specified index in the matrix.
     @param index The index for which to retrieve the value.
     @return The value at the specified index.
     */

    public int getValue(final Index index){
        return getPrimitiveMatrix()[index.getRow()][index.getColumn()];
    }

    /**
     Prints the matrix to the console.
     */

    public void printMatrix(){
        for (int[] row : getPrimitiveMatrix()) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    /**
     * Sets the primitive matrix for this object.
     *
     * @param primitiveMatrix The 2D integer array representing the primitive matrix to set.
     */
    public void setPrimitiveMatrix(int[][] primitiveMatrix) {
        this.primitiveMatrix = primitiveMatrix;
    }

    /**
     Returns the primitive 2D array representing the matrix.
     @return The primitive matrix.
     */

    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

    /**
     A sample usage of the Matrix class.
     @param args Command-line arguments.
     */

    public static void main(String[] args) {
        Matrix matrix = new Matrix(3,3);
        System.out.println(matrix);
        Index index1 = new Index(0,0);
        Index index2 = new Index(1,1);
        System.out.println(matrix.getNeighbors(index1));
        System.out.println(matrix.getNeighbors(index2));
    }


}
