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
import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

    private Context context;

    public ParseTreeInterpreter(Context context) {
        this.context = context;
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
            String msg = String.format("Expected types for left operand are String, Number, Date. Actual type is %s",
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

        TokenType operator = node.getOperator().getTokenType();

        if (leftVal == null) {
            if (operator == TokenType.EQUAL) {
                return null == rightVal;
            } else {
                return null != rightVal;
            }

        }

        if (operator  == TokenType.EQUAL) {
            return leftVal.equals(rightVal);
        } else {
            return !leftVal.equals(rightVal);
        }

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
        String funcName = node.getFuncIdToken().getSequence();

        // evaluate func args
        List<Object> args = new ArrayList<>(node.getFuncArgs().size());
        for (ParseTreeNode arg : node.getFuncArgs()) {
            args.add(visit(arg));
        }

        // evaluate index (if any)
        Integer index = null;
        if (node.getIndexNode() != null) {
            index = visitIndexNode(node.getIndexNode());
        }

        // func or method execution result
        Object result;

        if (node.getInvokerNode() == null) { // SEL function call (no invoker)
            Function func = FunctionsRegistry.getFunction(funcName);

            if (func == null) {
                throw new ParseTreeVisitorException("Undefined function", node);
            }

            try {
                result = func.execute(context, args);
            } catch(RuntimeException e) {
                throw new ParseTreeVisitorException("Error while executing function", e, node);
            }
        } else { // method call
            Object invoker = visit(node.getInvokerNode());
            try {
                result = invokeMethod(invoker, funcName, args);
            } catch (Exception e) {
                throw new ParseTreeVisitorException("Error while executing method", e, node);
            }
        }

        return index == null ? result : getValueAtIndex(result, index, node);

    }

    protected Object getValueAtIndex(Object val, Integer index, ParseTreeNode targetNode) {
        if (val instanceof List) {
            List<?> list = (List<?>) val;
            return list.get(index);
        } else if (val.getClass().isArray()) {
            return Array.get(val, index);
        } else {
            throw new ParseTreeVisitorException("Cannot use index on non-indexable value", targetNode);
        }
    }

    protected Object invokeMethod(Object invoker, String name, List<Object> args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (invoker instanceof ContextObject) {
            invoker = ((ContextObject) invoker).getObject();
        }

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

    protected Method findMethod(Object invoker, String name, Class<?>[] argTypes) {
        Class<?> clazz = (invoker instanceof Class<?>) ? (Class<?>) invoker : invoker.getClass();
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(name) && m.getParameterTypes().length == argTypes.length) {
                Class[] parameterTypes = m.getParameterTypes();

                // workaround for the issue with lack of auto-boxing/unboxing of parameter types
                if (typesAreCompatible(parameterTypes, argTypes)) {

                    /*
                     * fix for a problem with accessing public methods on inner non-public classes
                     * e.g. Arrays.asList() returns inner non-public implementation of List so calling a method
                     * such as List.get(int) throws error
                     */
                    m.setAccessible(true);

                    return m;
                }
            }
        }

        return null;
    }

    protected boolean typesAreCompatible(Class<?>[] parameterTypes, Class<?>[] argTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Class<?> argType = argTypes[i];

            if (!translateFromPrimitive(parameterType).isAssignableFrom(argType)) {
                return false;
            }
        }

        return true;
    }

    protected Class<?> translateFromPrimitive(Class<?> clazz) {
        if (!clazz.isPrimitive()) return (clazz);

        if (Boolean.TYPE.equals(clazz)) return (Boolean.class);
        if (Character.TYPE.equals(clazz)) return (Character.class);
        if (Byte.TYPE.equals(clazz)) return (Byte.class);
        if (Short.TYPE.equals(clazz)) return (Short.class);
        if (Integer.TYPE.equals(clazz)) return (Integer.class);
        if (Long.TYPE.equals(clazz)) return (Long.class);
        if (Float.TYPE.equals(clazz)) return (Float.class);
        if (Double.TYPE.equals(clazz)) return (Double.class);

        throw new RuntimeException("Error translating type:" + clazz);
    }

    @Override
    public Object visitIdentifierNode(IdentifierNode node) throws ParseTreeVisitorException {
        String name = node.getToken().getSequence();

        // evaluate index (if any)
        Integer index = null;
        if (node.getIndexNode() != null) {
            index = visitIndexNode(node.getIndexNode());
        }

        ObjectIdentifier objIdentifier = ObjectsRegistry.getObjectIdentifier(name);
        if (objIdentifier != null) {
            if (index != null) {
                throw new ParseTreeVisitorException("Object identifiers do not support access by index", node);
            }

            // execute previous identifiers in chain
            if (node.getInvokerNode() != null) {
                visit(node.getInvokerNode());
            }
            // then execute current identifier
            return objIdentifier.execute(context);
        } else { // property access
            if (node.getInvokerNode() != null) {
                Object invoker = visit(node.getInvokerNode());
                return getProperty(invoker, name, index, node);
            }

            throw new ParseTreeVisitorException("Invalid identifier node", node);
        }
    }

    protected Object getProperty(Object invoker, String name, Integer index, IdentifierNode node) {
        if (invoker == null) {
            throw new ParseTreeVisitorException("Cannot access property on null object", node.getInvokerNode());
        }

        if (invoker instanceof ContextObject) {
            ContextObject obj = (ContextObject) invoker;
            return index == null ? obj.getProperty(name) : obj.getPropertyAtIndex(name, index);
        }

        // try reflection
        try {
            Class<?> clazz = (invoker instanceof Class<?>) ? (Class<?>) invoker : invoker.getClass();

            // if java bean property
            for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                if (pd.getName().equals(name) && pd.getReadMethod() != null) {
                    return pd.getReadMethod().invoke(invoker);
                }
            }

            // if public (static) field
            Field f = clazz.getField(name);
            Object result = f.get(null);
            return index == null ? result : getValueAtIndex(result, index, node);
        } catch (Exception e) {
            // wrap property token into parse tree node object to satisfy ParseTreeVisitorException constructor
            // in order to properly indicate the token which is causing problems
            ParseTreeNode markerNode = new IdentifierNode(null, node.getToken());
            throw new ParseTreeVisitorException("Error while accessing property", e, markerNode);
        }
    }

    @Override
    public Integer visitIndexNode(IndexNode node) throws ParseTreeVisitorException {
        Object indexVal = visit(node.getIndex());
        return castOrThrow(indexVal, Number.class,
                new ParseTreeVisitorException("Index expression does not evaluate to number", node.getIndex())).intValue();
    }

    @Override
    public Object visitAssignNode(AssignNode node) throws ParseTreeVisitorException {
        ParseTreeNode objNode = null;

        ParseTreeNode leftNode = node.getLeftNode();

        if (leftNode instanceof IdentifierNode) {
            IdentifierNode leftIdentifierNode = (IdentifierNode) leftNode;

            // evaluate index (if any)
            Integer index = null;
            if (leftIdentifierNode.getIndexNode() != null) {
                index = visitIndexNode(leftIdentifierNode.getIndexNode());
            }

            if (leftIdentifierNode.getInvokerNode() != null) { // we have something like 'this.attribute'
                Object invokerObj = visit(leftIdentifierNode.getInvokerNode());
                String propertyName = leftIdentifierNode.getToken().getSequence();

                if (invokerObj instanceof ContextObject) {
                    ContextObject contextObject = (ContextObject) invokerObj;

                    Object valueToAssign = visit(node.getRightNode());

                    if (index != null) {
                        contextObject.setPropertyAtIndex(propertyName, valueToAssign, index);
                    } else {
                        contextObject.setProperty(propertyName, valueToAssign);
                    }

                    return valueToAssign;
                } else {
                    String msg = "Invoker object does not evaluate to ContextObject instance";
                    throw new ParseTreeVisitorException(msg, leftIdentifierNode.getInvokerNode());
                }

            } else { // there is no invoker node
                String msg = "Must specify an object identifier and a property (e.g. this.attribute)";
                throw new ParseTreeVisitorException(msg, leftIdentifierNode);
            }
        } else {
            String msg = "Left-hand side of assignment operation must be an object identifier property";
            throw new ParseTreeVisitorException(msg, leftNode);
        }
    }

    protected <T> T castOrThrow(Object val, Class<T> klass, RuntimeException e) {
        if (!klass.isAssignableFrom(val.getClass())) {
            throw e;
        }

        return klass.cast(val);
    }
}
