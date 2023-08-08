package pojo;

import java.util.Set;

public class Demo {
    public static void main(String[] args) {

        String name = null;
        System.out.println(name.length());
        int[][] myArray = {
                {1,1,0},
                {0,1,1},
                {0,1,1}
        };

        Matrix matrix = new Matrix(myArray);
        TraversableMatrix matrixAsGraph = new TraversableMatrix(matrix);
        System.out.println(matrixAsGraph.getInnerMatrix());
        matrixAsGraph.setSource(new Index(0,0));
        DfsVisit<Index> algorithm = new DfsVisit<>();
        Set<Index> connectedComponent = algorithm.traverse(matrixAsGraph, true);
        System.out.println(connectedComponent);


    }
}
