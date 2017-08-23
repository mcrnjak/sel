package mc.sel.parser;

import mc.sel.exception.ParserException;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.token.Token;

import java.util.List;

/**
 * Parses input tokens based on the rules defined in the grammar and creates a parse tree.
 *
 * @author Milan Crnjak
 */
public interface Parser {

    ParseTreeNode parse(List<Token> tokens) throws ParserException;

}
