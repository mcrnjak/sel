package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents an unary node, i.e. a value with an unary operator.
 *
 * @author Milan Crnjak
 */
public class UnaryNode implements ParseTreeNode {

    private ParseTreeNode node;
    private Token operator;

    public UnaryNode(ParseTreeNode node, Token operator) {
        this.node = node;
        this.operator = operator;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return getOperator().getTokenType().toString() + " " + node.toString();
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitUnaryNode(this);
    }
}
