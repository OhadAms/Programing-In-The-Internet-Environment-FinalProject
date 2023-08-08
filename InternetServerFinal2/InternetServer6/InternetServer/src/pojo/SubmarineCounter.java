package pojo;

import java.io.Serializable;
import java.util.*;

/**
 * The SubmarineCounter class is used to count the number of valid submarines in a graph.
 */
public class SubmarineCounter implements Serializable {

    // ThreadLocal variable to store the number of submarines
    private final ThreadLocal<Integer> numOfSubmarinesThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<HashSet<HashSet<Index>>> allConnectedComponentsThreadLocal = new ThreadLocal<>();

    /**
     * Counts the number of submarines in the graph.
     *
     * @param aTraversable The TraversableMatrix representing the graph.
     * @return The number of submarines.
     */
    public int countSubmarines(TraversableMatrix aTraversable) {
        getNumOfSubmarinesThreadLocal().set(0);
        AllConnectedComponents algorithm = new AllConnectedComponents();
        setAllConnectedComponentsThreadLocal(algorithm.getAllConnectedComponents(aTraversable));

        Map<Integer, List<Integer>> map = new HashMap<>();
        HashSet<HashSet<Index>> allConnectedComponents = getAllConnectedComponentsThreadLocal().get();
        // Iterate over each connected component
        for (HashSet<Index> connectedComponent : allConnectedComponents) {
            if (connectedComponent.size() > 1) {
                for (Index index : connectedComponent) {
                    int row = index.getRow();

                    // Retrieve the list for the current row from the map
                    List<Integer> rowList = map.getOrDefault(row, new ArrayList<>());

                    // Add the index to the row list
                    rowList.add(index.getColumn());

                    // Update the map with the modified row list
                    map.put(row, rowList);
                }

                // Convert the map values to a List<List<Index>> if needed
                List<List<Integer>> list = new ArrayList<>(map.values());
                System.out.println("list is: " + list);
                int flag = 0;
                if (!list.isEmpty()) {
                    List<Integer> first = list.get(0);

                    // Check if all sublist have the same values
                    for (List<Integer> sublist : list) {
                        if (!new HashSet<>(sublist).containsAll(first) || !new HashSet<>(first).containsAll(sublist)) {
                            // Values are not all the same
                            flag = 0;
                            break;
                        }
                        flag = 1;
                    }
                    if (flag == 1) {
                        // All values are the same, increment the count of submarines
                        int currentCount = getNumOfSubmarinesThreadLocal().get();
                        getNumOfSubmarinesThreadLocal().set(currentCount + 1);
                    }
                }
                // Clear the list and map for the next connected component
                list.clear();
                map.clear();
            }
        }

        int numOfSubmarines = getNumOfSubmarinesThreadLocal().get();
        getNumOfSubmarinesThreadLocal().remove();
        getAllConnectedComponentsThreadLocal().remove();
        return numOfSubmarines;
    }

    /**
     * Retrieves the thread-local variable for the number of submarines.
     *
     * @return The thread-local variable for the number of submarines.
     */
    public ThreadLocal<Integer> getNumOfSubmarinesThreadLocal() {
        return this.numOfSubmarinesThreadLocal;
    }

    /**
     * Retrieves the thread-local variable for the set of all connected components.
     *
     * @return The thread-local variable for the set of all connected components.
     */
    public ThreadLocal<HashSet<HashSet<Index>>> getAllConnectedComponentsThreadLocal() {
        return this.allConnectedComponentsThreadLocal;
    }

    /**
     * Sets the thread-local variable for the set of all connected components.
     *
     * @param allConnectedComponents The set of all connected components.
     */
    public void setAllConnectedComponentsThreadLocal(HashSet<HashSet<Index>> allConnectedComponents) {
        this.allConnectedComponentsThreadLocal.set(allConnectedComponents);
    }

}