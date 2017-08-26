package mc.sel.parsetree.impl;

import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Binary node which represent assign operation.
 *
 * @author Milan Crnjak
 */
public class AssignNode extends BinaryNode {


    public AssignNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitAssignNode(this);
    }
}
