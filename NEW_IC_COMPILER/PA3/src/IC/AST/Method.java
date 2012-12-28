package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Abstract base class for method AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Method extends ASTNode {

	protected Type type;

	protected String name;

	protected List<Formal> formals;

	protected List<Statement> statements;
	
	/** The symbol table of this method.
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

	/**
	 * Constructs a new method node. Used by subclasses.
	 * 
	 * @param type
	 *            Data type returned by method.
	 * @param name
	 *            Name of method.
	 * @param formals
	 *            List of method parameters.
	 * @param statements
	 *            List of method's statements.
	 */
	protected Method(Type type, String name, List<Formal> formals,
			List<Statement> statements) {
		super(type.getLine());
		setTable(new SymbolTable(SymbolTable.getNextId()));
		this.type = type;
		this.name = name;
		this.formals = formals;
		this.statements = statements;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public List<Formal> getFormals() {
		return formals;
	}

	public List<Statement> getStatements() {
		return statements;
	}
}