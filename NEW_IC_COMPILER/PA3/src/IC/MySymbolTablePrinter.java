package IC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.Method;
import IC.AST.MethodType;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.MySymbolRecord.Kind;

public class MySymbolTablePrinter implements Visitor<StringBuffer>{

	@Override
	public StringBuffer visit(Program program) {
		StringBuffer output = new StringBuffer();
		output.append("Global Symbol Table: ");
		output.append(new File(Compiler.getICFileParsed()).getName());
		output.append("\n");
		output.append(program.enclosingScope());
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();
		output.append("\n");
		output.append("Class Symbol Table: ");
		output.append(icClass.getName());
		output.append("\n");
		output.append(icClass.enclosingScope());
		for(Field f : icClass.getFields()){
			output.append(f.accept(this));
		}
		for(Method m : icClass.getMethods()){
			output.append(m.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(Field field) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(VirtualMethod method) {
		StringBuffer output = new StringBuffer();
		output.append("\n");
		output.append("Method Symbol Table: ");
		output.append(method.getName());
		output.append("\n");
		output.append(method.enclosingScope());
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(StaticMethod method) {
		StringBuffer output = new StringBuffer();
		output.append("\n");
		output.append("Method Symbol Table: ");
		output.append(method.getName());
		output.append("\n");
		output.append(method.enclosingScope());
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(LibraryMethod method) {
		StringBuffer output = new StringBuffer();
		output.append("\n");
		output.append("Method Symbol Table: ");
		output.append(method.getName());
		output.append("\n");
		output.append(method.enclosingScope());
		output.append(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			output.append(f.accept(this));
		}
		for(Statement s : method.getStatements()){
			output.append(s.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(Formal formal) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(PrimitiveType type) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(UserType type) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Assignment assignment) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(If ifStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(While whileStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(StatementsBlock statementsBlock) {
		StringBuffer output = new StringBuffer();
		output.append("\n");
		output.append("Statement Block Symbol Table: (located in ");
		output.append(statementsBlock.enclosingScope().getParent().getDescription()+")");
		output.append("\n");
		output.append(statementsBlock.enclosingScope());
		for(Statement s : statementsBlock.getStatements()){
			output.append(s.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(LocalVariable localVariable) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(VariableLocation location) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(StaticCall call) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(VirtualCall call) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(This thisExpression) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(NewClass newClass) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(NewArray newArray) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Length length) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(Literal literal) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public StringBuffer visit(MethodType methodType) {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}
}
