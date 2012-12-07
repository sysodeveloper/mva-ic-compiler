package IC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java_cup.runtime.Symbol;
import IC.AST.GraphEdgesPrinter;
import IC.AST.Graphviz;
import IC.AST.GrpahNodesPrinter;
import IC.AST.ICClass;
import IC.AST.Labeling;
import IC.AST.LibraryMethod;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.AST.TreePrinter;
import IC.Parser.*;
import JFlex.RegExp;

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
		if(args.length == 1){
			/////////////////////////////////////////
			/////////////////DEBUG//////////////////
			///////////////////////////////////////
/*			ICClass root = (ICClass) ParseLibraryFile(args[0]);
			Labeling l = new Labeling();
			l.visit(root, 0);
			TreePrinter tp = new TreePrinter();
			System.out.println(tp.visit(root));
			GraphvizLibraryAST(root);
*/
			//only ic file to parse
			ParseICFile(args[0]);
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
					break;
				}
			}
		}		
	}

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
	
	private static void LabelAST(Object root, int startFrom){
		Labeling l = new Labeling();
		l.visit((Program)root, startFrom);
	}
	
	//Prints the ast according to root node 
	private static void PrintASTCommand(Object root){
		try{
			TreePrinter treePrinter = new TreePrinter();
			treePrinter.visit((Program)root);
		}catch(ClassCastException e){
			System.out.println("Error printing program: " + e.getMessage());
		}catch(NullPointerException e1){
			System.out.println(e1.getMessage());
		}catch(Exception e2){
			e2.printStackTrace();
		}
	}
	
	//Adds the Library node to the root Program node
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
	
	//Parses the library file and outputs the correct user message
	//Returns the root node of the ast
	private static Object ParseLibraryFile(String libPath){
		try{ 
			File f = new File(libPath);
			FileReader fr = null;
			//Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			LibraryParser libParser = new LibraryParser(lex);
			libParser.printTokens = true;
			Symbol parsedSymbol = libParser.parse();
			System.out.println("Successfully parsed library file " +libPath);
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(e3.getMessage());
			System.exit(1);
		}catch (LexicalError e1){
			System.out.println(e1.getMessage());
			System.exit(1);
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + libPath + " doesn't exist.");
			System.exit(1);
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
		}
		return null;
	}
	
	//Parses the ic file and outputs the correct user message
	//Returns the root node of the ast
	private static Object ParseICFile(String filePath){
		try{ 
			File f = new File(filePath);
			FileReader fr = null;
			//Lexer
			fr = new FileReader(f);
			Lexer lex = new Lexer(fr);
			Parser parser = new Parser(lex);
			parser.printTokens = true;
			Symbol parsedSymbol = parser.parse();
			System.out.println("Successfully parsed " +filePath);
			return parsedSymbol.value;
		}catch (SyntaxError e3){
			System.out.println(e3.getMessage());
			System.exit(1);
		}catch (LexicalError e1){
			System.out.println(e1.getMessage());
			System.exit(1);
		}catch (FileNotFoundException e) {
			System.out.println("Error: The file " + filePath + " doesn't exist.");
			System.exit(1);
		} catch (Exception e2) {
			System.out.println("Error while parsing: " + e2.getMessage());
		}
		return null;
	}
	
}