package IC;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.Token;
import IC.Parser.sym;

/**
 * The IC Compiler.
 *
 */
public class Compiler
{
	/**
	 * Compile the given IC code.
	 * @param args - The IC file path.
	 */
    public static void main(String[] args)
    {
    	if(args.length < 1) {
    		System.err.println("Error: No input IC file.");
    		System.exit(1);
    	}
    	
    	File f = new File(args[0]);
    	FileReader fr = null;
    	List<Token> tokens = new ArrayList<Token>();
    	try {	
			 fr =  new FileReader(f);
			 Lexer lex = new Lexer(fr);
	    	 Token token = new Token(sym.EOF, 0, "");
	    	 do {
	    		try {
					token = lex.next_token();
					tokens.add(token);
				} catch (IOException e) {
					break;
				} catch (LexicalError e) {
					System.err.println("Lexical Error: at token: " + token + 
							" in line " + token.getLineNumber() + " ''" + e.getMessage() + "''");
					System.exit(1);
				}
	    	}
	    	while(token.sym != sym.EOF);
	    	for(Token t : tokens) {
	    		System.out.println(t.getLineNumber() + ": " + t.toString());
	    	}
		} catch (FileNotFoundException e1) {
			System.err.println("Error: The file doesn't exist.");
			System.exit(1);
		}
    }
}