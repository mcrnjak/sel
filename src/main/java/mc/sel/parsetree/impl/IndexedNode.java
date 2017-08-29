package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents indexed node, e.g. this.attr_name[0].
 *
 * @author Milan Crnjak
 */
public class IndexedNode implements ParseTreeNode {

    private ParseTreeNode node;
    private ParseTreeNode index;
    private Token openBracketToken;
    private Token closeBracketToken;

    public IndexedNode(ParseTreeNode node, Token openBracketToken, ParseTreeNode index, Token closeBracketToken) {
        this.node = node;
        this.openBracketToken = openBracketToken;
        this.index = index;
        this.closeBracketToken = closeBracketToken;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    public ParseTreeNode getIndex() {
        return index;
    }

    public Token getOpenBracketToken() {
        return openBracketToken;
    }

    public Token getCloseBracketToken() {
        return closeBracketToken;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitIndexedNode(this);
    }
}
