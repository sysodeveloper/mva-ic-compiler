package IC.AST;

import java.util.List;

import IC.SymbolTable;

/**
 * Root AST node for an IC program.
 * 
 * @author Tovi Almozlino
 */
public class Program extends ASTNode implements SymbolTableContainer {

	private List<ICClass> classes;
	
	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}
	/**
	 * Constructs a new program node.
	 * 
	 * @param classes
	 *            List of all classes declared in the program.
	 */
	public Program(List<ICClass> classes) {
		super(0);
		this.classes = classes;
	}

	public List<ICClass> getClasses() {
		return classes;
	}

	@Override
	public SymbolTable getInnerTable() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The global symbolTable.
	 */
	@Override
	public void setInnerTable(SymbolTable table) {
	}

}
