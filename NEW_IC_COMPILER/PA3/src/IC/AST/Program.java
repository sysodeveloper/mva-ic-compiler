package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Root AST node for an IC program.
 * 
 * @author Tovi Almozlino
 */
public class Program extends ASTNode {

	private List<ICClass> classes;

	/** The symbol table of this method.
	 */
	private SymbolTable m_InnerTable;
	
	/**
	 * @return The table.
	 */
	public SymbolTable getInnerTable() {
		return m_InnerTable;
	}

	
	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	public void setInnerTable(SymbolTable table) {
		m_InnerTable = table;		
	}
	
	/**
	 * Constructs a new program node.
	 * 
	 * @param classes
	 *            List of all classes declared in the program.
	 */
	public Program(List<ICClass> classes) {
		super(0);
		setInnerTable(new SymbolTable(SymbolTable.getNextId()));
		this.classes = classes;
	}

	public List<ICClass> getClasses() {
		return classes;
	}

}
