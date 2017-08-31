package mc.sel.parsetree.impl;

import mc.sel.parsetree.IndexableNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Represents identifier node.
 *
 * @author Milan Crnjak
 */
public class IdentifierNode implements IndexableNode {

    private ParseTreeNode invokerNode;
    private Token token;
    private IndexNode indexNode;

    public IdentifierNode(ParseTreeNode invokerNode, Token token) {
        this.invokerNode = invokerNode;
        this.token = token;
    }

    public ParseTreeNode getInvokerNode() {
        return invokerNode;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitIdentifierNode(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (invokerNode != null) {
            sb.append(invokerNode.toString()).append(".");
        }

        sb.append(token.getSequence());

        if (indexNode != null) {
            sb.append(indexNode.toString());
        }

        return sb.toString();
    }

    @Override
    public IndexNode getIndexNode() {
        return indexNode;
    }

    @Override
    public void setIndexNode(IndexNode index) {
        this.indexNode = index;
    }
}
