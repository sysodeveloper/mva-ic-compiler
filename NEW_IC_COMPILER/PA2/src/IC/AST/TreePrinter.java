package IC.AST;

public class TreePrinter implements Visitor{
	private int depth = 0;
	
	private void deleteLastComma(StringBuffer children){
		if(children.length() >= 1){
			children.deleteCharAt(children.length()-1);
		}
	}
	
	private void indent(StringBuffer output, ASTNode node) {
		for (int i = 0; i < depth; ++i)
			output.append(" ");
	}

	private void indent(StringBuffer output) {
		indent(output, null);
	}
	
	@Override
	public Object visit(Program program) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
			children.append(c.getID()+",");
		}
		deleteLastComma(children);
		indent(output);
		output.append(program.getID() + " - Program");
		output.append(" -> {");
		output.append(children.toString());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Field f : icClass.getFields()){
			output.append(f.accept(this));
			children.append(f.getID()+",");
		}
		for(Method m : icClass.getMethods()){
			output.append(m.accept(this));
			children.append(m.getID()+",");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output, icClass);
		output.append(icClass.getID() + " - Class " + icClass.getName());
		if(icClass.hasSuperClass()){
			output.append(" super class " + icClass.getSuperClassName());
		}
		output.append(" -> {");
		output.append(children.toString());
		output.append("}");
		output.append("\n");
		return output.toString();
	}
	
	@Override
	public Object visit(Field field) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(field.getType().accept(this));
		--depth;
		output.append(field.getID() + " - Field " + field.getName());
		output.append(" -> {");
		output.append(field.getType().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(VirtualMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID());
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID() + ",");
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + ",");
		}
		deleteLastComma(children);
		depth -= 2;
		indent(output,method);
		output.append(method.getID() + " - VirtualMethod " + method.getName());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(StaticMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID());
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID() + ",");
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + ",");
		}
		deleteLastComma(children);
		depth -= 2;
		indent(output,method);
		output.append(method.getID() + " - StaticMethod " + method.getName());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(LibraryMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		++depth;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID()+",");
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID()+",");
		}
		--depth;
		deleteLastComma(children);
		indent(output, method);
		output.append(method.getID() + " - LibraryMethod " + method.getName());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Formal formal) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(formal.getType().accept(this));
		--depth;
		indent(output, formal);
		output.append(formal.getID() + " - Formal " + formal.getName());
		output.append(" -> {");
		output.append(formal.getType().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(PrimitiveType type) {
		StringBuffer output = new StringBuffer();
		indent(output, type);
		output.append(type.getID() + " - PrimitiveType " + type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(UserType type) {
		StringBuffer output = new StringBuffer();
		indent(output, type);
		output.append(type.getID() + " - UserType " + type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Assignment assignment) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(assignment.getVariable().accept(this));
		children.append(assignment.getVariable().getID()+",");
		output.append(assignment.getAssignment().accept(this));
		children.append(assignment.getAssignment().getID()+",");
		depth -= 2;
		deleteLastComma(children);
		indent(output,assignment);
		output.append(assignment.getID() + " - Assignment");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(CallStatement callStatement) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(callStatement.getCall().accept(this));
		--depth;
		indent(output,callStatement);
		output.append(callStatement.getID() + " - CallStatement");
		output.append(" -> {");
		output.append(callStatement.getCall().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Return returnStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		if(returnStatement.hasValue()){
			++depth;
			output.append(returnStatement.getValue().accept(this));
			children.append(returnStatement.getValue().getID()+",");
			--depth;
		}
		deleteLastComma(children);
		indent(output,returnStatement);
		output.append(returnStatement.getID() + " - ReturnStatement");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(If ifStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(ifStatement.getCondition().accept(this));
		children.append(ifStatement.getCondition().getID()+",");
		output.append(ifStatement.getOperation().accept(this));
		children.append(ifStatement.getOperation().getID()+",");
		if(ifStatement.hasElse()){
			output.append(ifStatement.getElseOperation().accept(this));
			children.append(ifStatement.getElseOperation().getID()+",");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,ifStatement);
		output.append(ifStatement.getID() + " - IfStatement");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(While whileStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(whileStatement.getCondition().accept(this));
		children.append(whileStatement.getCondition().getID()+",");
		output.append(whileStatement.getOperation().accept(this));
		children.append(whileStatement.getOperation().getID()+",");
		depth -= 2;
		deleteLastComma(children);
		indent(output,whileStatement);
		output.append(whileStatement.getID() + " - WhileStatement");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Break breakStatement) {
		StringBuffer output = new StringBuffer();
		indent(output, breakStatement);
		output.append(breakStatement.getID() + " - BreakStatement");
		return output.toString();
	}

	@Override
	public Object visit(Continue continueStatement) {
		StringBuffer output = new StringBuffer();
		indent(output, continueStatement);
		output.append(continueStatement.getID() + " - ContinueStatement");
		return output.toString();
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Statement s : statementsBlock.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + ",");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,statementsBlock);
		output.append(statementsBlock.getID() + " - StatementsBlock");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		if(localVariable.hasInitValue()){
			++depth;
		}
		++depth;
		output.append(localVariable.getType().accept(this));
		children.append(localVariable.getType().getID()+",");
		if(localVariable.hasInitValue()){
			output.append(localVariable.getInitValue().accept(this));
			children.append(localVariable.getInitValue().getID()+",");
			--depth;
		}
		--depth;
		deleteLastComma(children);
		indent(output, localVariable);
		output.append(localVariable.getID() + " - LocalVariable " + localVariable.getName());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(VariableLocation location) {
		StringBuffer output = new StringBuffer();
		if(location.isExternal()){
			++depth;
			output.append(location.getLocation().accept(this));
			--depth;
		}
		indent(output, location);
		output.append(location.getID() + " - VariableLocation " + location.getName());
		if(location.isExternal()){
			output.append(" in external scope");
			output.append(" -> {");
			output.append(location.getLocation().getID());
		}else{
			output.append(" -> {");	
		}
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(ArrayLocation location) {
		StringBuffer output = new StringBuffer();
		depth += 2;
		output.append(location.getArray().accept(this));
		output.append(location.getIndex().accept(this));
		depth -= 2;
		indent(output, location);
		output.append(location.getID() + " - ArrayLocation");
		output.append(" -> {");
		output.append(location.getArray().getID()+",");
		output.append(location.getIndex().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(StaticCall call) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
			children.append(e.getID()+",");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,call);
		output.append(call.getID() + " - StaticCall to method " + call.getName());
		output.append(" in class " + call.getClassName());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(VirtualCall call) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		if(call.isExternal()){
			output.append(call.getLocation().accept(this));
			children.append(call.getLocation().getID()+",");
		}
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
			children.append(e.getID()+",");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,call);
		output.append(call.getID() + " - VirtualCall to method " + call.getName());
		if(call.isExternal()){
			output.append(" in external scope");
		}
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(This thisExpression) {
		StringBuffer output = new StringBuffer();
		indent(output,thisExpression);
		output.append(thisExpression.getID() + " - This reference");
		return output.toString();
	}

	@Override
	public Object visit(NewClass newClass) {
		StringBuffer output = new StringBuffer();
		indent(output,newClass);
		output.append(newClass.getID() + " - NewClass instantiation " + newClass.getName());
		return output.toString();
	}

	@Override
	public Object visit(NewArray newArray) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(newArray.getType().accept(this));
		children.append(newArray.getType().getID()+",");
		output.append(newArray.getSize().accept(this));
		children.append(newArray.getSize().getID()+",");
		depth -= 2;
		deleteLastComma(children);
		indent(output,newArray);
		output.append(newArray.getID() + " - NewArray allocation");
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Length length) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(length.getArray().accept(this));
		--depth;
		indent(output,length);
		output.append(length.getID() + " - Length of array");
		output.append(" -> {");
		output.append(length.getArray().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(binaryOp.getFirstOperand().accept(this));
		children.append(binaryOp.getFirstOperand().getID()+",");
		output.append(binaryOp.getSecondOperand().accept(this));
		children.append(binaryOp.getSecondOperand().getID()+",");
		depth -= 2;
		deleteLastComma(children);
		indent(output,binaryOp);
		output.append(binaryOp.getID() + " - MathBinaryOp " + binaryOp.getOperator().getDescription());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(binaryOp.getFirstOperand().accept(this));
		children.append(binaryOp.getFirstOperand().getID()+",");
		output.append(binaryOp.getSecondOperand().accept(this));
		children.append(binaryOp.getSecondOperand().getID()+",");
		depth -= 2;
		deleteLastComma(children);
		indent(output,binaryOp);
		output.append(binaryOp.getID() + " - LogicalBinaryOp " + binaryOp.getOperator().getDescription());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(unaryOp.getOperand().accept(this));
		--depth;
		indent(output,unaryOp);
		output.append(unaryOp.getID() + " - MathUnaryOp " + unaryOp.getOperator().getDescription());
		output.append(" -> {");
		output.append(unaryOp.getOperand().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(unaryOp.getOperand().accept(this));
		--depth;
		indent(output,unaryOp);
		output.append(unaryOp.getID() + " - LogicalUnaryOp " + unaryOp.getOperator().getDescription());
		output.append(" -> {");
		output.append(unaryOp.getOperand().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(Literal literal) {
		StringBuffer output = new StringBuffer();
		indent(output,literal);
		output.append(literal.getID() + " - Literal " + literal.getType().toFormattedString(literal.getValue()));
		output.append("\n");
		return output.toString();
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(expressionBlock.getExpression().accept(this));
		--depth;
		indent(output,expressionBlock);
		output.append(expressionBlock.getID() + " - ExpressionBlock");
		output.append(" -> {");
		output.append(expressionBlock.getExpression().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

}
