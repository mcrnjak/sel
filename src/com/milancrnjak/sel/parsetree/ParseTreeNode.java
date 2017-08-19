package com.milancrnjak.sel.parsetree;

import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;

/**
 * Parse tree node. Represents a single parse tree node. Since it can be a root of the parse tree
 * it is also an entry point to the parse tree.
 *
 * @author Milan Crnjak
 */
public interface ParseTreeNode {

    /**
     * Accepts the parse tree visitor. Calls the corresponding 'visit' method on the visitor
     * based on the type of parse tree node.
     *
     * @param visitor Visitor object.
     * @param <T> Visitor result type.
     *
     * @return Result object of type T.
     */
    <T> T accept(ParseTreeVisitor<T> visitor);

}
