package mc.sel.parsetree;

import mc.sel.token.Token;

/**
 * Represents a binary parse tree node, i.e. a node with left and right sub-nodes and an operator
 * for the left and right nodes (operands).
 *
 * @author Milan Crnjak
 */
public abstract  class BinaryNode implements ParseTreeNode {

    private ParseTreeNode leftNode;
    private ParseTreeNode rightNode;
    private Token operator;

    public BinaryNode(ParseTreeNode leftNode, ParseTreeNode rightNode, Token operator) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.operator = operator;
    }

    public ParseTreeNode getLeftNode() {
        return leftNode;
    }

    public ParseTreeNode getRightNode() {
        return rightNode;
    }

    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return leftNode.toString() + " " + operator.getTokenType().toString() + " " + rightNode.toString();
    }
}
