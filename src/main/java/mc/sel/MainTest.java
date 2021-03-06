package mc.sel;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.exception.ParserException;
import mc.sel.exception.TokenizerException;
import mc.sel.identifier.context.Context;
import mc.sel.identifier.context.MapContextObject;
import mc.sel.identifier.context.DefaultContextImpl;
import mc.sel.parser.DefaultParser;
import mc.sel.parser.Parser;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeInterpreter;
import mc.sel.parsetree.visitor.ParseTreeLispStringifier;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;
import mc.sel.tokenizer.RegexTokenizer;
import mc.sel.tokenizer.Tokenizer;
import mc.sel.util.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class MainTest {

    public static void main(String[] args) throws IOException {

        // context object
        Map<String, Object> obj = new HashMap<>();
        obj.put("greeting", "Hello");
        obj.put("words", Arrays.asList("Simple", "Expression", "Language"));

        Context ctx = new DefaultContextImpl(new MapContextObject(obj));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            while (!(input = br.readLine()).equalsIgnoreCase("q")) {

                try {
                    // tokenize the input
                    Tokenizer tokenizer = new RegexTokenizer();
                    List<Token> tokens = tokenizer.tokenize(input);

                    // parse the tokens
                    Parser parser = new DefaultParser();
                    ParseTreeNode parseTree = parser.parse(tokens);

                    // stringify as lisp structure
                    ParseTreeVisitor<String> stringifier = new ParseTreeLispStringifier();
                    System.out.println(stringifier.visit(parseTree));

                    // interpret
                    ParseTreeVisitor<Object> interpreter = new ParseTreeInterpreter(ctx);
                    System.out.println(interpreter.visit(parseTree));

                } catch (TokenizerException e) {
                    System.err.println("Error in input at position [" + e.getPosition() + "] " + e.getMessage());
                    System.err.println(ExceptionUtils.underlineError(input, e));
                } catch (ParserException e) {
                    System.err.println("Error in input at token [" +
                            e.getToken().getStartPos() + "," + e.getToken().getEndPos() + "] " + e.getMessage());
                    System.err.println(ExceptionUtils.underlineError(input, e));
                } catch (ParseTreeVisitorException e) {
                    System.err.println("Error in parse tree visitor.");
                    System.err.println(ExceptionUtils.underlineError(input, e));
                    e.printStackTrace();
                }
            }
        }
    }
}
