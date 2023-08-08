package pojo;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * The Traversable interface defines the common functionality required of all concrete graphs.
 * @param <T> The type of data stored in the graph nodes.
 */
public interface Traversable<T> {

    /**
     * Gets the root node of the graph.
     * @return The root node.
     */
    public abstract Node<T> getRoot();

    /**
     * Gets the collection of reachable nodes from a given node in the graph.
     * @param aNode The node to get the reachable nodes from.
     * @return The collection of reachable nodes.
     */
    public abstract Collection<Node<T>> getReachableNodes(Node<T> aNode);

    /**
     * Gets the collection of all reachable nodes from a given node in the graph.
     * @param aNode The node to get all reachable nodes from.
     * @return The collection of all reachable nodes.
     */
    public abstract Collection<Node<T>> getAllReachableNodes(Node<T> aNode);

    /**
     * Gets the collection of all neighbors of a given node in the graph.
     * @param aNode The node to get the neighbors from.
     * @return The collection of all neighbors.
     */
    public abstract Collection<Node<T>> getAllNeighbors(Node<T> aNode);

    /**
     * Sets the source index of the graph.
     * @param source The source index.
     */
    public abstract void setSource(@NotNull Index source);

    /**
     * Sets the destination index of the graph.
     * @param destination The destination index.
     */
    public abstract void setDestination(@NotNull Index destination);

    /**
     * Gets the destination node of the graph.
     * @return The destination node.
     */
    public abstract Node<T> getDestination();

    /**
     * Calculates the weight of a given path.
     * @param path The list of nodes representing the path.
     * @return The weight of the path as an integer.
     */
    public int getPathWeight(List<Node<Index>> path);

    /**
     * Gets the inner matrix of the graph.
     * @return The inner matrix.
     */
    public abstract Matrix getInnerMatrix();

    public int getValue(final Index index);

    public List<Index> getAllTraversableIndexes();

}
