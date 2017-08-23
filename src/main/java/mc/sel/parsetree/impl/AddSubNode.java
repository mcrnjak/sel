package mc.sel.parsetree.impl;

import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * A binary node which represents an addition (or string concatenation) or subtraction operation.
 *
 * @author Milan Crnjak
 */
public class AddSubNode extends BinaryNode {

    public AddSubNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        super(leftNode, rightNode, operator);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitAddSubNode(this);
    }
}
