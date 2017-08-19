package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.BinaryNode;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

/**
 * A binary node which represents an equality operation (==, !=).
 *
 * @author Milan Crnjak
 */
public class EqualNode extends BinaryNode {

    public EqualNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token token) {
        super(leftNode, rightNode, token);
    }
}
