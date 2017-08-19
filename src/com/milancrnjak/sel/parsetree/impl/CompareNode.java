package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.BinaryNode;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;
import com.milancrnjak.sel.token.Token;

/**
 * A binary node which represents a comparison operation (>, >=, <, <=) between the operand nodes.
 *
 * @author Milan Crnjak
 */
public class CompareNode extends BinaryNode {

    public CompareNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitCompareNode(this);
    }
}
