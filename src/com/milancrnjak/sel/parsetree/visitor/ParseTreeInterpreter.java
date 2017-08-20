package com.milancrnjak.sel.parsetree.visitor;

import com.milancrnjak.sel.exception.ParseTreeVisitorException;
import com.milancrnjak.sel.function.Function;
import com.milancrnjak.sel.function.FunctionsRegistry;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.impl.*;
import com.milancrnjak.sel.token.TokenType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Parse tree interpreter. Visits each node in the parse tree and interprets it according to the
 * node value (if literal node - true, false, null) or according to the node operator (addition, subtraction etc.).
 *
 * @author Milan Crnjak
 */
public class ParseTreeInterpreter implements ParseTreeVisitor<Object> {

    @Override
    public Object visit(ParseTreeNode node) {
        return node.accept(this);
    }

    @Override
    public Object visitAddSubNode(AddSubNode node) {
        TokenType tokenType = node.getOperator().getTokenType();

        Object leftVal = visit(node.getLeftNode());
        Object rightVal = visit(node.getRightNode());

        if (tokenType == TokenType.MINUS) {
            Double left = castOrThrow(leftVal, Double.class,
                    new ParseTreeVisitorException("Left operand must evaluate to Double", node.getLeftNode()));

            Double right = castOrThrow(rightVal, Double.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Double", node.getRightNode()));

            return left - right;
        } else if (tokenType == TokenType.PLUS) {
            if (leftVal instanceof String) {
                // string concatenation
                String left = (String) leftVal;
                return left + rightVal;
            }

            // else must be a number
            Double left = castOrThrow(leftVal, Double.class,
                    new ParseTreeVisitorException("Left operand must evaluate to Double", node.getLeftNode()));

            Double right = castOrThrow(rightVal, Double.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Double", node.getRightNode()));

            return left + right;
        }

        throw new ParseTreeVisitorException("Invalid operator in AddSubNode", node);
    }

    @Override
    public Object visitBoolNode(BoolNode node) {
        TokenType operatorType = node.getOperator().getTokenType();

        Object leftVal = visit(node.getLeftNode());
        Boolean left = castOrThrow(leftVal, Boolean.class,
                new ParseTreeVisitorException("Left operand must evaluate to Boolean", node.getLeftNode()));

        if (operatorType == TokenType.OR && left == true) {
            return true;
        }

        if (operatorType == TokenType.AND && left == false) {
            return false;
        }

        Object rightVal = visit(node.getRightNode());
        Boolean right = castOrThrow(rightVal, Boolean.class,
                new ParseTreeVisitorException("Right operand must evaluate to Boolean", node.getRightNode()));

        return right;
    }

    @Override
    public Object visitCompareNode(CompareNode node) {
        TokenType tokenType = node.getOperator().getTokenType();
        Object leftVal = visit(node.getLeftNode());
        Object rightVal = visit(node.getRightNode());

        if (leftVal instanceof Double) {
            Double left = (Double) leftVal;
            Double right = castOrThrow(rightVal, Double.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Double", node.getRightNode()));

            return compareDoubles(left, right, tokenType);
        } else if (leftVal instanceof String) {
            String left = (String) leftVal;
            String right = castOrThrow(rightVal, String.class,
                    new ParseTreeVisitorException("Right operand must evaluate to String", node.getRightNode()));

            return compareStrings(left, right, tokenType);
        } else if (leftVal instanceof Date) {
            Date left = (Date) leftVal;
            Date right = castOrThrow(rightVal, Date.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Date", node.getRightNode()));

            return compareDates(left, right, tokenType);
        } else {
            String msg = String.format("Expected types for left operand are String, Double, Date. Actual type is %s",
                    leftVal.getClass().getName());
            throw new ParseTreeVisitorException(msg, node.getLeftNode());
        }
    }

    protected Object compareDoubles(Double left, Double right, TokenType tokenType) {
        if (tokenType == TokenType.GTE) {
            return left >= right;
        } else if (tokenType == TokenType.GT) {
            return left > right;
        } else if (tokenType == TokenType.LTE) {
            return left <= right;
        } else if (tokenType == TokenType.LT) {
            return left < right;
        }

        throw new RuntimeException("Invalid operator " + tokenType);
    }

    protected Object compareStrings(String left, String right, TokenType tokenType) {
        int compared = left.compareTo(right);

        if (tokenType == TokenType.GTE) {
            return compared >= 0;
        } else if (tokenType == TokenType.GT) {
            return  compared > 0;
        } else if (tokenType == TokenType.LTE) {
            return compared <= 0;
        } else if (tokenType == TokenType.LT) {
            return compared < 0;
        }

        throw new RuntimeException("Invalid operator " + tokenType);
    }

    protected Object compareDates(Date left, Date right, TokenType tokenType) {
        if (tokenType == TokenType.GTE) {
            return left.after(right) || left.equals(right);
        } else if (tokenType == TokenType.GT) {
            return  left.after(right);
        } else if (tokenType == TokenType.LTE) {
            return left.before(right) || left.equals(right);
        } else if (tokenType == TokenType.LT) {
            return left.before(right);
        }

        throw new RuntimeException("Invalid operator " + tokenType);
    }

    @Override
    public Object visitEqualNode(EqualNode node) {
        Object leftVal = visit(node.getLeftNode());
        Object rightVal = visit(node.getRightNode());

        if (leftVal == null) {
            return null == rightVal;
        }

        return leftVal.equals(rightVal);
    }

    @Override
    public Object visitMulDivNode(MulDivNode node) {
        Object leftVal = visit(node.getLeftNode());
        Double leftDouble =  castOrThrow(leftVal, Double.class,
                new ParseTreeVisitorException("Left node does not evaluate to Double", node.getLeftNode()));

        Object rightVal = visit(node.getRightNode());
        Double rightDouble = castOrThrow(rightVal, Double.class,
                new ParseTreeVisitorException("Right node does not evaluate to Double", node.getRightNode()));

        if (node.getOperator().getTokenType() == TokenType.MUL) {
            return leftDouble * rightDouble;
        } else if (node.getOperator().getTokenType() == TokenType.DIV) {
            return leftDouble / rightDouble;
        }

        throw new ParseTreeVisitorException("Invalid operator in MulDivNode", node);
    }

    @Override
    public Object visitParenthesesNode(ParenthesesNode node) {
        return visit(node.getNode());
    }

    @Override
    public Object visitUnaryNode(UnaryNode node) {
        TokenType tokenType = node.getOperator().getTokenType();
        Object val = visit(node.getNode());

        if (tokenType == TokenType.NOT) {
            Boolean boolVal = castOrThrow(val, Boolean.class,
                    new ParseTreeVisitorException("Node does not evaluate to Boolean", node.getNode()));
            return !boolVal;
        } else if (tokenType == TokenType.MINUS) {
            Double doubleVal = castOrThrow(val, Double.class,
                    new ParseTreeVisitorException("Node does not evaluate to Double", node.getNode()));
            return -doubleVal;
        }

        throw new ParseTreeVisitorException("Invalid unary operator", node);
    }

    @Override
    public Object visitLiteralNode(LiteralNode node) {
        TokenType tokenType = node.getToken().getTokenType();
        if (tokenType == TokenType.NULL) return null;
        if (tokenType == TokenType.TRUE) return true;
        if (tokenType == TokenType.FALSE) return false;

        try {
            return Double.parseDouble(node.getToken().getSequence());
        } catch (NumberFormatException e) {
            // it's a string
            String seq = node.getToken().getSequence();
            // remove the first and last quote
            return seq.substring(1, seq.length() - 1);
        }
    }

    @Override
    public Object visitFunctionNode(FunctionNode node) {
        String funcName = node.getToken().getSequence();
        Function func = FunctionsRegistry.getFunction(funcName);

        List<Object> funcArgs = new ArrayList<>(node.getFuncArgs().size());

        for (ParseTreeNode arg : node.getFuncArgs()) {
            funcArgs.add(visit(arg));
        }

        try {
            return func.execute(funcArgs);
        } catch(RuntimeException e) {
            throw new ParseTreeVisitorException("Error while executing function", e, node);
        }
    }

    protected <T> T castOrThrow(Object val, Class<T> klass, RuntimeException e) {
        if (!klass.isAssignableFrom(val.getClass())) {
            throw e;
        }

        return klass.cast(val);
    }
}