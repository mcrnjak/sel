package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

import java.util.List;

/**
 * Represents a function node.
 *
 * @author Milan Crnjak
 */
public class FunctionNode implements ParseTreeNode {

    private ParseTreeNode invokerNode;
    private Token token;
    private List<ParseTreeNode> funcArgs;

    public FunctionNode(ParseTreeNode invokerNode, Token token, List<ParseTreeNode> funcArgs) {
        this.invokerNode = invokerNode;
        this.token = token;
        this.funcArgs = funcArgs;
    }

    public Token getToken() {
        return token;
    }

    public List<ParseTreeNode> getFuncArgs() {
        return funcArgs;
    }

    public ParseTreeNode getInvokerNode() {
        return invokerNode;
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

        sb.append(getToken().getSequence()).append("(");

        for (ParseTreeNode argNode : getFuncArgs()) {
            sb.append(argNode.toString()).append(",");
        }

        if (!getFuncArgs().isEmpty()) {
            // remove the trailing space after the last argument
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")");

        return sb.toString();
    }
}
