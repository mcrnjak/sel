package mc.sel.exception;

import mc.sel.token.Token;

/**
 * Thrown when the input tokens are not grammatically correct.
 *
 * @author Milan Crnjak
 */
public class ParserException extends RuntimeException {

    private Token token;

    public ParserException(String message, Token token) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
