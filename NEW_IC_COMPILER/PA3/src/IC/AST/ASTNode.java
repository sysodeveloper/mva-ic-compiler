package IC.AST;

/**
 * Abstract AST node base class.
 * 
 * @author Tovi Almozlino
 */
public abstract class ASTNode {

	protected int line;
	private int id;
	
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