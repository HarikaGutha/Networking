import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * The connection handler class.
 * author 190026870
 */
public class ConnectionHandler {
    private Socket conn;
    private BufferedReader br = null;
    private BufferedOutputStream out = null;
    private PrintWriter pw = null;
    private String inputstring;
    private StringTokenizer parse;
    private String fileRequested = null;
    private File root;
    private byte[] fileData;
    private String content;
    private int fileLength;
    private String method;
    private Log l;

    /**
     * constructor.
     *
     * @param conn   the socket object
     * @param source the sourcef file
     * @param l      the Log object
     */
    public ConnectionHandler(Socket conn, String source, Log l) {
        try {
            this.conn = conn;
            this.l = l;
            root = new File(source);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            pw = new PrintWriter(conn.getOutputStream());
            out = new BufferedOutputStream(conn.getOutputStream());
            inputstring = br.readLine();
            parse = new StringTokenizer(inputstring);
            method = parse.nextToken().toUpperCase();
            fileRequested = parse.nextToken().toLowerCase();
            if (!root.exists()) {
                pw.println("HTTP/1.1 404 Not Found");
                pw.flush();
            } else {
                if (!method.equals("GET") && !method.equals("HEAD")) {
                    pw.println("HTTP/1.1 501 Not Implemented");
                    pw.flush();
                    l.logger.info("METHOD:" + method + " URL " + fileRequested + " 501 " + "0");
                } else {
                    File file = new File(root, fileRequested);
                    if (!file.exists()) {
                        pw.println("HTTP/1.1 404 Not Found");
                        pw.flush();
                        l.logger.info("METHOD:" + method + " URL " + fileRequested + " 404 " + "0");

                    }
                    fileLength = (int) file.length();
                    content = getMimeType(fileRequested);
                    fileData = readFileData(file, fileLength);

                    printClientData(method, content, fileLength, fileData);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Method that gets the MIME type.
     *
     * @param fileRequested the requested file (query string)
     * @return string type
     */
    private String getMimeType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
            return "text/html";
        } else if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileRequested.endsWith(".png")) {
            return "image/png";
        } else if (fileRequested.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "text/plain";
        }
    }

    /* The method that reads fil data.
     *
     * @param file       the file object
     * @param fileLength length of the file
     * @return byte[] to store file data
     * @throws IOException
     */
    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
        return fileData;
    }

    /**
     * prints the client data.
     *
     * @throws DisconnectedException the exception
     * @throws IOException           the exception
     */
    private void printClientData(String method, String content, int fileLength, byte[] fileData) throws DisconnectedException, IOException {
        this.method = method;
        this.content = content;
        this.fileLength = fileLength;
        this.fileData = fileData;
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-Type: " + content);
        pw.println("Content-Length: " + fileLength);
        pw.println("");
        l.logger.info("METHOD:" + method + " URL " + fileRequested + " 200 " + fileLength);
        pw.flush();
        if (method.equals("GET")) {
            //System.out.println(fileData.length);
            out.write(fileData);
            out.flush();
        }
    }

    /**
     * Methods for closing logics.
     */
    private void cleanup() {
        try {
            pw.close();
            out.close();
            br.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
