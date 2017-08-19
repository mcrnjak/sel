package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

/**
 * Represents a literal value (true, false, null, string, number etc.)
 *
 * @author Milan Crnjak
 */
public class LiteralNode implements ParseTreeNode {

    private Token token;

    public LiteralNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.getSequence();
    }
}
