package mc.sel.exception;

/**
 * Thrown when an unknown symbol is found during input scanning and tokenizing.
 *
 * @author Milan Crnjak
 */
public class TokenizerException extends RuntimeException {

    private int position;

    public TokenizerException(String message, int position) {
        super(message);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
