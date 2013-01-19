package IC.lir;

import java.util.List;

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
import IC.AST.PropagatingVisitor;
import IC.AST.Return;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;

public class Translator implements PropagatingVisitor<Object, List<StringBuffer>>{
	private class TranslationInfo{
		public ClassLayout classLayout;
		public List<Integer> freeRegisters;
		
		public TranslationInfo(ClassLayout cl, List<Integer> fr) {
			this.classLayout = cl;
			this.freeRegisters = fr;
		}
	}
	@Override
	public List<StringBuffer> visit(Program program, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(ICClass icClass, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Field field, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(VirtualMethod method, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(StaticMethod method, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(LibraryMethod method, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Formal formal, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(PrimitiveType type, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(UserType type, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Assignment assignment, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(CallStatement callStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Return returnStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(If ifStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(While whileStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Break breakStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Continue continueStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(StatementsBlock statementsBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(LocalVariable localVariable, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(VariableLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(ArrayLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(StaticCall call, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(VirtualCall call, Object d) {
		
		return null;
	}

	@Override
	public List<StringBuffer> visit(This thisExpression, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(NewClass newClass, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(NewArray newArray, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Length length, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(MathBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(LogicalBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(MathUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(LogicalUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(Literal literal, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(ExpressionBlock expressionBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StringBuffer> visit(MethodType methodType, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

}
