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

public class Translator implements PropagatingVisitor<ClassLayout, TranslationInfo>{
	LayoutsManager layoutManager;
	//Global instructions
	private TranslationInfo dispatchVectors;
	private TranslationInfo stringLiterals;
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
	private StringBuffer makeComment(String str){ 
		return new StringBuffer("#"+str);
	}
	//Start of visitor
	public Translator(LayoutsManager layoutManager){
		variableUnique = 0;
		parameterUnique = 0;
		fieldUnique = 0;
		this.layoutManager = layoutManager;
		this.stringLiterals = new TranslationInfo();
		this.dispatchVectors = new TranslationInfo();
	}

	@Override
	public TranslationInfo visit(Program program, ClassLayout d) {
		TranslationInfo tInfo = new TranslationInfo();
		TranslationInfo classInfo = null;
		/* Program Translation */
		tInfo.instructions.add(makeComment("A new program begins..."));
		tInfo.instructions.add(makeComment(""));
		tInfo.instructions.add(makeComment(""));
		tInfo.instructions.add(makeComment("String Literals"));
		tInfo.instructions.addAll(stringLiterals.instructions);
		tInfo.instructions.add(makeComment("Dispatch Vectors"));
		tInfo.instructions.addAll(dispatchVectors.instructions);
		for(ICClass c : program.getClasses()){
			classInfo = c.accept(this,d);
			tInfo.instructions.addAll(classInfo.instructions);
		}
		return tInfo;
	}

	@Override
	public TranslationInfo visit(ICClass icClass, ClassLayout d) {
		TranslationInfo tInfo = new TranslationInfo();
		TranslationInfo childInfo = null;
		tInfo.instructions.add(makeComment("Class " + icClass.getName()));
		/* Class layout */
		ClassLayout cl =  layoutManager.getClassLayout(icClass.getName());
		/* Class Dispatch Vector */
		if(cl.hasVirtaulMethos()){
			dispatchVectors.instructions.add(cl.printDispatchVector());
		}
		/* Class Translation */
		for(Field f : icClass.getFields()){
			childInfo = f.accept(this,cl);
			tInfo.instructions.addAll(childInfo.instructions);
		}
		childInfo = null;
		for(Method m : icClass.getMethods()){
			childInfo = m.accept(this,cl);
			tInfo.instructions.addAll(childInfo.instructions);
			/* Registers are freed after each method */
		}
		return tInfo;		
	}

	@Override
	public TranslationInfo visit(Field field, ClassLayout d) {
		TranslationInfo tInfo = new TranslationInfo();
		return tInfo;
	}

	@Override
	public TranslationInfo visit(VirtualMethod method, ClassLayout d) {
		TranslationInfo tInfo = new TranslationInfo();
		TranslationInfo childInfo = null;
		tInfo.instructions.add(makeComment("Virtual Method " + method.getName()));
		/* Method Label */
		tInfo.instructions.add(new StringBuffer(d.makeSymbolicName(method.getName())+":"));
		/* Method Translation */
		for(Formal f : method.getFormals()){
			childInfo = f.accept(this,d);
			childInfo.instructions.addAll(childInfo.instructions);
		}
		childInfo = null;
		for(Statement s : method.getStatements()){
			childInfo = s.accept(this,d);
			tInfo.instructions.addAll(childInfo.instructions);
		}
		/* Method Return If Void */
		if(method.getReturnType().getName().compareTo("void") == 0){
			tInfo.instructions.add("Return")
		}
		return tInfo;
	}

	@Override
	public TranslationInfo visit(StaticMethod method, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(LibraryMethod method, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Formal formal, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(PrimitiveType type, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(UserType type, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Assignment assignment, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(CallStatement callStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Return returnStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(If ifStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(While whileStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Break breakStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Continue continueStatement, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(StatementsBlock statementsBlock, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(LocalVariable localVariable, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(VariableLocation location, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(ArrayLocation location, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(StaticCall call, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(VirtualCall call, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(This thisExpression, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(NewClass newClass, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(NewArray newArray, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Length length, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(MathBinaryOp binaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(LogicalBinaryOp binaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(MathUnaryOp unaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(LogicalUnaryOp unaryOp, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(Literal literal, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(ExpressionBlock expressionBlock, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TranslationInfo visit(MethodType methodType, ClassLayout d) {
		// TODO Auto-generated method stub
		return null;
	}
}
