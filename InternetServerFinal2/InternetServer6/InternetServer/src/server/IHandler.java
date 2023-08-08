package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * The IHandler interface represents a handler for client requests.
 */

public interface IHandler {

    /**
     * Handles the client request by processing the input from the client and providing a response.
     * @param fromClient The input stream from the client.
     * @param toClient   The output stream to the client.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public abstract void handleClient(InputStream fromClient,
                                      OutputStream toClient) throws IOException, ClassNotFoundException;
}
