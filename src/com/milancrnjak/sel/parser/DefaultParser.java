package com.milancrnjak.sel.parser;

import com.milancrnjak.sel.exception.ParserException;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

import java.util.List;

/**
 * Default implementation of the {@link Parser} interface. Uses recursive descent parser algorithm based on the
 * defined grammar to parse input tokens and build the parse tree.
 *
 * @author Milan Crnjak
 */
public class DefaultParser implements Parser {

    private List<Token> tokens;
    private Token lookahead;
    private Token current;


    @Override
    public ParseTreeNode parse(List<Token> tokens) throws ParserException {
        return null;
    }

    protected List<Token> getTokens() {
        return tokens;
    }

    protected Token getLookahead() {
        return lookahead;
    }

    protected Token getCurrent() {
        return current;
    }
}
