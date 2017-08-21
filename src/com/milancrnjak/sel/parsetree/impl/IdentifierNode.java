package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;
import com.milancrnjak.sel.token.Token;

/**
 * Represents identifier node.
 *
 * @author Milan Crnjak
 */
public class IdentifierNode implements ParseTreeNode {

    private ParseTreeNode invokerNode;
    private Token token;

    public IdentifierNode(ParseTreeNode invokerNode, Token token) {
        this.invokerNode = invokerNode;
        this.token = token;
    }

    public ParseTreeNode getInvokerNode() {
        return invokerNode;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitIdentifierNode(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (invokerNode != null) {
            sb.append(invokerNode.toString()).append(".");
        }

        sb.append(token.getSequence());

        return sb.toString();
    }
}
