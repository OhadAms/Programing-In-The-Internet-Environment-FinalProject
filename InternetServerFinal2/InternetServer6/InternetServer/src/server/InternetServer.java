package server;

import pojo.MatrixHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The InternetServer class represents a TCP server that can handle multiple clients concurrently.
 * It can solve different algorithmic problems using dedicated handler types.
 */
public class InternetServer {
    private final int port;
    /*
     Happens-Before guarantee does NOT ensure thread-safety nor executed quickly.
     It makes sure that if only one thread can change the value at a time, the data will be updated before any other thread can
     access it
     */
    private final AtomicBoolean stopServer;

    private ThreadPoolExecutor clientsPool; // handle multiple clients concurrently
    /*
    ThreadPoolExecutor is a data structure that mainly contains 2 components:
    1. A dynamic array of threads
    2. Queue of tasks - Runnable/Callable tasks
     */
    private IHandler requestHandler;

    /**
     * Creates a new instance of InternetServer with the specified port.
     * @param port The port number for the server.
     */
    public InternetServer(int port){
        this.port = port;
        this.clientsPool = null;
        this.requestHandler = null;
        this.stopServer = new AtomicBoolean(false);
    }

    /**
     * Configures the server to support clients with the specified handler.
     * @param concreteHandler The handler to be used for client requests.
     */
    public void supportClients(IHandler concreteHandler) {
        this.requestHandler = concreteHandler;

        /*
         * No matter if handling one client or multiple clients,
         * once a server has several operations at the same time,
         * we ought to define different executable paths (threads)
         */
        Runnable clientHandling = () -> {
            this.clientsPool = new ThreadPoolExecutor(
                    10, 15, 200, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );

            try {
                ServerSocket serverSocket = new ServerSocket(this.port, 50);

                while (!stopServer.get()) {
                    // listen + accept (phases 3+4) are done by accept method
                    Runnable acceptThread = () -> {
                        try {
                            Socket clientToServerConnection = serverSocket.accept();
                            System.out.println("Server: accepting client in " + Thread.currentThread().getName() + " Thread");


                            // Once a client is accepted, pass it to the specific client handling thread
                            Runnable specificClientHandling = () -> {
                                System.out.println("Server: Handling a client in " + Thread.currentThread().getName() + " Thread");

                                try {
                                    requestHandler.handleClient(clientToServerConnection.getInputStream(),
                                            clientToServerConnection.getOutputStream());
                                } catch (IOException | ClassNotFoundException ioException) {
                                    ioException.printStackTrace();
                                }
                                // We stopped handling the specific client
                                try {
                                    clientToServerConnection.getOutputStream().close();
                                    clientToServerConnection.getInputStream().close();
                                    clientToServerConnection.close();
                                } catch (IOException ioException) {
                                    //ioException.printStackTrace();
                                    System.out.println("Socket is closed!");
                                }
                            };

                            clientsPool.execute(specificClientHandling);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    };
                    new Thread(acceptThread).start();
                }

                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };

        new Thread(clientHandling).start();
    }

    /**
     * Stops the server and shuts down the client thread pool.
     */
    public void stop() {
        if (!stopServer.get()) {
            // Atomically set the stopServer to true if it is currently false
            if (stopServer.compareAndSet(false, true)) {
                if (clientsPool != null)
                    clientsPool.shutdown();
            }
        }
    }

    /**
     * The main method of the server that starts the server on the specified port
     * and supports clients using the MatrixHandler.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        InternetServer server = new InternetServer(8010);
        server.supportClients(new MatrixHandler());

        //server.stop();
    }
}