package pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The AllConnectedComponents class calculates all connected components in a TraversableMatrix.
 */
public class AllConnectedComponents implements Serializable {

    /*
    *
    1. Initialize an empty HashSet called allConnectedComponents to store the connected components.
    2. Get a list of all traversable indexes in the TraversableMatrix.
    3. Iterate over each index in the list.
    4. Set the current index as the source in the TraversableMatrix.
    5. Perform a depth-first search (DFS) traversal using the DfsVisitTls algorithm on the TraversableMatrix, considering diagonal neighbors as valid paths.
    6. Retrieve the set of nodes in the connected component obtained from the traversal.
    7. Check if the allConnectedComponents set already contains the connected component and if the value at the current index in the TraversableMatrix is not zero.
    8. If the connected component is not already in the set and the value is not zero, add a new HashSet of the connected component to the allConnectedComponents set.
    9. Repeat steps 5-9 for each index in the list.
    10. Return the allConnectedComponents set containing all the connected components found in the TraversableMatrix. */

    /**
     * Calculates all connected components in the given TraversableMatrix.
     *
     * @param aTraversable The TraversableMatrix representing the graph.
     * @return A HashSet containing all connected components as HashSet of Index objects.
     */
    public HashSet<HashSet<Index>> getAllConnectedComponents(TraversableMatrix aTraversable) {
        HashSet<HashSet<Index>> allConnectedComponents = new HashSet<>();
        Set<Index> connectedComponent;
        List<Index> indexList = aTraversable.getAllTraversableIndexes();

        for (Index index : indexList) {
            aTraversable.setSource(index);

            // Sending false as a parameter to indicate that we want to use also the diagonal neighbors as a valid path (inside it's using the 'getAllReachableNodes()').
            DfsVisitTls<Index> algorithm = new DfsVisitTls<>();
            connectedComponent = algorithm.traverse(aTraversable, false);

            if (!allConnectedComponents.contains(connectedComponent) && aTraversable.getInnerMatrix().getValue(index) != 0) {
                allConnectedComponents.add(new HashSet<>(connectedComponent));
            }
        }
        return allConnectedComponents;
    }
}