package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Abstract base class for method AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Method extends ASTNode implements SymbolTableContainer {

	protected Type type;

	protected String name;

	protected List<Formal> formals;

	protected List<Statement> statements;
	
	/** The symbol table of this method.
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
		setInnerTable(new SymbolTable(SymbolTable.getNextId()));
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