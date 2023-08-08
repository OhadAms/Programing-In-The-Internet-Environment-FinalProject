package pojo;

import server.IHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The MatrixHandler class handles Matrix-related tasks and adapts the functionality of IHandler to a Matrix object.
 */
public class MatrixHandler implements IHandler {

    private Matrix matrix; // The matrix object used by this class.
    private Index sourceIndex; // The source index used for a specific operation.
    private Index destinationIndex; // The destination index used for a specific operation.
    private boolean doWork; // A flag indicating whether work should be performed or not.


    /**
     * Handles the client request by reading from the input stream and writing to the output stream.
     * @param fromClient The input stream from the client.
     * @param toClient   The output stream to the client.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */

    @Override
    public void handleClient(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        data is sent eventually as bytes
        read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives
        and Reference types
         */
        ExecutorService executor = null;
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        setDoWork(true);
        while (isDoWork()) {
            switch (objectInputStream.readObject().toString()) {
                case "matrix" -> {
                    // expect to get a 2d array. handler will create a Matrix object
                    try {
                        int[][] anArray = (int[][]) objectInputStream.readObject();
                        System.out.println("Got 2d array");
                        setMatrix(new Matrix(anArray));
                        getMatrix().printMatrix();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case "get neighbors" -> {
                    setSourceIndex((Index) objectInputStream.readObject());

                    if (getMatrix() != null) {

                       // ExecutorService executor = null;

                        try {
                            executor = Executors.newSingleThreadExecutor();
                            Callable<List<Index>> getNeighborsCallable = createGetNeighborsCallable(getSourceIndex());
                            Future<List<Index>> neighborsFuture = executor.submit(getNeighborsCallable);

                            List<Index> neighbors = neighborsFuture.get();
                            System.out.println("Neighbors of " + getSourceIndex() + " are: " + neighbors);
                            objectOutputStream.writeObject(neighbors);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } /*finally {
                            if(executor != null) {
                                executor.shutdown();
                            }
                        }*/
                    }
                }
                case "connected component" -> {
                    setSourceIndex((Index) objectInputStream.readObject());
                    if (getMatrix() != null) {
                        TraversableMatrix matrixAsGraph = new TraversableMatrix(getMatrix());
                        matrixAsGraph.setSource(getSourceIndex());

                        //ExecutorService executor = null;

                        try {
                        executor = Executors.newSingleThreadExecutor();
                        // Sending true as a parameter to indicate that we ** Don't ** want to use the diagonal neighbors as a valid path (inside its using the 'getReachableNodes()').
                        Callable<Set<Index>> dfsCallable = createDFSCallable(matrixAsGraph, true);
                        Future<Set<Index>> dfsFuture = executor.submit(dfsCallable);
                        Set<Index> connectedComponent = dfsFuture.get();

                        if(connectedComponent.isEmpty()) {
                            System.out.println("The Index " + matrixAsGraph.getRoot().getData() + " has no connected component");
                            objectOutputStream.writeObject("The Index " + matrixAsGraph.getRoot().getData() + "has no connected component");
                        } else {
                            objectOutputStream.writeObject(connectedComponent);
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } /*finally {
                            if (executor != null) {
                                executor.shutdown();
                            }
                        }*/
                    }
                }

                //The new case we created for "all connected components" containing the diagonals
                case "all connected component" -> {

                    HashSet<HashSet<Index>> allConnectedComponents = new HashSet<>();

                    if (getMatrix() != null) {
                        TraversableMatrix matrixAsGraph = new TraversableMatrix(getMatrix());

                       // ExecutorService executor = null;

                        try {
                            executor = Executors.newSingleThreadExecutor();
                            Callable<HashSet<HashSet<Index>>> AllConnectedComponentsCallable = createAllConnectedComponentsCallable(matrixAsGraph);
                            Future<HashSet<HashSet<Index>>> future = executor.submit(AllConnectedComponentsCallable);
                            allConnectedComponents = future.get();

                        } catch (Exception e) {
                            e.printStackTrace();
                        } /*finally {
                            if (executor != null) {
                                executor.shutdown();
                            }
                        }*/
                    }
                    if(allConnectedComponents.isEmpty()) {
                        System.out.println("There are no connected components in the traversable matrix!");
                        objectOutputStream.writeObject("There are no connected components in this traversable matrix!");
                    } else {
                        System.out.println("all Connected Component are: " +  allConnectedComponents);
                        objectOutputStream.writeObject(allConnectedComponents);
                    }
                }

                case "all shortest paths" -> {

                    setSourceIndex((Index) objectInputStream.readObject());
                    setDestinationIndex((Index) objectInputStream.readObject());

                    if (getMatrix() != null) {
                        TraversableMatrix matrixAsGraph = new TraversableMatrix(getMatrix());
                        matrixAsGraph.setSource(getSourceIndex());

                        // ExecutorService executor = null;
                        try {
                            // BfsTls<Index> algorithm = new BfsTls<>();

                           // List<List<Node<Index>>> shortestPaths = algorithm.traverse(matrixAsGraph, sourceIndex, destinationIndex);

                            executor = Executors.newSingleThreadExecutor();

                            Callable<List<List<Node<Index>>>> bfsCallable = createBfsCallable(matrixAsGraph, getSourceIndex(), getDestinationIndex());
                            Future<List<List<Node<Index>>>> bfsFuture = executor.submit(bfsCallable);
                            List<List<Node<Index>>> shortestPaths = bfsFuture.get();

                            if(shortestPaths.isEmpty()) {
                                System.out.println("The destination " + getDestinationIndex() + " is not reachable from the source " + getSourceIndex());
                                objectOutputStream.writeObject("The destination is not reachable from the source");
                            }
                            else {
                                objectOutputStream.writeObject(shortestPaths);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } /*finally {
                            if(executor != null)
                                executor.shutdown();
                        }*/
                    }
                }
                case "submarines" -> {

                    if (getMatrix() != null) {
                        // Create a TraversableMatrix object based on the matrix
                        TraversableMatrix matrixAsGraph = new TraversableMatrix(getMatrix());

                        //ExecutorService executor = null;
                        try {
                            executor = Executors.newSingleThreadExecutor();
                            Callable<Integer> countSubmarinesCallable = createCountSubmarinesCallable(matrixAsGraph);
                            Future<Integer> countFuture = executor.submit(countSubmarinesCallable);

                            int numOfSubmarines = countFuture.get();
                            // Print the number of valid submarines to the console
                            System.out.println("The number of valid submarines is: " + numOfSubmarines);

                            // Write the number of valid submarines to the ObjectOutputStream
                            objectOutputStream.writeObject(numOfSubmarines);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } /*finally {
                            if(countExecutor != null) {
                                countExecutor.shutdown();
                            }
                        }*/
                    }
                }
                case "lightest paths" -> {

                    setSourceIndex((Index) objectInputStream.readObject());
                    setDestinationIndex((Index) objectInputStream.readObject());

                    if (getMatrix() != null) {
                        TraversableMatrix matrixAsGraph = new TraversableMatrix(getMatrix());
                        matrixAsGraph.setSource(getSourceIndex());
                        matrixAsGraph.setDestination(getDestinationIndex());

                       // ExecutorService executor = null;
                        try {
                            executor = Executors.newSingleThreadExecutor();

                            Callable<HashSet<List<Node<Index>>>> findPathsCallable = createLightestPathsCallable(matrixAsGraph, getSourceIndex(), getDestinationIndex());
                            Future<HashSet<List<Node<Index>>>> findPathsFuture = executor.submit(findPathsCallable);
                            HashSet<List<Node<Index>>> lightestPaths = findPathsFuture.get();
                            if(lightestPaths.isEmpty()) {
                                System.out.println("There are infinite path possible!");
                                objectOutputStream.writeObject("There are infinite path possible!");
                            } else {
                                objectOutputStream.writeObject(lightestPaths);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }/*finally {
                            if (executor != null) {
                                executor.shutdown();
                            }
                        }*/
                    }
                }
                case "stop" -> {
                    setDoWork(false);
                    if(executor != null) {
                        executor.shutdown();
                    }
                }
            }
        }
    }

    /**
     * Creates a callable object that finds the lightest paths between a source index and a destination index.
     * @param aTraversable-   The graph represented as a TraversableMatrix.
     * @param sourceIndex     The source index.
     * @param destinationIndex The destination index.
     * @return The callable object.
     */

    private Callable<HashSet<List<Node<Index>>>> createLightestPathsCallable(TraversableMatrix aTraversable, Index sourceIndex, Index destinationIndex) {
        return () -> {
            Dijkstra dijkstra = new Dijkstra();
            return dijkstra.findLightestPaths(aTraversable, sourceIndex, destinationIndex);
        };
    }

    /**
     * Creates a callable object that returns a list of neighbors for the given source index.
     * @param sourceIndex The source index.
     * @return The callable object.
     */

    private Callable<List<Index>> createGetNeighborsCallable(Index sourceIndex) {
        return () -> new ArrayList<>(getMatrix().getNeighbors(sourceIndex));
    }

    /**
     * Creates a callable object that traverses the graph and returns a set of indices representing a connected component.
     * @param matrixAsGraph The graph represented as a TraversableMatrix.
     * @param bool          A boolean value indicating whether to include diagonal neighbors as valid paths.
     * @return The callable object.
     */

    private Callable<Set<Index>> createTraverseCallable(TraversableMatrix matrixAsGraph, boolean bool) {
        return () -> {
            DfsVisitTls<Index> algorithm = new DfsVisitTls<>();
            return algorithm.traverse(matrixAsGraph, bool);
        };
    }

    /**
     * Creates a callable object that performs breadth-first search and returns a list of the shortest paths.
     * @param matrixAsGraph   The graph represented as a TraversableMatrix.
     * @param source          The source index.
     * @param destination     The destination index.
     * @return The callable object.
     */

    private Callable<List<List<Node<Index>>>> createBfsCallable(TraversableMatrix matrixAsGraph, Index source, Index destination) {
        return () -> {
            BfsTls<Index> algorithm = new BfsTls<>();
            return algorithm.traverse(matrixAsGraph, source, destination);
        };
    }

    /**
     * Creates a callable object that counts the number of valid submarines given a set of connected components.
     * @param aTraversable a traversable matrix.
     * @return The callable object.
     */

    private Callable<Integer> createCountSubmarinesCallable(TraversableMatrix aTraversable) {
        return () -> {
            SubmarineCounter algorithm = new SubmarineCounter();
            return algorithm.countSubmarines(aTraversable);
        };
    }

    /**
     * Creates a callable object that traverses the graph and returns a set of indices representing a connected component.
     * @param matrixAsGraph The graph represented as a TraversableMatrix.
     * @param bool          A boolean value indicating whether to include diagonal neighbors as valid paths.
     * @return The callable object.
     */

    private Callable<Set<Index>> createDFSCallable(TraversableMatrix matrixAsGraph,boolean bool) {
        return () -> {
            DfsVisit<Index> algorithm = new DfsVisit<>();
            return algorithm.traverse(matrixAsGraph, bool);
        };
    }

    /**
     * Creates a callable object that calculates all connected components in the given TraversableMatrix.
     *
     * @param matrixAsGraph The TraversableMatrix representing the graph.
     * @return A callable object that returns a HashSet containing all connected components as HashSet of Index objects.
     */
    private Callable<HashSet<HashSet<Index>>> createAllConnectedComponentsCallable(TraversableMatrix matrixAsGraph) {
        return () -> {
            AllConnectedComponents algorithm = new AllConnectedComponents();
            return algorithm.getAllConnectedComponents(matrixAsGraph);
        };
    }

    /**
     * Sets the matrix for this object.
     *
     * @param matrix The matrix to set.
     */
    private void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Retrieves the matrix associated with this object.
     *
     * @return The matrix object.
     */
    private Matrix getMatrix() {
        return matrix;
    }

    /**
     * Retrieves the source index associated with this object.
     *
     * @return The source index object.
     */
    private Index getSourceIndex() {
        return sourceIndex;
    }

    /**
     * Sets the source index for this object.
     *
     * @param sourceIndex The source index to set.
     */
    private void setSourceIndex(Index sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    /**
     * Retrieves the destination index associated with this object.
     *
     * @return The destination index object.
     */
    private Index getDestinationIndex() {
        return destinationIndex;
    }

    /**
     * Sets the destination index for this object.
     *
     * @param destinationIndex The destination index to set.
     */
    private void setDestinationIndex(Index destinationIndex) {
        this.destinationIndex = destinationIndex;
    }

    /**
     * Gets the value of the doWork variable.
     * @return The value of doWork.
     */
    private boolean isDoWork() {
        return doWork;
    }

    /**
     * Sets the value of the doWork variable.
     * @param doWork The new value for doWork.
     */
    private void setDoWork(boolean doWork) {
        this.doWork = doWork;
    }

}
