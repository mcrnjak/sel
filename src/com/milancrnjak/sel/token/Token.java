package com.milancrnjak.sel.token;

/**
 * Represents a single expression token. Contains the token description such as
 * token type, text sequence, start position and end position in the expression input.
 *
 * @author Milan Crnjak
 */
public class Token {

    private TokenType tokenType;
    private String sequence;
    private int startPos;
    private int endPos;

    public Token(TokenType tokenType, String sequence, int startPos, int endPos) {
        this.tokenType = tokenType;
        this.sequence = sequence;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getSequence() {
        return sequence;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", sequence='" + sequence + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                '}';
    }
}
