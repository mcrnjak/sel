package mc.sel.parsetree.impl;

import mc.sel.parsetree.IndexableNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

import java.util.List;

/**
 * Represents a function node.
 *
 * @author Milan Crnjak
 */
public class FunctionNode implements IndexableNode {

    private ParseTreeNode invokerNode;
    private Token funcIdToken;
    private List<ParseTreeNode> funcArgs;
    private Token openParenToken;
    private Token closeParenToken;
    private IndexNode indexNode;

    public FunctionNode(ParseTreeNode invokerNode, Token funcIdToken, Token openParenToken, List<ParseTreeNode> funcArgs,
                        Token closeParenToken) {
        this.invokerNode = invokerNode;
        this.funcIdToken = funcIdToken;
        this.openParenToken = openParenToken;
        this.funcArgs = funcArgs;
        this.closeParenToken = closeParenToken;
    }

    public Token getFuncIdToken() {
        return funcIdToken;
    }

    public List<ParseTreeNode> getFuncArgs() {
        return funcArgs;
    }

    public ParseTreeNode getInvokerNode() {
        return invokerNode;
    }

    public Token getOpenParenToken() {
        return openParenToken;
    }

    public Token getCloseParenToken() {
        return closeParenToken;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitFunctionNode(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (invokerNode != null) {
            sb.append(invokerNode.toString()).append(".");
        }

        sb.append(getFuncIdToken().getSequence()).append("(");

        for (ParseTreeNode argNode : getFuncArgs()) {
            sb.append(argNode.toString()).append(",");
        }

        if (!getFuncArgs().isEmpty()) {
            // remove the trailing space after the last argument
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

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
