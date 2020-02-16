import java.net.Socket;

/**
 * Class that implements the concept of multi threading.
 */
public class Threading extends Thread {
    private Socket conn = null;
    private String source;
    private Log l;

    /**
     * constructor.
     *
     * @param conn   the socket object.
     * @param source the source file
     * @param l      the Log object
     */
    public Threading(Socket conn, String source, Log l) {
        this.conn = conn;
        this.source = source;
        this.l = l;
    }

    /**
     * overriding run method of Thread class.
     */
    @Override
    public void run() {
        //System.out.println("thread started");
        ConnectionHandler ch = new ConnectionHandler(conn, source, l);
    }
}
