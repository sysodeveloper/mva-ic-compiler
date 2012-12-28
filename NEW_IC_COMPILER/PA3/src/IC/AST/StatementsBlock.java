package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Statements block AST node.
 * 
 * @author Tovi Almozlino
 */
public class StatementsBlock extends Statement implements SymbolTableContainer {

	private List<Statement> statements;
	
	/** The symbol table of this block.
	 */
	private SymbolTable m_InnerTable;
	
	/**
	 * @return The table.
	 */
	public SymbolTable getInnerTable() {
		return m_InnerTable;
	}

	/**
	 * @param table The table to set.
	 */
	public void setInnerTable(SymbolTable table) {
		m_InnerTable = table;		
	}

	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new statements block node.
	 * 
	 * @param line
	 *            Line number where block begins.
	 * @param statements
	 *            List of all statements in block.
	 */
	public StatementsBlock(int line, List<Statement> statements) {
		super(line);
		setInnerTable(new SymbolTable(SymbolTable.getNextId()));
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
