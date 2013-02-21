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
import IC.myTypes.MyStringType;
import IC.myTypes.MyType;
import IC.myTypes.MyVoidType;



public class LIROpt implements PropagatingVisitor<DownType, UpType>{
	
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
		//return "p"+id + parName;
		return "p0" + parName;
	}
	
	private String getFieldTranslationName(String fieldName,MySymbolTable enclosingScope){
		int id = enclosingScope.Lookup(fieldName).getId();
		return "f"+id + fieldName;
	}	
	
	private String getLabelName(String labelName){
		return labelName+(labelUnique++);
	}
	
	private String getStringLiteralTranslationName(String strLiteral){
		return "str"+stringLiteralUnique++;
	}
	
	//Comments
	private String makeComment(String str){ 
		return "#"+str;
	}
	
	//Start of visitor
	public LIROpt(LayoutsManager layoutManager){
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
		
		for(ICClass c1:program.getClasses()){
			if(c1.getName().compareTo("Library") == 0){
				continue;
			}
			ClassLayout cl =  layoutManager.getClassLayout(c1.getName());
			dispatchVectors.add(cl.printDispatchVector());
			dispatchNames.put(c1.getName(),"_DV_"+c1.getName());
		}
		
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
		String[] errors={"Runtime Error: Division by zero!","Runtime Error: Array allocation with negative array size!","Runtime Error: Array index out of bounds!","Runtime Error: Null pointer dereference!"};
		for(String error:errors){
			setLabel(error);
		}
		
		tempInst.addAll(stringLiterals);
		tempInst.add(makeComment("Dispatch Vectors"));
		tempInst.addAll(dispatchVectors);
		
		// error labels
		tempInst.add("_labelDBE:"); // division by zero
		tempInst.add(spec.Library("__println("+stringNames.get(errors[0])+")", "Rdummy"));
		tempInst.add(spec.Library("__exit(1)", "Rdummy"));
		tempInst.add("_labelASE:"); // array allocation
		tempInst.add(spec.Library("__println("+stringNames.get(errors[1])+")", "Rdummy"));
		tempInst.add(spec.Library("__exit(1)", "Rdummy"));
		tempInst.add("_labelABE:"); // array bounds
		tempInst.add(spec.Library("__println("+stringNames.get(errors[2])+")", "Rdummy"));
		tempInst.add(spec.Library("__exit(1)", "Rdummy"));
		tempInst.add("_labelNPE:"); // null pointer
		tempInst.add(spec.Library("__println("+stringNames.get(errors[3])+")", "Rdummy"));
		tempInst.add(spec.Library("__exit(1)", "Rdummy"));
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
		//}
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
		d.endScope();
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
		d.endScope();		
		UpType up = new UpType();
		d.endScope();
		return up;
	}

	@Override
	public UpType visit(StaticMethod method, DownType d) {
		d.prevNode = method;
		//List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Static Method " + method.getName()));
		/* Allocate Registers */
		d.startScope();
		/* Method Label */
		if(method.getName().compareTo("main")==0)
			instructions.add("_ic_main:");
		else
			instructions.add("_"+method.getName()+":");
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
			if(method.getName().compareTo("main")!=0)
				instructions.add("Return 9999");
			else
				instructions.add(spec.Library("__exit(0)", "Rdummy"));
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
		if(upTypeExpr.getTarget() != null) d.freeRegister(upTypeExpr.getTarget());
		d.endScope();
		return new UpType();
	}

	@Override
	public UpType visit(CallStatement callStatement, DownType d) {
		d.prevNode = callStatement;		
		UpType upType = callStatement.getCall().accept(this,d);
		if(upType!=null)
			return new UpType(upType); // ---> I changed it, it was return new UpType(), need to talk about it
		d.endScope();
		return new UpType();
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
		d.endScope();
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
			instructions.add(falseLabel+":");
			UpType upTypeElse = ifStatement.getElseOperation().accept(this,d);
			if(upTypeElse == null) return null;
			instructions.add(endLabel+":");
		}else{
			String endLabel = getLabelName("_end_label");
			instructions.add(spec.JumpTrue(endLabel));
			UpType upTypeOp = ifStatement.getOperation().accept(this,d);
			if(upTypeOp == null) return null;
			instructions.add(endLabel+":");
		}
		d.endScope();
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
		d.endScope();
		return new UpType();
	}

	@Override
	public UpType visit(Break breakStatement, DownType d) {
		instructions.add(spec.Jump(whileEndLoop));
		d.endScope();
		return new UpType();
	}

	@Override
	public UpType visit(Continue continueStatement, DownType d) {
		instructions.add(spec.Jump(whileBeginLoop));
		d.endScope();
		return new UpType();
	}

	@Override
	public UpType visit(StatementsBlock statementsBlock, DownType d) {
		for(Statement s : statementsBlock.getStatements()){
			UpType upType = s.accept(this,d);
			if(upType == null) return null;
		}
		d.endScope();
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
		d.endScope();
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
			d.loadOrStore = false; //load
			UpType upType = location.getLocation().accept(this,d);
			if(upType == null) return null;
			String reg = upType.getTarget();
			/*if(!isReg(reg)){
				reg = d.nextRegister();
				instructions.add(spec.Move(upType.getTarget(), reg));
			}*/
			instructions.addAll(nullPointer(reg,d)); // runtime check null pointer reference
			d.loadOrStore = loadOrStore;
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
				instructions.add(spec.MoveFieldStore(downRegister,reg,  cl.getFieldOffset(location.getName())+""));
			}else{
				//Load
				//String name = getFieldTranslationName(location.getName(), externalTable);
				//String tempReg = d.nextRegister();
				//instructions.add(spec.Move(name, tempReg));
				resultRegister = reg;//upType.getTarget();//d.nextRegister();
				instructions.add(spec.MoveFieldLoad(reg,cl.getFieldOffset(location.getName())+"",resultRegister));
				//d.freeRegister(tempReg);
				upTypeReturned.setTarget(resultRegister);
			}
		}else{
			//Field Or Local Variable (in current scope or in upper scopes)
			MySymbolRecord rec = location.enclosingScope().Lookup(location.getName());
			if(rec == null) return null;
			if(rec.getKind() == Kind.Field){
				UpType thisField = visit(new This(0),d);
				
				if(loadOrStore){
					//Store
					instructions.add(spec.MoveFieldStore(downRegister, thisField.getTarget(),  d.currentClassLayout.getFieldOffset(location.getName())+""));
				}else{
					//Load
					String name = getFieldTranslationName(location.getName(), location.enclosingScope());
					String reg = thisField.getTarget();//d.nextRegister();
					instructions.add(spec.MoveFieldLoad(thisField.getTarget(),d.currentClassLayout.getFieldOffset(location.getName())+"",reg));
					upTypeReturned.setTarget(reg);
				}
			}
			if(rec.getKind() == Kind.Local_Variable || rec.getKind() == Kind.Parameter){
				String name = null;
				if(rec.getKind() == Kind.Local_Variable)
					name = getVariableTranslationName(location.getName(),location.enclosingScope());
				else
					name = getParameterTranslationName(location.getName(), location.enclosingScope());
				if(loadOrStore){
					//Store
					instructions.add(spec.Move(downRegister, name));
				}else{
					//Load
					//avoid loading locals to registers
					String reg = name;//d.nextRegister();
					upTypeReturned.setTarget(reg);
					instructions.add(spec.Move(name, reg));
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
		d.loadOrStore = false; //load
		UpType upTypeExpr = location.getArray().accept(this,d);
		if(upTypeExpr == null) return null;
		String regExpr = upTypeExpr.getTarget();
		if(!isReg(regExpr)){
			regExpr = d.nextRegister();
			instructions.add(spec.Move(upTypeExpr.getTarget(), regExpr));
		}
		instructions.addAll(nullPointer(regExpr,d));//runtime null pointer
		UpType upTypeIndex = location.getIndex().accept(this,d);
		if (upTypeIndex == null) return null;
		String newReg = upTypeIndex.getTarget();
		if(isMemory(upTypeIndex.getTarget())){
			//index must be immediate or register
			newReg = d.nextRegister();
			instructions.add(spec.Move(upTypeIndex.getTarget(),newReg));
			//d.freeRegister(newReg);
		}
		String newExprReg = regExpr;
		/*if(!isReg(upTypeExpr.getTarget())){
			//load to register
			newExprReg = d.nextRegister();
			instructions.add(spec.Move(upTypeExpr.getTarget(), newExprReg));
			//d.freeRegister(newRegExpr);
		}*/
		instructions.addAll(arrayAccess(newExprReg,newReg,d));//runtime array access
		d.loadOrStore = loadOrStore;
		if(loadOrStore){
			//Store
			instructions.add(spec.MoveArrayStore(downRegister, newExprReg, newReg));
		}else{
			//Load
			String reg = d.nextRegister();
			instructions.add(spec.MoveArrayLoad(newExprReg,newReg, reg));
			upTypeReturned.setTarget(reg);
		}
		d.freeRegister(newExprReg);
		return upTypeReturned;
	}

	@Override
	public UpType visit(StaticCall call, DownType d) {
		List<String> instructions = new ArrayList<String>();
		String paramsExpr = "(";
		d.prevNode = call;
		MySymbolTable func;	
		String retReg;
		boolean library=false;
		if(call.getClassName().compareTo("Library")==0)
			library=true;
		
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
			if(library){
				paramsExpr+=upArg.getTarget(); // R1,Reg45,...
			}			
			else
				paramsExpr+=getParameterTranslationName(formals.get(index), func)+"="+upArg.getTarget(); // param1=R1,param2=Reg45,...
			if(index<call.getArguments().size()-1)
				paramsExpr+=",";
			index++;			
		}
		paramsExpr=paramsExpr.concat(")");
		if(func.getEntries().get("$ret").getMyType() instanceof MyVoidType )
			retReg = "Rdummy";
		else
			retReg = d.nextRegister();
		
		if(library)
			instructions.add(spec.Library("__"+call.getName()+paramsExpr, retReg));
		else
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
		UpType caller = null;
		// check if it is a static call to a function of the current class
		if(!call.isExternal()){
			if(d.currentClassLayout.getMethodOffset(call.getName())==-1){
				return visit(new StaticCall(call.getLine(), d.currentClassLayout.getClassName(), call.getName(), call.getArguments()),d);
			}
		}
		
		if(call.isExternal()){
			caller = call.getLocation().accept(this, d);
			if(caller == null) return null;
			String reg = caller.getTarget();
			/*if(!isReg(caller.getTarget())){
				reg = d.nextRegister();
				instructions.add(spec.Move(caller.getTarget(), reg));
			}*/
			instructions.addAll(nullPointer(reg,d));
			d.freeRegister(reg);
		}
		
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
			paramsExpr+=getParameterTranslationName(formals.get(index), func)+"="+upArg.getTarget(); // param1=R1,param2=Reg45,...
			if(index<call.getArguments().size()-1)
				paramsExpr+=",";
			index++;			
		}
		paramsExpr=paramsExpr.concat(")");
		
		if(func.getEntries().get("$ret").getMyType() instanceof MyVoidType )
			retReg = "Rdummy";
		else
			retReg = d.nextRegister();
		String reg = caller.getTarget();
		if(!isReg(reg)){
			reg = d.nextRegister();
			instructions.add(spec.Move(caller.getTarget(), reg));
			d.freeRegister(reg);
		}
		instructions.add(spec.VirtualCall(reg+"."+call_offset+paramsExpr, retReg));		
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
		String newClassInst = spec.allocateObject(Integer.toString(4*classSize));
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
		String reg = arraySize.getTarget();
		/*if(!isReg(reg)){
			reg = d.nextRegister();
			instructions.add(spec.Move(arraySize.getTarget(), reg));
		}*/
		instructions.addAll(arraySize(reg,d)); //runtime check array size
		String arrHolder = d.nextRegister();
		instructions.add(makeComment(arrHolder+" = new "+newArray.getType().getName()+"[]"));
		String accReg = reg;
		if(!(newArray.getType() instanceof PrimitiveType)){
			// need to get size of classlayout for this type			
			int typeFactor = layoutManager.getClassLayout(newArray.getType().getName()).getLayoutSize();
			accReg = ReturnAccumulator(typeFactor+"", arraySize.getTarget(), d);
			if(accReg.compareTo(arraySize.getTarget()) != 0){
				instructions.add(spec.Move(arraySize.getTarget(), accReg));
			}
			instructions.add(spec.Mul(Integer.toString(typeFactor), accReg));
		}
		else{
			int typeFactor = 4;
			accReg = ReturnAccumulator(typeFactor+"", arraySize.getTarget(), d);
			if(accReg.compareTo(arraySize.getTarget()) != 0){
				instructions.add(spec.Move(arraySize.getTarget(), accReg));
			}
			instructions.add(spec.Mul(Integer.toString(typeFactor), accReg));
		}
				
		String newArrInst = spec.allocateArray(accReg);
		
		instructions.add(spec.Library(newArrInst, arrHolder));
		UpType up = new UpType(arrHolder);		
		this.instructions.addAll(instructions);
		d.freeRegister(reg);
		return up;
	}

	@Override
	public UpType visit(Length length, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = length;
		UpType firstOperand = length.getArray().accept(this, d);
		if(firstOperand==null)
			return null;
		String reg = firstOperand.getTarget();
		instructions.addAll(nullPointer(reg,d));
		String newReg = d.nextRegister();
		instructions.add(spec.ArrayLength(reg, newReg));
		UpType up = new UpType(newReg);		
		this.instructions.addAll(instructions);
		d.freeRegister(reg);//free register that holds array		
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
		//user accumulators
		String accuReg = ReturnAccumulator(firstOperand.getTarget(),secondOperand.getTarget(),d);
		String firstPlace, secondPlace;
		if(accuReg.compareTo(firstReg) == 0){
			firstPlace = secondReg;
			secondPlace = accuReg;
		}else if(accuReg.compareTo(secondReg) == 0){
			firstPlace = firstReg;
			secondPlace = accuReg;
		}else{
			if(binaryOp.getOperator()==BinaryOps.PLUS && binaryOp.getFirstOperand().getTypeFromTable() instanceof MyStringType){
				
			}else{
				instructions.add(spec.Move(firstReg, accuReg));
			}
			firstPlace = secondReg;
			secondPlace = accuReg;			
		}
		if(binaryOp.getOperator()==BinaryOps.PLUS){
			if(binaryOp.getFirstOperand().getTypeFromTable() instanceof MyStringType){
				instructions.add(spec.Library("__stringCat("+firstReg+","+secondReg+")", accuReg));
			}
			else{
				instructions.add(spec.Add(firstPlace, secondPlace));
			}
		}
		if(binaryOp.getOperator()==BinaryOps.MINUS)
			instructions.add(spec.Sub(firstPlace, secondPlace));
		if(binaryOp.getOperator()==BinaryOps.MULTIPLY)
			instructions.add(spec.Mul(firstPlace, secondPlace));
		if(binaryOp.getOperator()==BinaryOps.DIVIDE){
			String next = secondReg;
			/*if(!isReg(secondReg)){
				next = d.nextRegister();
				instructions.add(spec.Move(secondReg, next));
				d.freeRegister(next);
				firstPlace = next;
			}*/
			instructions.addAll(divisionByZero(next,d));//runtime check division by zero
			instructions.add(spec.Div(firstPlace, secondPlace));			
		}
		if(binaryOp.getOperator()==BinaryOps.MOD){
			String next = secondReg;
			/*if(!isReg(secondReg)){
				next = d.nextRegister();
				instructions.add(spec.Move(secondReg, next));
				d.freeRegister(next);
				firstPlace = next;
			}*/
			instructions.addAll(divisionByZero(next,d));//runtime check division by zero
			instructions.add(spec.Mod(next, secondPlace));
		}
		this.instructions.addAll(instructions);
		//free second operand
		//d.freeRegister(secondReg);
		UpType up = new UpType(accuReg);  // the result is stored in first operand
		return up;
	}

	@Override
	public UpType visit(LogicalBinaryOp binaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = binaryOp;
		String resultReg = d.nextRegister();
		UpType firstOperand = binaryOp.getFirstOperand().accept(this,d);
		if(firstOperand==null)
			return null;
		String firstReg = firstOperand.getTarget();
		UpType secondOperand = binaryOp.getSecondOperand().accept(this,d);
		if(secondOperand==null)
			return null;
		String secondReg = secondOperand.getTarget();
		//user accumulators
		String accuReg = ReturnAccumulator(firstOperand.getTarget(),secondOperand.getTarget(),d);
		String firstPlace, secondPlace;
		if(accuReg.compareTo(firstReg) == 0){
			firstPlace = accuReg;
			secondPlace = secondReg;
		}else if(accuReg.compareTo(secondReg) == 0){
			firstPlace = accuReg;
			secondPlace = firstReg;
		}else{
			instructions.add(spec.Move(secondReg, accuReg));
			firstPlace = accuReg;
			secondPlace = firstReg;
		}
		String label = getLabelName("_"+binaryOp.getOperator()+"_end");
		resultReg = d.nextRegister();
		instructions.add(spec.Move("0", resultReg));
		if(binaryOp.getOperator()!=BinaryOps.LAND && binaryOp.getOperator()!=BinaryOps.LOR){
			instructions.add(spec.Compare(secondPlace,firstPlace));			
			if(binaryOp.getOperator() == BinaryOps.EQUAL)
				instructions.add(spec.JumpFalse(label));
			if(binaryOp.getOperator() == BinaryOps.NEQUAL)
				instructions.add(spec.JumpTrue(label));
			if(binaryOp.getOperator() == BinaryOps.GT)
				instructions.add(spec.JumpGE(label));
			if(binaryOp.getOperator() == BinaryOps.GTE)
				instructions.add(spec.JumpG(label));
			if(binaryOp.getOperator() == BinaryOps.LT)
				instructions.add(spec.JumpLE(label));
			if(binaryOp.getOperator() == BinaryOps.LTE)
				instructions.add(spec.JumpL(label));
			}
		else{
			if(binaryOp.getOperator()==BinaryOps.LAND){
				instructions.add(spec.Compare("0",secondPlace));
				instructions.add(spec.JumpTrue(label));
				instructions.add(spec.And(firstPlace,secondPlace));				
			}
			if(binaryOp.getOperator()==BinaryOps.LOR){
				instructions.add(spec.Compare("1",secondPlace));
				instructions.add(spec.JumpTrue(label));
				instructions.add(spec.Or(firstPlace,secondPlace));				
			}
			
		}
		instructions.add(spec.Move("1", resultReg));
		instructions.add(label+":");
		// here dont do nothing
		this.instructions.addAll(instructions);
		//free second operand
		//d.freeRegister(firstReg);
		//d.freeRegister(secondReg);
		d.freeRegister(accuReg);
		UpType up = new UpType(resultReg);  // the result is stored in first operand
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
		String reg = acceptedUp.getTarget();
		String newReg = reg;
		if(!isReg(reg)){
			newReg = d.nextRegister();
			instructions.add(spec.Move(reg, newReg));
		}
		instructions.add(spec.Neg(newReg));	
		this.instructions.addAll(instructions);
		UpType up = new UpType(newReg);
		return up;
	}

	@Override
	public UpType visit(LogicalUnaryOp unaryOp, DownType d) {
		List<String> instructions = new ArrayList<String>();
		d.prevNode = unaryOp;
		UpType acceptedUp = unaryOp.getOperand().accept(this, d);
		if(acceptedUp==null)
			return null;
		String reg = acceptedUp.getTarget();
		String newReg = reg;
		instructions.add(makeComment("! "+acceptedUp.getTarget()));
		if(!isReg(reg)){
			newReg = d.nextRegister();
			instructions.add(spec.Move(reg, newReg));
		}

		instructions.add(spec.Not(newReg));	
		this.instructions.addAll(instructions);
		UpType up = new UpType(newReg);
		return up;
	}

	@Override
	public UpType visit(Literal literal, DownType d) {
		List<String> instructions = new ArrayList<String>();		
		//resultRegister = d.nextRegister();		
		resultRegister = null;
		if(literal.getType() == LiteralTypes.STRING){
			String strLiteral = null;
			if(!stringNames.containsKey(literal.getValue())){
				strLiteral = getStringLiteralTranslationName((String)literal.getValue());
				stringNames.put((String)literal.getValue(), strLiteral);
				stringLiterals.add(strLiteral+": "+'"'+(String)literal.getValue()+'"');
			}else{
				strLiteral = stringNames.get(literal.getValue().toString());
			}
			resultRegister = strLiteral;
			//load to register
			//instructions.add(spec.Move(strLiteral,resultRegister));
			
		}else{
			String val = literal.getValue().toString();
			if(val.compareTo("null")==0 || val.compareTo("false") == 0)
				val="0";
			else if(val.compareTo("true") == 0)
				val="1";
			//avoid storing literal in registers
			resultRegister = val;
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
			//Remove Move x,x
			if(instruction.startsWith("Move")){
				String str = instruction.substring(instruction.indexOf(" ")+1);
				String[] arr = str.split(",");
				if(arr.length == 2 && arr[0].compareTo(arr[1]) == 0){
					continue;
				}
			}
			System.out.println(instruction);
		}
	}
	private void setLabel(String label){
		String strLiteral = null;
		if(!stringNames.containsKey(label)){
			strLiteral = getStringLiteralTranslationName(label);
			stringNames.put(label, strLiteral);
			stringLiterals.add(strLiteral+": "+'"'+label+'"');
		}else{
			strLiteral = stringNames.get(label);
		}	
	}
	private List<String> nullPointer(String reg, DownType d){
		List<String> inst = new ArrayList<String>();
		String newReg = reg;
		if(!isReg(reg)){
			newReg = d.nextRegister();
			instructions.add(spec.Move(reg, newReg));
			d.freeRegister(newReg);
			//return null;
		}
		inst.add(spec.Compare("0", newReg));
		inst.add(spec.JumpTrue("_labelNPE"));
		return inst;
	}
	
	private List<String> arrayAccess(String array,String index, DownType d){
		List<String> inst = new ArrayList<String>();
		String length = d.nextRegister();
		inst.add(spec.ArrayLength(array, length));
		inst.add(spec.Compare(index, length));
		inst.add(spec.JumpLE("_labelABE"));
		if(!isReg(index)){
			String newReg = d.nextRegister();
			inst.add(spec.Move(index, newReg));
			inst.add(spec.Compare("0", newReg));
			d.freeRegister(newReg);
			//return null;
		}else{
			inst.add(spec.Compare("0", index));
		}
		inst.add(spec.JumpL("_labelABE"));
		d.freeRegister(length);
		return inst;
	}
	
	private List<String> divisionByZero(String reg,DownType d){
		List<String> inst = new ArrayList<String>();
		String newReg = reg;
		if(!isReg(reg)){
			newReg = d.nextRegister();
			instructions.add(spec.Move(reg, newReg));
			d.freeRegister(newReg);
//			return null;
		}
		inst.add(spec.Compare("0", newReg));
		inst.add(spec.JumpTrue("_labelDBE"));
		return inst;
	}
	
	private List<String> arraySize(String reg,DownType d){
		String newReg = reg;
		if(!isReg(reg)){
			newReg = d.nextRegister();
			instructions.add(spec.Move(reg, newReg));
			d.freeRegister(newReg);
			//return null;
		}
		List<String> inst = new ArrayList<String>();
		inst.add(spec.Compare("0", newReg));
		inst.add(spec.JumpLE("_labelASE"));
		return inst;
	}
	
	private boolean isMemory(String reg){
		return !isConst(reg) && !isReg(reg);
	}
	
	private boolean isConst(String reg){
		if(reg.charAt(0) >= '0' && reg.charAt(0) <= '9') return true;
		return false;
	}
	
	private boolean isReg(String reg){
		if(reg.charAt(0) == 'R') return true;
		return false;
	}
	
	private String ReturnAccumulator(String firstOperand,String secondOperand,DownType d){
		if(isConst(firstOperand) && isConst(secondOperand)){
			return d.nextRegister();
		}
		if(isConst(firstOperand) && isMemory(secondOperand)){
			return d.nextRegister();
		}
		if(isConst(firstOperand) && isReg(secondOperand)){
			return secondOperand;
		}
		if(isMemory(firstOperand) && isConst(secondOperand)){d.nextRegister();
			return d.nextRegister();
		}
		if(isMemory(firstOperand) && isMemory(secondOperand)){
			return d.nextRegister();
		}
		if(isMemory(firstOperand) && isReg(secondOperand)){
			return secondOperand;
		}
		if(isReg(firstOperand) && isConst(secondOperand)){
			return firstOperand;
		}
		if(isReg(firstOperand) && isMemory(secondOperand)){
			return firstOperand;
		}
		if(isReg(firstOperand) && isReg(secondOperand)){
			d.freeRegister(secondOperand);
			return firstOperand;
		}
		return d.nextRegister();
	}
}
