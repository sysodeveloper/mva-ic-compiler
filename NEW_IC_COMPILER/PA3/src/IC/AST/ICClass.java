package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Class declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class ICClass extends ASTNode implements SymbolTableContainer {

	private String name;

	private String superClassName = null;

	private List<Field> fields;

	private List<Method> methods;
	
	/** The symbol table of this class.
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

	@Override
	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new class node.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, List<Field> fields,
			List<Method> methods) {
		super(line);
		this.name = name;
		this.fields = fields;
		this.methods = methods;
		setInnerTable(new SymbolTable(SymbolTable.getNextId()));
	}

	/**
	 * Constructs a new class node, with a superclass.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param superClassName
	 *            Superclass identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, String superClassName,
			List<Field> fields, List<Method> methods) {
		this(line, name, fields, methods);
		this.superClassName = superClassName;
	}

	public String getName() {
		return name;
	}

	public boolean hasSuperClass() {
		return (superClassName != null);
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<Method> getMethods() {
		return methods;
	}

}