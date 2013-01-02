package IC;

import java.util.Map;

import IC.SymbolRecord.Kind;
import IC.SymbolTable;
import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.ICClass;
import IC.AST.INameable;
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
		printTable(SymbolTable.getRoot());
		
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
			name = ": " + icFile;
		}
		else if(table.getParentNode() instanceof ICClass) {
			name = ": " + ((ICClass)table.getParentNode()).getName();
		}
		else if(table.getParentNode() instanceof Method) {
			name = ": " + ((Method)table.getParentNode()).getName();
		}
		else if(table.getParentNode() instanceof StatementsBlock) {
			name = " ( located" + getStatementName(table) + " )";
		}
		
		return name;
	}

	/**
	 * Get the statement block name of the current nested Symbol table.
	 * @param table The statement block table.
	 * @return The string name.
	 */
	private String getStatementName(SymbolTable table) {
		String name;
		StringBuilder located = new StringBuilder();
		SymbolTableContainer c = (SymbolTableContainer)table.getParentNode();
		c = ((StatementsBlock) c).getOuterTable().getParentNode();
		while(!(c instanceof Method)) {
			if(c instanceof StatementsBlock) {
				located.append(" in statement block");
				c = ((StatementsBlock) c).getOuterTable().getParentNode();
			}
		}		
		located.append(" in " + ((Method)c).getName());
		name = located.toString();
		return name;
	}
	
	/**
	 * Print the current table.
	 * @param table The table to print.
	 */
	private void printTable(SymbolTable table) {
		output.append("\n" + typeName(table) + " Symbol Table" + tableName(table) + "\n");
		SymbolTableContainer parent = table.getParentNode();
		
		if(parent instanceof Program) {
			for (SymbolRecord record : table.getEntries().values()) {
				output.append("    Class: " + 
						((INameable)record.getNode()).getName() + "\n");
			}
		}
		else if(parent instanceof ICClass) {
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.FIELD) {
					output.append("    Field: " + record.getType().getFullName()
							+ " " + ((INameable)record.getNode()).getName() + "\n");
				}
			}
			for (SymbolRecord record : ((ICClass)table.getParentNode()).getStaticTable().getEntries().values()) {
				if(record.getKind() == Kind.STATIC_METHOD) {
					output.append("    Static method: " 
				            + ((INameable)record.getNode()).getName() 
							+ " " + record.getType().getName() + "\n");
				}
			}
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.VIRTUAL_METHOD) {
					output.append("    Virtual method: " 
							+ ((INameable)record.getNode()).getName() 
							+ " " + record.getType().getName() + "\n");
				}
			}
			for (SymbolRecord record : ((ICClass)table.getParentNode()).getStaticTable().getEntries().values()) {
				if(record.getKind() == Kind.LIBRARY_METHOD) {
					output.append("    Static method: " 
							+ ((INameable)record.getNode()).getName() 
							+ " " + record.getType().getName() + "\n");
				}
			}
		}
		else if(parent instanceof Method) {
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.PARAMETER) {
					output.append("    Parameter: " + record.getType().getFullName()
							+ " " + ((INameable)record.getNode()).getName() + "\n");
				}
			}
			
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.LOCAL_VARIABLE) {
					output.append("    Local variable: " + record.getType().getFullName()
							+ " " + ((INameable)record.getNode()).getName() + "\n");
				}
			}
		}
		else if(parent instanceof StatementsBlock) {
			for (SymbolRecord record : table.getEntries().values()) {
				if(record.getKind() == Kind.LOCAL_VARIABLE) {
					output.append("    Local variable: " + record.getType().getFullName()
							+ " " + ((INameable)record.getNode()).getName() + "\n");
				}
			}
		}
		if(!table.getChildTables().isEmpty()) {
			printChildren(table);
		}
		
		for (SymbolTable t : table.getChildTables()) {
			printTable(t);
		}
	}

	/**
	 * Print the children of the current table.
	 * @param table The table which contains the children.
	 */
	private void printChildren(SymbolTable table) {
		boolean first = true;
		output.append("Children tables: ");
		for (SymbolTable t : table.getChildTables()) {
			if(!first) {
				output.append(", ");
			}
			
			if(t.getParentNode() instanceof INameable) {
				output.append(((INameable)t.getParentNode()).getName());
			}
			else // Statement block.
			{
				output.append("statement block" + getStatementName(t));
			}
			first = false;
		}
		output.append("\n");
	}
}