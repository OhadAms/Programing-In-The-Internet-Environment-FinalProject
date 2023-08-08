package server;

import pojo.Index;
import pojo.Node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;


/**
 * The Client class represents a client that connects to a server and sends requests.
 */
public class Client {
    public static void main(String[] args) throws ClassNotFoundException, IOException {

        try {
            Socket clientSocket = new Socket("127.0.0.1",8010);
            System.out.println("Socket created");

            ObjectOutputStream toServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(clientSocket.getInputStream());

            int[][] sourceArray = {
                    {1,1,1,0,1},
                    {1,1,1,0,1},
                    {1,1,1,0,1},
                    {1,1,1,0,1},
                    {0,0,0,0,0}
            };

            toServer.writeObject("matrix");
            toServer.writeObject(sourceArray);

            Index index1 = new Index(0,0);
            Index index2 = new Index(2,2);
            Index index3 = new Index(3,1);
            Index index4 = new Index(3,3);
            Index index5 = new Index(2,2);
            Index index6 = new Index(0,3);
            Index index7 = new Index(2,4);

            //-----------------------------------------------------------------------------------------------------------------------

            toServer.writeObject("get neighbors");
            toServer.writeObject(index2);

            try {
                List<Index> neighbors = new ArrayList<Index>((List<Index>)fromServer.readObject());
                System.out.println("Neighbors of " + index2 + ": " + neighbors);
            }catch (IOException | RuntimeException e) {
                System.out.println("Oops, Something went wrong :( ");
            }

            //-----------------------------------------------------------------------------------------------------------------------

            toServer.writeObject("connected component");
            toServer.writeObject(index1);

            try {
                Set<Index> connectedComponent = new LinkedHashSet<Index>((Set<Index>)fromServer.readObject());
                System.out.println("Connected Component of " + index1 + ": " + connectedComponent);
            } catch (IOException | RuntimeException e) {
                System.out.println("The Index " + index1 + " has no connected component");
            }

            //-----------------------------------------------------------------------------------------------------------------------

            /*
             Exercise 1 - DFS
             */

            toServer.writeObject("all connected component");

           try{
               HashSet<HashSet<Index>> allConnectedComponents = new HashSet<HashSet<Index>> ((HashSet<HashSet<Index>>)fromServer.readObject());
               System.out.println("all Connected Component are: " +  allConnectedComponents);
           } catch (IOException | RuntimeException e) {
               System.out.println("There are no connected components in this traversable matrix!");
           }

            //-----------------------------------------------------------------------------------------------------------------------

            /*
             Exercise 2 - BFS
             */

            toServer.writeObject("all shortest paths");
            toServer.writeObject(index1);
            toServer.writeObject(index2);

            try {
                List<List<Node<Index>>> shortestPaths = new ArrayList<> ((List<List<Node<Index>>>) fromServer.readObject());
                System.out.println("Shortest paths are: " + shortestPaths);
            } catch (IOException | RuntimeException e) {
               System.out.println("The destination " + index2 + " is not reachable from the source " + index1);
            }

            //-----------------------------------------------------------------------------------------------------------------------

            /*
             Exercise 3 - submarines
             */

            toServer.writeObject("submarines");

            try {
                Integer validSubmarinesOnBoard = ((Integer) fromServer.readObject());
                System.out.println("valid Submarines On Board: " + validSubmarinesOnBoard);
            } catch (IOException ioException) {
                System.out.println("Oops, Something went wrong :( ");
            }

            //-----------------------------------------------------------------------------------------------------------------------

            /*
             Exercise 4 - Dijkstra
             */

            toServer.writeObject("lightest paths");
            toServer.writeObject(index1);
            toServer.writeObject(index2);

            try {
                HashSet<List<Node<Index>>> paths = new HashSet<>((HashSet<List<Node<Index>>>)fromServer.readObject());
                System.out.println(paths);
                System.out.println("Total lightest paths is " + paths.size());
            } catch (IOException | RuntimeException e) {
                System.out.println("There are infinite path possible!");
            }

            toServer.writeObject("stop");


            //-----------------------------------------------------------------------------------------------------------------------
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
