package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.BinaryNode;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

/**
 * A binary node which represents an addition (or string concatenation) or subtraction operation.
 *
 * @author Milan Crnjak
 */
public class AddSubNode extends BinaryNode {

    public AddSubNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }
}
