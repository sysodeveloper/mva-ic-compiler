package IC.AST;

import IC.UnaryOps;

/**
 * Logical unary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class LogicalUnaryOp extends UnaryOp {

	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new logical unary operation node.
	 * 
	 * @param operator
	 *            The operator.
	 * @param operand
	 *            The operand.
	 */
	public LogicalUnaryOp(UnaryOps operator, Expression operand) {
		super(operator, operand);
	}

}
