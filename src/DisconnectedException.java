/**
 * the exception class.
 * author 190026870
 */
public class DisconnectedException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * constructor.
     *
     * @param message the exception message.
     */
    public DisconnectedException(String message) {
        super(message);
    }
}
