package IC;

import java.util.ArrayList;
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
import IC.AST.Method;
import IC.AST.MethodType;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.PropagatingVisitor;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.Type;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;



public class MyTypeBuilder implements PropagatingVisitor<Object, MyType> {
	private MyTypeTable types;
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	
	private boolean TypeOK(MyType t1, MyType t2){
		//left t1, right t2
		if(t1 == t2){
			return true;
		}else if(t2.subtypeOf(t1)){
			return true;
		}
		return false;
	}
	
	public MyTypeBuilder(MyTypeTable table){
		this.types = table;
	}
	@Override
	public MyType visit(Program program, Object d) {
		for(ICClass c : program.getClasses()){
			c.accept(this,d);
		}
		return null;
	}
	@Override
	public MyType visit(ICClass icClass, Object d) {
		icClass.getUserType().accept(this,d);
		for(Field f : icClass.getFields()){
			f.accept(this,d);
		}
		for(Method m: icClass.getMethods()){
			m.accept(this,d);
		}
		return null;
	}
	@Override
	public MyType visit(Field field, Object d) {
		return field.getType().accept(this,d);
	}

	public MyType visit(Method method, Object d){
		method.getType().accept(this,d);
		for(Formal f : method.getFormals()){
			f.accept(this,d);
		}
		for(Statement s : method.getStatements()){
			s.accept(this,d);
		}
		return method.getMyType();
	}
	@Override
	public MyType visit(VirtualMethod method, Object d) {
		return visit((Method)method,d);
	}
	@Override
	public MyType visit(StaticMethod method, Object d) {
		return visit((Method)method,d);
	}
	@Override
	public MyType visit(LibraryMethod method, Object d) {
		return visit((Method)method,d);
	}
	@Override
	public MyType visit(Formal formal, Object d) {
		return formal.getType().accept(this,d);
	}
	@Override
	public MyType visit(PrimitiveType type, Object d) {
		return types.insertType(type.getMyType());
	}
	@Override
	public MyType visit(UserType type, Object d) {
		return types.insertType(type.getMyType());
	}
	@Override
	public MyType visit(Assignment assignment, Object d) {
		MyType varType = assignment.getVariable().accept(this,d);
		MyType exprType = assignment.getAssignment().accept(this,d);
		if(TypeOK(varType,exprType)){
			return varType;
		}
		semanticErrors.add(new SemanticError("Cannot assign different types",assignment.getLine()));
		return null;
	}
	@Override
	public MyType visit(CallStatement callStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(Return returnStatement, Object d) {
		MySymbolRecord retRecord = returnStatement.enclosingScope().Lookup("$ret");
		if(retRecord == null){
			System.out.println("MEGA ERROR!");
		}
		MyType methodType = retRecord.getMyType();
		
		if(returnStatement.hasValue()){
			MyType ret = returnStatement.getValue().accept(this,d);
			if(TypeOK(methodType, ret)){
				return methodType;
			}
			semanticErrors.add(new SemanticError("Cannot return different types",returnStatement.getLine()));	
		}
		//void
		MyType typeTable = types.insertType(new MyVoidType());
		if(TypeOK(methodType,typeTable)){
			return typeTable;
		}
		return null;
	}
	@Override
	public MyType visit(If ifStatement, Object d) {
		return null;
	}
	@Override
	public MyType visit(While whileStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(Break breakStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(Continue continueStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(StatementsBlock statementsBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(LocalVariable localVariable, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(VariableLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(ArrayLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(StaticCall call, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(VirtualCall call, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(This thisExpression, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(NewClass newClass, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(NewArray newArray, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(Length length, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(MathBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		MyType leftType = binaryOp.getFirstOperand().accept(this, d);
		MyType rightType = binaryOp.getSecondOperand().accept(this, d);
		if(TypeOK(leftType, rightType))
			return leftType;
		
		semanticErrors.add(new SemanticError("Cannot operate on different types",binaryOp.getLine()));
		return null;
	}
	@Override
	public MyType visit(LogicalBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(MathUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(LogicalUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(Literal literal, Object d) {
		// TODO Auto-generated method stub		
		return types.insertType(literal.getMyType());
	}
	@Override
	public MyType visit(ExpressionBlock expressionBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public MyType visit(MethodType methodType, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
}
