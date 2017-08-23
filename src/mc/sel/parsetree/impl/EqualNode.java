package mc.sel.parsetree.impl;

import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * A binary node which represents an equality operation (==, !=).
 *
 * @author Milan Crnjak
 */
public class EqualNode extends BinaryNode {

    public EqualNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token token) {
        super(leftNode, rightNode, token);
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitEqualNode(this);
    }
}
