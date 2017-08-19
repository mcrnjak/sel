package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;

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
}
