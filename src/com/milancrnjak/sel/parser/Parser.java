package com.milancrnjak.sel.parser;

import com.milancrnjak.sel.exception.ParserException;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;

import java.util.List;

/**
 * Parses input tokens based on the rules defined in the grammar and creates a parse tree.
 *
 * @author Milan Crnjak
 */
public interface Parser {

    ParseTreeNode parse(List<Token> tokens) throws ParserException;

}
