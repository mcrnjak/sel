package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;
import com.milancrnjak.sel.token.Token;

/**
 * Represents an unary node, i.e. a value with an unary operator.
 *
 * @author Milan Crnjak
 */
public class UnaryNode implements ParseTreeNode {

    private ParseTreeNode node;
    private Token operator;

    public UnaryNode(ParseTreeNode node, Token operator) {
        this.node = node;
        this.operator = operator;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return getOperator().getTokenType().toString() + " " + node.toString();
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitUnaryNode(this);
    }
}
