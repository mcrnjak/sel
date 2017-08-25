package mc.sel.parser;

import mc.sel.exception.ParserException;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.impl.*;
import mc.sel.token.Token;
import mc.sel.token.TokenType;

import java.util.LinkedList;
import java.util.List;

import static mc.sel.token.TokenType.*;

/**
 * Default implementation of the {@link Parser} interface. Uses recursive descent parser algorithm based on the
 * defined grammar to parse input tokens and build the parse tree.
 *
 * @author Milan Crnjak
 */
public class DefaultParser implements Parser {

    private LinkedList<Token> tokens;
    private Token lookaheadToken;
    private Token currentToken;
    private Token endToken = new Token(END, "", -1, -1);

    @Override
    public ParseTreeNode parse(List<Token> inputTokens) throws ParserException {
        if (inputTokens.isEmpty()) {
            throw new ParserException("Cannot parse empty input", endToken());
        }

        setTokens(new LinkedList<>(inputTokens));
        setLookaheadToken(getTokens().getFirst());

        ParseTreeNode parseTree = parseExpression();

        if (lookaheadToken().getTokenType() != END) {
            throw new ParserException("Unexpected token detected", lookaheadToken());
        }

        return parseTree;
    }

    /**
     * Begins parsing the input tokens by calling the first rule in the grammar.
     *
     * @return Parse tree root node.
     */
    protected ParseTreeNode parseExpression() {
        return parseBoolOr();
    }

    /**
     * Parses the 'boolOr' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseBoolOr() {
        ParseTreeNode node = parseBoolAnd();

        while (match(OR)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseBoolAnd();
            node = new BoolNode(node, rightNode, operator);
        }

        return node;
    }

    /**
     * Parses the 'boolAnd' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseBoolAnd() {
        ParseTreeNode node = parseEqual();

        while (match(AND)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseEqual();
            node = new BoolNode(node, rightNode, operator);
        }

        return node;

    }

    /**
     * Parses the 'equal' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseEqual() {
        ParseTreeNode node = parseCompare();

        while (match(EQUAL, NOT_EQUAL)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseCompare();
            node = new EqualNode(node, rightNode, operator);
        }

        return node;
    }

    /**
     * Parses the 'compare' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseCompare() {
        ParseTreeNode node = parseAddSub();

        while (match(GTE, GT, LTE, LT)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseAddSub();
            node = new CompareNode(node, rightNode, operator);
        }

        return node;
    }

    /**
     * Parses the 'addSub' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseAddSub() {
        ParseTreeNode node = parseMulDiv();

        while (match(PLUS, MINUS)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseMulDiv();
            node = new AddSubNode(node, rightNode, operator);
        }

        return node;
    }

    /**
     * Parses the 'mulDiv' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseMulDiv() {
        ParseTreeNode node = parseUnary();

        while (match(MUL, DIV)) {
            Token operator = currentToken();
            ParseTreeNode rightNode = parseUnary();
            node = new MulDivNode(node, rightNode, operator);
        }

        return node;
    }

    /**
     * Parses the 'unary' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseUnary() {
        if (match(NOT, MINUS)) {
            Token operator = currentToken();
            ParseTreeNode node = parsePrimary();
            return new UnaryNode(node, operator);
        }
        return parsePrimary();
    }

    /**
     * Parses the 'primary' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parsePrimary() {
        ParseTreeNode node;

        if (match(STRING, DOUBLE, INT, TRUE, FALSE, NULL)) {
            node = new LiteralNode(currentToken());
        } else if (match(LEFT_PAREN)) {
            node = parseExpression();
            consumeOrThrow(RIGHT_PAREN, "Expected closing ')' for parentheses expression");
            node = new ParenthesesNode(node);
        } else if (match(ID)) {
            node = parseCallable(null);
        } else {
            throw new ParserException("Unknown symbol detected", lookaheadToken());
        }

        while (match(DOT)) {
            if (match(ID)) {
                node = parseCallable(node);
            } else {
                throw new ParserException("Expected identifier token", lookaheadToken());
            }
        }

        return node;

    }

    /**
     * Parses the 'callable' rule from the grammar.
     *
     * @return corresponding parse tree node.
     */
    protected ParseTreeNode parseCallable(ParseTreeNode invoker) {
        Token identifier = currentToken();
        ParseTreeNode node = new IdentifierNode(invoker, identifier);

        if (match(LEFT_PAREN)) {
            List<ParseTreeNode> funcArgs = parseArgs();
            node = new FunctionNode(invoker, identifier, funcArgs);
            consumeOrThrow(RIGHT_PAREN, "Closing ')' expected for function");
        }

        if (match(LEFT_BRACKET)) {
            ParseTreeNode index = parseAddSub();
            node = new IndexedNode(node, index);
            consumeOrThrow(RIGHT_BRACKET, "Closing ']' expected for index");
        }

        return node;
    }

    /**
     * Parses the 'args' rule from the grammar.
     *
     * @return List of parse tree nodes (each argument is a node in the list).
     */
    protected List<ParseTreeNode> parseArgs() {
        List<ParseTreeNode> funcArgs = new LinkedList<>();

        if (lookaheadToken().getTokenType() != RIGHT_PAREN) {
            ParseTreeNode arg = parseExpression();
            funcArgs.add(arg);

            while(match(ARGSEP)) {
                arg = parseExpression();
                funcArgs.add(arg);
            }
        }

        return funcArgs;
    }

    /**
     * Checks whether the next token is the one we expect and if so consumes it (points the lookahead token
     * to the next token). Otherwise throws an exception.
     *
     * @param tokenType expected type of token
     * @param errorMessage exception message
     *
     * @return Consumed token
     */
    protected Token consumeOrThrow(TokenType tokenType, String errorMessage) {
        if (match(tokenType)) {
            return currentToken();
        }
        throw new ParserException(errorMessage, lookaheadToken());
    }

    /**
     * Check whether the lookahead token points to any of the passed token types. If yes, points the lookahead to the
     * next token.
     *
     * @param tokenTypes token types
     *
     * @return true if lookahead token matches any of the passed token types, false otherwise.
     */
    protected boolean match(TokenType... tokenTypes) {
        for (TokenType tokenType : tokenTypes) {
            if (lookaheadToken().getTokenType() == tokenType) {
                nextToken();
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the lookahead token reference to the next token in the list or sets it to END token
     * if there are no more tokens. It also sets the current token to what lookahead was pointing.
     */
    protected void nextToken() {
        setCurrentToken(getTokens().pop());

        if (getTokens().isEmpty()) {
            setLookaheadToken(endToken());
        } else {
            setLookaheadToken(getTokens().getFirst());
        }

    }

    protected LinkedList<Token> getTokens() {
        return tokens;
    }

    protected void setTokens(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    protected Token lookaheadToken() {
        return lookaheadToken;
    }

    protected void setLookaheadToken(Token token) {
        this.lookaheadToken = token;
    }

    protected Token currentToken() {
        return currentToken;
    }

    protected void setCurrentToken(Token token) {
        this.currentToken = token;
    }

    protected Token endToken() {
        return endToken;
    }
}
