package IC.lir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.ASTNode;
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
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;
import IC.mySymbolTable.MySymbolRecord;
import IC.mySymbolTable.MySymbolTable;
import IC.mySymbolTable.MySymbolRecord.Kind;

public class Translator implements PropagatingVisitor<ClassLayout, List<String>>{
	LayoutsManager layoutManager;
	//Global instructions
	private List<String> dispatchVectors;
	private List<String> stringLiterals;
	private Instructions spec;
	//String Literals
	Map<String,String> stringNames;
	//Register Manager
	private RegisterManager registers;
	private String resultRegister;
	//While labels for continue and break
	private String whileBeginLoop;
	private String whileEndLoop;
	//Assignment
	private Kind assignmentKind;
	private boolean assignmentLeft;
	//Naming conventions
	private int variableUnique;
	private int parameterUnique;
	private int fieldUnique;
	private int labelUnique;
	
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
	//Comments
	private String makeComment(String str){ 
		return "#"+str;
	}
	//Start of visitor
	public Translator(LayoutsManager layoutManager){
		variableUnique = 0;
		parameterUnique = 0;
		fieldUnique = 0;
		labelUnique = 0;
		this.layoutManager = layoutManager;
		this.stringLiterals = new ArrayList<String>();
		this.dispatchVectors = new ArrayList<String>();
		registers = new RegisterManager();
		stringNames = new HashMap<String, String>();
		resultRegister = null;
		whileBeginLoop = null;
		whileEndLoop = null;
		assignmentLeft = false;
	}

	@Override
	public List<String> visit(Program program, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		/* Program Translation */
		instructions.add(makeComment("A new program begins..."));
		instructions.add(makeComment(""));
		instructions.add(makeComment(""));
		instructions.add(makeComment("String Literals"));
		instructions.addAll(stringLiterals);
		instructions.add(makeComment("Dispatch Vectors"));
		instructions.addAll(dispatchVectors);
		for(ICClass c : program.getClasses()){
			instructions.addAll(c.accept(this,d));
		}
		return instructions;
	}

	@Override
	public List<String> visit(ICClass icClass, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Class " + icClass.getName()));
		/* Class layout */
		ClassLayout cl =  layoutManager.getClassLayout(icClass.getName());
		/* Class Dispatch Vector */
		if(cl.hasVirtaulMethos()){
			dispatchVectors.add(cl.printDispatchVector());
		}
		/* Class Translation */
		for(Field f : icClass.getFields()){
			instructions.addAll(f.accept(this,cl));
		}
		for(Method m : icClass.getMethods()){
			instructions.addAll(m.accept(this,cl));
			/* Registers are freed after each method */
		}
		return instructions;		
	}

	@Override
	public List<String> visit(Field field, ClassLayout d) {
		return new ArrayList<String>();
	}

	@Override
	public List<String> visit(VirtualMethod method, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Virtual Method " + method.getName()));
		/* Allocate Registers */
		registers.addAllocator();
		/* Method Label */
		instructions.add(d.makeSymbolicName(method.getName()+":"));
		/* Method Translation */
		instructions.addAll(method.getType().accept(this,d));
		for(Formal f : method.getFormals()){
			instructions.addAll(f.accept(this,d));
		}
		for(Statement s : method.getStatements()){
			instructions.addAll(s.accept(this,d));
		}
		/* Method Return If Void */
		if(method.getReturnType().getName().compareTo("void") == 0){
			instructions.add("Return 9999");
		}
		/* Free Register Allocated */
		registers.freeRegisters();
		return instructions;
	}

	@Override
	public List<String> visit(StaticMethod method, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Static Method " + method.getName()));
		/* Allocate Registers */
		registers.addAllocator();
		/* Method Label */
		instructions.add(d.makeSymbolicName(method.getName()+":"));
		/* Method Translation */
		instructions.addAll(method.getType().accept(this,d));
		for(Formal f : method.getFormals()){
			instructions.addAll(f.accept(this,d));
		}
		for(Statement s : method.getStatements()){
			instructions.addAll(s.accept(this,d));
		}
		/* Method Return If Void */
		if(method.getReturnType().getName().compareTo("void") == 0){
			instructions.add("Return 9999");
		}
		/* Free Register Allocated */
		registers.freeRegisters();
		return instructions;
		
	}

	@Override
	public List<String> visit(LibraryMethod method, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		return instructions;
	}

	@Override
	public List<String> visit(Formal formal, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.addAll(formal.getType().accept(this,d));
		return instructions;
	}

	@Override
	public List<String> visit(PrimitiveType type, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		return instructions;	
	}

	@Override
	public List<String> visit(UserType type, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		return instructions;
	}

	@Override
	public List<String> visit(Assignment assignment, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		Kind leftSide, rightSide;
		instructions.add(makeComment("Assignment Line " + assignment.getLine()));
		instructions.addAll(assignment.getAssignment().accept(this,d));
		rightSide = assignmentKind;
		String exprResultReg = resultRegister;
		assignmentLeft = true;
		instructions.addAll(assignment.getVariable().accept(this,d));
		assignmentLeft = false;
		leftSide = assignmentKind;
		String varResultReg = resultRegister;
		switch(assignmentKind){
		case Local_Variable:
			instructions.add(spec.Move(exprResultReg, varResultReg));
			break;
		case Field:
			break;
		}
		return instructions;
	}

	@Override
	public List<String> visit(CallStatement callStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.addAll(callStatement.getCall().accept(this,d));
		return instructions;
	}

	@Override
	public List<String> visit(Return returnStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("Return Line " + returnStatement.getLine()));
		if(returnStatement.hasValue()){
			instructions.addAll(returnStatement.getValue().accept(this,d));
			instructions.add(spec.Return(resultRegister));
		}else{
			instructions.add(spec.Return("9999"));
		}
		return instructions;
	}

	@Override
	public List<String> visit(If ifStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("If Line " + ifStatement.getLine()));
		instructions.addAll(ifStatement.getCondition().accept(this,d));
		instructions.add(spec.Compare("0", resultRegister));
		if(ifStatement.hasElse()){
			String falseLabel = getLabelName("_false_label");
			String endLabel = getLabelName("_end_label");
			instructions.add(spec.JumpTrue(falseLabel));
			instructions.addAll(ifStatement.getOperation().accept(this,d));
			instructions.add(spec.Jump(endLabel));
			instructions.add(falseLabel);
			instructions.addAll(ifStatement.getElseOperation().accept(this,d));
			instructions.add(endLabel+":");
		}else{
			String endLabel = getLabelName("_end_label");
			instructions.add(spec.JumpTrue(endLabel));
			instructions.addAll(ifStatement.getOperation().accept(this,d));
			instructions.add(getLabelName(endLabel+":"));
		}
		return instructions;	
	}

	@Override
	public List<String> visit(While whileStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(makeComment("While Line " + whileStatement.getLine()));
		String testLabel = getLabelName("_test_label");
		String endLabel = getLabelName("_end_label");
		whileBeginLoop = testLabel;
		whileEndLoop = endLabel;
		instructions.add(testLabel+":");
		instructions.addAll(whileStatement.getCondition().accept(this,d));
		instructions.add(spec.Compare("0", resultRegister));
		instructions.add(spec.JumpTrue(endLabel));
		instructions.addAll(whileStatement.getOperation().accept(this,d));
		instructions.add(spec.Jump(testLabel));
		instructions.add(endLabel+":");
		return instructions;
	}

	@Override
	public List<String> visit(Break breakStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(spec.Jump(whileEndLoop));
		return instructions;
	}

	@Override
	public List<String> visit(Continue continueStatement, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.add(spec.Jump(whileBeginLoop));
		return instructions;
	}

	@Override
	public List<String> visit(StatementsBlock statementsBlock, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		for(Statement s : statementsBlock.getStatements()){
			instructions.addAll(s.accept(this,d));
		}
		return instructions;
	}

	@Override
	public List<String> visit(LocalVariable localVariable, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		instructions.addAll(localVariable.getType().accept(this,d));
		if(localVariable.hasInitValue()){
			//int x = 5;
			instructions.addAll(localVariable.getInitValue().accept(this,d));
			String name = getVariableTranslationName(localVariable.getName(),localVariable.enclosingScope());
			instructions.add(spec.Move(resultRegister, name));
		}
		//int x - nothing...
		assignmentKind = Kind.Local_Variable;
		return instructions;
	}

	@Override
	public List<String> visit(VariableLocation location, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		String externalResult = null;
		if(location.isExternal()){
			instructions.addAll(location.getLocation().accept(this,d));
			externalResult = resultRegister;
			//Get the correct class layout
			
			
		}else{
			MySymbolRecord rec = location.enclosingScope().Lookup(location.getName());
			if(rec == null) return instructions;
			if(rec.getKind() == Kind.Field){
				//Field - Class Layout is d
				if(!assignmentLeft){
					String name = getFieldTranslationName(location.getName(), location.enclosingScope());
					instructions.add(spec.MoveFieldLoad(name,d.getFieldOffset(location.getName())+"",registers.nextRegister()));
					resultRegister = registers.lastRegisterUsed();
				}
				assignmentKind = Kind.Field;
			}else if(rec.getKind() == Kind.Local_Variable){
				//Local variable
				if(!assignmentLeft){
					String name = getVariableTranslationName(location.getName(), location.enclosingScope());
					instructions.add(spec.Move(name, registers.nextRegister()));
					resultRegister = registers.lastRegisterUsed();
				}
				assignmentKind = Kind.Local_Variable;
			}
		}
		

		return instructions;
	}

	@Override
	public List<String> visit(ArrayLocation location, ClassLayout d) {
		List<String> instructions = new ArrayList<String>();
		
		return instructions;
	}

	@Override
	public List<String> visit(StaticCall call, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(VirtualCall call, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(This thisExpression, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(NewClass newClass, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(NewArray newArray, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Length length, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(MathBinaryOp binaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(LogicalBinaryOp binaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(MathUnaryOp unaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(LogicalUnaryOp unaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Literal literal, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(ExpressionBlock expressionBlock, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(MethodType methodType, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}	
}
