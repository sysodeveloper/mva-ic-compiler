package IC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;

import java_cup.runtime.Symbol;
import IC.AST.GraphEdgesPrinter;
import IC.AST.GrpahNodesPrinter;
import IC.AST.ICClass;
import IC.AST.Labeling;
import IC.AST.Program;
import IC.AST.TreePrinter;
import IC.Parser.*;

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
	 *            - The Library file path.
	 *            - Supports -print-ast command 
	 */
	private static String ICFileParsed;
	
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Error: No input IC file.");
			System.exit(1);
		}
		if(args.length == 1){
			//only parse the ic file
			Object root = ParseICFile(args[0]);
			BuildSymbolTables((Program)root, args[0]);
		}else{
			//check which arguments are entered
			Object root = ParseICFile(args[0]);
			Object libraryClass = null;
			//check if the library needs to be parsed
			for(int i=1;i<args.length;i++){
				if(args[i].startsWith("-L")){
					libraryClass = ParseLibraryFile(args[i].substring(2));
					AddLibraryToRoot(root,libraryClass);
					break;
				}
			}
			//check if needs to be printed
			for(int i=1;i<args.length;i++){
				if(args[i].compareTo("-print-ast") == 0){
					LabelAST(root, 0);
					PrintASTCommand(root);
					//GraphvizAST((Program)root);
					break;
				}
			}
			BuildSymbolTables((Program)root, args[0]);
		}		
	}

	public static String getICFileParsed(){
		return Compiler.ICFileParsed;
	}
	
	/**
	 * 
	 * @param root - the root of the ast tree
	 * creates a file out.txt that can be viewed in graphviz program
	 */
	private static void GraphvizAST(Program root){
		try{
			FileWriter fstream = new FileWriter("out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("digraph G{");
			out.write("\r\n");
			GraphEdgesPrinter edges = new GraphEdgesPrinter();
			out.write(edges.visit(root).toString().replace("\n", "\r\n"));
			out.write("\r\n");
			GrpahNodesPrinter nodes = new GrpahNodesPrinter();
			out.write(nodes.visit(root).toString().replace("\n", "\r\n"));
			out.write("\r\n");
			out.write("}");
			//Close the output stream
			out.close();
		}catch(Exception e){
			
		}	
	}
	
	/**
	 * 
	 * @param root
	 * creates a file out.txt that can be viewed in graphviz program
	 */
	private static void GraphvizLibraryAST(ICClass root){
		try{
			FileWriter fstream = new FileWriter("out.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("digraph G{");
			out.write("\r\n");
			GraphEdgesPrinter edges = new GraphEdgesPrinter();
			out.write(edges.visit(root).toString().replace("\n", "\r\n"));
			out.write("\r\n");
			GrpahNodesPrinter nodes = new GrpahNodesPrinter();
			out.write(nodes.visit(root).toString().replace("\n", "\r\n"));
			out.write("\r\n");
			out.write("}");
			//Close the output stream
			out.close();
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 
	 * @param root - the root of the ast tree
	 * @param startFrom - a number to start the ids of the nodes from
	 */
	private static void LabelAST(Object root, int startFrom){
		Labeling l = new Labeling();
		l.visit((Program)root, startFrom);
	}
	
	/**
	 * @param root - the root of the ast tree
	 * Prints the ast tree, needs to run after LabelAst(root,someNumber)
	 */
	private static void PrintASTCommand(Object root){
		try{
			TreePrinter treePrinter = new TreePrinter();
			String[] out = ((String)treePrinter.visit((Program)root)).split("\n");
			for(int i=out.length-1;i>=0;i--){
				System.out.println(out[i]);
			}
		}catch(ClassCastException e){
			System.out.println("Error printing program: " + e.getMessage());
		}catch(NullPointerException e1){
			System.out.println("Null Pointer in PrintASTCommand");
		}catch(Exception e2){
			System.out.println("Error while printing: " + e2.getMessage());
		}
	}
	
	/**
	 * Adds the Library node to the root Program node
	 * @param root
	 * @param lib
	 */
	private static void AddLibraryToRoot(Object root, Object lib){
		try{
			((Program)root).getClasses().add((ICClass)lib);
		}catch(ClassCastException e){
			System.out.println("Error adding Library as class to the root program: " + e.getMessage());
		}catch(NullPointerException e1){
			System.out.println(e1.getMessage());
		}catch(Exception e2){
			System.out.println(e2.getMessage());
		}
	}
	
	/**
	 * Parses the library file and outputs the correct user message 
	 * @param libPath - full path to library file
	 * @return Returns the root node of the ast
	 */

	private static Object ParseLibraryFile(String libPath){
		try{ 
			File f = new File(libPath);
			FileReader fr = null;
			//Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			LibraryParser libParser = new LibraryParser(lex);
			libParser.printTokens = false;
			Symbol parsedSymbol = libParser.parse();
			System.out.println("Successfully parsed library file " +libPath);
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(libPath + " " + e3.getMessage());
			System.exit(1);
		}catch (LexicalError e1){
			System.out.println(libPath + " " +e1.getMessage());
			System.exit(1);
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + libPath + " doesn't exist.");
			System.exit(1);
		} catch (Exception e2) {
			System.out.println(libPath + " " + e2.getMessage());
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * Parses the ic file and outputs the correct user message
	 * @param filePath - full path to ic file to parse
	 * @return the root node of the ast or null if there is an error
	 */
	private static Object ParseICFile(String filePath){
		try{ 
			File f = new File(filePath);
			FileReader fr = null;
			//Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			Parser parser = new Parser(lex);
			parser.printTokens = false;
			Symbol parsedSymbol = parser.parse();
			System.out.println("Successfully parsed " +filePath);
			Compiler.ICFileParsed = filePath;
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(filePath + " " + e3.getMessage());
			System.exit(1);
		}catch (LexicalError e1){
			System.out.println(filePath + " " + e1.getMessage());
			System.exit(1);	
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + filePath + " doesn't exist.");
			System.exit(1);
		} catch (Exception e2) {
			System.out.println(filePath + " " + "Error while parsing: " + e2.getMessage());
			System.exit(1);
		}
		return null;
	}
	
	private static Boolean BuildSymbolTables(Program root, String filePath){
		SemanticAnalyse sa = SemanticAnalyse.getInstance();
		sa.setRoot(root);
		sa.analyze();
		SymbolTablePrinter tablePrinter = new SymbolTablePrinter(Compiler.ICFileParsed,root);
		//TypeTablePrinter typePrinter = new TypeTablePrinter(
		//		SymbolTable.getUsedType(), (new File(filePath).getName()));
		//System.out.println(typePrinter);
		System.out.println(tablePrinter);
		return true;
		
	}
}