package com.milancrnjak.sel;

import com.milancrnjak.sel.exception.ParserException;
import com.milancrnjak.sel.exception.TokenizerException;
import com.milancrnjak.sel.parser.DefaultParser;
import com.milancrnjak.sel.parser.Parser;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.token.Token;
import com.milancrnjak.sel.tokenizer.RegexTokenizer;
import com.milancrnjak.sel.tokenizer.Tokenizer;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        String input = "(true and false) == 4 < 5";

        Tokenizer tokenizer = new RegexTokenizer();
        try {
            List<Token> tokens = tokenizer.tokenize(input);
            Parser parser = new DefaultParser();
            ParseTreeNode parseTree = parser.parse(tokens);
            System.out.println(parseTree);
        } catch (TokenizerException e) {
            System.err.println("Error in input at position [" + e.getPosition() + "] " + e.getMessage());
        } catch (ParserException e) {
            System.err.println("Error in input at token [" + e.getToken().getStartPos() + "," + e.getToken().getEndPos() + "] " + e.getMessage());
        }
    }
}
