package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.BinaryNode;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

/**
 * A binary node which represents a boolean (and, or) operation.
 *
 * @author Milan Crnjak
 */
public class BoolNode extends BinaryNode {

    public BoolNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }
}
