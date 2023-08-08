package pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * BfsTls is a class that implements the breadth-first search (BFS) algorithm using thread-local storage (TLS) to traverse a graph or tree.
 *
 * @param <T> The type of data stored in the nodes.
 */
public class BfsTls<T> implements Serializable {
    // TLS - Thread Local Storage

    private ThreadLocal<Queue<Node<T>>> queue;

    /**
     * Constructs a new BfsTls object.
     * Initializes the thread-local queue.
     */
    public BfsTls(){
        setQueue(ThreadLocal.withInitial(LinkedList::new));
    }


    /* traverse-
    1. It initializes an ArrayList called shortestPaths to store the shortest paths.
    2. The source and destination nodes are set in the Traversable object.
    3. The root/source node and destination node are obtained from the Traversable.
    4. The source node is enqueued in the queue to start the BFS traversal.
    5. The BFS traversal is performed until the destination node is reached.
    6. In each iteration, the number of nodes in the current level is obtained.
    7. The nodes in the current level are processed one by one.
    8. If a node is the destination, the shortest path is constructed by backtracking from the destination to the source.
    9. The shortest path is added to the list of the shortest paths, ensuring the path is from source to destination order.
    10. The nodes reachable from the current node are enqueued.
    11. If the destination is reached in the current level, the traversal is stopped.
    12. Finally, the list of the shortest paths is returned. */

    /**
     * Traverses the Traversable object using the BFS algorithm and returns a list of the shortest paths from source to destination.
     *
     * @param aTraversable The Traversable object representing the graph or tree.
     * @param source       The source index/node.
     * @param destination  The destination index/node.
     * @return A list of the shortest paths from source to destination.
     */
    public List<List<Node<T>>> traverse(Traversable<T> aTraversable, Index source, Index destination) {

        //Get the connected component of the source index
        DfsVisitTls<Index> algorithm = new DfsVisitTls<>();
        // Sending false as a parameter to indicate that we want to use also the diagonal neighbors as a valid path (inside its using the 'getAllReachableNodes()').
        Set<Index> connectedComponent =  algorithm.traverse((Traversable<Index>) aTraversable, false);


        // Initialize the list to store shortest paths
        List<List<Node<T>>> shortestPaths = new ArrayList<>();

        //Check if the source index is in the same connected component as the destination index
        if (connectedComponent.contains(destination)) {

            // Set the source and destination nodes in the Traversable
            aTraversable.setSource(source);
            aTraversable.setDestination(destination);

            // Get the root/source node and destination node
            Node<T> sourceIndex = aTraversable.getRoot();
            Node<T> destinationIndex = aTraversable.getDestination();

            // Initialize the queue and enqueue the source node
            threadLocalEnqueue(sourceIndex);

            // Perform BFS traversal until the destination node is reached
            while (!threadLocalIsEmpty()) {
                // Get the number of nodes in the current level
                int levelSize = getQueue().get().size();

                // Flag to track if the destination node is reached in the current level
                boolean destinationReached = false;

                // Process nodes in the current level
                for (int i = 0; i < levelSize; i++) {
                    // Remove a node from the queue
                    Node<T> removed = threadLocalDequeue();

                    // Check if the removed node is the destination
                    if (removed.getData().equals(destinationIndex.getData())) {
                        // Backtrack to construct the shortest path
                        List<Node<T>> shortestPath = new ArrayList<>();
                        shortestPath.add(removed);
                        Node<T> current = removed;
                        while (current.getParent() != null) {
                            current = current.getParent();
                            shortestPath.add(current);
                        }

                        // Reverse the shortest path to get it from source to destination order
                        Collections.reverse(shortestPath);

                        // Add the shortest path to the list of the shortest paths
                        shortestPaths.add(shortestPath);

                        // Mark the destination as reached in the current level
                        destinationReached = true;
                    }

                    // Get all reachable nodes from the removed node
                    Collection<Node<T>> reachableNodes = aTraversable.getAllReachableNodes(removed);
                    for (Node<T> node : reachableNodes) {
                        // Enqueue the node and set its parent
                        node.setParent(removed);
                        threadLocalEnqueue(node);
                    }
                }

                // If the destination is reached in the current level, exit the traversal
                if (destinationReached) {
                    break;
                }
            }

            // Clean up the thread-local variables
            getQueue().remove();
        }

        // Return the list of the shortest paths
        return shortestPaths;
    }

    /**
     * Sets the thread-local variable containing the queue of nodes.
     *
     * @param queue The thread-local variable containing the queue of nodes.
     */
    private void setQueue(ThreadLocal<Queue<Node<T>>> queue) {
        this.queue = queue;
    }

    /**
     * Retrieves the thread-local variable containing the queue of nodes.
     *
     * @return The thread-local variable containing the queue of nodes.
     */
    private ThreadLocal<Queue<Node<T>>> getQueue() {
        return queue;
    }

    /**
     * Enqueues a node into the thread-local queue.
     *
     * @param node The node to enqueue.
     */
    private void threadLocalEnqueue(Node<T> node){
        getQueue().get().offer(node);
    }

    /**
     * Dequeues a node from the thread-local queue.
     *
     * @return The dequeued node.
     */
    private Node<T> threadLocalDequeue(){
        return getQueue().get().remove();
    }

    /**
     * Checks if the thread-local queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    private boolean threadLocalIsEmpty(){
        return getQueue().get().isEmpty();
    }

}