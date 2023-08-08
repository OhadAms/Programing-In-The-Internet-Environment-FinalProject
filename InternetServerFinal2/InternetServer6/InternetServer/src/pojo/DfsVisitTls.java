package pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;



/*
1.The DfsVisitTls class implements the depth-first search (DFS) algorithm using thread-local storage (TLS) for thread safety in a multithreaded environment.
2.It uses thread-local variables, workingStack and finished, to maintain separate instances for each thread.
3.The constructor initializes the thread-local workingStack as an empty stack and the finished set as an empty linked hash set.
4.The traverse method performs DFS traversal on the Traversable object and returns a set of visited nodes' data.
5.It starts by pushing the root node onto the thread-local workingStack.
6.The traversal continues while the thread-local workingStack is not empty.
7.In each iteration, a node is popped from the workingStack and added to the finished set to mark it as visited.
8.Depending on the bool flag, reachable nodes are retrieved for the connected component or all connected components.
9.Each reachable node is added to the workingStack if it hasn't been visited and is not already in the stack.
10.After traversal, a new set called visitedData is created to store the data of visited nodes from the finished set.
11.Data from each node in the finished set is added to the visitedData set.
12.The finished set is cleared to prepare for subsequent traversals.
13.The visitedData set is returned, containing the data from the visited nodes during traversal.
 */

/**
 * DfsVisitTls is a class that implements the depth-first search (DFS) algorithm using thread-local storage (TLS) to traverse a graph or tree.
 * @param <T> The type of data stored in the nodes.
 */
public class DfsVisitTls<T> implements Serializable {

    // TLS - Thread Local Storage
    private ThreadLocal<Stack<Node<T>>> workingStack;
    private ThreadLocal<Set<Node<T>>> finished;

    /**
     * Constructs a new DfsVisitTls object.
     * Initializes the thread-local working stack and the finished set.
     */
    public DfsVisitTls(){
        setWorkingStack(ThreadLocal.withInitial(Stack::new));
        setFinished(ThreadLocal.withInitial(LinkedHashSet::new));
    }

    /**
     * Traverses the Traversable object using the DFS algorithm and returns a set of visited nodes' data.
     * @param aTraversable The Traversable object representing the graph or tree.
     * @param bool         A flag indicating whether to consider only the connected component (true) or all connected components (false).
     * @return A set of data from the visited nodes.
     */
    public Set<T> traverse(Traversable<T> aTraversable , Boolean bool){

        Collection<Node<T>> reachableNodes;
        threadLocalPush(aTraversable.getRoot());
        while (!threadLocalIsEmpty()){
            Node<T> removed = threadLocalPop();
            getFinished().get().add(removed);
            if (bool) {
                reachableNodes = aTraversable.getReachableNodes(removed);
            }else{
                reachableNodes = aTraversable.getAllReachableNodes(removed);
            }
            if (reachableNodes != null){
                for(Node<T> reachableNode :reachableNodes){
                    if (!getFinished().get().contains(reachableNode) &&
                            !getWorkingStack().get().contains(reachableNode)){
                        threadLocalPush(reachableNode);
                    }
                }
            }
        }
        Set<T> visitedData = new LinkedHashSet<>();
        for (Node<T> node: getFinished().get())
            visitedData.add(node.getData());
        getFinished().get().clear();
        getWorkingStack().get().clear();

        // Clean up the thread-local variables
        getFinished().remove();
        getWorkingStack().remove();

        return visitedData;
    }


    /**
     * Retrieves the thread-local variable for the working stack.
     *
     * @return The thread-local variable for the working stack.
     */
    private ThreadLocal<Stack<Node<T>>> getWorkingStack() {
        return workingStack;
    }

    /**
     * Sets the thread-local variable for the working stack.
     *
     * @param workingStack The thread-local variable for the working stack.
     */
    private void setWorkingStack(ThreadLocal<Stack<Node<T>>> workingStack) {
        this.workingStack = workingStack;
    }

    /**
     * Retrieves the thread-local variable for the set of finished nodes.
     *
     * @return The thread-local variable for the set of finished nodes.
     */
    private ThreadLocal<Set<Node<T>>> getFinished() {
        return finished;
    }

    /**
     * Sets the thread-local variable for the set of finished nodes.
     *
     * @param finished The thread-local variable for the set of finished nodes.
     */
    private void setFinished(ThreadLocal<Set<Node<T>>> finished) {
        this.finished = finished;
    }

    /**
     * Pushes a node to the thread-local working stack.
     * @param node The node to push.
     */
    private void threadLocalPush(Node<T> node){
        workingStack.get().push(node);
    }

    /**
     * Pops a node from the thread-local working stack.
     * @return The popped node.
     */
    private Node<T> threadLocalPop(){
        return workingStack.get().pop();
    }

    /**
     * Checks if the thread-local working stack is empty.
     * @return true if the stack is empty, false otherwise.
     */
    private boolean threadLocalIsEmpty(){
        return workingStack.get().isEmpty();
    }
}