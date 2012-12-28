package IC.AST;

/**
 * While statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class While extends Statement {

	private Expression condition;

	private Statement operation;

	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a While statement node.
	 * 
	 * @param condition
	 *            Condition of the While statement.
	 * @param operation
	 *            Operation to perform while condition is true.
	 */
	public While(Expression condition, Statement operation) {
		super(condition.getLine());
		this.condition = condition;
		this.operation = operation;
	}
	
	public Expression getCondition() {
		return condition;
	}

	public Statement getOperation() {
		return operation;
	}

}
