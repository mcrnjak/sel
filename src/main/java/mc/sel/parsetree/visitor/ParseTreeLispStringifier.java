package mc.sel.parsetree.visitor;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.impl.*;

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

    @Override
    public String visitFunctionNode(FunctionNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(node.getFuncIdToken().getSequence());

        if (node.getInvokerNode() != null) {
            sb.append(" ").append(visit(node.getInvokerNode()));
        }

        for (ParseTreeNode argNode : node.getFuncArgs()) {
            sb.append(" ").append(visit(argNode));
        }

        sb.append(")");

        if (node.getIndexNode() != null) {
            String result = sb.toString();
            return "(" + visit(node.getIndexNode()) + " " + result + ")";
        }

        return sb.toString();
    }

    @Override
    public String visitIdentifierNode(IdentifierNode node) throws ParseTreeVisitorException {
        String result;

        if (node.getInvokerNode() == null) {
            result = node.getToken().getSequence();
        } else {
            result = "(get " + visit(node.getInvokerNode()) + " " + node.getToken().getSequence() + ")";
        }

        if (node.getIndexNode() != null) {
            result = "(" + visit(node.getIndexNode()) + " " + result + ")";
        }

        return result;
    }

    @Override
    public String visitIndexNode(IndexNode node) throws ParseTreeVisitorException {
        return "idx " + visit(node.getIndex());
    }

    @Override
    public String visitAssignNode(AssignNode node) throws ParseTreeVisitorException {
        return visitBinaryNode(node);
    }

    private String visitBinaryNode(BinaryNode node) {
        return "(" + node.getOperator().getTokenType() + " " + visit(node.getLeftNode()) + " " + visit(node.getRightNode()) + ")";
    }
}
