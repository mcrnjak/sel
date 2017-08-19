package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;

/**
 * Represents an expression inside of parentheses.
 *
 * @author Milan Crnjak
 */
public class ParenthesesNode implements ParseTreeNode {

    private ParseTreeNode node;

    public ParenthesesNode(ParseTreeNode node) {
        this.node = node;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "(" + node.toString() + ")";
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitParenthesesNode(this);
    }
}
