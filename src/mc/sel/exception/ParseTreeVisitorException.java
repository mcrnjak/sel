package mc.sel.exception;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;

/**
 * Thrown by the {@link ParseTreeVisitor} when it encounters an error
 * while visiting the parse tree.
 *
 * @author Milan Crnjak
 */
public class ParseTreeVisitorException extends RuntimeException {

    private ParseTreeNode node;

    public ParseTreeVisitorException(String message, ParseTreeNode node) {
        super(message);
        this.node = node;
    }

    public ParseTreeVisitorException(String message, Throwable cause, ParseTreeNode node) {
        super(message, cause);
        this.node = node;
    }

    public ParseTreeNode getNode() {
        return node;
    }
}
