package com.milancrnjak.sel.parsetree.impl;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;
import com.milancrnjak.sel.token.Token;

import java.util.List;

/**
 * Represents a function node.
 *
 * @author Milan Crnjak
 */
public class FunctionNode implements ParseTreeNode {

    private Token token;
    private List<ParseTreeNode> funcArgs;

    public FunctionNode(Token token, List<ParseTreeNode> funcArgs) {
        this.token = token;
        this.funcArgs = funcArgs;
    }

    public Token getToken() {
        return token;
    }

    public List<ParseTreeNode> getFuncArgs() {
        return funcArgs;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitFunctionNode(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getToken().getSequence()).append("(");

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
