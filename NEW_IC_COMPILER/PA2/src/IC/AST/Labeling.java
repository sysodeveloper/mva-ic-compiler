package IC.AST;

public class Labeling implements PropagatingVisitor{

	@Override
	public Object visit(Program program, Object context) {
		int id = (Integer)context;
		for(ICClass c : program.getClasses()){
			id = (Integer) c.accept(this,id);
		}
		program.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(ICClass icClass, Object context) {
		int id = (Integer)context;
		for(Field f : icClass.getFields()){
			id = (Integer)f.accept(this,id);
		}
		for(Method m : icClass.getMethods()){
			id = (Integer)m.accept(this,id);
		}
		icClass.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Field field, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualMethod method, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticMethod method, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LibraryMethod method, Object context) {
		int id = (Integer)context;
		id = (Integer) method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id = (Integer) f.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Formal formal, Object context) {
		int id = (Integer)context;
		id = (Integer)formal.getType().accept(this,id);
		formal.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(PrimitiveType type, Object context) {
		int id = (Integer)context;
		type.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(UserType type, Object context) {
		int id = (Integer)context;
		type.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Assignment assignment, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VariableLocation location, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayLocation location, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewArray newArray, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock, Object context) {
		// TODO Auto-generated method stub
		return null;
	}

}
