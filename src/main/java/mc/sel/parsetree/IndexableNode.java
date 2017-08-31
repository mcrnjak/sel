package mc.sel.parsetree;

import mc.sel.parsetree.impl.IndexNode;

public interface IndexableNode extends ParseTreeNode {

    IndexNode getIndexNode();

    void setIndexNode(IndexNode index);

}
