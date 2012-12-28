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
		int id = (Integer)context;
		id = (Integer) field.getType().accept(this,id);
		field.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(VirtualMethod method, Object context) {
		int id = (Integer)context;
		id = (Integer) method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id = (Integer) f.accept(this,id);
		}
		for(Statement s : method.getStatements()){
			id = (Integer) s.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(StaticMethod method, Object context) {
		int id = (Integer)context;
		id = (Integer) method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id = (Integer) f.accept(this,id);
		}
		for(Statement s : method.getStatements()){
			id = (Integer) s.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
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
		int id = (Integer)context;
		id = (Integer)assignment.getVariable().accept(this,id);
		id = (Integer)assignment.getAssignment().accept(this,id);
		assignment.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(CallStatement callStatement, Object context) {
		int id = (Integer)context;
		id = (Integer) callStatement.getCall().accept(this,id);
		callStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Return returnStatement, Object context) {
		int id = (Integer)context;
		if(returnStatement.hasValue()){
			id = (Integer) returnStatement.getValue().accept(this,id);
		}
		returnStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(If ifStatement, Object context) {
		int id = (Integer)context;
		id = (Integer) ifStatement.getCondition().accept(this,id);
		if(ifStatement.hasElse()){
			id = (Integer) ifStatement.getElseOperation().accept(this,id);
		}
		id = (Integer) ifStatement.getOperation().accept(this,id);
		ifStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(While whileStatement, Object context) {
		int id = (Integer)context;
		id = (Integer) whileStatement.getCondition().accept(this,id);
		id = (Integer) whileStatement.getOperation().accept(this,id);
		whileStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Break breakStatement, Object context) {
		int id = (Integer)context;
		breakStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Continue continueStatement, Object context) {
		int id = (Integer)context;
		continueStatement.setID(id+1);
		return id+1;	
	}

	@Override
	public Object visit(StatementsBlock statementsBlock, Object context) {
		int id = (Integer)context;
		for(Statement s : statementsBlock.getStatements()){
			id = (Integer) s.accept(this,id);
		}
		statementsBlock.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(LocalVariable localVariable, Object context) {
		int id = (Integer)context;
		id = (Integer) localVariable.getType().accept(this,id);
		if(localVariable.hasInitValue()){
			id = (Integer) localVariable.getInitValue().accept(this,id);
		}
		localVariable.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(VariableLocation location, Object context) {
		int id = (Integer)context;
		if(location.isExternal()){
			id = (Integer) location.getLocation().accept(this,id);
		}
		location.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(ArrayLocation location, Object context) {
		int id = (Integer)context;
		id = (Integer) location.getArray().accept(this,id);
		id = (Integer) location.getIndex().accept(this,id);
		location.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(StaticCall call, Object context) {
		int id = (Integer)context;
		for(Expression e : call.getArguments()){
			id = (Integer) e.accept(this,id);
		}
		call.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(VirtualCall call, Object context) {
		int id = (Integer)context;
		if(call.isExternal()){
			id = (Integer) call.getLocation().accept(this,id);
		}
		for(Expression e : call.getArguments()){
			id = (Integer) e.accept(this,id);
		}
		call.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(This thisExpression, Object context) {
		int id = (Integer)context;
		thisExpression.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(NewClass newClass, Object context) {
		int id = (Integer)context;
		newClass.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(NewArray newArray, Object context) {
		int id = (Integer)context;
		id = (Integer) newArray.getType().accept(this,id);
		id = (Integer) newArray.getSize().accept(this,id);
		newArray.setID(id+1);
		return id+1;	
	}

	@Override
	public Object visit(Length length, Object context) {
		int id = (Integer)context;
		id = (Integer) length.getArray().accept(this,id);
		length.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp, Object context) {
		int id = (Integer)context;
		id = (Integer) binaryOp.getFirstOperand().accept(this,id);
		id = (Integer) binaryOp.getSecondOperand().accept(this,id);
		binaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp, Object context) {
		int id = (Integer)context;
		id = (Integer) binaryOp.getFirstOperand().accept(this,id);
		id = (Integer) binaryOp.getSecondOperand().accept(this,id);
		binaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp, Object context) {
		int id = (Integer)context;
		id = (Integer) unaryOp.getOperand().accept(this,id);
		unaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp, Object context) {
		int id = (Integer)context;
		id = (Integer) unaryOp.getOperand().accept(this,id);
		unaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(Literal literal, Object context) {
		int id = (Integer)context;
		literal.setID(id+1);
		return id+1;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock, Object context) {
		int id = (Integer)context;
		id = (Integer) expressionBlock.getExpression().accept(this,id);
		expressionBlock.setID(id+1);
		return id+1;
	}

}
