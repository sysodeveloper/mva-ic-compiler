package IC.AST;

public class GrpahNodesPrinter implements Visitor{

	private String PrintNode(ASTNode node, String desc){
		StringBuffer output = new StringBuffer();
		output.append(node.getID() + "[");
		output.append("label=");
		output.append('"');
		output.append(desc);
		output.append('"');
		output.append("]");
		output.append("\n");
		return output.toString();
	}
	
	@Override
	public Object visit(Program program) {
		StringBuffer output = new StringBuffer();
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
		}
		output.append(PrintNode(program,"Program"));
		return output.toString();
	}

	@Override
	public Object visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();
		for(Field f : icClass.getFields()){
			output.append(f.accept(this));
		}
		for(Method m : icClass.getMethods()){
			output.append(m.accept(this));
		}
		if(icClass.hasSuperClass()){
			output.append(PrintNode(icClass,"Class " + icClass.getName() + " has super class" + icClass.getSuperClassName()));
		}else{
			output.append(PrintNode(icClass, "Class " + icClass.getName()));
		}
		return output.toString();
	}

	@Override
	public Object visit(Field field) {
		StringBuffer output = new StringBuffer();
		output.append(field.getType().accept(this));
		output.append(PrintNode(field,"Field  " + field.getName()));
		return output.toString();
	}

	@Override
	public Object visit(VirtualMethod method) {
		StringBuffer output = new StringBuffer();
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
		}
		output.append(PrintNode(method,"VirtualMethod " + method.getName()));
		return output.toString();
	}

	@Override
	public Object visit(StaticMethod method) {
		StringBuffer output = new StringBuffer();
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
		}
		output.append(PrintNode(method,"StaticMethod " + method.getName()));
		return output.toString();	}

	@Override
	public Object visit(LibraryMethod method) {
		StringBuffer output = new StringBuffer();
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		output.append(PrintNode(method,"LibraryMethod " + method.getName()));
		return output.toString();
	}

	@Override
	public Object visit(Formal formal) {
		StringBuffer output = new StringBuffer();
		output.append(formal.getType().accept(this));
		output.append(PrintNode(formal,"Formal " + formal.getName()));
		return output.toString();
	}

	@Override
	public Object visit(PrimitiveType type) {
		StringBuffer output = new StringBuffer();
		output.append("PrimitiveType " + type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		return PrintNode(type,output.toString());
	}

	@Override
	public Object visit(UserType type) {
		StringBuffer output = new StringBuffer();
		output.append("UserType " + type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		return PrintNode(type,output.toString());
	}

	@Override
	public Object visit(Assignment assignment) {
		StringBuffer output = new StringBuffer();
		output.append(assignment.getVariable().accept(this));
		output.append(assignment.getAssignment().accept(this));
		output.append(PrintNode(assignment,"Assignment"));
		return output.toString();
	}

	@Override
	public Object visit(CallStatement callStatement) {
		StringBuffer output = new StringBuffer();
		output.append(callStatement.getCall().accept(this));
		output.append(PrintNode(callStatement,"CallStatement"));
		return output.toString();
	}

	@Override
	public Object visit(Return returnStatement) {
		StringBuffer output = new StringBuffer();
		if(returnStatement.hasValue()){
			output.append(returnStatement.getValue().accept(this));
		}
		output.append(PrintNode(returnStatement,"ReturnStatement"));
		return output.toString();
	}

	@Override
	public Object visit(If ifStatement) {
		StringBuffer output = new StringBuffer();
		output.append(ifStatement.getCondition().accept(this));
		output.append(ifStatement.getOperation().accept(this));
		if(ifStatement.hasElse()){
			output.append(ifStatement.getElseOperation().accept(this));
		}
		output.append(PrintNode(ifStatement,"IfStatement"));
		return output.toString();
	}

	@Override
	public Object visit(While whileStatement) {
		StringBuffer output = new StringBuffer();
		output.append(whileStatement.getCondition().accept(this));
		output.append(whileStatement.getOperation().accept(this));
		output.append(PrintNode(whileStatement,"whileStatement"));
		return output.toString();
	}

	@Override
	public Object visit(Break breakStatement) {
		return PrintNode(breakStatement,"BreakStatement");
	}

	@Override
	public Object visit(Continue continueStatement) {
		return PrintNode(continueStatement,"ContinueStatement");
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		StringBuffer output = new StringBuffer();
		for(Statement s : statementsBlock.getStatements()){
			output.append(s.accept(this));
		}
		output.append(PrintNode(statementsBlock,"StatementsBlock"));
		return output.toString();
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		StringBuffer output = new StringBuffer();
		output.append(localVariable.getType().accept(this));
		if(localVariable.hasInitValue()){
			output.append(localVariable.getInitValue().accept(this));
		}
		output.append(PrintNode(localVariable,"LocalVariable " + localVariable.getName()));
		return output.toString();
	}

	@Override
	public Object visit(VariableLocation location) {
		StringBuffer output = new StringBuffer();
		if(location.isExternal()){
			output.append(location.getLocation().accept(this));
		}
		if(location.isExternal()){
			output.append(PrintNode(location,"VariableLocation " + location.getName()+" in external scope"));
		}
		output.append(PrintNode(location,"VariableLocation " + location.getName()));
		return output.toString();
	}

	@Override
	public Object visit(ArrayLocation location) {
		StringBuffer output = new StringBuffer();
		output.append(location.getArray().accept(this));
		output.append(location.getIndex().accept(this));
		output.append(PrintNode(location,"ArrayLocation"));
		return output.toString();
	}

	@Override
	public Object visit(StaticCall call) {
		StringBuffer output = new StringBuffer();
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
		}
		output.append(PrintNode(call,"StaticCall to method " + call.getName()));
		return output.toString();
	}

	@Override
	public Object visit(VirtualCall call) {
		StringBuffer output = new StringBuffer();
		if(call.isExternal()){
			output.append(call.getLocation().accept(this));
		}
		for(Expression e : call.getArguments()){
			output.append(e.accept(this));
		}
		if(call.isExternal()){
			output.append(PrintNode(call,"VirtualCall to method " + call.getName() + " in external scope"));
		}else{
			output.append(PrintNode(call,"VirtualCall to method " + call.getName()));
		}
		return output.toString();
	}

	@Override
	public Object visit(This thisExpression) {
		return PrintNode(thisExpression,"This reference");
	}

	@Override
	public Object visit(NewClass newClass) {
		return PrintNode(newClass,"NewClass instantiation " + newClass.getName());
	}

	@Override
	public Object visit(NewArray newArray) {
		StringBuffer output = new StringBuffer();
		output.append(newArray.getType().accept(this));
		output.append(newArray.getSize().accept(this));
		output.append(PrintNode(newArray,"NewArray allocation"));
		return output.toString();
	}

	@Override
	public Object visit(Length length) {
		StringBuffer output = new StringBuffer();
		output.append(length.getArray().accept(this));
		output.append(PrintNode(length,"Length of array"));
		return output.toString();
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		output.append(binaryOp.getFirstOperand().accept(this));
		output.append(binaryOp.getSecondOperand().accept(this));
		output.append(PrintNode(binaryOp,"MathBinaryOp " + binaryOp.getOperator().getDescription()));
		return output.toString();
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		StringBuffer output = new StringBuffer();
		output.append(binaryOp.getFirstOperand().accept(this));
		output.append(binaryOp.getSecondOperand().accept(this));
		output.append(PrintNode(binaryOp,"LogicalBinaryOp " + binaryOp.getOperator().getDescription()));
		return output.toString(); 
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		output.append(unaryOp.getOperand().accept(this));
		output.append(PrintNode(unaryOp, "MathUnaryOp " + unaryOp.getOperator().getDescription()));
		return output.toString();
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		StringBuffer output = new StringBuffer();
		output.append(unaryOp.getOperand().accept(this));
		output.append( PrintNode(unaryOp,"LogicalUnaryOp " + unaryOp.getOperator().getDescription()));
		return output.toString();
	}

	@Override
	public Object visit(Literal literal) {
		return PrintNode(literal,"Literal " + literal.getType().toFormattedString(literal.getValue()));
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		StringBuffer output = new StringBuffer();
		output.append(expressionBlock.getExpression().accept(this));
		output.append(PrintNode(expressionBlock,"ExpressionBlock"));
		return output.toString();
	}
}
