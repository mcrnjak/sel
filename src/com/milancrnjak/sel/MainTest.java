package com.milancrnjak.sel;

import com.milancrnjak.sel.exception.ParseTreeVisitorException;
import com.milancrnjak.sel.exception.ParserException;
import com.milancrnjak.sel.exception.TokenizerException;
import com.milancrnjak.sel.identifier.context.MapContextObject;
import com.milancrnjak.sel.parser.DefaultParser;
import com.milancrnjak.sel.parser.Parser;
import com.milancrnjak.sel.parsetree.ParseTreeNode;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeInterpreter;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeLispStringifier;
import com.milancrnjak.sel.parsetree.visitor.ParseTreeVisitor;
import com.milancrnjak.sel.token.Token;
import com.milancrnjak.sel.tokenizer.RegexTokenizer;
import com.milancrnjak.sel.tokenizer.Tokenizer;

import java.lang.reflect.Method;
import java.util.*;

public class MainTest {

    public static void main(String[] args) {
        String input = "list(pow(this.aaa.length() - 1, 2), this.bbb - (this.bbb / 45)).get(1) - this.ccc.get(0) * 2";

        try {
            // tokenize the input
            Tokenizer tokenizer = new RegexTokenizer();
            List<Token> tokens = tokenizer.tokenize(input);

            // parse the tokens
            Parser parser = new DefaultParser();
            ParseTreeNode parseTree = parser.parse(tokens);

            // parse tree to string
            System.out.println(parseTree);

            // stringify as lisp structure
            ParseTreeVisitor<String> stringifier = new ParseTreeLispStringifier();
            System.out.println(stringifier.visit(parseTree));

            // context object
            Map<String, Object> obj = new HashMap<>();
            obj.put("aaa", "Hello");
            obj.put("bbb", 123);
            List<Integer> list = new ArrayList<>();
            list.add(23);
            list.add(54);
            obj.put("ccc", list);

            // interpret
            ParseTreeVisitor<Object> interpreter = new ParseTreeInterpreter(new MapContextObject(obj));
            System.out.println(interpreter.visit(parseTree));

        } catch (TokenizerException e) {
            System.err.println("Error in input at position [" + e.getPosition() + "] " + e.getMessage());
        } catch (ParserException e) {
            System.err.println("Error in input at token [" +
                    e.getToken().getStartPos() + "," + e.getToken().getEndPos() + "] " + e.getMessage());
        } catch (ParseTreeVisitorException e) {
            System.err.println("Error in parse tree visitor.");
            e.printStackTrace();
        }
    }
}
