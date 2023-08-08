package pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

    /*
    1.The DfsVisit class implements the depth-first search (DFS) algorithm to traverse a graph or tree.
    2.It maintains a workingStack to track nodes during the traversal and a finished set to store visited nodes.
    3.The constructor initializes the workingStack as an empty stack and the finished set as an empty linked hash set.
    4.The traverse method performs the DFS traversal and returns a set of visited nodes' data.
    5.The method starts by pushing the root node onto the workingStack.
    6.While the workingStack is not empty, it pops a node from the stack and adds it to the finished set.
    7.Depending on the bool flag, it retrieves reachable nodes either for the connected component or for all connected components.
    8.Each reachable node is added to the workingStack if it has not been visited before.
    9.After the traversal, a new set called visitedData is created to store the data of the visited nodes.
    10.The data from each node in the finished set is added to the visitedData set.
    11.The finished set is cleared, and the visitedData set is returned.
    --The strategy emphasizes using stack-based traversal, tracking visited nodes, and distinguishing between connected component and all connected components modes.--
     */


/**
 * DfsVisit is a class that implements the depth-first search (DFS) algorithm to traverse a graph or tree.
 *
 * @param <T> The type of data stored in the nodes.
 */
public class DfsVisit<T> implements Serializable {

    private Stack<Node<T>> workingStack;
    private Set<Node<T>> finished;

    /**
     * Constructs a new DfsVisit object.
     * Initializes the working stack and the finished set.
     */
    public DfsVisit(){
        setWorkingStack(new Stack<>());
        setFinished(new LinkedHashSet<>());
    }

    /**
     * Traverses the Traversable object using the DFS algorithm and returns a set of visited nodes' data.
     *
     * @param aTraversable The Traversable object representing the graph or tree.
     * @param bool         A flag indicating whether to consider only the connected component (true) or all connected components (false).
     * @return A set of data from the visited nodes.
     */
    public Set<T> traverse(Traversable<T> aTraversable, Boolean bool){
        Collection<Node<T>> reachableNodes;
        getWorkingStack().push(aTraversable.getRoot());
        while (!getWorkingStack().empty()){
            Node<T> removed = getWorkingStack().pop();
            getFinished().add(removed);
            //"connected component" case
            if (bool){
                reachableNodes = aTraversable.getReachableNodes(removed);
            }
            //"all connected components" case
            else{
                reachableNodes = aTraversable.getAllReachableNodes(removed);
            }
            if (reachableNodes != null){
                for(Node<T> reachableNode :reachableNodes){
                    if (!getFinished().contains(reachableNode) &&
                            !getWorkingStack().contains(reachableNode)){
                        getWorkingStack().push(reachableNode);
                    }
                }
            }
        }
        Set<T> visitedData = new LinkedHashSet<>();
        for (Node<T> node: getFinished())
            visitedData.add(node.getData());

        getFinished().clear();
        getWorkingStack().clear();

        // if the only connected component of the source is the source itself and the value of that source is 0 then it is not a connected component.
        if(visitedData.size() == 1 && visitedData.contains(aTraversable.getRoot().getData()) && aTraversable.getValue((Index) aTraversable.getRoot().getData()) == 0) {
            visitedData.clear();
        }
        return visitedData;
    }

    private Stack<Node<T>> getWorkingStack() {
        return workingStack;
    }

    private void setWorkingStack(Stack<Node<T>> workingStack) {
        this.workingStack = workingStack;
    }

    private Set<Node<T>> getFinished() {
        return finished;
    }

    private void setFinished(Set<Node<T>> finished) {
        this.finished = finished;
    }
}