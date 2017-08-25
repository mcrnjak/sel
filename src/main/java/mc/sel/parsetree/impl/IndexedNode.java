package mc.sel.parsetree.impl;

import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeVisitor;

/**
 * Represents indexed node, e.g. this.attr_name[0].
 *
 * @author Milan Crnjak
 */
public class IndexedNode implements ParseTreeNode {

    private ParseTreeNode node;
    private ParseTreeNode index;

    public IndexedNode(ParseTreeNode node, ParseTreeNode index) {
        this.node = node;
        this.index = index;
    }

    public ParseTreeNode getNode() {
        return node;
    }

    public ParseTreeNode getIndex() {
        return index;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<T> visitor) {
        return visitor.visitIndexedNode(this);
    }
}
