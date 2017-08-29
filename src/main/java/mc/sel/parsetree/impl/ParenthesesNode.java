package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents an expression inside of parentheses.
 *
 * @author Milan Crnjak
 */
public class ParenthesesNode implements ParseTreeNode {

    private Token openParenToken;
    private ParseTreeNode node;
    private Token closeParenToken;

    public ParenthesesNode(Token openParenToken, ParseTreeNode node, Token closeParenToken) {
        this.node = node;
        this.openParenToken = openParenToken;
        this.closeParenToken = closeParenToken;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    public Token getOpenParenToken() {
        return openParenToken;
    }

    public Token getCloseParenToken() {
        return closeParenToken;
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
