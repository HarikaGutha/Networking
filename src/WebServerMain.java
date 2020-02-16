/**
 * The main class.
 * author 190026870
 */
public class WebServerMain {
    /**
     * main method.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java WebServerMain <document_root> <port>");
            System.exit(1);
        }
        Server s = new Server(args[0], Integer.parseInt(args[1]));
    }
}
