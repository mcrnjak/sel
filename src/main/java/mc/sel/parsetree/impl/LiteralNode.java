package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents a literal value (true, false, null, string, number etc.)
 *
 * @author Milan Crnjak
 */
public class LiteralNode implements ParseTreeNode {

    private Token token;

    public LiteralNode(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.getSequence();
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitLiteralNode(this);
    }
}
