package IC.AST;

import IC.MySymbolRecord;
import IC.MySymbolTable;
import IC.SymbolTable;

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
	
	
	private SymbolTable m_OuterTable;
	
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
		setOuterTable(null);
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

	/**
	 * @return the m_OuterTable
	 */
	public SymbolTable getOuterTable() {
		return m_OuterTable;
	}

	/**
	 * @param m_OuterTable the m_OuterTable to set
	 */
	public void setOuterTable(SymbolTable m_OuterTable) {
		this.m_OuterTable = m_OuterTable;
	}

}