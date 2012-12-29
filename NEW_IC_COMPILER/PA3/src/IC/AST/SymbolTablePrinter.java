package IC.AST;

import IC.SymbolTable;

public class SymbolTablePrinter implements Visitor<StringBuffer> {
	@Override
	public StringBuffer visit(Program program) {
		StringBuffer output = new StringBuffer();
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
		}
		StringBuffer children = new StringBuffer();
		//output.append("Global Symbol Table: " + IC.Compiler.getICFileParsed()+"\n");
		for(ICClass c : program.getClasses()){
			output.append("\t Class: " + c.getName() + "\n");
			if(program.getOuterTable() == c.getOuterTable().getParent()){
				children.append(c.getName()+",");
			}
		}
		output.append("Children tables: " + children.toString()+"\n");
		return output;
	}

	@Override
	public StringBuffer visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();
		output.append("Class Symbol Table: " + icClass.getName() + "\n");
		for(Field f : icClass.getFields()){
			
		}
		return output;
	}

	@Override
	public StringBuffer visit(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(VirtualMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(StaticMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(LibraryMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Formal formal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(PrimitiveType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(UserType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Assignment assignment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(If ifStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(While whileStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(StatementsBlock statementsBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(LocalVariable localVariable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(VariableLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(StaticCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(VirtualCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(This thisExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(NewClass newClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(NewArray newArray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Length length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(Literal literal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer visit(MethodType methodType) {
		// TODO Auto-generated method stub
		return null;
	}

}
