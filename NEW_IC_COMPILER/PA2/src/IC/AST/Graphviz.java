package IC.AST;

import java.util.List;

public class Graphviz implements Visitor{
	private int counter;
	public Graphviz(){
		counter = 0;
	}
	
	public Object visit(ICClass icClass){
		StringBuffer output = new StringBuffer();
		//output.append(icClass.getLine() + ": ");
		for(Method m : icClass.getMethods()){
			output.append('"');
			output.append("Class ");
			output.append(icClass.getName());
			output.append('"');
			output.append(" ->");
			output.append('"');
			output.append("LibraryMethod ");
			output.append(m.getType().accept(this));
			output.append(" ");
			output.append(m.getName());
			output.append('"');
			output.append("\n");
		}
		for(Method m : icClass.getMethods()){
			output.append(m.accept(this));
		}
		return output.toString();
		
	}

	public Object visit(LibraryMethod method){
		StringBuffer output = new StringBuffer();
		//output.append(method.getLine() + ": ");
		for(Formal f : method.getFormals()){
			output.append('"');
			output.append("LibraryMethod ");
			output.append(method.getType().accept(this));
			output.append(" ");
			output.append(method.getName());
			output.append('"');
			output.append(" ->");
			output.append('"');
			output.append(f.accept(this));
			output.append(" (");
			output.append(counter++);
			output.append(")");
			output.append('"');
			output.append("\n");
		}
		return output.toString();
	}

	public Object visit(Formal formal){
		StringBuffer output = new StringBuffer();
		output.append(formal.getType().accept(this));
		output.append(" ");
		output.append(formal.getName());
		return output.toString();
	}

	public Object visit(PrimitiveType type){
		StringBuffer output = new StringBuffer();
		output.append(type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		return output.toString();
	}

	public Object visit(UserType type){
		StringBuffer output = new StringBuffer();
		output.append(type.getName());
		for(int i=1;i<=type.getDimension();i++){
			output.append("[]");
		}
		return output.toString();
	}

	public Object visit(Assignment assignment){
		StringBuffer output = new StringBuffer();
		//output.append(assignment.getLine() + ": ");
		output.append("Assign ->{");
		output.append(assignment.getVariable().accept(this));
		output.append(", ");
		output.append(assignment.getAssignment().accept(this));
		output.append("}");
		return output.toString();
	}

	public Object visit(Literal literal){
		StringBuffer output = new StringBuffer();
		output.append(literal.getType().toFormattedString(literal.getValue()));
		return output.toString();
	}

@Override
public Object visit(Program program) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(Field field) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(VirtualMethod method) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(StaticMethod method) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(CallStatement callStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(Return returnStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(If ifStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(While whileStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(Break breakStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(Continue continueStatement) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(StatementsBlock statementsBlock) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(LocalVariable localVariable) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(VariableLocation location) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(ArrayLocation location) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(StaticCall call) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(VirtualCall call) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(This thisExpression) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(NewClass newClass) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(NewArray newArray) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(Length length) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(MathBinaryOp binaryOp) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(LogicalBinaryOp binaryOp) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(MathUnaryOp unaryOp) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(LogicalUnaryOp unaryOp) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object visit(ExpressionBlock expressionBlock) {
	// TODO Auto-generated method stub
	return null;
}

	private void removeUnwantedCommas(List lst,StringBuffer output){
		if(lst.size() > 0){
			output.deleteCharAt(output.length()-1);
			output.deleteCharAt(output.length()-1);
		}
	}
}