package mc.sel.parsetree.visitor;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.function.Function;
import mc.sel.function.FunctionsRegistry;
import mc.sel.identifier.ObjectIdentifier;
import mc.sel.identifier.ObjectsRegistry;
import mc.sel.identifier.context.Context;
import mc.sel.identifier.context.ContextObject;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.impl.*;
import mc.sel.token.TokenType;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private ContextObject contextObject;

    public ParseTreeInterpreter(ContextObject contextObject) {
        this.contextObject = contextObject;
    }

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
            Number left = castOrThrow(leftVal, Number.class,
                    new ParseTreeVisitorException("Left operand must evaluate to Number", node.getLeftNode()));

            Number right = castOrThrow(rightVal, Number.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Number", node.getRightNode()));

            return left.doubleValue() - right.doubleValue();
        } else if (tokenType == TokenType.PLUS) {
            if (leftVal instanceof String) {
                // string concatenation
                String left = (String) leftVal;
                return left + rightVal;
            }

            // else must be a number
            Number left = castOrThrow(leftVal, Number.class,
                    new ParseTreeVisitorException("Left operand must evaluate to Number", node.getLeftNode()));

            Number right = castOrThrow(rightVal, Number.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Number", node.getRightNode()));

            return left.doubleValue() + right.doubleValue();
        }

        throw new ParseTreeVisitorException("Invalid operator in AddSubNode", node);
    }

    @Override
    public Object visitBoolNode(BoolNode node) {
        TokenType operatorType = node.getOperator().getTokenType();

        Object leftVal = visit(node.getLeftNode());
        Boolean left = castOrThrow(leftVal, Boolean.class,
                new ParseTreeVisitorException("Left operand must evaluate to Boolean", node.getLeftNode()));

        if (operatorType == TokenType.OR && left) {
            return true;
        }

        if (operatorType == TokenType.AND && !left) {
            return false;
        }

        Object rightVal = visit(node.getRightNode());
        return castOrThrow(rightVal, Boolean.class,
                new ParseTreeVisitorException("Right operand must evaluate to Boolean", node.getRightNode()));
    }

    @Override
    public Object visitCompareNode(CompareNode node) {
        TokenType tokenType = node.getOperator().getTokenType();
        Object leftVal = visit(node.getLeftNode());
        Object rightVal = visit(node.getRightNode());

        if (leftVal instanceof Number) {
            Number left = (Number) leftVal;
            Number right = castOrThrow(rightVal, Number.class,
                    new ParseTreeVisitorException("Right operand must evaluate to Number", node.getRightNode()));

            return compareNumbers(left, right, tokenType);
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

    protected Object compareNumbers(Number left, Number right, TokenType tokenType) {
        if (tokenType == TokenType.GTE) {
            return left.doubleValue() >= right.doubleValue();
        } else if (tokenType == TokenType.GT) {
            return left.doubleValue() > right.doubleValue();
        } else if (tokenType == TokenType.LTE) {
            return left.doubleValue() <= right.doubleValue();
        } else if (tokenType == TokenType.LT) {
            return left.doubleValue() < right.doubleValue();
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
        Number leftNum =  castOrThrow(leftVal, Number.class,
                new ParseTreeVisitorException("Left node does not evaluate to Number", node.getLeftNode()));

        Object rightVal = visit(node.getRightNode());
        Number rightNum = castOrThrow(rightVal, Number.class,
                new ParseTreeVisitorException("Right node does not evaluate to Number", node.getRightNode()));

        if (node.getOperator().getTokenType() == TokenType.MUL) {
            return leftNum.doubleValue() * rightNum.doubleValue();
        } else if (node.getOperator().getTokenType() == TokenType.DIV) {
            return leftNum.doubleValue() / rightNum.doubleValue();
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
            Number doubleVal = castOrThrow(val, Number.class,
                    new ParseTreeVisitorException("Node does not evaluate to Number", node.getNode()));
            return -doubleVal.doubleValue();
        }

        throw new ParseTreeVisitorException("Invalid unary operator", node);
    }

    @Override
    public Object visitLiteralNode(LiteralNode node) {
        TokenType tokenType = node.getToken().getTokenType();
        if (tokenType == TokenType.NULL) return null;
        if (tokenType == TokenType.TRUE) return true;
        if (tokenType == TokenType.FALSE) return false;

        if (tokenType == TokenType.INT) {
            try {
                return Integer.parseInt(node.getToken().getSequence());
            } catch (NumberFormatException e) {
                throw new ParseTreeVisitorException("Error while parsing Int value", e, node);
            }
        }

        if (tokenType == TokenType.DOUBLE) {
            try {
                return Double.parseDouble(node.getToken().getSequence());
            } catch (NumberFormatException e) {
                throw new ParseTreeVisitorException("Error while parsing Double value", e, node);
            }
        }

        if (tokenType == TokenType.STRING) {
            String seq = node.getToken().getSequence();
            // remove the first and last quote
            return seq.substring(1, seq.length() - 1).replace("\\'", "'");
        }

        throw new ParseTreeVisitorException("Unknown literal node type", node);
    }

    @Override
    public Object visitFunctionNode(FunctionNode node) {
        String name = node.getToken().getSequence();

        List<Object> args = new ArrayList<>(node.getFuncArgs().size());
        for (ParseTreeNode arg : node.getFuncArgs()) {
            args.add(visit(arg));
        }

        if (node.getInvokerNode() == null) {
            Function func = FunctionsRegistry.getFunction(name);

            if (func == null) {
                throw new ParseTreeVisitorException("Undefined function", node);
            }

            try {
                return func.execute(args);
            } catch(RuntimeException e) {
                throw new ParseTreeVisitorException("Error while executing function", e, node);
            }
        }

        Object invoker = visit(node.getInvokerNode());

        if (invoker instanceof ContextObject) {
            invoker = ((ContextObject) invoker).getObject();
        }

        try {
            return invoke(invoker, name, args);
        } catch (Exception e) {
            throw new ParseTreeVisitorException("Error while executing method", e, node);
        }

    }

    private Object invoke(Object invoker, String name, List<Object> args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?>[] argTypes = new Class[args.size()];
        for (int i = 0; i < args.size(); i++) {
            argTypes[i] = args.get(i).getClass();
        }

        Method method = findMethod(invoker, name, argTypes);
        if (method != null) {
            return method.invoke(invoker, args.toArray());
        } else {
            throw new RuntimeException("No such method exception: " + name);
        }
    }

    private Method findMethod(Object invoker, String name, Class<?>[] argTypes) {
        Class<?> clazz = (invoker instanceof Class<?>) ? (Class<?>) invoker : invoker.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name) && m.getParameterTypes().length == argTypes.length) {
                Class[] parameterTypes = m.getParameterTypes();

                // workaround for the issue with lack of auto-boxing/unboxing of parameter types
                if (typesAreCompatible(parameterTypes, argTypes)) {
                    return m;
                }
            }
        }

        return null;
    }

    private boolean typesAreCompatible(Class<?>[] parameterTypes, Class<?>[] argTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Class<?> argType = argTypes[i];

            if (!translateFromPrimitive(parameterType).isAssignableFrom(argType)) {
                return false;
            }
        }

        return true;
    }

    private Class<?> translateFromPrimitive(Class<?> clazz) {
        if (!clazz.isPrimitive()) return (clazz);

        if (Boolean.TYPE.equals(clazz)) return (Boolean.class);
        if (Character.TYPE.equals(clazz)) return (Character.class);
        if (Byte.TYPE.equals(clazz)) return (Byte.class);
        if (Short.TYPE.equals(clazz)) return (Short.class);
        if (Integer.TYPE.equals(clazz)) return (Integer.class);
        if (Long.TYPE.equals(clazz)) return (Long.class);
        if (Float.TYPE.equals(clazz)) return (Float.class);
        if (Double.TYPE.equals(clazz)) return (Double.class);

        throw new RuntimeException( "Error translating type:" + clazz );
    }

    @Override
    public Object visitIdentifierNode(IdentifierNode node) throws ParseTreeVisitorException {
        String name = node.getToken().getSequence();

        ObjectIdentifier objIdentifier = ObjectsRegistry.getObjectIdentifier(name);
        Object invoker = null;

        if (node.getInvokerNode() != null) {
            invoker = visit(node.getInvokerNode());
        }

        Context ctx = new Context();
        ctx.setContextObject(contextObject);

        if (objIdentifier != null) {
            return objIdentifier.execute(ctx);
        } else {
            // it's a property access
            if (invoker == null) {
                throw new NullPointerException();
            }

            if (invoker instanceof ContextObject) {
                ContextObject obj = (ContextObject) invoker;
                return obj.getProperty(name);
            }

            try {
                for (PropertyDescriptor pd : Introspector.getBeanInfo(invoker.getClass()).getPropertyDescriptors()) {
                    if (pd.getName().equals(name) && pd.getReadMethod() != null) {
                        return pd.getReadMethod().invoke(invoker);
                    }
                }
            } catch (Exception e) {
                throw new ParseTreeVisitorException("Error while accessing property", node);
            }

            throw new ParseTreeVisitorException("Cannot resolve identifier", node);
        }

    }

    protected <T> T castOrThrow(Object val, Class<T> klass, RuntimeException e) {
        if (!klass.isAssignableFrom(val.getClass())) {
            throw e;
        }

        return klass.cast(val);
    }
}
