package IC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import IC.AST.*;
import java_cup.runtime.Symbol;

import IC.Parser.Lexer;
import IC.Parser.LibraryParser;

/**
 * The IC Compiler.
 * 
 */
public class Compiler {
	/**
	 * Compile the given IC code.
	 * 
	 * @param args
	 *            - The IC file path.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Error: No input IC file.");
			System.exit(1);
		}
		try {
			File f = new File(args[0]);
			FileReader fr = null;
			// Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			/*
			 * Token token = new Token(sym.EOF, 0, ""); do { try { token =
			 * lex.next_token(); System.out.println(token.getLineNumber() + ": "
			 * + token.toString()); } catch (IOException e) { break; } catch
			 * (LexicalError e) { System.err.println("Lexical Error: at token: "
			 * + token + " in line " + token.getLineNumber() + " ''" +
			 * e.getMessage() + "''"); System.exit(1); } } while(token.sym !=
			 * sym.EOF);
			 * System.out.println("-------------------------------------------"
			 * );
			 */
			// Parser
			LibraryParser libParser = new LibraryParser(lex);
			libParser.printTokens = true;
			Symbol parsedSymbol = libParser.parse();
			System.out.println("Parsed Symbol: " + parsedSymbol.value);
			
			/*
			Program root = (Program) parsedSymbol.value;
			PrettyPrinter printer = new PrettyPrinter(args[0]);
			printer.visit(root);
			// Interpret the program
			// TODO: Check breaks.
			
			// TODO: bonux 2 - Check decleration statements after if, while..
			 * 
			 */
		} catch (FileNotFoundException e1) {
			System.err.println("Error: The file doesn't exist.");
			System.exit(1);
		} catch (Exception e3) {
			System.err.println("Error: " + e3.getMessage());
			System.exit(1);
		}
	}
}