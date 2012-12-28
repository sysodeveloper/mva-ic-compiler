package IC.AST;
/**
 * A class to print the edges of the ast in a manner that Graphviz can accept
 */
public class GraphEdgesPrinter implements Visitor<String> {
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
	public String visit(Program program) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
			children.append(c.getID()+" ");
		}
		deleteLastComma(children);
		indent(output);
		output.append(program.getID());
		output.append(" -> {");
		output.append(children.toString());
		output.append("}");
		output.append("\n");

		return output.toString();
	}

	@Override
	public String visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Field f : icClass.getFields()){
			output.append(f.accept(this));
			children.append(f.getID()+" ");
		}
		for(Method m : icClass.getMethods()){
			output.append(m.accept(this));
			children.append(m.getID()+" ");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output, icClass);
		output.append(icClass.getID());
		if(icClass.hasSuperClass()){
			
		}
		output.append(" -> {");
		output.append(children.toString());
		output.append("}");
		output.append("\n");
		return output.toString();
	}
	
	@Override
	public String visit(Field field) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(field.getType().accept(this));
		--depth;
		output.append(field.getID());
		output.append(" -> {");
		output.append(field.getType().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(VirtualMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID() + " ");
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID() + " ");
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + " ");
		}
		deleteLastComma(children);
		depth -= 2;
		indent(output,method);
		output.append(method.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(StaticMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID()+" ");
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID() + " ");
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + " ");
		}
		deleteLastComma(children);
		depth -= 2;
		indent(output,method);
		output.append(method.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(LibraryMethod method) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		++depth;
		output.append(method.getType().accept(this));
		children.append(method.getType().getID()+" ");
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
			children.append(f.getID()+" ");
		}
		--depth;
		deleteLastComma(children);
		indent(output, method);
		output.append(method.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Formal formal) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(formal.getType().accept(this));
		--depth;
		indent(output, formal);
		output.append(formal.getID());
		output.append(" -> {");
		output.append(formal.getType().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(PrimitiveType type) {
		StringBuffer output = new StringBuffer();
		indent(output, type);
		output.append(type.getID());
		for(int i=1;i<=type.getDimension();i++){
			//output.append("[]");
		}
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(UserType type) {
		StringBuffer output = new StringBuffer();
		indent(output, type);
		output.append(type.getID() );
		for(int i=1;i<=type.getDimension();i++){
			//output.append("[]");
		}
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Assignment assignment) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(assignment.getVariable().accept(this));
		children.append(assignment.getVariable().getID()+" ");
		output.append(assignment.getAssignment().accept(this));
		children.append(assignment.getAssignment().getID()+" ");
		depth -= 2;
		deleteLastComma(children);
		indent(output,assignment);
		output.append(assignment.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(CallStatement callStatement) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(callStatement.getCall().accept(this));
		--depth;
		indent(output,callStatement);
		output.append(callStatement.getID());
		output.append(" -> {");
		output.append(callStatement.getCall().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Return returnStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		if(returnStatement.hasValue()){
			++depth;
			output.append(returnStatement.getValue().accept(this));
			children.append(returnStatement.getValue().getID()+" ");
			--depth;
		}
		deleteLastComma(children);
		indent(output,returnStatement);
		output.append(returnStatement.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(If ifStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(ifStatement.getCondition().accept(this));
		children.append(ifStatement.getCondition().getID()+" ");
		output.append(ifStatement.getOperation().accept(this));
		children.append(ifStatement.getOperation().getID()+" ");
		if(ifStatement.hasElse()){
			output.append(ifStatement.getElseOperation().accept(this));
			children.append(ifStatement.getElseOperation().getID()+" ");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,ifStatement);
		output.append(ifStatement.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(While whileStatement) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(whileStatement.getCondition().accept(this));
		children.append(whileStatement.getCondition().getID()+" ");
		output.append(whileStatement.getOperation().accept(this));
		children.append(whileStatement.getOperation().getID()+" ");
		depth -= 2;
		deleteLastComma(children);
		indent(output,whileStatement);
		output.append(whileStatement.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Break breakStatement) {
		StringBuffer output = new StringBuffer();
		indent(output, breakStatement);
		output.append(breakStatement.getID());
		return output.toString();
	}

	@Override
	public String visit(Continue continueStatement) {
		StringBuffer output = new StringBuffer();
		indent(output, continueStatement);
		output.append(continueStatement.getID());
		return output.toString();
	}

	@Override
	public String visit(StatementsBlock statementsBlock) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Statement s : statementsBlock.getStatements()){
			output.append(s.accept(this));
			children.append(s.getID() + " ");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,statementsBlock);
		output.append(statementsBlock.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(LocalVariable localVariable) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		if(localVariable.hasInitValue()){
			++depth;
		}
		++depth;
		output.append(localVariable.getType().accept(this));
		children.append(localVariable.getType().getID()+" ");
		if(localVariable.hasInitValue()){
			output.append(localVariable.getInitValue().accept(this));
			children.append(localVariable.getInitValue().getID()+" ");
			--depth;
		}
		--depth;
		deleteLastComma(children);
		indent(output, localVariable);
		output.append(localVariable.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(VariableLocation location) {
		StringBuffer output = new StringBuffer();
		if(location.isExternal()){
			++depth;
			output.append(location.getLocation().accept(this));
			--depth;
		}
		indent(output, location);
		output.append(location.getID());
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
	public String visit(ArrayLocation location) {
		StringBuffer output = new StringBuffer();
		depth += 2;
		output.append(location.getArray().accept(this));
		output.append(location.getIndex().accept(this));
		depth -= 2;
		indent(output, location);
		output.append(location.getID());
		output.append(" -> {");
		output.append(location.getArray().getID()+" ");
		output.append(location.getIndex().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(StaticCall call) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
			children.append(e.getID()+" ");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,call);
		output.append(call.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(VirtualCall call) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		if(call.isExternal()){
			output.append(call.getLocation().accept(this));
			children.append(call.getLocation().getID()+" ");
		}
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
			children.append(e.getID()+" ");
		}
		depth -= 2;
		deleteLastComma(children);
		indent(output,call);
		output.append(call.getID());
		if(call.isExternal()){
			//output.append(" in external scope");
		}
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(This thisExpression) {
		StringBuffer output = new StringBuffer();
		indent(output,thisExpression);
		output.append(thisExpression.getID());
		return output.toString();
	}

	@Override
	public String visit(NewClass newClass) {
		StringBuffer output = new StringBuffer();
		indent(output,newClass);
		output.append(newClass.getID());
		return output.toString();
	}

	@Override
	public String visit(NewArray newArray) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(newArray.getType().accept(this));
		children.append(newArray.getType().getID()+" ");
		output.append(newArray.getSize().accept(this));
		children.append(newArray.getSize().getID()+" ");
		depth -= 2;
		deleteLastComma(children);
		indent(output,newArray);
		output.append(newArray.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Length length) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(length.getArray().accept(this));
		--depth;
		indent(output,length);
		output.append(length.getID());
		output.append(" -> {");
		output.append(length.getArray().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(MathBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(binaryOp.getFirstOperand().accept(this));
		children.append(binaryOp.getFirstOperand().getID()+" ");
		output.append(binaryOp.getSecondOperand().accept(this));
		children.append(binaryOp.getSecondOperand().getID()+" ");
		depth -= 2;
		deleteLastComma(children);
		indent(output,binaryOp);
		output.append(binaryOp.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(LogicalBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		StringBuffer children = new StringBuffer();
		depth += 2;
		output.append(binaryOp.getFirstOperand().accept(this));
		children.append(binaryOp.getFirstOperand().getID()+" ");
		output.append(binaryOp.getSecondOperand().accept(this));
		children.append(binaryOp.getSecondOperand().getID()+" ");
		depth -= 2;
		deleteLastComma(children);
		indent(output,binaryOp);
		output.append(binaryOp.getID());
		output.append(" -> {");
		output.append(children);
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(MathUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(unaryOp.getOperand().accept(this));
		--depth;
		indent(output,unaryOp);
		output.append(unaryOp.getID());
		output.append(" -> {");
		output.append(unaryOp.getOperand().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(LogicalUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(unaryOp.getOperand().accept(this));
		--depth;
		indent(output,unaryOp);
		output.append(unaryOp.getID());
		output.append(" -> {");
		output.append(unaryOp.getOperand().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(Literal literal) {
		StringBuffer output = new StringBuffer();
		indent(output,literal);
		output.append(literal.getID());
		output.append("\n");
		return output.toString();
	}

	@Override
	public String visit(ExpressionBlock expressionBlock) {
		StringBuffer output = new StringBuffer();
		++depth;
		output.append(expressionBlock.getExpression().accept(this));
		--depth;
		indent(output,expressionBlock);
		output.append(expressionBlock.getID());
		output.append(" -> {");
		output.append(expressionBlock.getExpression().getID());
		output.append("}");
		output.append("\n");
		return output.toString();
	}
}
