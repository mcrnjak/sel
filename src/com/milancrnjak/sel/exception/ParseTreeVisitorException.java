package com.milancrnjak.sel.exception;

import com.milancrnjak.sel.parsetree.ParseTreeNode;

/**
 * Thrown by the {@link com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor} when it encounters an error
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
