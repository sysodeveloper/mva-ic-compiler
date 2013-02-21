package IC.lir;

import IC.BinaryOps;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.Expression;
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

public class SethiUllman implements Visitor<Integer> {

	@Override
	public Integer visit(Program program) {
		for(ICClass c : program.getClasses())
			c.accept(this);
		return 0;
	}

	@Override
	public Integer visit(ICClass icClass) {
		for(Method m : icClass.getMethods())
			m.accept(this);
		return 0;
	}

	@Override
	public Integer visit(Field field) {
		field.setWeight(0);
		return 0;
	}

	@Override
	public Integer visit(VirtualMethod method) {
		int weight = 0 ;
		for(Statement s :method.getStatements())
			weight+=s.accept(this);
		method.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(StaticMethod method) {
		int weight = 0 ;
		for(Statement s :method.getStatements())
			weight+=s.accept(this);
		method.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(LibraryMethod method) {
		method.setWeight(0);
		return 0;
	}

	@Override
	public Integer visit(Formal formal) {
		formal.setWeight(0);
		return 0;
	}

	@Override
	public Integer visit(PrimitiveType type) {
		type.setWeight(0);
		return 0;
	}

	@Override
	public Integer visit(UserType type) {
		type.setWeight(0);
		return 0;
	}

	@Override
	public Integer visit(Assignment assignment) {
		int first = assignment.getAssignment().accept(this);
		int second = assignment.getVariable().accept(this);
		int weight = (first>second)?first:second+1;
		assignment.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(CallStatement callStatement) {
		int weight = callStatement.getCall().accept(this);
		callStatement.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(Return returnStatement) {
		int weight = 0;
		if(returnStatement.hasValue())
			weight+=returnStatement.accept(this);
		returnStatement.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(If ifStatement) {
		int weight = ifStatement.getCondition().accept(this);
		weight+=ifStatement.getOperation().accept(this);
		if(ifStatement.hasElse())
			weight+=ifStatement.getElseOperation().accept(this);
		ifStatement.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(While whileStatement) {
		int weight = whileStatement.getCondition().accept(this) + whileStatement.getOperation().accept(this);		 
		whileStatement.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(Break breakStatement) {
		breakStatement.setWeight(0);
		System.out.println(0);
		return 0;
	}

	@Override
	public Integer visit(Continue continueStatement) {
		continueStatement.setWeight(0);
		System.out.println(0);
		return 0;
	}

	@Override
	public Integer visit(StatementsBlock statementsBlock) {
		int weight=0;
		for(Statement s : statementsBlock.getStatements())
			weight+=s.accept(this);
		statementsBlock.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(LocalVariable localVariable) {
		localVariable.setWeight(0);
		System.out.println(0);
		return 0;
	}

	@Override
	public Integer visit(VariableLocation location) {
		int weight = 1;
		if(location.isExternal())
			weight=location.getLocation().accept(this);			
		location.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(ArrayLocation location) {
		int weight = location.getIndex().accept(this) + location.getArray().accept(this);
		location.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(StaticCall call) {
		int weight=1;
		for(Expression param :call.getArguments()){
			weight+=param.accept(this);
		}
		call.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(VirtualCall call) {
		int weight=1;
		for(Expression param :call.getArguments()){
			weight+=param.accept(this);
		}
		call.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(This thisExpression) {
		thisExpression.setWeight(1);
		System.out.println(1);
		return 1;
	}

	@Override
	public Integer visit(NewClass newClass) {
		newClass.setWeight(1);
		System.out.println(1);
		return 1;
	}

	@Override
	public Integer visit(NewArray newArray) {
		newArray.setWeight(2);
		System.out.println(2);
		return 2;
	}

	@Override
	public Integer visit(Length length) {
		length.setWeight(1);
		System.out.println(1);
		return 1;
	}

	@Override
	public Integer visit(MathBinaryOp binaryOp) {
		int first = binaryOp.getFirstOperand().accept(this);
		int second = binaryOp.getSecondOperand().accept(this);	
		int weight=-1;
		if((binaryOp.getOperator()==BinaryOps.PLUS || binaryOp.getOperator()==BinaryOps.MULTIPLY)){
			weight = (first==second)?first+1:(first>second)?first:second;			
		}
		else
			weight = (first<=second)?second+1:first;
		binaryOp.setWeight(weight);	
		System.out.println(weight);
		return weight;	
	}

	@Override
	public Integer visit(LogicalBinaryOp binaryOp) {		
		int first = binaryOp.getFirstOperand().accept(this);
		int second = binaryOp.getSecondOperand().accept(this);	
		int weight=-1;
		if((binaryOp.getOperator()==BinaryOps.EQUAL || binaryOp.getOperator()==BinaryOps.NEQUAL)){
			weight = (first==second)?first+1:(first>second)?first:second;			
		}
		else
			weight = (first<=second)?second+1:first;
		binaryOp.setWeight(weight);	
		System.out.println(weight);
		return weight;	
		
	}	
	
	@Override
	public Integer visit(MathUnaryOp unaryOp) {
		int w = unaryOp.getOperand().accept(this);
		if(w==0)
			w=w+1;
		unaryOp.setWeight(w);
		System.out.println(w);
		return w;
	}

	@Override
	public Integer visit(LogicalUnaryOp unaryOp) {
		int w = unaryOp.getOperand().accept(this);
		if(w==0)
			w=w+1;
		unaryOp.setWeight(w);
		System.out.println(w);
		return w;
	}

	@Override
	public Integer visit(Literal literal) {
		literal.setWeight(0);
		System.out.println(0);
		return 0;
	}

	@Override
	public Integer visit(ExpressionBlock expressionBlock) {
		int weight = expressionBlock.getExpression().accept(this);
		expressionBlock.setWeight(weight);
		System.out.println(weight);
		return weight;
	}

	@Override
	public Integer visit(MethodType methodType) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
