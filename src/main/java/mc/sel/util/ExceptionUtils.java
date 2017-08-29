package mc.sel.util;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.exception.ParserException;
import mc.sel.exception.TokenizerException;
import mc.sel.parsetree.BinaryNode;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.impl.*;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;

/**
 * Holds utility methods for handling custom SEL exceptions.
 *
 * @author Milan Crnjak
 */
public class ExceptionUtils {

    /**
     * Underlines the problematic symbol in the input.
     *
     * @param input expression input
     * @param e tokenizer exception
     * @return expression input with problematic symbol underlined
     */
    public static String underlineError(String input, TokenizerException e) {
        return underline(input, e.getPosition(), e.getPosition());
    }

    /**
     * Underlines the problematic token.
     *
     * @param input expression input
     * @param e parser exception
     * @return expression input with problematic token underlined
     */
    public static String underlineError(String input, ParserException e) {
        return underline(input, e.getToken());
    }

    /**
     * Underlines the problematic node.
     *
     * @param input expression input
     * @param e parse tree visitor exception
     * @return expression input with problematic node underlined
     */
    public static String underlineError(String input, ParseTreeVisitorException e) {
        return underline(input, e.getNode());
    }

    private static String underline(String input, int start, int end) {
        StringBuilder sb = new StringBuilder(input);
        sb.append("\n");

        for (int i = 0; i < start; i++) {
            sb.append(" ");
        }

        for (int i = start; i <= end; i++) {
            sb.append("^");
        }

        return sb.toString();
    }

    private static String underline(String input, ParseTreeNode node) {
        Position pos = new NodePositionVisitor().visit(node);
        return underline(input, pos.start, pos.end);
    }

    private static String underline(String input, Token token) {
        return underline(input, token.getStartPos(), token.getEndPos());
    }

    /**
     * Determines the start and end position for the parse tree node
     */
    private static class NodePositionVisitor implements ParseTreeVisitor<Position> {

        @Override
        public Position visit(ParseTreeNode node) throws ParseTreeVisitorException {
            return node.accept(this);
        }

        @Override
        public Position visitAddSubNode(AddSubNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        @Override
        public Position visitBoolNode(BoolNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        @Override
        public Position visitCompareNode(CompareNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        @Override
        public Position visitEqualNode(EqualNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        @Override
        public Position visitMulDivNode(MulDivNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        @Override
        public Position visitParenthesesNode(ParenthesesNode node) throws ParseTreeVisitorException {
            return new Position(node.getOpenParenToken().getStartPos(), node.getCloseParenToken().getEndPos());
        }

        @Override
        public Position visitUnaryNode(UnaryNode node) throws ParseTreeVisitorException {
            int start = node.getOperator().getStartPos();
            int end = visit(node.getNode()).end;
            return new Position(start, end);
        }

        @Override
        public Position visitLiteralNode(LiteralNode node) throws ParseTreeVisitorException {
            return new Position(node.getToken().getStartPos(), node.getToken().getEndPos());
        }

        @Override
        public Position visitFunctionNode(FunctionNode node) throws ParseTreeVisitorException {
            int start = node.getFuncIdToken().getStartPos();
            int end = node.getCloseParenToken().getEndPos();
            return new Position(start, end);
        }

        @Override
        public Position visitIdentifierNode(IdentifierNode node) throws ParseTreeVisitorException {
            return new Position(node.getToken().getStartPos(), node.getToken().getEndPos());
        }

        @Override
        public Position visitIndexedNode(IndexedNode node) throws ParseTreeVisitorException {
            int start = visit(node.getNode()).start;
            int end = node.getCloseBracketToken().getEndPos();
            return new Position(start, end);
        }

        @Override
        public Position visitAssignNode(AssignNode node) throws ParseTreeVisitorException {
            return visitBinaryNode(node);
        }

        private Position visitBinaryNode(BinaryNode node) {
            Position leftPos = visit(node.getLeftNode());
            Position rightPos = visit(node.getRightNode());
            return new Position(leftPos.start, rightPos.end);
        }
    }

    private static class Position {
        int start;
        int end;

        Position(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
