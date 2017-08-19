package com.milancrnjak.sel;

import com.milancrnjak.sel.exception.TokenizerException;
import com.milancrnjak.sel.token.Token;
import com.milancrnjak.sel.tokenizer.RegexTokenizer;
import com.milancrnjak.sel.tokenizer.Tokenizer;

import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        Tokenizer tokenizer = new RegexTokenizer();
        try {
            List<Token> tokens = tokenizer.tokenize("('afasdf' + 34) - 54 / null * false and true || 'adsf' >= 4 > 4 <= 44 <23 == 44 != 44");
            System.out.println(tokens);
        } catch (TokenizerException e) {
            System.err.println("Error in input at position [" + e.getPosition() + "] " + e.getMessage());
        }
    }
}
