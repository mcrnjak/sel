package mc.sel;

import mc.sel.exception.ParseTreeVisitorException;
import mc.sel.exception.ParserException;
import mc.sel.exception.TokenizerException;
import mc.sel.identifier.context.MapContextObject;
import mc.sel.parser.DefaultParser;
import mc.sel.parser.Parser;
import mc.sel.parsetree.ParseTreeNode;
import mc.sel.parsetree.visitor.ParseTreeInterpreter;
import mc.sel.parsetree.visitor.ParseTreeLispStringifier;
import mc.sel.parsetree.visitor.ParseTreeVisitor;
import mc.sel.token.Token;
import mc.sel.tokenizer.RegexTokenizer;
import mc.sel.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {

    public static void main(String[] args) throws IOException {
//        String input = "pow(this.aaa.length() - 1, list(1,2).get(1)) * this.bbb - this.get('ccc').get(0) * 2";
//        String input = "this.aaa + ' ' + class('mc.sel.util.StringUtils').join('|', list('1','2','3')) + '|' " +
//                "+ class('java.lang.Math').pow(2.0, pow(2,3)).intValue()";

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

                    // parse tree to string
//                    System.out.println(parseTree);

                    // stringify as lisp structure
                    ParseTreeVisitor<String> stringifier = new ParseTreeLispStringifier();
                    System.out.println(stringifier.visit(parseTree));

                    // context object
                    Map<String, Object> obj = new HashMap<>();
                    obj.put("aaa", "Hello");
                    obj.put("bbb", 10);
                    List<Integer> list = new ArrayList<>();
                    list.add(5);
                    list.add(15);
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
    }
}