package IC.AST;

public class Labeling implements PropagatingVisitor<Integer, Integer>{

	@Override
	public Integer visit(Program program, Integer id) {
		for(ICClass c : program.getClasses()){
			id =  c.accept(this,id);
		}
		program.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(ICClass icClass, Integer id) {
		for(Field f : icClass.getFields()){
			id = f.accept(this,id);
		}
		for(Method m : icClass.getMethods()){
			id = m.accept(this,id);
		}
		icClass.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Field field, Integer id) {
		id =  field.getType().accept(this,id);
		field.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(VirtualMethod method, Integer id) {
		id =  method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id =  f.accept(this,id);
		}
		for(Statement s : method.getStatements()){
			id =  s.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(StaticMethod method, Integer id) {
		id =  method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id =  f.accept(this,id);
		}
		for(Statement s : method.getStatements()){
			id =  s.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(LibraryMethod method, Integer id) {
		id =  method.getType().accept(this,id);
		for(Formal f : method.getFormals()){
			id =  f.accept(this,id);
		}
		method.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Formal formal, Integer id) {
		id = formal.getType().accept(this,id);
		formal.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(PrimitiveType type, Integer id) {
		type.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(UserType type, Integer id) {
		type.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Assignment assignment, Integer id) {
		id = assignment.getVariable().accept(this,id);
		id = assignment.getAssignment().accept(this,id);
		assignment.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(CallStatement callStatement, Integer id) {
		id =  callStatement.getCall().accept(this,id);
		callStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Return returnStatement, Integer id) {
		if(returnStatement.hasValue()){
			id =  returnStatement.getValue().accept(this,id);
		}
		returnStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(If ifStatement, Integer id) {
		id =  ifStatement.getCondition().accept(this,id);
		if(ifStatement.hasElse()){
			id =  ifStatement.getElseOperation().accept(this,id);
		}
		id =  ifStatement.getOperation().accept(this,id);
		ifStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(While whileStatement, Integer id) {
		id =  whileStatement.getCondition().accept(this,id);
		id =  whileStatement.getOperation().accept(this,id);
		whileStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Break breakStatement, Integer id) {
		breakStatement.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Continue continueStatement, Integer id) {
		continueStatement.setID(id+1);
		return id+1;	
	}

	@Override
	public Integer visit(StatementsBlock statementsBlock, Integer id) {
		for(Statement s : statementsBlock.getStatements()){
			id =  s.accept(this,id);
		}
		statementsBlock.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(LocalVariable localVariable, Integer id) {
		id =  localVariable.getType().accept(this,id);
		if(localVariable.hasInitValue()){
			id =  localVariable.getInitValue().accept(this,id);
		}
		localVariable.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(VariableLocation location, Integer id) {
		if(location.isExternal()){
			id =  location.getLocation().accept(this,id);
		}
		location.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(ArrayLocation location, Integer id) {
		id =  location.getArray().accept(this,id);
		id =  location.getIndex().accept(this,id);
		location.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(StaticCall call, Integer id) {
		for(Expression e : call.getArguments()){
			id =  e.accept(this,id);
		}
		call.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(VirtualCall call, Integer id) {
		if(call.isExternal()){
			id =  call.getLocation().accept(this,id);
		}
		for(Expression e : call.getArguments()){
			id =  e.accept(this,id);
		}
		call.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(This thisExpression, Integer id) {
		thisExpression.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(NewClass newClass, Integer id) {
		newClass.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(NewArray newArray, Integer id) {
		id =  newArray.getType().accept(this,id);
		id =  newArray.getSize().accept(this,id);
		newArray.setID(id+1);
		return id+1;	
	}

	@Override
	public Integer visit(Length length, Integer id) {
		id =  length.getArray().accept(this,id);
		length.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(MathBinaryOp binaryOp, Integer id) {
		id =  binaryOp.getFirstOperand().accept(this,id);
		id =  binaryOp.getSecondOperand().accept(this,id);
		binaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(LogicalBinaryOp binaryOp, Integer id) {
		id =  binaryOp.getFirstOperand().accept(this,id);
		id =  binaryOp.getSecondOperand().accept(this,id);
		binaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(MathUnaryOp unaryOp, Integer id) {
		id =  unaryOp.getOperand().accept(this,id);
		unaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(LogicalUnaryOp unaryOp, Integer id) {
		id =  unaryOp.getOperand().accept(this,id);
		unaryOp.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(Literal literal, Integer id) {
		literal.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(ExpressionBlock expressionBlock, Integer id) {
		id =  expressionBlock.getExpression().accept(this,id);
		expressionBlock.setID(id+1);
		return id+1;
	}

	@Override
	public Integer visit(MethodType methodType, Integer id) {
		methodType.setID(id);
		return id + 1;
	}

}
