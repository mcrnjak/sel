package com.milancrnjak.sel.parsetree.visitor;

import com.milancrnjak.sel.parsetree.BinaryNode;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.impl.*;

/**
 * Parse tree visitor that builds a LISP representation of the parse tree.
 * Useful for visualizing the parse tree.
 *
 * @author Milan Crnjak
 */
public class ParseTreeLispStringifier implements ParseTreeVisitor<String> {

    @Override
    public String visit(ParseTreeNode node) {
        return node.accept(this);
    }

    @Override
    public String visitAddSubNode(AddSubNode node) {
        return visitBinaryNode(node);
    }

    @Override
    public String visitBoolNode(BoolNode node) {
        return visitBinaryNode(node);
    }

    @Override
    public String visitCompareNode(CompareNode node) {
        return visitBinaryNode(node);
    }

    @Override
    public String visitEqualNode(EqualNode node) {
        return visitBinaryNode(node);
    }

    @Override
    public String visitMulDivNode(MulDivNode node) {
        return visitBinaryNode(node);
    }

    @Override
    public String visitParenthesesNode(ParenthesesNode node) {
        return visit(node.getNode());
    }

    @Override
    public String visitUnaryNode(UnaryNode node) {
        return "(" + node.getOperator().getTokenType() + " " + visit(node.getNode()) + ")";
    }

    @Override
    public String visitLiteralNode(LiteralNode node) {
        return node.getToken().getSequence();
    }

    private String visitBinaryNode(BinaryNode node) {
        return "(" + node.getOperator().getTokenType() + " " + visit(node.getLeftNode()) + " " + visit(node.getRightNode()) + ")";
    }
}
