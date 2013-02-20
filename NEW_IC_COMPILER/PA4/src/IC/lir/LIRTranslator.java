package IC.lir;

import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.BinaryOps;
import IC.LiteralTypes;
import IC.AST.ASTNode;
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
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;
import IC.mySymbolTable.MySymbolRecord;
import IC.mySymbolTable.MySymbolTable;
import IC.mySymbolTable.MySymbolRecord.Kind;

import IC.myTypes.MyClassType;
import IC.myTypes.MyType;
import IC.myTypes.MyVoidType;



public class LIRTranslator implements PropagatingVisitor<DownType, UpType>{
	
	LayoutsManager layoutManager;
	//Global instructions
	private Map<String,String> dispatchNames;
	private List<String> dispatchVectors;
	private List<String> stringLiterals;
	private Instructions spec;
	UpType upInfo;
	//String Literals
	Map<String,String> stringNames;
	private List<String> instructions;
	//While labels for continue and break
	private String whileBeginLoop;
	private String whileEndLoop;
	
	//Naming conventions
	private int variableUnique;
	private int parameterUnique;
	private int fieldUnique;
	private int labelUnique;
	private int stringLiteralUnique;
	//Global symbol table
	private MySymbolTable globalScope;	
	String resultRegister=null; // to avoid allocating in every visit
	
	private String getVariableTranslationName(String varName,MySymbolTable enclosingScope){
		int id = enclosingScope.Lookup(varName).getId();
		return "v"+id + varName;
	}
	
	private String getParameterTranslationName(String parName,MySymbolTable enclosingScope){
		int id = enclosingScope.Lookup(parName).getId();
		return "p"+id + parName;
	}
	
	private String getFieldTranslationName(String fieldName,MySymbolTable enclosingScope){
		int id = enclosingScope.Lookup(fieldName).getId();
		return "f"+id + fieldName;
	}	
	
	private String getLabelName(String labelName){
		return labelName+(labelUnique++);
	}
	
	private String getStringLiteralTranslationName(String strLiteral){
		return "str"+stringLiteralUnique+": "+"''"+strLiteral+"''";
	}
	//Comments
	private String makeComment(String str){ 
		return "#"+str;
	}
	
	//Start of visitor
	public LIRTranslator(LayoutsManager layoutManager){
		variableUnique = 0;
		parameterUnique = 0;
		fieldUnique = 0;
		labelUnique = 0;
		stringLiteralUnique =0;
		this.layoutManager = layoutManager;
		this.stringLiterals = new ArrayList<String>();
		this.dispatchVectors = new ArrayList<String>();
		this.upInfo = new UpType();
		stringNames = new HashMap<String, String>();
		dispatchNames = new HashMap<String, String>();		
		whileBeginLoop = null;
		whileEndLoop = null;		
		globalScope = null;		
		instructions = new ArrayList<String>();
		spec = new Instructions();
	}
	
	

	@Override
	public UpType visit(Program program, DownType d) {
		DownType down = new DownType(null, false, program, -1);			
		
		globalScope = program.enclosingScope();			
		for(ICClass c : program.getClasses()){			
			if(c.accept(this,down)==null)
				return null;
		}
		List<String> tempInst = new ArrayList<String>(); 	
		/* Program Translation */
		tempInst.add(makeComment("A new program begins..."));
		tempInst.add(makeComment(""));
		tempInst.add(makeComment(""));
		tempInst.add(makeComment("String Literals"));
		tempInst.addAll(stringLiterals);
		tempInst.add(makeComment("Dispatch Vectors"));
		tempInst.addAll(dispatchVectors);		
		instructions.addAll(0, tempInst);
		
		return new UpType();				
	}

	@Override
	public UpType visit(ICClass icClass, DownType d) {
		d.prevNode = icClass;
		instructions.add(makeComment("Class " + icClass.getName()));
		/* Class layout */
		ClassLayout cl =  layoutManager.getClassLayout(icClass.getName());
		d.currentClassLayout = cl; //To check with Ahia 
		/* Class Dispatch Vector */
		if(cl.hasVirtaulMethos()){
			dispatchVectors.add(cl.printDispatchVector());
			dispatchNames.put(icClass.getName(), cl.printDispatchVector());
		}
		UpType upType;
		/* Class Translation */
		for(Field f : icClass.getFields()){
			upType = f.accept(this,d);
			if(upType == null) return null;
		}
		for(Method m : icClass.getMethods()){
			upType = m.accept(this,d);
			/* Registers are freed after each method */
			if(upType == null) return null;
		}
		return new UpType();		
	}

	@Override
	public UpType visit(Field field, DownType d) {
		return new UpType();
	}

	@Override
	public UpType visit(VirtualMethod method, DownType d) {
		d.prevNode = method;
		instructions.add(makeComment("Virtual Method " + method.getName()));
		/* Allocate Registers */
		d.startScope();
		/* Method Label */
		instructions.add(d.currentClassLayout.makeSymbolicName(method.getName()+":"));
		/* Method Translation */
		UpType upType = method.getType().accept(this,d);
		if(upType == null) return null;
		for(Formal f : method.getFormals()){
			upType = f.accept(this,d);
			if(upType == null) return null;	
		}
		for(Statement s : method.getStatements()){
			upType = s.accept(this,d);
			if(upType == null) return null;
		}
		/* Method Return If Void */
		if(method.getReturnType().getName().compareTo("void") == 0){
			instructions.add("Return 9999");
		}
		/* Free Register Allocated */
		d.freeRegister();
		UpType up = new UpType();
		return up;
	}

	@Override
	public UpType visit(StaticMethod method, DownType d) {
		d.prevNode = method;
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Static Method " + method.getName()));
		/* Allocate Registers */
		d.startScope();
		/* Method Label */
		instructions.add(d.currentClassLayout.makeSymbolicName(method.getName()+":"));
		/* Method Translation */
		UpType upType = method.getType().accept(this,d);
		if(upType == null) return null;
		for(Formal f : method.getFormals()){
			upType = f.accept(this,d);
			if(upType == null) return null;
		}
		for(Statement s : method.getStatements()){
			upType = s.accept(this,d);
			if(upType == null) return null;
		}
		/* Method Return If Void */
		if(method.getReturnType().getName().compareTo("void") == 0){
			instructions.add("Return 9999");
		}
		/* Free Register Allocated */
		d.freeRegister();
		return new UpType();
	}

	@Override
	public UpType visit(LibraryMethod method, DownType d) {
		/* No Translation For Library Method */
		return new UpType();
	}

	@Override
	public UpType visit(Formal formal, DownType d) {
		formal.getType().accept(this,d);
		return new UpType();
	}

	@Override
	public UpType visit(PrimitiveType type, DownType d) {
		return new UpType();
	}

	@Override
	public UpType visit(UserType type, DownType d) {
		return new UpType();		
	}

	@Override
	public UpType visit(Assignment assignment, DownType d) {
		d.prevNode = assignment;
		instructions.add(makeComment("Assignment Line " + assignment.getLine()));
		/* Expression */
		d.loadOrStore = false;	//load
		UpType upTypeExpr = assignment.getAssignment().accept(this,d);
		if(upTypeExpr == null) return null;
		/* Variable */
		d.loadOrStore = true;
		d.downRegister = upTypeExpr.getTarget();
		UpType upTypeVar = assignment.getVariable().accept(this,d);
		if(upTypeVar == null) return null;
		//reset loadOrStore
		d.loadOrStore = false;
		d.downRegister = null;
/*		Kind leftSide = assignment.getVariable().getRecord().getKind();
		switch(leftSide){
		case Local_Variable:
			instructions.add(spec.Move(upTypeExpr.getTarget(), d.nextRegister()));
			break;
		case Field:
			instructions.add(spec.MoveFieldStore(upTypeExpr.getTarget(), d.nextRegister(), assignmentLeftOffset+""));
			break;
		}
		resultRegister = registers.lastRegisterUsed();*/
		return new UpType();
	}

	@Override
	public UpType visit(CallStatement callStatement, DownType d) {
		d.prevNode = callStatement;		
		UpType upType = callStatement.getCall().accept(this,d);
		return new UpType(upType); // ---> I changed it, it was return new UpType(), need to talk about it
	}

	@Override
	public UpType visit(Return returnStatement, DownType d) {
		d.prevNode = returnStatement;
		instructions.add(makeComment("Return Line " + returnStatement.getLine()));
		if(returnStatement.hasValue()){
			UpType upType = returnStatement.getValue().accept(this,d);
			if(upType == null) return null;
			instructions.add(spec.Return(upType.getTarget()));
		}else{
			instructions.add(spec.Return("9999"));
		}
		return new UpType();
	}

	@Override
	public UpType visit(If ifStatement, DownType d) {
		d.prevNode = ifStatement;
		instructions.add(makeComment("If Line " + ifStatement.getLine()));
		UpType upTypeCond = ifStatement.getCondition().accept(this,d);
		if(upTypeCond == null) return null;
		instructions.add(spec.Compare("0", upTypeCond.getTarget()));
		if(ifStatement.hasElse()){
			String falseLabel = getLabelName("_false_label");
			String endLabel = getLabelName("_end_label");
			instructions.add(spec.JumpTrue(falseLabel));
			UpType upTypeOp = ifStatement.getOperation().accept(this,d);
			if(upTypeOp == null) return null;
			instructions.add(spec.Jump(endLabel));
			instructions.add(falseLabel);
			UpType upTypeElse = ifStatement.getElseOperation().accept(this,d);
			if(upTypeElse == null) return null;
			instructions.add(endLabel+":");
		}else{
			String endLabel = getLabelName("_end_label");
			instructions.add(spec.JumpTrue(endLabel));
			UpType upTypeOp = ifStatement.getOperation().accept(this,d);
			if(upTypeOp == null) return null;
			instructions.add(getLabelName(endLabel+":"));
		}
		return new UpType();	

	}

	@Override
	public UpType visit(While whileStatement, DownType d) {
		d.prevNode = whileStatement;
		instructions.add(makeComment("While Line " + whileStatement.getLine()));
		String testLabel = getLabelName("_test_label");
		String endLabel = getLabelName("_end_label");
		whileBeginLoop = testLabel;
		whileEndLoop = endLabel;
		instructions.add(testLabel+":");
		UpType upType = whileStatement.getCondition().accept(this,d);
		if(upType == null) return null;
		instructions.add(spec.Compare("0", upType.getTarget()));
		instructions.add(spec.JumpTrue(endLabel));
		upType = whileStatement.getOperation().accept(this,d);
		if(upType == null) return null;
		instructions.add(spec.Jump(testLabel));
		instructions.add(endLabel+":");
		return new UpType();
	}

	@Override
	public UpType visit(Break breakStatement, DownType d) {
		instructions.add(spec.Jump(whileEndLoop));
		return new UpType();
	}

	@Override
	public UpType visit(Continue continueStatement, DownType d) {
		instructions.add(spec.Jump(whileBeginLoop));
		return new UpType();
	}

	@Override
	public UpType visit(StatementsBlock statementsBlock, DownType d) {
		for(Statement s : statementsBlock.getStatements()){
			UpType upType = s.accept(this,d);
			if(upType == null) return null;
		}
		return new UpType();

	}

	@Override
	public UpType visit(LocalVariable localVariable, DownType d) {
		d.prevNode = localVariable;
		UpType upType = localVariable.getType().accept(this,d);
		if(upType == null) return null;
		if(localVariable.hasInitValue()){
			//int x = 5;
			upType = localVariable.getInitValue().accept(this,d);
			if(upType == null) return null;
			String name = getVariableTranslationName(localVariable.getName(),localVariable.enclosingScope());
			instructions.add(spec.Move(upType.getTarget(), name));
		}
		//int x - nothing...
		return new UpType();
	}

	@Override
	public UpType visit(VariableLocation location, DownType d) {
		ASTNode lastAST = d.prevNode;
		String downRegister = d.downRegister;
		boolean loadOrStore = d.loadOrStore;
		d.prevNode = location;
		UpType upTypeReturned = new UpType();
		if(location.isExternal()){
			//Must Be A Field
			UpType upType = location.getLocation().accept(this,d);
			if(upType == null) return null;
			/* Get Class Layout, class is external */
			MyType t = location.getLocation().getTypeFromTable();			
			
			if(t == null){
				System.out.println("Huge error in linking types to nodes!");
				return null;
			}
			ClassLayout cl = layoutManager.getClassLayout(t.getName());
			if(location.getLocation() instanceof This){
				cl = d.currentClassLayout;
			}
			/* Find ClassLayout Scope */
			String className = cl.getClassName();
			MySymbolTable externalTable = globalScope.getChildTable(cl.getClassName());
			if(externalTable == null){
				System.out.println("******************* Get Child Table failed! *******************");
				return null;
			}
			/* Store Or Load */
			if(loadOrStore){
				//Store
				instructions.add(spec.MoveFieldStore(downRegister, d.nextRegister(),  cl.getFieldOffset(location.getName())+""));
			}else{
				//Load
				String name = getFieldTranslationName(location.getName(), externalTable);
				resultRegister = d.nextRegister();
				instructions.add(spec.MoveFieldLoad(name,cl.getFieldOffset(location.getName())+"",resultRegister));
				upTypeReturned.setTarget(resultRegister);
			}
		}else{
			//Field Or Local Variable (in current scope or in upper scopes)
			MySymbolRecord rec = location.enclosingScope().Lookup(location.getName());
			if(rec == null) return null;
			if(rec.getKind() == Kind.Field){
				if(loadOrStore){
					//Store
					instructions.add(spec.MoveFieldStore(downRegister, d.nextRegister(),  d.currentClassLayout.getFieldOffset(location.getName())+""));
				}else{
					//Load
					String name = getFieldTranslationName(location.getName(), location.enclosingScope());
					String reg = d.nextRegister();
					instructions.add(spec.MoveFieldLoad(name,d.currentClassLayout.getFieldOffset(location.getName())+"",reg));
					upTypeReturned.setTarget(reg);
				}
			}

		}
		return upTypeReturned;
	}

	@Override
	public UpType visit(ArrayLocation location, DownType d) {
		d.prevNode = location;
		String downRegister = d.downRegister;
		boolean loadOrStore = d.loadOrStore;
		UpType upTypeReturned = new UpType();
		UpType upTypeExpr = location.getArray().accept(this,d);
		if(upTypeExpr == null) return null;
		UpType upTypeIndex = location.getIndex().accept(this,d);
		if(upTypeIndex == null) return null;
		if(loadOrStore){
			//Store
			instructions.add(spec.MoveArrayStore(downRegister, upTypeExpr.getTarget(), upTypeIndex.getTarget()));
		}else{
			//Load
			instructions.add(spec.MoveArrayLoad(upTypeExpr.getTarget(),upTypeIndex.getTarget(), downRegister));
			upTypeReturned.setTarget(downRegister);
		}
		return upTypeReturned;
	}

	@Override
	public UpType visit(StaticCall call, DownType d) {
		List<String> instructions = new ArrayList<String>();
		String paramsExpr = "(";
		d.prevNode = call;
		MySymbolTable func;	
		String retReg;
		ClassLayout cl =  layoutManager.getClassLayout(call.getClassName());
		func = globalScope.getChildTable(cl.getClassName()).getChildTable(call.getName());
		// construct params list (y=reg4,)
		List<String> formals = new ArrayList<String>();
		for(String name:func.getEntries().keySet()){
			if(func.getEntries().get(name).getKind()==Kind.Parameter)
				formals.add(name);
		}		
		int index=0;
		for(Expression arg :call.getArguments()){
			UpType upArg = (arg.accept(this, d));
			if(upArg==null) return null;			 
			paramsExpr+=formals.get(index)+"="+upArg.getTarget()+","; // param1=R1,param2=Reg45,...
			index++;			
		}
		char[] toChar = paramsExpr.toCharArray();
		toChar[paramsExpr.length()-1]=')';
		paramsExpr = new String(toChar) ;
		
		if(func.getEntries().get("$ret").getMyType() instanceof MyVoidType )
			retReg = "Rdummy";
		else
			retReg = d.nextRegister();
		
		instructions.add(spec.StaticCall("_"+call.getName()+paramsExpr, retReg));		
		this.instructions.addAll(instructions);
		
		UpType up = new UpType();
		if(retReg!="Rdummy")
			up.setTarget(retReg);
		return up;
	}

	@Override
	public UpType visit(VirtualCall call, DownType d) {
		List<String> instructions = new ArrayList<String>();
		String paramsExpr = "(";
		d.prevNode = call;		
		String callerReg;
		int call_offset;
		MySymbolTable func;
		String retReg;
		
		UpType caller = call.getLocation().accept(this, d);
		if(caller == null) return null;
		
		if(!(call.getLocation() instanceof This) && !call.isExternal()){ // call like ... func();
			caller = visit((This)call.getLocation(),d);			
		}		
		
		if(call.getLocation() instanceof This || !call.isExternal()){ // case this.func()..			
			call_offset = d.currentClassLayout.getMethodOffset(call.getName());	
			func = globalScope.getChildTable(d.currentClassLayout.getClassName()).getChildTable(call.getName());
		}
		else{ // case v.func() ..
			String className =((MyClassType) call.getLocation().getTypeFromTable()).getName();
			ClassLayout cl =  layoutManager.getClassLayout(className);
			call_offset = cl.getMethodOffset(call.getName());
			func = globalScope.getChildTable(cl.getClassName()).getChildTable(call.getName());
		}
		
		// construct params list (y=reg4,)
		List<String> formals = new ArrayList<String>();
		for(String name:func.getEntries().keySet()){
			if(func.getEntries().get(name).getKind()==Kind.Parameter)
				formals.add(name);
		}		
		int index=0;
		for(Expression arg :call.getArguments()){
			UpType upArg = (arg.accept(this, d));
			if(upArg==null) return null;			 
			paramsExpr+=formals.get(index)+"="+upArg.getTarget()+","; // param1=R1,param2=Reg45,...
			index++;			
		}
		char[] toChar = paramsExpr.toCharArray();
		toChar[paramsExpr.length()-1]=')';
		paramsExpr = new String(toChar) ;
		
		if(func.getEntries().get("$ret").getMyType() instanceof MyVoidType )
			retReg = "Rdummy";
		else
			retReg = d.nextRegister();
		
		instructions.add(spec.VirtualCall(caller.getTarget()+"."+call_offset+paramsExpr, retReg));		
		this.instructions.addAll(instructions);
		
		UpType up = new UpType();
		if(retReg!="Rdummy")
			up.setTarget(retReg);
		return up;
	}

	@Override
	public UpType visit(This thisExpression, DownType d) {
		String thisReg = d.nextRegister();
		instructions.add(makeComment(thisReg+" = this "));
		instructions.add(spec.Move("this", thisReg));
		//instructions.add(spec.MoveFieldStore(dispatchNames.get(d.currentClassLayout.getClassName()), thisReg, "0"));
		UpType up = new UpType(thisReg);
		return up;
	}

	@Override
	public UpType visit(NewClass newClass, DownType d) {
		
		List<String> instructions = new ArrayList<String>();
		String holder = d.nextRegister();		
		int classSize = layoutManager.getClassLayout(newClass.getName()).getLayoutSize();
		String newClassInst = spec.allocateObject(Integer.toString(classSize));
		instructions.add(makeComment(holder+" = new "+newClass.getName()+"()"));
		instructions.add(spec.Library(newClassInst, holder));
		instructions.add(spec.MoveFieldStore(dispatchNames.get(newClass.getName()), holder, "0"));
		this.instructions.addAll(instructions);
		UpType up = new UpType(holder);			
		return up;
	}

	@Override
	public UpType visit(NewArray newArray, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = newArray;
		UpType arrayType = newArray.getType().accept(this, d);	
		if(arrayType==null) return null;
		UpType arraySize =newArray.getSize().accept(this,d);
		if(arraySize==null) return null;
		String arrHolder = d.nextRegister();
		instructions.add(makeComment(arrHolder+" = new "+newArray.getType().getName()+"[]"));
		if(!(newArray.getType() instanceof PrimitiveType)){
			// need to get size of classlayout for this type			
			int typeFactor = layoutManager.getClassLayout(newArray.getType().getName()).getLayoutSize(); 
			instructions.add(spec.Mul(Integer.toString(typeFactor), arraySize.getTarget()));
		}
				
		String newArrInst = spec.allocateArray(arraySize.getTarget());
		
		instructions.add(spec.Library(newArrInst, arrHolder));
		UpType up = new UpType(arrHolder);		
		this.instructions.addAll(instructions);
		return up;
	}

	@Override
	public UpType visit(Length length, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = length;
		UpType firstOperand = length.getArray().accept(this, d);
		if(firstOperand==null)
			return null;
		String newReg = d.nextRegister();
		instructions.add(spec.ArrayLength(firstOperand.getTarget(), newReg));
		UpType up = new UpType(newReg);		
		this.instructions.addAll(instructions);
		d.freeRegister(firstOperand.getTarget());//free register that holds array		
		return up;
	}

	@Override
	public UpType visit(MathBinaryOp binaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		UpType firstOperand = binaryOp.getFirstOperand().accept(this,d);
		d.prevNode = binaryOp;
		if(firstOperand==null)
			return null;
		String firstReg = firstOperand.getTarget();
		UpType secondOperand = binaryOp.getSecondOperand().accept(this,d);
		if(secondOperand==null)
			return null;
		String secondReg = secondOperand.getTarget();
		if(binaryOp.getOperator()==BinaryOps.PLUS)
			instructions.add(spec.Add(secondReg, firstReg));
		if(binaryOp.getOperator()==BinaryOps.MINUS)
			instructions.add(spec.Sub(secondReg, firstReg));
		if(binaryOp.getOperator()==BinaryOps.MULTIPLY)
			instructions.add(spec.Mul(secondReg, firstReg));
		if(binaryOp.getOperator()==BinaryOps.DIVIDE)
			instructions.add(spec.Div(secondReg, firstReg));
		if(binaryOp.getOperator()==BinaryOps.MOD)
			instructions.add(spec.Mod(secondReg, firstReg));
		this.instructions.addAll(instructions);
		//free second operand
		d.freeRegister(secondReg);
		UpType up = new UpType(firstOperand);  // the result is stored in first operand
		return up;
	}

	@Override
	public UpType visit(LogicalBinaryOp binaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = binaryOp;
		UpType firstOperand = binaryOp.getFirstOperand().accept(this,d);
		if(firstOperand==null)
			return null;
		String firstReg = firstOperand.getTarget();
		UpType secondOperand = binaryOp.getSecondOperand().accept(this,d);
		if(secondOperand==null)
			return null;
		String secondReg = secondOperand.getTarget();
		String label = getLabelName("_"+binaryOp.getOperator()+"_end");
		
		if(binaryOp.getOperator()!=BinaryOps.LAND && binaryOp.getOperator()!=BinaryOps.LOR){
			instructions.add(spec.Compare(secondReg,firstReg));			
			if(binaryOp.getOperator() == BinaryOps.EQUAL)
				instructions.add(spec.JumpTrue(label));
			if(binaryOp.getOperator() == BinaryOps.NEQUAL)
				instructions.add(spec.JumpFalse(label));
			if(binaryOp.getOperator() == BinaryOps.GT)
				instructions.add(spec.JumpG(label));
			if(binaryOp.getOperator() == BinaryOps.GTE)
				instructions.add(spec.JumpGE(label));
			if(binaryOp.getOperator() == BinaryOps.LT)
				instructions.add(spec.JumpL(label));
			if(binaryOp.getOperator() == BinaryOps.LTE)
				instructions.add(spec.JumpLE(label));
			}
		else{
			if(binaryOp.getOperator()==BinaryOps.LAND){
				instructions.add(spec.Compare("0",firstReg));
				instructions.add(spec.JumpTrue(label));
				instructions.add(spec.And(secondReg,firstReg));				
			}
			if(binaryOp.getOperator()==BinaryOps.LOR){
				instructions.add(spec.Compare("1",firstReg));
				instructions.add(spec.JumpTrue(label));
				instructions.add(spec.Or(secondReg,firstReg));				
			}
			
		}
		instructions.add(label+":");
		this.instructions.addAll(instructions);
		//free second operand
		d.freeRegister(secondReg);
		UpType up = new UpType(firstOperand);  // the result is stored in first operand
		return up;
	}

	@Override
	public UpType visit(MathUnaryOp unaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = unaryOp;
		UpType acceptedUp = unaryOp.getOperand().accept(this, d);
		if(acceptedUp==null)
			return null;
		instructions.add(makeComment("- "+acceptedUp.getTarget()));
		instructions.add(spec.Neg(acceptedUp.getTarget()));	
		this.instructions.addAll(instructions);
		UpType up = new UpType(acceptedUp);
		return up;
	}

	@Override
	public UpType visit(LogicalUnaryOp unaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = unaryOp;
		UpType acceptedUp = unaryOp.getOperand().accept(this, d);
		if(acceptedUp==null)
			return null;
		instructions.add(makeComment("! "+acceptedUp.getTarget()));
		instructions.add(spec.Not(acceptedUp.getTarget()));	
		this.instructions.addAll(instructions);
		UpType up = new UpType(acceptedUp);
		return up;
	}

	@Override
	public UpType visit(Literal literal, DownType d) {
		List<String> instructions = new ArrayList<String>();		
		resultRegister = d.nextRegister();		
		
		if(literal.getType() == LiteralTypes.STRING){
			String strLiteral = null;
			if(!stringNames.containsKey(literal.getValue())){
				strLiteral = getStringLiteralTranslationName((String)literal.getValue());
				stringNames.put((String)literal.getValue(), strLiteral);
				stringLiterals.add(strLiteral);
			}else{
				strLiteral = stringNames.get(literal.getValue().toString());
			}
			//load to register
			
			instructions.add(spec.Move(strLiteral,resultRegister));
			
		}else{
			String val = literal.getValue().toString();
			
			String ins = spec.Move(val, resultRegister);
			instructions.add(ins);			
		}
		this.instructions.addAll(instructions);
		UpType up = new UpType();
		up.setTarget(resultRegister);
		return up;
	}

	@Override
	public UpType visit(ExpressionBlock expressionBlock, DownType d) {
		d.prevNode=expressionBlock;
		d.startScope();
		UpType returnedType = expressionBlock.getExpression().accept(this, d);
		d.endScope();	
		return new UpType(returnedType);
	}

	@Override
	public UpType visit(MethodType methodType, DownType d) {
		// TODO Auto-generated method stub
		return new UpType();
	}
	
	public void printTranslation(){
		for(String instruction:this.instructions){
			System.out.println(instruction);
		}
	}

}
