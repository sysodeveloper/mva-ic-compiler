package IC.semanticChecks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import IC.BinaryOps;
import IC.AST.*;
import IC.myTypes.*;
import IC.mySymbolTable.*;
import IC.mySymbolTable.MySymbolRecord.Kind;



public class MyTypeBuilder implements PropagatingVisitor<Object, MyType> {
	private MySymbolTable globalScope;
	private MyTypeTable types;
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	private MyType intType;
	private MyType boolType;
	private MyType stringType;
	private MyType nullType;
	private MyType voidType;
	private MyType mainMethodType;
	//exactly one main
	private int mainMethods = 0;
	//for array new and reference
	private boolean fromNewArray = false;
	private boolean fromVariableLocation = false;
	private boolean TypeOK(MyType t1, MyType t2){
		//left t1, right t2
		if(t1 == t2){
			return true;
		}else if(t2.subtypeOf(t1)){
			return true;
		}
		return false;
	}
	
	public boolean hasErrors(){
		return (this.semanticErrors.size() > 0);
	}
	
	public MyTypeBuilder(MyTypeTable table){
		this.types = table;
		intType = table.insertType(new MyIntType());
		boolType = table.insertType(new MyBoolType());
		stringType = table.insertType(new MyStringType());
		voidType = table.insertType(new MyVoidType());
		//main
		mainMethodType = new MyMethodType();
		ArrayList<MyType> mainParams = new ArrayList<MyType>();
		MyArrayType arrParam = new MyArrayType();
		arrParam.setElementType(stringType);
		arrParam.setDimention(1);
		arrParam.setFullName();
		mainParams.add(arrParam);
		MyMethodType mainType = new MyMethodType();
		mainType.setParamTypes(mainParams);
		mainType.setReturnType(voidType);
		mainType.setFullName();
		mainMethodType = mainType;
	}
	@Override
	public MyType visit(Program program, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		globalScope = program.enclosingScope();
		for(ICClass c : program.getClasses()){
			c.accept(this,d);
		}
		if(mainMethods != 1){
			semanticErrors.add(new SemanticError("Entry method is undefined",program.getLine()));
			return voidType;
		}
		return voidType;
	}
	@Override
	public MyType visit(ICClass icClass, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		icClass.getUserType().accept(this,d);
		for(Field f : icClass.getFields()){
			f.accept(this,d);
		}
		for(Method m: icClass.getMethods()){
			m.accept(this,d);
		}
		MyType type = icClass.getRecord().getMyType();
		icClass.setTypeFromTable(type);
		return type;
	}
	@Override
	public MyType visit(Field field, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MyType type = field.getRecord().getMyType();
		field.setTypeFromTable(type);
		return type;
	}

	public MyType visit(Method method, Object d){
		fromNewArray = false;
		fromVariableLocation = false;
		
		if(mainMethods > 1){
			semanticErrors.add(new SemanticError("Cannot have more than one main method",method.getLine()));
			return voidType;
		}
		method.getType().accept(this,d);
		for(Formal f : method.getFormals()){
			f.accept(this,d);
		}
		for(Statement s : method.getStatements()){
			s.accept(this,d);
		}
		MyType type = method.getRecord().getMyType();
		method.setTypeFromTable(type);
		return type;
	}
	@Override
	public MyType visit(VirtualMethod method, Object d) {
		MyType mtype = visit((Method)method,d);
		method.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(StaticMethod method, Object d) {
		MyType fromTable = types.insertType(method.getRecord().getMyType());
		if(fromTable.getName().compareTo(mainMethodType.getName()) == 0){
			mainMethods++;
		}
		MyType mtype =visit((Method)method,d);
		method.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(LibraryMethod method, Object d) {
		MyType mtype = visit((Method)method,d);
		method.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(Formal formal, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MyType mtype = formal.getType().accept(this,d);
		formal.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(PrimitiveType type, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MyType mtype = types.insertType(type.getMyType());
		type.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(UserType type, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MyType mtype = types.insertType(type.getMyType());
		type.setTypeFromTable(mtype);
		return mtype;
	}
	@Override
	public MyType visit(Assignment assignment, Object d) {
		if(hasErrors()){
			return voidType;
		}
		fromNewArray = false;
		fromVariableLocation = false;
		MyType varType = types.insertType(assignment.getVariable().accept(this,d));
		MyType exprType = types.insertType(assignment.getAssignment().accept(this,d));
		if(TypeOK(varType,exprType)){
			assignment.setTypeFromTable(varType);
			return varType;
		}

		semanticErrors.add(new SemanticError("Variable of type "+varType.getName()+" could not be assigned in type "+exprType.getName(),assignment.getLine()));

		//semanticErrors.add(new SemanticError("Cannot assign different types " + varType.getName() + " = " + exprType.getName(),assignment.getLine()));

		return voidType;
	}
	@Override
	public MyType visit(CallStatement callStatement, Object d) {
		// TODO Auto-generated method stub
		fromNewArray = false;
		fromVariableLocation = false;
		MyType type = callStatement.getCall().accept(this, d);
		callStatement.setTypeFromTable(type);
		return type;
	
	}
	@Override
	public MyType visit(Return returnStatement, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MySymbolRecord retRecord = returnStatement.enclosingScope().Lookup("$ret");
		if(retRecord == null){
			System.out.println("MEGA ERROR!");
		}
		MyType methodType = retRecord.getMyType();
		
		if(returnStatement.hasValue()){
			MyType ret = types.insertType(returnStatement.getValue().accept(this,d));
			if(TypeOK(methodType, ret)){
				returnStatement.setTypeFromTable(methodType);
				return methodType;
			}
			semanticErrors.add(new SemanticError("Return value type "+ret.getName()+" doesn't match function return type "+methodType.getName(),returnStatement.getLine()));
			return voidType;
		}
		//void		
		if(methodType==voidType){
			return voidType;
		}
		semanticErrors.add(new SemanticError("Function return statement could not be empty",returnStatement.getLine()));
		return voidType;
	}
	@Override
	public MyType visit(If ifStatement, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		//Check that the condition is boolean type
		MyType conditionType = ifStatement.getCondition().accept(this,d);
		if(!TypeOK(conditionType,boolType)){
			semanticErrors.add(new SemanticError("If condition should be boolean type and not " + conditionType.getName(), ifStatement.getLine()));
		}
		//Continue traversing the tree
		
		if(ifStatement.hasElse()){
			ifStatement.getElseOperation().accept(this,d);
		}
		ifStatement.setTypeFromTable(conditionType);
		return conditionType;
	}
	@Override
	public MyType visit(While whileStatement, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		//Check that the condition is boolean type
		MyType conditionType = whileStatement.getCondition().accept(this,d);
		if(!TypeOK(conditionType,boolType)){
			semanticErrors.add(new SemanticError("While condition should be boolean type and not " + conditionType.getName(), whileStatement.getLine()));
		}
		//Continue traversing the tree
		whileStatement.getOperation().accept(this,d);
		whileStatement.setTypeFromTable(conditionType);
		return conditionType;
	}
	@Override
	public MyType visit(Break breakStatement, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		// TODO Auto-generated method stub
		return voidType;
	}
	@Override
	public MyType visit(Continue continueStatement, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		// TODO Auto-generated method stub
		return voidType;
	}
	@Override
	public MyType visit(StatementsBlock statementsBlock, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		for(Statement s : statementsBlock.getStatements())
			s.accept(this, d);
		return voidType; // maybe we need to return void here ???
	}
	@Override
	public MyType visit(LocalVariable localVariable, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MyType varType =  localVariable.getRecord().getMyType();
		if(!localVariable.hasInitValue())
			return varType;
		MyType initType =  localVariable.getInitValue().accept(this, d);
		if(hasErrors()){
			return voidType;
		}
		if(!TypeOK(varType, initType)){
			semanticErrors.add(new SemanticError("Type of initializing expresion "+initType.getName()+" didnt match variable type "+varType.getName(),localVariable.getLine()));
			return voidType;
		}
		localVariable.setTypeFromTable(varType);
		return varType;
	}
	@Override
	public MyType visit(VariableLocation location, Object d) {
		if(hasErrors()){
			return voidType;
		}
		fromNewArray = false;
		fromVariableLocation = false;
		this.fromVariableLocation = true;
		//System.out.println("FROM VARIABLE LOCATION ");
		if(location.isExternal()){// need to check if exists in external context
			MyType t = location.getLocation().accept(this, d);
			MyType locType = types.insertType(t);
			
			//current class field
			if(location.getLocation() instanceof This){
				MyType type = getVarType(location.getName(),location.enclosingScope(),Kind.Field);
				location.setTypeFromTable(type);
				return type;
			}
			//field of other class  need to check if exists
			MySymbolRecord externalField = ((MyClassType)locType).getClassAST().enclosingScope().Lookup(location.getName());
			if(externalField == null){
				semanticErrors.add(new SemanticError("Field with the name "+location.getName()+" does not exist", location.getLine()));
				return voidType;
			}
			MyType type = externalField.getMyType();
			location.setTypeFromTable(type);
			return type;
		
		}	
		MyType type = location.enclosingScope().Lookup(location.getName()).getMyType();
		location.setTypeFromTable(type);
		return type;
	}
	@Override
	public MyType visit(ArrayLocation location, Object d) {
		if(hasErrors()){
			return voidType;
		}
		//DO NOT !!
		//fromNewArray = false;
		//fromVariableLocation = false;
		if(location.getIndex().accept(this, d) != intType){
			if(hasErrors()){
				return voidType;
			}
			semanticErrors.add(new SemanticError("Type of array index expression can be only int type",location.getLine()));
			return voidType;
		}

		MyType mtype = location.getArray().accept(this, d); 
			if(!(mtype instanceof MyArrayType)){
				if(hasErrors()){
					return voidType;
				}
				semanticErrors.add(new SemanticError("Type of the expression must be an array type, not "+mtype.getName(), location.getLine()));
				return voidType;
			}
			//return ((MyArrayType)mtype).getElementType();		
        if(this.fromVariableLocation){
			if(mtype.getDimention() > 1){
				//array type - get new from the table
				MyArrayType newType = (MyArrayType) mtype.clone();
/*				MyType elementType = ((MyArrayType)mtype).getElementType();  
				newType.setElementType(elementType);
				newType.setFullName();*/
				newType.setDimention(newType.getDimention()-1);
				MyType ret = types.insertType(newType);
				location.setTypeFromTable(ret);
				return ret;
			}else if(mtype.getDimention() == 1){
				//now array must be converted to base type
				MyType ret =  types.insertType(((MyArrayType)mtype).getElementType().clone());
				location.setTypeFromTable(ret);
				return ret;
			}
        }else if(this.fromNewArray){
			//array type - get new from the table
			MyArrayType newType = (MyArrayType) mtype.clone();
			newType.setDimention(newType.getDimention()+1);
			newType.setFullName();
			MyType type = types.insertType(newType); 
			location.setTypeFromTable(type);
			return type;       	
        }
       
		location.setTypeFromTable(mtype);
		return mtype;

	}
	@Override
	public MyType visit(StaticCall call, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
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
			if(!TypeOK(formalType,callArgs.get(i).accept(this, d))){
				semanticErrors.add(new SemanticError("function "+call.getName()+" expects parameter of type "+formalType.getName()+" as argument number "+(i+1),call.getLine()));
				return voidType;
			}
		}
		MyType type =  func.getNode().enclosingScope().Lookup("$ret").getMyType();
		call.setTypeFromTable(func.getMyType());
		return type;
	}
	@Override
	public MyType visit(VirtualCall call, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MySymbolRecord func = null;
		if(call.isExternal()){ // need to check if this function exists in external context
			MyType exType = call.getLocation().accept(this, d);
			if(!(exType instanceof MyClassType) && !(call.getLocation() instanceof This)){
				semanticErrors.add(new SemanticError("Type of location expression for function calling can be only user defined type ot this keyword",call.getLine()));
				return voidType;
			}
			// search for that function in the specified class
			if(exType instanceof MyClassType){
				exType = types.insertType(exType);
				func = ((MyClassType)exType).getClassAST().enclosingScope().Lookup(call.getName());
				if(func == null){
					semanticErrors.add(new SemanticError("function with the name "+call.getName()+"does not exsists in class "+exType.getName(),call.getLine()));
					return voidType;
				}		
			}
			
			if(call.getLocation() instanceof This)
				func = call.enclosingScope().Lookup(call.getName());
		}
		else{  // function exists in current context, checked before by analyzer
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
			MyType formalType = types.insertType(funcFormals.get(i).accept(this, d));
			//if(!callArgs.get(i).accept(this, d).subtypeOf(formalType)){
			MyType argType = types.insertType(callArgs.get(i).accept(this, d));
			if(!TypeOK(formalType,argType)){
				semanticErrors.add(new SemanticError("function "+call.getName()+" expects parameter of type "+formalType.getName()+" as argument number "+(i+1),call.getLine()));
				return voidType;
			}
		}
		MyType type=func.getNode().enclosingScope().Lookup("$ret").getMyType();
		call.setTypeFromTable(func.getMyType());
		return type;
	}
	@Override
	public MyType visit(This thisExpression, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MySymbolTable enc = thisExpression.enclosingScope();
		while (enc.getParent() != globalScope){
			enc = enc.getParent();
		}
		MySymbolRecord rec = enc.Lookup(enc.getDescription());
		if(rec != null){
			MyType type=rec.getMyType();
			thisExpression.setTypeFromTable(type);
			return type;
		}
		return voidType;
	}
	@Override
	public MyType visit(NewClass newClass, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MyClassType c = new MyClassType();
		c.setName(newClass.getName());
		MyType type = types.insertType(c);
		newClass.setTypeFromTable(type);
		return type;
	}
	@Override
	public MyType visit(NewArray newArray, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		if(newArray.getSize().accept(this, d) != intType){
			semanticErrors.add(new SemanticError("Type of array size expression can be only int type",newArray.getLine()));
			return voidType;
		}
		MyType baseType = newArray.getType().accept(this, d);
		MyArrayType arr = new MyArrayType();
		arr.setElementType(baseType.clone());
		arr.setDimention(1);
		arr.setFullName();
		this.fromNewArray = true;
		MyType type = this.types.insertType(arr);
		newArray.setTypeFromTable(type);
		return type;

	}
	@Override
	public MyType visit(Length length, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MyType expType = length.getArray().accept(this, d);
		if(!(expType instanceof MyArrayType)){
			semanticErrors.add(new SemanticError("length can be applyed only to array types",length.getLine()));
			return voidType;
		}
		length.setTypeFromTable(intType);
		return intType;
	}
	@Override
	public MyType visit(MathBinaryOp binaryOp, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MyType leftType = binaryOp.getFirstOperand().accept(this, d);
		MyType rightType = binaryOp.getSecondOperand().accept(this, d);
		if(hasErrors()){
			return voidType;
		}
		
		if(leftType != rightType){		
			semanticErrors.add(new SemanticError("Cannot perform math operation on different types",binaryOp.getLine()));
			return voidType;
		}
		//special case for strings 
		if(leftType == stringType){
			if(binaryOp.getOperator()==BinaryOps.PLUS){
				binaryOp.setTypeFromTable(leftType);
				return leftType;
			}
			semanticErrors.add(new SemanticError("Cannot perform "+binaryOp.getOperator().getDescription()+ " operation on string types",binaryOp.getLine()));
			return voidType;
		}
		if(leftType != intType){ // math operation on types which are not integers
			semanticErrors.add(new SemanticError("Cannot perform math operation on "+leftType.getName() ,binaryOp.getLine()));
			return voidType;
		}
		binaryOp.setTypeFromTable(leftType);
		return leftType;
	}
	@Override
	public MyType visit(LogicalBinaryOp binaryOp, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
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
			binaryOp.setTypeFromTable(boolType);
			return boolType;
		}
		// binaryOps && , || - only on booleans 
		if(binaryOp.getOperator() == BinaryOps.LAND || binaryOp.getOperator() == BinaryOps.LOR){
			if(leftType != boolType || rightType != boolType){
				semanticErrors.add(new SemanticError("Can perform "+binaryOp.getOperator().getDescription()+" operation only on boolean types",binaryOp.getLine()));
				return voidType;
			}
			binaryOp.setTypeFromTable(boolType);
			return boolType;
			
		}
		
		//other cases: <=,<,>=,> ,  only on integers
		if(!LRtypesOK || !RLtypesOK || leftType!=intType){
			semanticErrors.add(new SemanticError("Can perform "+binaryOp.getOperator().getDescription()+" operation only on int types",binaryOp.getLine()));
			return voidType;
		}
		binaryOp.setTypeFromTable(boolType);
		return boolType;
	}
	@Override
	public MyType visit(MathUnaryOp unaryOp, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		
		MyType operandType = unaryOp.getOperand().accept(this, new Boolean(true));
		
		if(operandType != intType){
			semanticErrors.add(new SemanticError("Can perform "+unaryOp.getOperator().getDescription()+" operation only on int types",unaryOp.getLine()));
			return voidType;
		}
		unaryOp.setTypeFromTable(intType);
		return intType;
	}
	@Override
	public MyType visit(LogicalUnaryOp unaryOp, Object d) {
		MyType operandType = unaryOp.getOperand().accept(this, d);
		if(operandType != boolType){
			semanticErrors.add(new SemanticError("Can perform "+unaryOp.getOperator().getDescription()+" operation only on boolean types",unaryOp.getLine()));
			return voidType;
		}
		unaryOp.setTypeFromTable(boolType);
		return boolType;
	}
	@Override
	public MyType visit(Literal literal, Object d) {
		fromNewArray = false;
		fromVariableLocation = false;
		MyType type =  types.insertType(literal.getMyType());
		if(type!=intType){
			literal.setTypeFromTable(type);
			return type;
		}
		try{
			Boolean minusBefore = new Boolean(false);
			if(d instanceof Boolean){
				minusBefore = ((Boolean)d).booleanValue();
			}
			int value = 0;
			if(!minusBefore){
				 value = Integer.parseInt(((String)literal.getValue()));	
			}else{
				value = Integer.parseInt(((String)"-"+literal.getValue()));
			}
		}catch(NumberFormatException e){
			semanticErrors.add(new SemanticError("the number is out of range",literal.getLine()));
			return voidType;
		}
		literal.setTypeFromTable(type);
		return type;
	}
	@Override
	public MyType visit(ExpressionBlock expressionBlock, Object d) {
		if(hasErrors()){
			return voidType;
		}
		MyType type = expressionBlock.getExpression().accept(this, d);
		expressionBlock.setTypeFromTable(type);
		return  type;
	}
	@Override
	public MyType visit(MethodType methodType, Object d) {
		// TODO Auto-generated method stub
		fromNewArray = false;
		fromVariableLocation = false;
		
		return voidType;
	}
	
	private MyType getVarType(String varName, MySymbolTable scope,Kind varKind){
		
		while(scope!=null){
			Map<String,MySymbolRecord> records = scope.getEntries();
			if(records.containsKey(varName) && 
					(records.get(varName).getKind()==varKind))
				return records.get(varName).getMyType();
			scope = scope.getParent();
		}
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
}
