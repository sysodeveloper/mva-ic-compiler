package IC.lir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.LiteralTypes;
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
import IC.AST.MethodType;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.PropagatingVisitor;
import IC.AST.Return;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;
import IC.mySymbolTable.MySymbolTable;
import IC.mySymbolTable.MySymbolRecord.Kind;

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
	List<String> instructions;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(ICClass icClass, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Field field, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(VirtualMethod method, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(StaticMethod method, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(LibraryMethod method, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Formal formal, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(PrimitiveType type, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(UserType type, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Assignment assignment, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(CallStatement callStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Return returnStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(If ifStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(While whileStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Break breakStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Continue continueStatement, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(StatementsBlock statementsBlock, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(LocalVariable localVariable, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(VariableLocation location, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(ArrayLocation location, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(StaticCall call, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(VirtualCall call, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(This thisExpression, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(NewClass newClass, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(NewArray newArray, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(Length length, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(MathBinaryOp binaryOp, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(LogicalBinaryOp binaryOp, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(MathUnaryOp unaryOp, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(LogicalUnaryOp unaryOp, DownType d) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpType visit(MethodType methodType, DownType d) {
		// TODO Auto-generated method stub
		return null;
	}

}
