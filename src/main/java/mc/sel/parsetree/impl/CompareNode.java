package mc.sel.parsetree.impl;

import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

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
