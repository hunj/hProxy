import java.io.*;
import java.net.*;

/**
 * hProxy by hunj (160311)
 * EECS325 Computer Networks Project 1
 *
 * If you want to try running on your local host (i.e. your computer),
 * try changing your HTTP proxy server to localhost, with given port number (default 5016).
 *
 */
public class hProxy {

    // program would be run in a way:
    // $ javac proxyd.java ; java proxyd -port 1231
    public static void main(String[] args) throws IOException {

        // the default port to use if no port is specified
        int port = 5016;

        System.out.println("hProxy v1.0 initiated...");

        // check argument and start at given port or default port, or give error if arguments are invalid.
        if (checkArguments(args)) {
            try {
                System.out.println("<SYSTEM>: Establishing connection on hProxy on port " + args[1]);
                port = Integer.parseInt(args[1]);
            }
            catch (Exception e) {
                System.err.println("< ERR! >: please provide correct integer for port number, i.e. -port 1234");
                e.printStackTrace();
            }
        } else {
            System.out.println("<SYSTEM>: port number not given; defaulting to 5016...");
        }

        // finally, start running.
        startRunningAtPort(port);
    }


    /**
     * checks the arguments
     * @param argumentArray the given arguments to the program
     * @return whether the arguments are valid or not
     * @throws Exception when argument is weird?
     */
    private static boolean checkArguments(String[] argumentArray) {
        return argumentArray.length > 0 && (argumentArray[0].equals("-port") || argumentArray[0].equals("-p"));
    }

    /**
     * Starts the proxy on given port.
     * @param portNumber the given port number to use
     * @throws Excpetion when something is wrong
     */
    private static void startRunningAtPort(int portNumber) {

        // give cute welcome message to user
        System.out.println("<SYSTEM>: Welcome to hProxy!");
        System.out.println("<SYSTEM>: Socket trying on port " + portNumber + "...");

        // empty socket instance to run
        ServerSocket hSocket = null;
        boolean keepRunning = true;

        // make a new server socket
        try {
            hSocket = new ServerSocket(portNumber);
            System.out.println("<SYSTEM>: Socket is now running on port " + portNumber + ".");
            while (keepRunning) {
                System.out.println("<SYSTEM>: hProxy ready to serve: ");
                new hProxyThread(hSocket.accept()).start();
            }

            // when done, safely close the socket.
            hSocket.close();

            // some error catching
        } catch (IOException e) {
            System.err.println("< ERR! >: Could not listen on port " + portNumber);
        }

        // whenever keepRunning is triggered, safely close the socket and exit.
        System.out.println("<SYSTEM>: hProxy terminated. Good bye...");
        System.exit(-1);
    }

}














