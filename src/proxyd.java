import java.io.*;
import java.net.*;
import java.util.*;

/**
 * hProxy by hunj (160311)
 * EECS325 Computer Networks Project 1
 *
 * If you want to try running on your local host (i.e. your computer),
 * try changing your HTTP proxy server to localhost, with given port number (default 5016).
 */
public class proxyd {
    // {hostname => (IP Address, TTL)}
    public static Hashtable<String, Tuple> record = new Hashtable<String, Tuple>();

    private static boolean silentMode = false;
    private static final long TTL = 30000;

    // program would be run in a way:
    // $ javac proxyd.java
    // $ java proxyd -port 1234
    public static void main(String[] args) throws IOException {

        // the default port to use if no port is specified
        int port = 5016;

        System.out.println("<SYSTEM>: hProxy v1.2 initiated...");

        // check argument and start at given port or default port, or give error if arguments are invalid.
        if (checkArguments(args)) {
            try {
                System.out.println("<SYSTEM>: Establishing connection on hProxy on port " + args[1]);

                // parsing Int from a string might throw exception if user puts weird input?
                port = Integer.parseInt(args[1]);
            }
            catch (Exception e) { // gotta catch 'em all
                System.err.println("< ERR! >: please provide correct integer for port number, i.e. -port 1234");
                e.printStackTrace();
            }
        } else {
            System.out.println("<SYSTEM>: port number not given; defaulting to 5016...");
        }

        // finally, start running at given port
        startRunningAtPort(port);
    }


    /**
     * checks the arguments
     * @param argumentArray the given arguments to the program
     * @return whether the arguments are valid or not
     */
    private static boolean checkArguments(String[] argumentArray) {
        for (String argument : argumentArray) {
            if (argument.equals("-s"))
                silentMode = true;
        }
        return argumentArray.length > 0 && argumentArray[0].equals("-p");
    }

    /**
     * Starts the proxy on given port.
     * @param portNumber the given port number to use
     * @throws IOException when sum ting wong
     */
    private static void startRunningAtPort(int portNumber) {
        // give cute welcome message to user
        System.out.println("<SYSTEM>: Welcome to hProxy!");
        System.out.println("<SYSTEM>: Socket trying on port " + portNumber + "...");

        boolean keepRunning = true;

        // make a new server socket
        try {
            ServerSocket hSocket = new ServerSocket(portNumber);
            System.out.println("<SYSTEM>: Socket is now running on port " + portNumber + ".");
            System.out.println("<SYSTEM>: hProxy ready to serve.");

            // here is the sweet spot
            while (keepRunning) {


                if (silentMode) {
                    new hProxyThreadSilent(hSocket.accept()).start();
                } else {
                    new hProxyThread(hSocket.accept()).start();
                }

            }

            // when done, safely close the socket.
            hSocket.close();

        } catch (IOException e) { // gotta catch 'em all
            System.err.println("< ERR! >: Could not listen on port " + portNumber);
        }

        // whenever keepRunning is triggered, safely close the socket and exit.
        System.out.println("<SYSTEM>: hProxy terminated. Good bye...");
        System.exit(-1);
    }

    /**
     * addDNSRecord adds the DNS resolution to our record.
     * @param hostname the hostname
     * @param ipaddress the IP address of that host
     */
    public static InetAddress addDNSRecord(String hostname, InetAddress ipAddress) {
        record.put(hostname, new Tuple(ipAddress, System.currentTimeMillis()));
        return ipAddress;
    }

    /**
     * getDNSRecord checks whether the DNS resolution for given hostname exists, returns if so, else adds a new record.
     * @param hostname the hostname
     * @return the ipAddress (InetAddress) of the host
     * @throws Exception when sum ting wong
     */
    public static InetAddress getDNSRecord(String hostname) throws Exception {
        if (record.get(hostname) != null && (System.currentTimeMillis() - record.get(hostname).TTL < TTL)) {
            System.out.println("<SYSTEM>: hDNS resolution for " + hostname + " found.");
            return record.get(hostname).ipAddress;
        } else {
            addDNSRecord(hostname, InetAddress.getByName(hostname));
            System.out.println("<SYSTEM>: hDNS resolution for " + hostname + " added.");
            return InetAddress.getByName(hostname);
        }
    }
}