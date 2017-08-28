package mc.sel.tokenizer;

import mc.sel.exception.TokenizerException;
import mc.sel.token.Token;
import mc.sel.token.TokenType;

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
        addTokenDescription("\\[", TokenType.LEFT_BRACKET);
        addTokenDescription("\\]", TokenType.RIGHT_BRACKET);
        addTokenDescription("\\b(and)\\b|&&", TokenType.AND);
        addTokenDescription("\\b(or)\\b|\\|\\|", TokenType.OR);
        addTokenDescription("\\b(gte)\\b|>=", TokenType.GTE);
        addTokenDescription("\\b(gt)\\b|>", TokenType.GT);
        addTokenDescription("\\b(lte)\\b|<=", TokenType.LTE);
        addTokenDescription("\\b(lt)\\b|<", TokenType.LT);
        addTokenDescription("\\b(eq)\\b|==", TokenType.EQUAL);
        addTokenDescription("\\b(true)\\b", TokenType.TRUE);
        addTokenDescription("\\b(false)\\b", TokenType.FALSE);
        addTokenDescription("\\b(null)\\b", TokenType.NULL);
        addTokenDescription("\\b(ne)\\b|!=", TokenType.NOT_EQUAL);
        addTokenDescription("!", TokenType.NOT);
        addTokenDescription("'((\\\\')|[^'])*?'", TokenType.STRING);
        addTokenDescription("[0-9]+\\.[0-9]+", TokenType.DOUBLE);
        addTokenDescription("[0-9]+", TokenType.INT);
        addTokenDescription("[a-zA-Z][a-zA-Z0-9_]*", TokenType.ID);
        addTokenDescription("\\.", TokenType.DOT);
        addTokenDescription(",", TokenType.ARGSEP);
        addTokenDescription("=", TokenType.ASSIGN);
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
