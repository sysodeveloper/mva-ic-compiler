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
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.AST.TreePrinter;
import IC.Parser.*;
import IC.mySymbolTable.BuildMySymbolTable;
import IC.mySymbolTable.MySymbolTablePrinter;
import IC.myTypes.MyTypeTable;
import IC.semanticChecks.MySemanticAnalyzer;
import IC.semanticChecks.MyTypeBuilder;
import IC.semanticChecks.ReturnCheck;

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
	private static boolean libraryExists = false;
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Error: No input IC file.");
		}
		//check which arguments are entered
		Object root = ParseICFile(args[0]);
		if(root == null){
			return;
		}
		Object libraryClass = null;
		//check if the library needs to be parsed
		boolean foundlibrary = false;
		for(int i=1;i<args.length;i++){
			if(args[i].startsWith("-L")){
				libraryClass = ParseLibraryFile(args[i].substring(2));
				if(libraryClass == null && libraryExists) return;
				if(libraryExists && libraryClass != null){
					AddLibraryToRoot(root,libraryClass);
					foundlibrary = true;
				}
				break;
			}
		}
		//find library file in the current directory
		if(!foundlibrary){
			String fileIC = args[0];
			String sep = File.separator;
			fileIC = fileIC.substring(0,fileIC.lastIndexOf(sep));
			String defLib = fileIC + sep + "libic.sig";
			libraryClass = ParseLibraryFile(defLib);
			if(libraryClass == null && libraryExists){
				return;
			}else if(libraryClass != null && libraryExists){
				AddLibraryToRoot(root,libraryClass);
			}
		}
		//check if needs to be printed
		for(int i=1;i<args.length;i++){
			if(args[i].compareTo("-print-ast") == 0){
				LabelAST(root, 0);
				PrintASTCommand(root);
				GraphvizAST((Program)root);
				break;
			}
		}
		System.out.println();
		//handle symbol and type tables
		MyTypeTable types = null;
		try{
			types = BuildSymbolTables((Program) root, getICFileParsed());
			if(types == null) return;
			boolean semanticPass = SemanticChecks((Program) root);
			if(semanticPass){
				boolean typesPass = TypeChecks((Program) root, types);
				if(!typesPass){
					return;
				}
			}else{
				return;
			}
		}catch(Exception exp){
			
		}
		//check if needs to dump symbol table and type table.
		if(types == null) return;
		for(int i = 1; i < args.length; ++i){
			if(args[i].compareTo("-dump-symtab") == 0){
				dumpTable((Program) root,types);
				break;
			}
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
		try{
			Labeling l = new Labeling();
			l.visit((Program)root, startFrom);
		}catch(Exception exp){
			System.out.println("Error lanbeling ast " + exp.getMessage());
		}
	}
	
	/**
	 * @param root - the root of the ast tree
	 * Prints the ast tree, needs to run after LabelAst(root,someNumber)
	 */
	private static void PrintASTCommand(Object root){
		try{
			PrettyPrinter printer = new PrettyPrinter(ICFileParsed);
			System.out.println(printer.visit((Program)root));
		}catch(Exception exp){
			System.out.println("Error printing ast " + exp.getMessage());
		}
	}
	
	/**
	 * Adds the Library node to the root Program node
	 * @param root
	 * @param lib
	 */
	private static void AddLibraryToRoot(Object root, Object lib){
		try{
			((Program)root).getClasses().add(0, (ICClass)lib);
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
			libraryExists = true;
			File f = new File(libPath);
			FileReader fr = null;
			//Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			LibraryParser libParser = new LibraryParser(lex);
			//libParser.printTokens = false;
			Symbol parsedSymbol = libParser.parse();
			System.out.println("Parsed " +libPath + " successfully!");
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(libPath + " " + e3.getMessage());
		}catch (LexicalError e1){
			System.out.println(libPath + " " +e1.getMessage());
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + libPath + " doesn't exist.");
			libraryExists = false;
		} catch (Exception e2) {
			System.out.println(libPath + " " + e2.getMessage());
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
			System.out.println("Parsed " +filePath + " successfully!");
			Compiler.ICFileParsed = filePath;
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(filePath + " " + e3.getMessage());
		}catch (LexicalError e1){
			System.out.println(filePath + " " + e1.getMessage());	
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + filePath + " doesn't exist.");
		} catch (Exception e2) {
			System.out.println(filePath + " " + "Error while parsing: " + e2.getMessage());
		}
		return null;
	}
	
	private static void PrintSymbolTables(Program root){
		try{
			MySymbolTablePrinter printer = new MySymbolTablePrinter();
			System.out.println(printer.visit(root));
		}catch(Exception exp){
			
		}
	}
	
	private static void PrintTypeTable(Program root, MyTypeTable types){
		try{
			types.printTypeTable();
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
	
	private static boolean TypeChecks(Program root, MyTypeTable types){
		MyTypeBuilder typeBuilder = new MyTypeBuilder(types);
		try{
			typeBuilder.visit(root, null);
			if(typeBuilder.hasErrors()){
				typeBuilder.printErrorStack();
				return false;
			}
			ReturnCheck returnChecker = new ReturnCheck();
			Boolean correctReturns = returnChecker.visit(root);
			if(!correctReturns){
				returnChecker.printErrorStack();
				return false;
			}
		}catch(Exception exp){
			typeBuilder.printErrorStack();
			return false;
		}
		return true;
	}
	
	private static boolean SemanticChecks(Program root){
		boolean analyze = false;
		MySemanticAnalyzer analyzer = new MySemanticAnalyzer();
		try{
			analyze = analyzer.visit(root,null);
			if(!analyze){
				analyzer.printErrorStack();
				return false;
			}
		}catch(Exception exp){
			analyzer.printErrorStack();
			return false;
		}
		return analyze;
	}
	
	private static MyTypeTable BuildSymbolTables(Program root, String filePath){
		BuildMySymbolTable buider = new BuildMySymbolTable();
		boolean success = buider.visit(root, null);
		if(!success){
			buider.printErrorStack();
			return null;
		}			
		return buider.getTypeTable();
	}
	
	/**
	 * Dump the symbol and type table.
	 */
	private static void dumpTable(Program root, MyTypeTable types) {
		PrintSymbolTables(root);
		PrintTypeTable(root, types);
	}
}