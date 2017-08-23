package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;

/**
 * Represents an expression inside of parentheses.
 *
 * @author Milan Crnjak
 */
public class ParenthesesNode implements ParseTreeNode {

    private ParseTreeNode node;

    public ParenthesesNode(ParseTreeNode node) {
        this.node = node;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "(" + node.toString() + ")";
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitParenthesesNode(this);
    }
}
