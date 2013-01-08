/*package IC;

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



public class MyTypeBuilder implements PropagatingVisitor<Object, Type> {
	private MyTypeTable types;
	private int uniqueId=0;
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	public MyTypeBuilder(){
		types = new MyTypeTable();
	}
	
	@Override
	public Type visit(Program program, Object d) {
		// TODO Auto-generated method stub
		for(ICClass c:program.getClasses())
			c.accept(this, d);
		return null;
	}

	@Override
	public Type visit(ICClass icClass, Object d) {
		// TODO Auto-generated method stub
		for(Field f:icClass.getFields())
			f.accept(this, d);
		for(Method m:icClass.getMethods())
				m.accept(this, d);
		Type t = icClass.getUserType().accept(this, d);	
		types.insertType(t);
		icClass.setTypeFromTable(t);
		return t;
	}

	@Override
	public Type visit(Field field, Object d) {
		// TODO Auto-generated method stub
		Type t = field.getType().accept(this, d);
		types.insertType(t);
		field.setTypeFromTable(t);
		return t;
	}

	public Type visit(Method method, Object d) {
		for(Formal f:method.getFormals())
			f.accept(this, d);
		for(Statement s:method.getStatements())
			s.accept(this, d);		
		Type t = method.getType().accept(this, d);
		types.insertType(t);
		method.setTypeFromTable(t);
		return t;
	}
	@Override
	public Type visit(VirtualMethod method, Object d) {
		// TODO Auto-generated method stub
		return visit((Method)method,d);
	}

	@Override
	public Type visit(StaticMethod method, Object d) {
		// TODO Auto-generated method stub
		return visit((Method)method,d);
	}

	@Override
	public Type visit(LibraryMethod method, Object d) {
		// TODO Auto-generated method stub
		return visit((Method)method,d);
	}

	@Override
	public Type visit(Formal formal, Object d) {
		// TODO Auto-generated method stub
		Type t =formal.getType().accept(this, d);
		types.insertType(t);
		formal.setTypeFromTable(t);
		return t;
	}

	@Override
	public Type visit(PrimitiveType type, Object d) {
		// TODO Auto-generated method stub
		types.insertType(type);
		type.setTypeFromTable(type);
		return type;
	}

	@Override
	public Type visit(UserType type, Object d) {
		// TODO Auto-generated method stub
		types.insertType(type);
		type.setTypeFromTable(type);
		return type;
	}

	@Override
	public Type visit(Assignment assignment, Object d) {
		// TODO Auto-generated method stub
		Type leftValue = assignment.getVariable().accept(this, d);
		Type rightValue = assignment.getAssignment().accept(this, d);
		if(leftValue.compareTo(rightValue)>=0){
			types.insertType(leftValue);
			leftValue.setTypeFromTable(leftValue);
			rightValue.setTypeFromTable(leftValue);
			return leftValue;
		}
			
		return null;
	}

	@Override
	public Type visit(CallStatement callStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Return returnStatement, Object d) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Type visit(If ifStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(While whileStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Break breakStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Continue continueStatement, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(StatementsBlock statementsBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(LocalVariable localVariable, Object d) {
		// TODO Auto-generated method stub
		Type localType = localVariable.getType().accept(this, d); 
		if(!localVariable.hasInitValue()){
			
			types.insertType(localType);
			localVariable.setTypeFromTable(localType);
			return localType;
		}
			
		Type initValueType = localVariable.getInitValue().accept(this, d);
		if(localType.compareTo(initValueType) >= 0){			
			types.insertType(localType);
			localVariable.setTypeFromTable(localType);
			return localType;
		}
		semanticErrors.add(new SemanticError("variable of type "+localType.getFullName()+" cannot be initialized with value of type "+initValueType.getFullName(), localType.getLine()));
		return null;	
	}

	@Override
	public Type visit(VariableLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayLocation location, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(StaticCall call, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(VirtualCall call, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(This thisExpression, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewClass newClass, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewArray newArray, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Length length, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(MathBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(LogicalBinaryOp binaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(MathUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		Type operand = unaryOp.getOperand().accept(this, d);
		if(unaryOp.getOperator() == UnaryOps.UMINUS && operand.compareTo(new PrimitiveType(unaryOp.getLine(), DataTypes.INT))>=0)
			return operand;
		if(unaryOp.getOperator() == UnaryOps.LNEG && operand.compareTo(new PrimitiveType(unaryOp.getLine(), DataTypes.BOOLEAN))>=0)
			return operand;
		semanticErrors.add(new SemanticError("unsupported unary operation "+unaryOp.getOperator().getDescription()+" on type "+operand.getName(), operand.getLine()));
		return null;
	}

	@Override
	public Type visit(LogicalUnaryOp unaryOp, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Literal literal, Object d) {
		// TODO Auto-generated method stub
		types.insertType(literal.getICType());
		literal.setTypeFromTable(literal.getICType());
		return literal.getICType();
	}

	@Override
	public Type visit(ExpressionBlock expressionBlock, Object d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(MethodType methodType, Object d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void printErrorStack(){
		for(SemanticError e: semanticErrors)
			try {
				throw e;
			} catch (SemanticError e1) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
			}
	}
}*/
