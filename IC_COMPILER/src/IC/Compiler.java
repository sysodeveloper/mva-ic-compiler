package IC;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.LibraryParser;
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
			 LibraryParser libParser = new LibraryParser(lex);
			 libParser.parse();
    	} catch (FileNotFoundException e1) {
			System.err.println("Error: The file doesn't exist.");
			System.exit(1);
    	}catch (Exception e3){
			System.err.println("Error: Unknown Error.");
			System.exit(1);
		}
    }
}