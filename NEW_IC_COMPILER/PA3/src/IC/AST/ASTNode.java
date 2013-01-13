package IC.AST;

import IC.*;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;

/**
 * Abstract AST node base class.
 * 
 * @author Tovi Almozlino
 */
public abstract class ASTNode {

	protected int line;
	private int id;
	
	private MySymbolTable enclosingScope;
	
	private MySymbolRecord record;
	
	private Type tableType;
	
	public Type getTypeFromTable() {
		return tableType;
	}

	public void setTypeFromTable(Type type) {
		this.tableType = type;
	}

	public MySymbolRecord getRecord() {
		return record;
	}

	public void setRecord(MySymbolRecord record) {
		this.record = record;
	}

	public MySymbolTable enclosingScope(){
		return this.enclosingScope;
	}
	
	public void setEnclosingScope(MySymbolTable scope){
		this.enclosingScope = scope;
	}
	
	
	
	/**
	 * Double dispatch method, to allow a visitor to visit a specific subclass.
	 * 
	 * @param visitor
	 *            The visitor.
	 * @return A value propagated by the visitor.
	 */
	public abstract <UpType> UpType accept(Visitor<UpType> visitor);
	
	/***
	 * Double dispatch method, to allow a visitor to visit a specific subclass, including holding 
	 * result object
	 * @param visitor
	 * 			The visitor.
	 * @param context
	 * 			The object to pass
	 * @return A value propagated by the visitor
	 */
	public abstract <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d);

	/**
	 * Constructs an AST node corresponding to a line number in the original
	 * code. Used by subclasses.
	 * 
	 * @param line
	 *            The line number.
	 */
	protected ASTNode(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}
	
	/***
	 * Each node cann be assigned an ID
	 * @return id of the node
	 */
	public int getID(){
		return id;
	}
	/**
	 * Sets the id of a node 
	 * @param id
	 */
	public void setID(int id){
		this.id = id;
	}
}