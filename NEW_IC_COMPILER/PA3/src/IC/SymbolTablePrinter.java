package IC;

import java.util.Map;

import sun.org.mozilla.javascript.ast.AstNode;

import IC.SymbolRecord.Kind;
import IC.SymbolTable;
import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.ICClass;
import IC.AST.Method;
import IC.AST.Program;
import IC.AST.StatementsBlock;
import IC.AST.SymbolTableContainer;

public class SymbolTablePrinter{
	private Program root;
	private String icFile;
	StringBuffer output;
	public SymbolTablePrinter(String icFile, Program root) {
		this.root = root;
		this.icFile = icFile;
		this.output = new StringBuffer();
	}
	
	public String toString(){
		output.append("Global Symbol Table: " + icFile + "\n");
		for (SymbolRecord record : SymbolTable.getRoot().getEntries().values()) {
			output.append("    Class: " + 
					((ICClass)record.getNode()).getName() + "\n");
		}
		
		output.append("Children tables: ");
		
		boolean first = true;
		for (SymbolTable table : SymbolTable.getRoot().getChildTables()) {
			if(!first) {
				output.append(", ");
			}
			output.append(((ICClass)table.getParentNode()).getName());
			first = false;
		}
		output.append("\n");
		
		return output.toString();
	}
	
	private String typeName(SymbolTable table) {
		String name = "";
		if(table.getParentNode() instanceof Program) {
			name = "Global";
		}
		else if(table.getParentNode() instanceof ICClass) {
			name = "Class";
		}
		else if(table.getParentNode() instanceof Method) {
			name = "Method";
		}
		else if(table.getParentNode() instanceof StatementsBlock) {
			name = "Statements Block";
		}
		
		return name;
	}
	
	private String tableName(SymbolTable table) {
		String name = "";
		if(table.getParentNode() instanceof Program) {
			name = icFile;
		}
		else if(table.getParentNode() instanceof ICClass) {
			name = ((ICClass)table.getParentNode()).getName();
		}
		else if(table.getParentNode() instanceof Method) {
			name = ((Method)table.getParentNode()).getName();
		}
		else if(table.getParentNode() instanceof StatementsBlock) {
			StringBuilder located = new StringBuilder("( located ");
			SymbolTableContainer c = (SymbolTableContainer)table.getParentNode();
			while(!(c instanceof Method)) {
				if(c instanceof StatementsBlock) {
					located.append("in statement block");
					c = ((StatementsBlock) c).getOuterTable().getParentNode();
				}
			}
			located.append("in " + ((Method)c).getName() + " )");
			name = located.toString();
		}
		
		return name;
	}
	
	private void printTable(SymbolTable table) {
		output.append(typeName(table) + " Symbol Table: " + tableName(table) + "\n");
		SymbolTableContainer parent = table.getParentNode();
		
		if(parent instanceof Program) {
			for (SymbolRecord record : table.getEntries().values()) {
				output.append("    Class: " + 
						((ICClass)record.getNode()).getName() + "\n");
			}
		}
		else if(parent instanceof ICClass) {
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.FIELD) {
					output.append("Field: " + record.getType().getName()
							+ " " + ((Field)record.getNode()).getName());
				}
			}
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.STATIC_METHOD) {
					output.append("Static method: " 
				            + ((Method)record.getNode()).getName() 
							+ " " + record.getType().getName());
				}
			}
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.VIRTUAL_METHOD) {
					output.append("Virtual method: " 
							+ ((Method)record.getNode()).getName() 
							+ " " + record.getType().getName());
				}
			}
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.VIRTUAL_METHOD) {
					output.append("Library method: " 
							+ ((Method)record.getNode()).getName() 
							+ " " + record.getType().getName());
				}
			}
		}
		else if(parent instanceof Method) {
			for (SymbolRecord record : table.getEntries().values()) {
				
			}
		}
		else if(parent instanceof StatementsBlock) {
			for (SymbolRecord record : table.getEntries().values()) {
				
			}
		}
		
		printChildren(table);
		/*
    Field: string str
    Field: int i
    Static method: sfunc {boolean -> int}
    Virtual method: vfunc {A, int, int -> void}
Children tables: sfunc, vfunc, B	    
*/
	}

	private void printChildren(SymbolTable table) {
		boolean first = true;
		output.append("Children tables: ");
		for (SymbolTable t : table.getChildTables()) {
			if(!first) {
				output.append(", ");
			}
			//output.append(((SymbolTableContainer)t.getParentNode()).getName());
			first = false;
		}
		output.append("\n");
	}
}