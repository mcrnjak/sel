package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents indexed node, e.g. this.attr_name[0].
 *
 * @author Milan Crnjak
 */
public class IndexNode implements ParseTreeNode {

    private Token openBracketToken;
    private ParseTreeNode index;
    private Token closeBracketToken;

    public IndexNode(Token openBracketToken, ParseTreeNode index, Token closeBracketToken) {
        this.openBracketToken = openBracketToken;
        this.index = index;
        this.closeBracketToken = closeBracketToken;
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
        return visitor.visitIndexNode(this);
    }

    @Override
    public String toString() {
        return "[" + index.toString() + "]";
    }
}
