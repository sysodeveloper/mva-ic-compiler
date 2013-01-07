package IC.AST;

import IC.DataTypes;

/**
 * Primitive data type AST node.
 * 
 * @author Tovi Almozlino
 */
public class PrimitiveType extends Type {

	private DataTypes type;

	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new primitive data type node.
	 * 
	 * @param line
	 *            Line number of type declaration.
	 * @param type
	 *            Specific primitive data type.
	 */
	public PrimitiveType(int line, DataTypes type) {
		super(line);
		this.type = type;
	}

	public String getName() {
		return (type != null)?type.getDescription():"null";
	}
	
	/**
	 * Clone the object.
	 */
	@Override
	public Object clone() {
		return new PrimitiveType(line, type);
	}

	@Override
	public int compareTo(Type o) {
		// TODO Auto-generated method stub
		if(o.equals(new PrimitiveType(o.getLine(), null)))
			return 1;
		if(this.equals(o))
			return 0;
		return -1;
	}
}