package com.milancrnjak.sel.parsetree.visitor;

import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.impl.*;

/**
 * Parse tree visitor.
 *
 * @param <T> Result type
 *
 * @author Milan Crnjak
 */
public interface ParseTreeVisitor<T> {

    /**
     * Visitor's entry point. Calls the {@link ParseTreeNode#accept(ParseTreeVisitor)} method and passes itself.
     * Depending on the node class, the call will be delegated to one of the <i>visitXyzNode</i> methods which
     * perform the actual logic for corresponding parse tree nodes.
     *
     * @param node node to perform the work on
     * @return
     */
    T visit(ParseTreeNode node);

    /**
     * Visits the {@link AddSubNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitAddSubNode(AddSubNode node);

    /**
     * Visits the {@link BoolNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitBoolNode(BoolNode node);

    /**
     * Visits the {@link CompareNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitCompareNode(CompareNode node);

    /**
     * Visits the {@link EqualNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitEqualNode(EqualNode node);

    /**
     * Visits the {@link MulDivNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitMulDivNode(MulDivNode node);

    /**
     * Visits the {@link ParenthesesNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitParenthesesNode(ParenthesesNode node);

    /**
     * Visits the {@link UnaryNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitUnaryNode(UnaryNode node);

    /**
     * Visits the {@link LiteralNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitLiteralNode(LiteralNode node);
}
