package mc.sel.tokenizer;

import mc.sel.exception.TokenizerException;
import mc.sel.token.Token;
import mc.sel.token.TokenType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link Tokenizer} interface with regular expressions.
 *
 * @author Milan Crnjak
 */
public class RegexTokenizer implements Tokenizer {

    private Map<TokenType, Pattern> tokenDescriptions = new LinkedHashMap<>();

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

            for (TokenType tokenType : tokenDescriptions.keySet()) {
                Pattern regex = tokenDescriptions.get(tokenType);
                Matcher m = regex.matcher(s);

                if (m.find()) {
                    match = true;
                    String tokenSequence = m.group();
                    int startPos = totalLength - currentLength;
                    int endPos = startPos + tokenSequence.length() - 1;

                    tokens.add(new Token(tokenType, tokenSequence, startPos, endPos));

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

    /**
     * Defines regular expressions for supported token types, as well as the order in which they are checked.
     */
    protected void initialize() {
        addTokenDescription(TokenType.PLUS, "[+]");
        addTokenDescription(TokenType.MINUS, "-");
        addTokenDescription(TokenType.MUL, "[*]");
        addTokenDescription(TokenType.DIV, "/");
        addTokenDescription(TokenType.LEFT_PAREN, "[(]");
        addTokenDescription(TokenType.RIGHT_PAREN, "[)]");
        addTokenDescription(TokenType.LEFT_BRACKET, "\\[");
        addTokenDescription(TokenType.RIGHT_BRACKET, "\\]");
        addTokenDescription(TokenType.AND, "\\b(and)\\b|&&");
        addTokenDescription(TokenType.OR, "\\b(or)\\b|\\|\\|");
        addTokenDescription(TokenType.GTE, "\\b(gte)\\b|>=");
        addTokenDescription(TokenType.GT, "\\b(gt)\\b|>");
        addTokenDescription(TokenType.LTE, "\\b(lte)\\b|<=");
        addTokenDescription(TokenType.LT, "\\b(lt)\\b|<");
        addTokenDescription(TokenType.EQUAL, "\\b(eq)\\b|==");
        addTokenDescription(TokenType.TRUE, "\\b(true)\\b");
        addTokenDescription(TokenType.FALSE, "\\b(false)\\b");
        addTokenDescription(TokenType.NULL, "\\b(null)\\b");
        addTokenDescription(TokenType.NOT_EQUAL, "\\b(ne)\\b|!=");
        addTokenDescription(TokenType.NOT, "!");
        addTokenDescription(TokenType.STRING, "'((\\\\')|[^'])*?'");
        addTokenDescription(TokenType.DOUBLE, "[0-9]+\\.[0-9]+");
        addTokenDescription(TokenType.INT, "[0-9]+");
        addTokenDescription(TokenType.ID, "[a-zA-Z][a-zA-Z0-9_]*");
        addTokenDescription(TokenType.DOT, "\\.");
        addTokenDescription(TokenType.ARGSEP, ",");
        addTokenDescription(TokenType.ASSIGN, "=");
    }

    /**
     * Adds a new token type -> regex mapping or replaces an existing mapping.
     *
     * @param tokenType token type
     * @param regex regex for the token type
     */
    protected void addTokenDescription(TokenType tokenType, String regex) {
        tokenDescriptions.put(tokenType, Pattern.compile("^(" + regex + ")"));
    }

    protected Map<TokenType, Pattern> getTokenDescriptions() {
        return tokenDescriptions;
    }
}
