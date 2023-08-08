package pojo;

import java.util.*;

    /*
    * Explanation of how the algorithm works:

    1. The Dijkstra's algorithm finds the lightest paths in a graph from a source node to a destination node.
    2. The `findLightestPaths` method takes a traversable matrix, a source index, and a destination index as input.
    3. It initializes thread-local variables for a queue and a set of the lightest paths.
    4. It sets the source and destination nodes in the traversable matrix.
    5. The algorithm starts by enqueueing an initial path with the source node.
    6. It then enters a loop until the queue is empty.
    7. In each iteration, it dequeues a path from the queue and retrieves the last node in the path.
    8. If the last node is the destination node, it calculates the weight of the path and compares it with the current minimum weight.
    9. If the weight is smaller, it updates the minimum weight and clears the set of the lightest paths.
    10. If the weight is equal to the minimum weight, it adds the current path to the set of the lightest paths.
    11. Next, it retrieves all the neighbors of the current node in the traversable matrix.
    12. For each neighbor, it calculates the distance to the neighbor by adding the weight of the edge between the nodes.
    13. If the calculated weight is smaller than the current recorded weight to the neighbor, it updates the weight.
    14. It creates a new path by appending the neighbor to the current path and enqueues the new path.
    15. After the loop finishes, it creates a new HashSet containing the lightest paths from the thread-local set.
    16. Finally, it cleans up the thread-local variables and returns the set of the lightest paths.
    */

/**
 * Dijkstra class for finding all the shortest paths in a traversable matrix from a source index to a destination index.
 */
    class Dijkstra {

        // Thread-local variable for storing the queue of paths
        ThreadLocal<Queue<List<Node<Index>>>> queueTLS = new ThreadLocal<>();

        // Thread-local variable for storing the set of the shortest paths
        ThreadLocal<HashSet<List<Node<Index>>>> shortestPathsTLS = new ThreadLocal<>();

        /**
         * Finds the shortest paths in a traversable matrix from a source index to a destination index.
         *
         * @param aTraversable  The traversable matrix to search for paths.
         * @param sourceIndex        The index of the source node.
         * @param destinationIndex   The index of the destination node.
         * @return                   A set of the shortest paths from the source to the destination.
         */
        HashSet<List<Node<Index>>> findLightestPaths(TraversableMatrix aTraversable, Index sourceIndex, Index destinationIndex) {

            // Initialize the thread-local variables
            getQueueTLS().set(new LinkedList<>());
            getShortestPathsTLS().set(new HashSet<>());

            // Set the source and destination indices in the traversable matrix
            aTraversable.setSource(sourceIndex);
            aTraversable.setDestination(destinationIndex);

            // Get the source and destination nodes from the traversable matrix
            Node<Index> sourceNode = aTraversable.getRoot();
            Node<Index> destinationNode = aTraversable.getDestination();

            int minWeight = Integer.MAX_VALUE;

            // Map to store the distances from the source node to each node
            Map<Node<Index>, Integer> distances = new HashMap<>();
            distances.put(sourceNode, aTraversable.getInnerMatrix().getValue(sourceNode.getData()));

            // Create the first path containing only the source node and enqueue it
            List<Node<Index>> firstPath = new ArrayList<>();
            firstPath.add(sourceNode);
            threadLocalEnqueue(firstPath);

            // Perform Dijkstra's algorithm
            while (!threadLocalIsEmpty()) {
                List<Node<Index>> currentPath = threadLocalDequeue();
                Node<Index> currentNode = currentPath.get(currentPath.size() - 1);

                // If the current node is the destination node
                if (currentNode.equals(destinationNode)) {
                    int currentPathWeight = aTraversable.getPathWeight(currentPath);
                    // If the current path has a lower weight than the minimum weight found so far
                    if (currentPathWeight < minWeight) {
                        minWeight = currentPathWeight;
                        // Clear the set of the shortest paths and add the current path
                        getShortestPathsTLS().get().clear();
                        getShortestPathsTLS().get().add(currentPath);
                    }
                    // If the current path has the same weight as the minimum weight found so far
                    else if (currentPathWeight == minWeight) {
                        // Add the current path to the set of the shortest paths
                        getShortestPathsTLS().get().add(currentPath);
                    }
                }

                // Get the neighbors of the current node
                Collection<Node<Index>> neighbors = aTraversable.getAllNeighbors(currentNode);

                // Explore the neighbors
                for (Node<Index> neighbor : neighbors) {

                    // If we have 2 zeros one next to each other than we have infinite paths.
                    if(aTraversable.getValue(currentNode.getData()) == 0 && aTraversable.getValue(neighbor.getData()) == 0) {
                        return HashSet.newHashSet(0); // return an empty hashSet.
                    }
                    // Calculate the distance to the neighbor from the source node
                    int distanceToNeighbor = distances.get(currentNode) + aTraversable.getValue(neighbor.getData());

                    // If the distance to the neighbor is shorter than or equal to the current distance
                    if (distanceToNeighbor <= distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        // Update the distance to the neighbor
                        distances.put(neighbor, distanceToNeighbor);

                        // Create a new path by extending the current path with the neighbor
                        List<Node<Index>> newPath = new ArrayList<>(currentPath);
                        newPath.add(neighbor);

                        // Enqueue the new path
                        threadLocalEnqueue(newPath);
                    }
                }
            }

            // Create a new set of the shortest paths from the thread-local variable
            HashSet<List<Node<Index>>> shortestPaths = new HashSet<>(getShortestPathsTLS().get());

            // Clean up the thread-local variables
            getQueueTLS().remove();
            getShortestPathsTLS().remove();

            // Return the set of the shortest paths
            return shortestPaths;
        }

        /**
         * Returns the thread-local variable for the queue of paths.
         *
         * @return The thread-local variable for the queue of paths.
         */
        public ThreadLocal<Queue<List<Node<Index>>>> getQueueTLS() {
            return queueTLS;
        }

        /**
         * Returns the thread-local variable for the set of the shortest paths.
         *
         * @return The thread-local variable for the set of the shortest paths.
         */
        public ThreadLocal<HashSet<List<Node<Index>>>> getShortestPathsTLS() {
            return shortestPathsTLS;
        }

        /**
         * Enqueues a path into the thread-local queue.
         *
         * @param path The path to enqueue.
         */
        private void threadLocalEnqueue(List<Node<Index>> path) {
            getQueueTLS().get().offer(path);
        }

        /**
         * Dequeues a path from the thread-local queue.
         *
         * @return The dequeued path.
         */
        private List<Node<Index>> threadLocalDequeue() {
            return getQueueTLS().get().remove();
        }

        /**
         * Checks if the thread-local queue is empty.
         *
         * @return True if the queue is empty, false otherwise.
         */
        private boolean threadLocalIsEmpty() {
            return getQueueTLS().get().isEmpty();
        }
    }