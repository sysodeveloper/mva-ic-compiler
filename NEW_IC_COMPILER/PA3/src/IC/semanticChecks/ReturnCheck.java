package IC.semanticChecks;

import java.util.ArrayList;
import java.util.List;

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

//Check that every control path has a return value
//this visitor should be called after type checking, therefor it is assumed that
//all types of return are correct and are not checked again
public class ReturnCheck implements Visitor<Boolean>{
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	
	private boolean methodIsVoid = false;
	boolean returnInPath = false;
	@Override
	public Boolean visit(Program program) {
		Boolean returnValue = new Boolean(true);
		for(ICClass c : program.getClasses()){
			returnValue &= c.accept(this);
		}
		return (semanticErrors.size() <= 0);
	}

	@Override
	public Boolean visit(ICClass icClass) {
		Boolean returnValue = new Boolean(true);
		for(Field f : icClass.getFields()){
			returnValue &= f.accept(this);
		}
		for(Method m : icClass.getMethods()){
			returnValue &= m.accept(this);
		}
		return returnValue;	
	}

	@Override
	public Boolean visit(Field field) {
		Boolean returnValue = new Boolean(true);
		returnValue &= field.getType().accept(this);
		return false;
	}

	public Boolean visit(Method method){
		//init variables
		methodIsVoid = false;
		returnInPath = false;
		Boolean returnValue = new Boolean(true);
		methodIsVoid = method.getType().accept(this);
		if(methodIsVoid){
			methodIsVoid = false;
			return true;
		}
		for(Formal f : method.getFormals()){
			returnValue &= f.accept(this);
		}
		//only go to the last statement
		if(method.getStatements().size() > 0){
			returnValue &= method.getStatements().get(method.getStatements().size()-1).accept(this);
			if(returnInPath){
				return true;
			}
		}
		semanticErrors.add(new SemanticError("Not every control path leads to a return value in " + method.getName(),method.getLine()));
		return false;
	}
	
	@Override
	public Boolean visit(VirtualMethod method) {
		return visit((Method)method);
	}

	@Override
	public Boolean visit(StaticMethod method) {
		return visit((Method)method);
	}

	@Override
	public Boolean visit(LibraryMethod method) {
		return true;
	}

	@Override
	public Boolean visit(Formal formal) {
		Boolean returnValue = new Boolean(true);
		returnValue &= formal.getType().accept(this);
		return false;
	}

	@Override
	public Boolean visit(PrimitiveType type) {
		Boolean returnValue = new Boolean(true	);
		return false;
	}

	@Override
	public Boolean visit(UserType type) {
		Boolean returnValue = new Boolean(true);
		return false;
	}

	@Override
	public Boolean visit(Assignment assignment) {
		Boolean returnValue = new Boolean(true);
		returnValue &= assignment.getVariable().accept(this);
		returnValue &= assignment.getAssignment().accept(this);
		return false;
	}

	@Override
	public Boolean visit(CallStatement callStatement) {
		Boolean returnValue = new Boolean(true);
		returnValue &= callStatement.getCall().accept(this);
		return false;	
	}

	@Override
	public Boolean visit(Return returnStatement) {
		Boolean returnValue = new Boolean(true);
		if(returnStatement.hasValue()){
			returnValue &= returnStatement.getValue().accept(this);
		}
		returnInPath = true;
		return returnValue;
	}

	@Override
	public Boolean visit(If ifStatement) {
		Boolean returnValue = new Boolean(true);
		returnValue &= ifStatement.getCondition().accept(this);
		returnValue &= ifStatement.getOperation().accept(this);
		if(returnInPath){
			returnInPath = false;
		}else{
			return false;
		}
		if(ifStatement.hasElse()){
			returnValue &= ifStatement.getElseOperation().accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(While whileStatement) {
		Boolean returnValue = new Boolean(true);
		returnValue &= whileStatement.getOperation().accept(this);
		returnValue &= whileStatement.getCondition().accept(this);
		return returnValue;	
	}

	@Override
	public Boolean visit(Break breakStatement) {
		Boolean returnValue = new Boolean(true);
		return returnValue;
	}

	@Override
	public Boolean visit(Continue continueStatement) {
		Boolean returnValue = new Boolean(true);
		return returnValue;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock) {
		Boolean returnValue = new Boolean(true);
		if(statementsBlock.getStatements().size() > 0){
			returnValue &= statementsBlock.getStatements().get(statementsBlock.getStatements().size()-1).accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(LocalVariable localVariable) {
		Boolean returnValue = new Boolean(true);
		returnValue &= localVariable.getType().accept(this);
		if(localVariable.hasInitValue()){
			returnValue &= localVariable.getInitValue().accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(VariableLocation location) {
		Boolean returnValue = new Boolean(true);
		if(location.isExternal()){
			returnValue &= location.getLocation().accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(ArrayLocation location) {
		Boolean returnValue = new Boolean(true);
		returnValue &= location.getArray().accept(this);
		returnValue &= location.getIndex().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(StaticCall call) {
		Boolean returnValue = new Boolean(true);
		for(Expression e : call.getArguments()){
			returnValue &= e.accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(VirtualCall call) {
		Boolean returnValue = new Boolean(true);
		for(Expression e : call.getArguments()){
			returnValue &= e.accept(this);
		}
		if(call.isExternal()){
			returnValue &= call.getLocation().accept(this);
		}
		return returnValue;
	}

	@Override
	public Boolean visit(This thisExpression) {
		Boolean returnValue = new Boolean(true);
		return returnValue;
	}

	@Override
	public Boolean visit(NewClass newClass) {
		Boolean returnValue = new Boolean(true);
		return returnValue;
	}

	@Override
	public Boolean visit(NewArray newArray) {
		Boolean returnValue = new Boolean(true);
		returnValue &= newArray.getType().accept(this);
		returnValue &= newArray.getSize().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(Length length) {
		Boolean returnValue = new Boolean(true);
		returnValue &= length.getArray().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp) {
		Boolean returnValue = new Boolean(true);
		returnValue &= binaryOp.getFirstOperand().accept(this);
		returnValue &= binaryOp.getSecondOperand().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp) {
		Boolean returnValue = new Boolean(true);
		returnValue &= binaryOp.getFirstOperand().accept(this);
		returnValue &= binaryOp.getSecondOperand().accept(this);
		return returnValue;	
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp) {
		Boolean returnValue = new Boolean(true);
		returnValue &= unaryOp.getOperand().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp) {
		Boolean returnValue = new Boolean(true);
		returnValue &= unaryOp.getOperand().accept(this);
		return returnValue;
	}

	@Override
	public Boolean visit(Literal literal) {
		Boolean returnValue = new Boolean(true);
		return returnValue;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock) {
		Boolean returnValue = new Boolean(true);
		returnValue &= expressionBlock.getExpression().accept(this);
		return returnValue;	
	}

	@Override
	public Boolean visit(MethodType methodType) {
		Boolean returnValue = new Boolean(true);
		if(methodType.getReturnType().getName().compareTo("void") == 0){
			return true;
		}
		return false;
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

}
