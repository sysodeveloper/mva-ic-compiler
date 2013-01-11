package IC;

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
	private MyType intType;
	private MyType boolType;
	private MyType stringType;
	private MyType nullType;
	private MyType voidType;
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
		intType = table.insertType(new MyIntType());
		boolType = table.insertType(new MyBoolType());
		stringType = table.insertType(new MyStringType());
		nullType = table.insertType(new MyNullType());
		voidType = table.insertType(new MyVoidType());
	}
	@Override
	public MyType visit(Program program, Object d) {
		for(ICClass c : program.getClasses()){
			c.accept(this,d);
		}
		return voidType;
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
		return icClass.getRecord().getMyType();
	}
	@Override
	public MyType visit(Field field, Object d) {
		return field.getRecord().getMyType();
	}

	public MyType visit(Method method, Object d){
		method.getType().accept(this,d);
		for(Formal f : method.getFormals()){
			f.accept(this,d);
		}
		for(Statement s : method.getStatements()){
			s.accept(this,d);
		}
		return method.getRecord().getMyType();
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
			if(varType == intType){ // here var and assignment are integers
				
			}
			return varType;
		}
		semanticErrors.add(new SemanticError("Cannot assign different types",assignment.getLine()));
		return voidType;
	}
	@Override
	public MyType visit(CallStatement callStatement, Object d) {
		// TODO Auto-generated method stub
		return callStatement.getCall().accept(this, d);
	
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
			return voidType;
		}
		//void		
		if(methodType==voidType){
			return voidType;
		}
		semanticErrors.add(new SemanticError("Cannot return different types",returnStatement.getLine()));
		return voidType;
	}
	@Override
	public MyType visit(If ifStatement, Object d) {
		//Check that the condition is boolean type
		MyType conditionType = ifStatement.getCondition().accept(this,d);
		if(!TypeOK(conditionType,boolType)){
			semanticErrors.add(new SemanticError("If condition should be boolean type and not " + conditionType.getName(), ifStatement.getLine()));
		}
		//Continue traversing the tree
		ifStatement.getElseOperation().accept(this,d);
		if(ifStatement.hasElse()){
			ifStatement.getElseOperation().accept(this,d);
		}
		return conditionType;
	}
	@Override
	public MyType visit(While whileStatement, Object d) {
		//Check that the condition is boolean type
		MyType conditionType = whileStatement.getCondition().accept(this,d);
		if(!TypeOK(conditionType,boolType)){
			semanticErrors.add(new SemanticError("While condition should be boolean type and not " + conditionType.getName(), whileStatement.getLine()));
		}
		//Continue traversing the tree
		whileStatement.getOperation().accept(this,d);
		return conditionType;
	}
	@Override
	public MyType visit(Break breakStatement, Object d) {
		// TODO Auto-generated method stub
		return voidType;
	}
	@Override
	public MyType visit(Continue continueStatement, Object d) {
		// TODO Auto-generated method stub
		return voidType;
	}
	@Override
	public MyType visit(StatementsBlock statementsBlock, Object d) {
		for(Statement s : statementsBlock.getStatements())
			s.accept(this, d);
		return voidType; // maybe we need to return void here ???
	}
	@Override
	public MyType visit(LocalVariable localVariable, Object d) {
		MyType varType =  localVariable.getRecord().getMyType();
		if(!localVariable.hasInitValue())
			return varType;
		MyType initType =  localVariable.getInitValue().accept(this, d);
		if(!TypeOK(varType, initType)){
			semanticErrors.add(new SemanticError("Type of initializing expresion "+initType.getName()+" didnt match variable type "+varType.getName(),localVariable.getLine()));
			return voidType;
		}
		return varType;
	}
	@Override
	public MyType visit(VariableLocation location, Object d) {
		if(location.isExternal())
			return location.getLocation().accept(this, d);
		return location.enclosingScope().Lookup(location.getName()).getMyType();
	}
	@Override
	public MyType visit(ArrayLocation location, Object d) {
		if(location.getIndex().accept(this, d) != intType){
			semanticErrors.add(new SemanticError("Type of array index expression can be only int type",location.getLine()));
			return voidType;
		}
		return location.getArray().accept(this, d);
	}
	@Override
	public MyType visit(StaticCall call, Object d) {
		MySymbolRecord func = null;
		MyClassType tempClass = new MyClassType();
		tempClass.setName(call.getClassName());
		MyType stClass = types.insertType(tempClass); // get the class type object of the static call class
		func = ((MyClassType)stClass).getClassAST().enclosingScope().Lookup(call.getName()); // static function exists, checked before in analyzer
		
		List<Formal> funcFormals = ((Method)func.getNode()).getFormals();
		List<Expression> callArgs = call.getArguments();
		
		if(callArgs.size() != funcFormals.size()){
			semanticErrors.add(new SemanticError("function "+call.getName()+" expects "+funcFormals.size()+" arguments",call.getLine()));
			return voidType;
		}
		// check every argument
		for(int i=0;i<funcFormals.size();i++){
			MyType formalType = funcFormals.get(i).accept(this, d);
			if(formalType!= callArgs.get(i).accept(this, d)){
				semanticErrors.add(new SemanticError("function "+call.getName()+" expects parameter of type "+formalType.getName()+" as argument number "+(i+1),call.getLine()));
				return voidType;
			}
		}
		
		return func.getNode().enclosingScope().Lookup("$ret").getMyType();
	}
	@Override
	public MyType visit(VirtualCall call, Object d) {
		MySymbolRecord func = null;
		if(call.isExternal()){ // need to check if this function exists in external context
			MyType exType = call.getLocation().accept(this, d);
			if(!(exType instanceof MyClassType)){
				semanticErrors.add(new SemanticError("Type of location expression can be only user defined type",call.getLine()));
				return voidType;
			}
			// search for that function in the specified class
			exType = types.insertType(exType);
			func = ((MyClassType)exType).getClassAST().enclosingScope().Lookup(call.getName());
			if(func == null){
				semanticErrors.add(new SemanticError("function with the name "+call.getName()+"does not exsists in class "+exType.getName(),call.getLine()));
				return voidType;
			}			
		}
		else{ // function exists in current context, checked before by analyzer
			func = call.enclosingScope().Lookup(call.getName());
		}
		// check that types of formals match the types that the function expects 
		List<Formal> funcFormals = ((Method)func.getNode()).getFormals();
		List<Expression> callArgs = call.getArguments();
		
		if(callArgs.size() != funcFormals.size()){
			semanticErrors.add(new SemanticError("function "+call.getName()+" expects "+funcFormals.size()+" arguments",call.getLine()));
			return voidType;
		}
		// check every argument
		for(int i=0;i<funcFormals.size();i++){
			MyType formalType = funcFormals.get(i).accept(this, d);
			if(formalType!= callArgs.get(i).accept(this, d)){
				semanticErrors.add(new SemanticError("function "+call.getName()+" expects parameter of type "+formalType.getName()+" as argument number "+(i+1),call.getLine()));
				return voidType;
			}
		}
		
		return func.getNode().enclosingScope().Lookup("$ret").getMyType();
	}
	@Override
	public MyType visit(This thisExpression, Object d) {
		//maybe we need this type to be the current class type? It is not one of the rules...
		return voidType;
	}
	@Override
	public MyType visit(NewClass newClass, Object d) {
		MyClassType c = new MyClassType();
		c.setName(newClass.getName());
		return types.insertType(c);
	}
	@Override
	public MyType visit(NewArray newArray, Object d) {
		if(newArray.getSize().accept(this, d) != intType){
			semanticErrors.add(new SemanticError("Type of array size expression can be only int type",newArray.getLine()));
			return voidType;
		}
		
		MyType baseType = newArray.getType().accept(this, d);		
		MyArrayType tempArray = new MyArrayType();
		tempArray.setElementType(baseType);
		tempArray.setDimantion(baseType.getDimention());
		
		return types.insertType(tempArray);
	}
	@Override
	public MyType visit(Length length, Object d) {
		MyType expType = length.getArray().accept(this, d);
		if(!(expType instanceof MyArrayType)){
			semanticErrors.add(new SemanticError("length can be applyed only to array types",length.getLine()));
			return voidType;
		}
		return intType;
	}
	@Override
	public MyType visit(MathBinaryOp binaryOp, Object d) {
		
		MyType leftType = binaryOp.getFirstOperand().accept(this, d);
		MyType rightType = binaryOp.getSecondOperand().accept(this, d);
		
		if(leftType != rightType){		
			semanticErrors.add(new SemanticError("Cannot perform math operation on different types",binaryOp.getLine()));
			return voidType;
		}
		//special case for strings 
		if(leftType == stringType){
			if(binaryOp.getOperator()==BinaryOps.PLUS)
				return leftType;
			semanticErrors.add(new SemanticError("Cannot perform "+binaryOp.getOperator().getDescription()+ " operation on string types",binaryOp.getLine()));
			return voidType;
		}
		if(leftType != intType){ // math operation on types which are not integers
			semanticErrors.add(new SemanticError("Cannot perform math operation on "+leftType.getName() ,binaryOp.getLine()));
			return voidType;
		}		
		return leftType;
	}
	@Override
	public MyType visit(LogicalBinaryOp binaryOp, Object d) {
		MyType leftType = binaryOp.getFirstOperand().accept(this, d);
		MyType rightType = binaryOp.getSecondOperand().accept(this, d);
		boolean LRtypesOK = TypeOK(leftType, rightType);
		boolean RLtypesOK = TypeOK(rightType, leftType);
		
		// binaryOps == , !=  
		if(binaryOp.getOperator() == BinaryOps.EQUAL || binaryOp.getOperator() == BinaryOps.NEQUAL){
			if(!LRtypesOK && !RLtypesOK){ // T0 not <= T1 AND T1 not <=T0
				semanticErrors.add(new SemanticError("Cannot perform logical operation on different types",binaryOp.getLine()));
				return voidType;
			}
			
		}
		// binaryOps && , || - only on booleans 
		if(binaryOp.getOperator() == BinaryOps.LAND || binaryOp.getOperator() == BinaryOps.LOR){
			if(leftType != boolType || rightType != boolType){
				semanticErrors.add(new SemanticError("Can perform "+binaryOp.getOperator().getDescription()+" operation only on boolean types",binaryOp.getLine()));
				return voidType;
			}
			return boolType;
			
		}
		
		//other cases: <=,<,>=,> ,  only on integers
		if(!LRtypesOK || !RLtypesOK || leftType!=intType){
			semanticErrors.add(new SemanticError("Can perform "+binaryOp.getOperator().getDescription()+" operation only on int types",binaryOp.getLine()));
			return voidType;
		}
		
		return boolType;
	}
	@Override
	public MyType visit(MathUnaryOp unaryOp, Object d) {
		MyType operandType = unaryOp.getOperand().accept(this, d);
		
		if(operandType != intType){
			semanticErrors.add(new SemanticError("Can perform "+unaryOp.getOperator().getDescription()+" operation only on int types",unaryOp.getLine()));
			return voidType;
		}
		return intType;
	}
	@Override
	public MyType visit(LogicalUnaryOp unaryOp, Object d) {
		MyType operandType = unaryOp.getOperand().accept(this, d);
		if(operandType != boolType){
			semanticErrors.add(new SemanticError("Can perform "+unaryOp.getOperator().getDescription()+" operation only on boolean types",unaryOp.getLine()));
			return voidType;
		}
		return boolType;
	}
	@Override
	public MyType visit(Literal literal, Object d) {
		MyType type =  types.insertType(literal.getMyType());
		if(type!=intType)
			return type;
		try{
			int value = Integer.parseInt(((String)literal.getValue()));
		}catch(NumberFormatException e){
			semanticErrors.add(new SemanticError("the number is out of range",literal.getLine()));
			return voidType;
		}		
		return type;
	}
	@Override
	public MyType visit(ExpressionBlock expressionBlock, Object d) {
		
		return  expressionBlock.getExpression().accept(this, d);
	}
	@Override
	public MyType visit(MethodType methodType, Object d) {
		// TODO Auto-generated method stub
		return voidType;
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
