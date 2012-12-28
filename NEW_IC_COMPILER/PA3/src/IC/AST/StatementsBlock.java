package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Statements block AST node.
 * 
 * @author Tovi Almozlino
 */
public class StatementsBlock extends Statement {

	private List<Statement> statements;
	
	/** The symbol table of this block.
	 */
	private SymbolTable m_table;
	
	/**
	 * @return The table.
	 */
	public SymbolTable getTable() {
		return m_table;
	}

	/**
	 * @param table The table to set.
	 */
	public void setTable(SymbolTable table) {
		m_table = table;		
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
		setTable(new SymbolTable(SymbolTable.getNextId()));
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
