import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * The server class.
 * author 190026870
 */
public class Server {
    private Socket conn;
    private ServerSocket ss;

    /**
     * constructor.
     *
     * @param port   the port number
     * @param source the source file
     */
    public Server(String source, int port) {
        try {
            ss = new ServerSocket(port);
            System.out.println("Server started ... listening on port " + port + " ...");
            Log l = new Log("log.txt");
            while (true) {
                conn = ss.accept();
                System.out.println("Server got new connection request from " + conn.getInetAddress());
                Threading t = new Threading(conn, source, l);
                t.start();
            }
        } catch (IOException ioe) {
            System.out.println("Oops " + ioe.getMessage());
        }
    }
}
