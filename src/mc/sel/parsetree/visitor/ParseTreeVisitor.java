package mc.sel.parsetree.visitor;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.impl.*;

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
    T visit(ParseTreeNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link AddSubNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitAddSubNode(AddSubNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link BoolNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitBoolNode(BoolNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link CompareNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitCompareNode(CompareNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link EqualNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitEqualNode(EqualNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link MulDivNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitMulDivNode(MulDivNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link ParenthesesNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitParenthesesNode(ParenthesesNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link UnaryNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitUnaryNode(UnaryNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link LiteralNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitLiteralNode(LiteralNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link FunctionNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     */
    T visitFunctionNode(FunctionNode node) throws ParseTreeVisitorException;

    /**
     * Visits the {@link IdentifierNode} and does the work specific to that node.
     *
     * @param node node to perform the work on
     * @return
     * @throws ParseTreeVisitorException
     */
    T visitIdentifierNode(IdentifierNode node) throws ParseTreeVisitorException;
}
