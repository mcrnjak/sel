package com.milancrnjak.sel.tokenizer;

import com.milancrnjak.sel.exception.TokenizerException;
import com.milancrnjak.sel.token.Token;

import java.util.List;

/**
 * Input tokenizer. Builds a list of tokens from the given input.
 *
 * @author Milan Crnjak
 */
public interface Tokenizer {

    /**
     * Converts a given input into a list of {@link Token}s.
     *
     * @param input expression input
     * @return list of tokens
     * @throws TokenizerException if unknown symbol is detected in input.
     */
    List<Token> tokenize(String input) throws TokenizerException;

}
