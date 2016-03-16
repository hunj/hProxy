import java.net.*;
import java.io.*;
import java.util.*;

public class hProxyThread extends Thread {
    private Socket socket = null;
    private int port;
    String requestURL;
    private static final int BUFFER_SIZE = 4096;

    /**
     * initializer for an instance of hProxyThread.
     * @param socket the socket to use for this thread.
     */
    hProxyThread(Socket socket) {
        super("hProxyThread");
        this.socket = socket;
    }

    /**
     * Everytime there is a request, an instance of hProxyThread will be created and run.
     */
    public void run() {
        try {
            // input and output streams to read in the data
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // some setup
            String inputLine;
            int count = 0;
            String requestedURL = "";
            ArrayList<String> httpRequest = new ArrayList<String>();

            // get request from client
            while ((inputLine = input.readLine()) != null) {
                try {
                    StringTokenizer token = new StringTokenizer(inputLine);
                    token.nextToken();
                } catch (Exception e) {
                    // give cute notification to user
                    System.out.println("<SYSTEM>: No more lines to read");
                    break;
                }

                if (count == 0) {
                    // token is probably in the format of:
                    // GET http://www.case.edu/eecs/eecs325.html ....
                    // so we need to take out the request URL
                    String[] tokens = inputLine.split(" ");
                    requestedURL = tokens[1];
                    System.out.println("<SYSTEM>: Request for: " + requestedURL);
                } else {
                    httpRequest.add(inputLine);
                }
                System.out.println(inputLine);
                count++;
            }


            // default/example
            String hostname = "eecslinab1.engineering.cwru.edu";
            String directory = ":hxl224/project1/";

            try {
                // strip "http://"
                String domain = requestedURL.substring(7, requestedURL.length());
                count = domain.indexOf("/");

                // hostname = www.case.edu
                hostname = domain.substring(0, count);

                // the requested file:
                directory = domain.substring(count, domain.length());

                System.out.println("<SYSTEM>: Socket for " + hostname);
                System.out.println(directory);
            } catch (Exception e) {
                System.out.println("< ERR! >: URL \"" + requestedURL + "\" cannot be processed.");
            }

            System.out.println("<SYSTEM>: Creating socket for " + hostname);

            // Here starts the sweet part
            // start connection with URL on port 80
            // *HTTPS (port 443) not supported
            Socket connectionSocket = new Socket(hostname, 80);
            System.out.println("<SYSTEM>: Socket successfully created.");

            // server sends GET request to host, using given directory
            PrintWriter server = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream())));
            System.out.println("<SYSTEM>: Sending GET request to " + requestedURL);
            server.println("GET " + directory + " HTTP/1.1");
            for (int i = 0; i < httpRequest.size(); i++) {
                server.println(httpRequest.get(i));
            }

            // tell me what we have
            server.println();

            // flush out
            server.flush();

            // read from the URL into this stream
            InputStream is = connectionSocket.getInputStream();

            byte[] data = new byte[BUFFER_SIZE];
            int check = is.read(data, 0, BUFFER_SIZE);
            while (check != -1) {
                output.write(data, 0, check);
                check = is.read(data, 0, BUFFER_SIZE);
            }

            // safely close out everything
            connectionSocket.close();
            input.close();
            output.close();
            is.close();


        }
        catch (Exception e) {
            // gotta catch em all
            System.err.println("< ERR! >: Error occurred:" + e.getMessage());
        }
    }

}
