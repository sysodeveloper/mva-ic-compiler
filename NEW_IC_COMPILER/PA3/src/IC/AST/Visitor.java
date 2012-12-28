package IC.AST;

/**
 * AST visitor interface. Declares methods for visiting each type of AST node.
 * 
 * @author Tovi Almozlino
 */
public interface Visitor<UpType> {

	public UpType visit(Program program);

	public UpType visit(ICClass icClass);

	public UpType visit(Field field);

	public UpType visit(VirtualMethod method);

	public UpType visit(StaticMethod method);

	public UpType visit(LibraryMethod method);

	public UpType visit(Formal formal);

	public UpType visit(PrimitiveType type);

	public UpType visit(UserType type);

	public UpType visit(Assignment assignment);

	public UpType visit(CallStatement callStatement);

	public UpType visit(Return returnStatement);

	public UpType visit(If ifStatement);

	public UpType visit(While whileStatement);

	public UpType visit(Break breakStatement);

	public UpType visit(Continue continueStatement);

	public UpType visit(StatementsBlock statementsBlock);

	public UpType visit(LocalVariable localVariable);

	public UpType visit(VariableLocation location);

	public UpType visit(ArrayLocation location);

	public UpType visit(StaticCall call);

	public UpType visit(VirtualCall call);

	public UpType visit(This thisExpression);

	public UpType visit(NewClass newClass);

	public UpType visit(NewArray newArray);

	public UpType visit(Length length);

	public UpType visit(MathBinaryOp binaryOp);

	public UpType visit(LogicalBinaryOp binaryOp);

	public UpType visit(MathUnaryOp unaryOp);

	public UpType visit(LogicalUnaryOp unaryOp);

	public UpType visit(Literal literal);

	public UpType visit(ExpressionBlock expressionBlock);
	
}
