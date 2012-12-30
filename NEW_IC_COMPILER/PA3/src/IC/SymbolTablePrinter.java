package IC;

import java.util.Map;

import IC.SymbolTable;
import IC.AST.ICClass;
import IC.AST.Program;

public class SymbolTablePrinter{
	private Program root;
	private String icFile;
	public SymbolTablePrinter(String icFile, Program root) {
		this.root = root;
		this.icFile = icFile;
	}
	
	public String toString(){
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		output.append("Global Symbol Table: ");
		output.append(icFile);
		output.append("\n");
		return output.toString();
	}
}