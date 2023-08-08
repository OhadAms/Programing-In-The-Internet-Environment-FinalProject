package pojo;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The TraversableMatrix class adapts a Matrix to the functionality of the Traversable interface.
 */
public class TraversableMatrix implements Traversable<Index>, Serializable {
    private final Matrix innerMatrix;
    private Index source;
    private Index destination;

    /**
     * Constructs a TraversableMatrix object with the specified inner matrix.
     * @param matrix The inner matrix to be adapted.
     */
    public TraversableMatrix(@NotNull Matrix matrix){
        this.innerMatrix = matrix;
    }

    /**
     * Constructs a TraversableMatrix object with an empty inner matrix of size 3x3 and a default source index (0, 0).
     */
    public TraversableMatrix(){
        this.innerMatrix = new Matrix(3,3);
        source = new Index(0,0);
    }

    /**
     * Gets the inner matrix of the TraversableMatrix.
     * @return The inner matrix.
     */
    @Override
    public Matrix getInnerMatrix() {
        return innerMatrix;
    }


    /**
     * Sets the source index of the TraversableMatrix.
     * @param source The source index to be set.
     */
    @Override
    public void setSource(@NotNull Index source){
        if((source.getRow() >=0 && source.getRow()<getInnerMatrix().getPrimitiveMatrix().length)
        && (source.getColumn() >=0 &&
                source.getColumn() < getInnerMatrix().getPrimitiveMatrix()[0].length)){
            this.source = source;
        }

    }

    /**
     * Sets the destination index of the TraversableMatrix.
     * @param destination The destination index to be set.
     */
    @Override
    public void setDestination(@NotNull Index destination) {
        if((destination.getRow() >=0 && destination.getRow()<getInnerMatrix().getPrimitiveMatrix().length)
                && (destination.getColumn() >=0 &&
                destination.getColumn() < getInnerMatrix().getPrimitiveMatrix()[0].length)){
            this.destination = destination;
        }
    }

    /**
     * Gets the destination node of the TraversableMatrix.
     * @return The destination node.
     */
    public Node<Index> getDestination() {return new Node<>(destination); }


    /**
     * Gets the root node of the TraversableMatrix.
     * @return The root node.
     */
    @Override
    public Node<Index> getRoot()
    {
        return new Node<>(source);
    }

    /**
     * Gets the collection of reachable nodes from a given node in the TraversableMatrix.
     * @param aNode The node to get the reachable nodes from.
     * @return The collection of reachable nodes.
     */
    @Override
    public Collection<Node<Index>> getReachableNodes(Node<Index> aNode) {
        if (getInnerMatrix().getValue(aNode.getData())==1){
            List<Node<Index>> reachableNodes = new ArrayList<>();
            for(Index index:getInnerMatrix().getNeighbors(aNode.getData())){
                if (getInnerMatrix().getValue(index)==1){
                    Node<Index> singleNode = new Node<>(index,aNode);
                    reachableNodes.add(singleNode);
                }
            }
            return reachableNodes;
        }
        return null;
    }

    /**
     * Gets the collection of all reachable nodes from a given node in the TraversableMatrix.
     * @param aNode The node to get all reachable nodes from.
     * @return The collection of all reachable nodes.
     */
    @Override
    public Collection<Node<Index>> getAllReachableNodes(Node<Index> aNode) {
        if (getInnerMatrix().getValue(aNode.getData())==1){
            List<Node<Index>> reachableNodes = new ArrayList<>();
            for(Index index:getInnerMatrix().getAllNeighborsIncludingDiagonals(aNode.getData())){
                if (getInnerMatrix().getValue(index)==1){
                    Node<Index> singleNode = new Node<>(index,aNode);
                    reachableNodes.add(singleNode);
                }
            }
            return reachableNodes;
        }
        return null;
    }

    /**
     * Gets the collection of all neighbors of a given node in the TraversableMatrix.
     * @param aNode The node to get the neighbors from.
     * @return The collection of all neighbors.
     */
    @Override
    public Collection<Node<Index>> getAllNeighbors(Node<Index> aNode) {

        List<Node<Index>> reachableNodes = new ArrayList<>();
        for(Index index:getInnerMatrix().getAllNeighborsIncludingDiagonals(aNode.getData())) {
            Node<Index> singleNode = new Node<>(index, aNode);
            reachableNodes.add(singleNode);
        }
        return reachableNodes;
    }

    /**
     Calculates the weight of a given path by summing up the values in the inner matrix for each node in the path.
     @param path The path for which to calculate the weight.
     @return The weight of the path.
     */
    public int getPathWeight(List<Node<Index>> path) {
        int weight = 0;
        for (Node<Index> node : path) {
            weight += getInnerMatrix().getValue(node.getData());
        }
        return weight;
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index The index of the value to retrieve.
     * @return The value at the specified index.
     */
    public int getValue(final Index index) {
        return getInnerMatrix().getValue(index);
    }

    /**
     * Retrieves a list of all traversable indexes in the TraversableMatrix.
     *
     * @return A list of Index objects representing all traversable indexes.
     */
    public List<Index> getAllTraversableIndexes() {
        List<Index> allIndexes = new ArrayList<>();

        // Iterate over each row and column in the primitive matrix
        for (int i = 0; i < getInnerMatrix().getPrimitiveMatrix().length; i++) {
            for (int j = 0; j < getInnerMatrix().getPrimitiveMatrix().length; j++) {
                Index index = new Index(i, j);
                allIndexes.add(index);
            }
        }

        return allIndexes;
    }

}