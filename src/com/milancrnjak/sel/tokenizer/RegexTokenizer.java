package com.milancrnjak.sel.tokenizer;

import com.milancrnjak.sel.exception.TokenizerException;
import com.milancrnjak.sel.function.FunctionsRegistry;
import com.milancrnjak.sel.token.Token;
import com.milancrnjak.sel.token.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link Tokenizer} interface with regular expressions.
 *
 * @author Milan Crnjak
 */
public class RegexTokenizer implements Tokenizer {

    private List<TokenDescription> tokenDescriptions = new ArrayList<>();

    public RegexTokenizer() {
        initialize();
    }



    @Override
    public List<Token> tokenize(String input) throws TokenizerException {
        String s = input.trim();
        int totalLength = input.length();


        LinkedList<Token> tokens = new LinkedList<>();

        while (!s.isEmpty()) {
            int currentLength = s.length();

            boolean match = false;

            for (TokenDescription tokenDescription : tokenDescriptions) {
                Matcher m = tokenDescription.regex.matcher(s);

                if (m.find()) {
                    match = true;
                    String tokenSequence = m.group();
                    int startPos = totalLength - currentLength;
                    int endPos = startPos + tokenSequence.length() - 1;

                    tokens.add(new Token(tokenDescription.tokenType, tokenSequence, startPos, endPos));

                    s = m.replaceFirst("").trim();
                    break;
                }
            }

            if (!match) {
                throw new TokenizerException("Unknown symbol detected", totalLength - currentLength);
            }

        }

        return tokens;
    }

    private void initialize() {
        addTokenDescription("[+]", TokenType.PLUS);
        addTokenDescription("-", TokenType.MINUS);
        addTokenDescription("[*]", TokenType.MUL);
        addTokenDescription("/", TokenType.DIV);
        addTokenDescription("[(]", TokenType.LEFT_PAREN);
        addTokenDescription("[)]", TokenType.RIGHT_PAREN);
        addTokenDescription("and|&&", TokenType.AND);
        addTokenDescription("or|\\|\\|", TokenType.OR);
        addTokenDescription("gte|>=", TokenType.GTE);
        addTokenDescription("gt|>", TokenType.GT);
        addTokenDescription("lte|<=", TokenType.LTE);
        addTokenDescription("lt|<", TokenType.LT);
        addTokenDescription("eq|==", TokenType.EQUAL);
        addTokenDescription("true", TokenType.TRUE);
        addTokenDescription("false", TokenType.FALSE);
        addTokenDescription("null", TokenType.NULL);
        addTokenDescription("ne|!=", TokenType.NOT_EQUAL);
        addTokenDescription("!", TokenType.NOT);
        addTokenDescription("'((\\\\')|[^'])*?'", TokenType.STRING);
        addTokenDescription("[0-9]+(\\.[0-9]+)*", TokenType.NUMBER);
        addTokenDescription(String.join("|", FunctionsRegistry.getRegisteredFunctions()), TokenType.FUNC);
        addTokenDescription(",", TokenType.ARGSEP);
    }

    private void addTokenDescription(String regex, TokenType tokenType) {
        tokenDescriptions.add(new TokenDescription(Pattern.compile("^(" + regex + ")"), tokenType));
    }

    private static class TokenDescription {
        TokenType tokenType;
        Pattern regex;

        TokenDescription(Pattern regex, TokenType tokenType) {
            this.regex = regex;
            this.tokenType = tokenType;
        }
    }


}
