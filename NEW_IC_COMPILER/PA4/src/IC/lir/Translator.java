package IC.lir;

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
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;

public class Translator implements PropagatingVisitor<ClassLayout, List<String>>{
	LayoutsManager layoutManager;
	//Global instructions
	private List<String> dispatchVectors;
	private List<String> stringLiterals;
	//Register Manager
	private RegisterManager registers;
	//Naming conventions
	private int variableUnique;
	private int parameterUnique;
	private int fieldUnique;
	
	private String getVariableTranslationName(String varName){
		return "v"+(variableUnique++) + varName;
	}
	
	private String getParameterTranslationName(String parName){
		return "p"+(parameterUnique++) + parName;
	}
	
	private String getFieldTranslationName(String fieldName){
		return "f"+(fieldUnique++) + fieldName;
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
		this.layoutManager = layoutManager;
		this.stringLiterals = new ArrayList<String>();
		this.dispatchVectors = new ArrayList<String>();
		registers = new RegisterManager();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(LibraryMethod method, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Formal formal, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(PrimitiveType type, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(UserType type, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Assignment assignment, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(CallStatement callStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Return returnStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(If ifStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(While whileStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Break breakStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(Continue continueStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(StatementsBlock statementsBlock, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(LocalVariable localVariable, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(VariableLocation location, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> visit(ArrayLocation location, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
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
