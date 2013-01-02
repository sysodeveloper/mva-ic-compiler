package IC;

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
import IC.AST.MethodType;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
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
		Map<Integer, String> children = new HashMap<Integer, String>();
		StringBuffer output = new StringBuffer();
		output.append("Global Symbol Table: ");
		output.append(Compiler.getICFileParsed());
		output.append("\n");
		for(Entry<String, MySymbolRecord> entry : program.enclosingScope().getEntries().entrySet()){
			output.append("\t");
			output.append(entry.getValue().getKind()+": ");
			output.append(entry.getKey());
			output.append("\n");
			children.put(entry.getValue().getId(),entry.getKey());
		}

		output.append("Children tables: ");
		for(MySymbolTable child : program.enclosingScope().getChildren()){
			output.append(children.get(child.getId()));
			output.append(",");
		}
		output.deleteCharAt(output.length()-1);
		output.append("\n");
		for(ICClass c : program.getClasses()){
			output.append(c.accept(this));
		}
		return output;
	}

	@Override
	public StringBuffer visit(ICClass icClass) {
		Map<Integer, String> children = new HashMap<Integer, String>();
		StringBuffer output = new StringBuffer();
		output.append("Class Symbol Table: ");
		output.append(icClass.getName());
		output.append("\n");
		for(Entry<String, MySymbolRecord> entry : icClass.enclosingScope().getEntries().entrySet()){
			output.append("\t");
			output.append(entry.getValue().getKind()+": ");
			output.append(entry.getKey());
			output.append("\n");
			children.put(entry.getValue().getId(),entry.getKey());
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
