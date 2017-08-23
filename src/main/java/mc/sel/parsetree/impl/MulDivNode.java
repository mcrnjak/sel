package mc.sel.parsetree.impl;

import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * A binary node which represents a multiplication or division operation.
 *
 * @author Milan Crnjak
 */
public class MulDivNode extends BinaryNode {

    public MulDivNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitMulDivNode(this);
    }
}
