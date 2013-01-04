package IC;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import IC.MySymbolRecord.Kind;
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
import IC.AST.Statement;

public class MySemanticAnalyzer implements PropagatingVisitor<MySymbolTable, Boolean> {
	private MySymbolTable globalScope;
	private Map<String,MySymbolTable> classScopes = new LinkedHashMap<String, MySymbolTable>();
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	@Override
	public Boolean visit(Program program, MySymbolTable d) {
		globalScope = program.enclosingScope();
		Boolean result = true;
		for(ICClass c:program.getClasses()){
			result &= c.accept(this,program.enclosingScope());
			
		}
		return result;
	}

	@Override
	public Boolean visit(ICClass icClass, MySymbolTable d) {
		MySymbolTable scope = icClass.enclosingScope();		
		putAllClasseScopes(scope);		
		boolean result =true;
		for(Field f:icClass.getFields()){
			result &= f.accept(this,scope);			
		}
		
		for(Method m:icClass.getMethods()){
			result &= m.accept(this,scope);			
		}
		
		return result;
	}

	@Override
	public Boolean visit(Field field, MySymbolTable d){
		// TODO Auto-generated method stub
		if(field.getType() instanceof PrimitiveType)
			return true;
		if (!checkUserType(field.getType())){
			semanticErrors.add(new SemanticError("undefined user type "+field.getType().getName()+" for field "+field.getName(), field.getLine()));
			return false;
		}
		return true;
	}

	@Override
	public Boolean visit(VirtualMethod method, MySymbolTable d) {
		boolean result = true;
		for(Formal f:method.getFormals()){
			result &=f.accept(this, d);
		}
		for(Statement s:method.getStatements())
			result &= s.accept(this,method.enclosingScope());
		return result;
	}

	@Override
	public Boolean visit(StaticMethod method, MySymbolTable d) {
		boolean result = true;
		for(Formal f:method.getFormals()){
			result &=f.accept(this, d);
		}
		for(Statement s:method.getStatements())
			result &= s.accept(this,method.enclosingScope());
		return result;
	}

	@Override
	public Boolean visit(LibraryMethod method, MySymbolTable d) {
		boolean result = true;
		for(Formal f:method.getFormals()){
			result &=f.accept(this, d);
		}
		for(Statement s:method.getStatements())
			result &= s.accept(this,method.enclosingScope());
		return result;
	}

	@Override
	public Boolean visit(Formal formal, MySymbolTable d) {
		// TODO Auto-generated method stub
		if(formal.getType() instanceof PrimitiveType)
			return true;
		if (!checkUserType(formal.getType())){
			semanticErrors.add(new SemanticError("undefined user type for formal "+formal.getName() , formal.getLine()));
			return false;
		}
		return true;
	}

	@Override
	public Boolean visit(PrimitiveType type, MySymbolTable d) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(UserType type, MySymbolTable d) {
		// TODO Auto-generated method stub
		return checkUserType(type);
	}

	@Override
	public Boolean visit(Assignment assignment, MySymbolTable d) {
		
		return assignment.getVariable().accept(this,d) && assignment.getAssignment().accept(this,d) ;
	}

	@Override
	public Boolean visit(CallStatement callStatement, MySymbolTable d) {
		// TODO Auto-generated method stub
		return callStatement.getCall().accept(this, d);
	}

	@Override
	public Boolean visit(Return returnStatement, MySymbolTable d) {
		// TODO Auto-generated method stub
		return returnStatement.getValue().accept(this, d) ;
	}

	@Override
	public Boolean visit(If ifStatement, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = ifStatement.getCondition().accept(this, d);
		result &= ifStatement.getOperation().accept(this, d);
		if(ifStatement.hasElse())
			result &= ifStatement.getElseOperation().accept(this, d);
		return result;
	}

	@Override
	public Boolean visit(While whileStatement, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = whileStatement.getCondition().accept(this, d);
		result &= whileStatement.getOperation().accept(this, whileStatement.enclosingScope());
		return result;
	}

	@Override
	public Boolean visit(Break breakStatement, MySymbolTable d) {
		if(! checkLoop(d)){
			semanticErrors.add(new SemanticError("break statement not inside while scope ", breakStatement.getLine()));
			return false;
		}
		return true;
	}

	@Override
	public Boolean visit(Continue continueStatement, MySymbolTable d) {
		if(! checkLoop(d)){
			semanticErrors.add(new SemanticError("continue statement not inside while scope ", continueStatement.getLine()));
			return false;
		}
		return true;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = true;
		for(Statement s:statementsBlock.getStatements()){
			result &= s.accept(this,statementsBlock.enclosingScope());
		}
		return result;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = localVariable.getType().accept(this, d);
		if(localVariable.hasInitValue())
			result &= localVariable.getInitValue().accept(this, d);
		return result;
	}

	@Override
	public Boolean visit(VariableLocation location, MySymbolTable d) {
		boolean result = true;
		/*if(!location.isExternal()){
			result = checkVariable(location.getName(), d);
			if(!result){
				semanticErrors.add(new SemanticError("undefined variable with the name "+location.getName() , location.getLine()));
				return false;
			}
		}*/
		if(location.isExternal()){ // need to think what to do here !!!
			result &= location.getLocation().accept(this, d);
			result &= checkVariable(location.getName(), location.getLocation().enclosingScope()); // need to check for location in another scope
		}	
		return result;
	}

	@Override
	public Boolean visit(ArrayLocation location, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = location.getArray().accept(this, d) && location.getIndex().accept(this, d);
		return result;
	}

	@Override
	public Boolean visit(StaticCall call, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = checkUserType(new UserType(0, call.getClassName()));
		if(result == false){
			semanticErrors.add(new SemanticError("undefined class for class name "+call.getClassName()+" in static call" , call.getLine()));			
			return false;
		}
		
		MySymbolTable calledClass = classScopes.get(call.getClassName());
		
		if(!calledClass.getEntries().containsKey(call.getName())){
			semanticErrors.add(new SemanticError("call to undefined static function with name "+call.getName()+" in class "+ call.getClassName(), call.getLine()));			
			return false;
		}
		
		if(calledClass.getEntries().get(call.getName()).getKind() != Kind.Static_Method){
			semanticErrors.add(new SemanticError("function "+call.getName()+" in class "+ call.getClassName()+" is not static", call.getLine()));			
			return false;
		}
		
		for(Expression e : call.getArguments()){
			result &= e.accept(this, d);
		}
		return result;
	}

	@Override
	public Boolean visit(VirtualCall call, MySymbolTable d) {
		// TODO Auto-generated method stub
		boolean result = true;
		if(!call.isExternal()){ // direct calling
			result = checkFunction(call.getName(), d);
			if(!result){
				semanticErrors.add(new SemanticError("call to undefined function "+call.getName(), call.getLine()));			
				return false;
			}
			
		}
		else{
			result &= call.getLocation().accept(this, d); // check the location first
			if(!result)
				return false;
			
			if(call.getLocation() instanceof This){ // calling to virtual method of current class
				result = checkFunction(call.getName(), d);
				if(!result){
					semanticErrors.add(new SemanticError("call to undefined function "+call.getName(), call.getLine()));			
					return false;
				}
				
			}
			else{ // calling to virtual method of other class, need to change the scope ???
				
			}
		}
		
		for(Expression f : call.getArguments()){
			result &= f.accept(this,d);
		}
		
		return result;
	}

	@Override
	public Boolean visit(This thisExpression, MySymbolTable d) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(NewClass newClass, MySymbolTable d) {
		// TODO Auto-generated method stub
		if(classScopes.containsKey(newClass.getName()))
			return true;
		semanticErrors.add(new SemanticError("trying to instantiate an undefined class "+newClass.getName(), newClass.getLine()));			
		return false;
		
	}

	@Override
	public Boolean visit(NewArray newArray, MySymbolTable d) {
		if(! (newArray.getType() instanceof PrimitiveType)){ // array of non primitive type
			if(! classScopes.containsKey(newArray.getType().getName())){ // check if type exists	 		
				semanticErrors.add(new SemanticError("trying to instantiate an array of an undefined class "+newArray.getType().getName(), newArray.getLine()));			
				return false;
			}
		}
		
		return newArray.getSize().accept(this, d);
	}

	@Override
	public Boolean visit(Length length, MySymbolTable d) {
		
		return length.getArray().accept(this, d);
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, MySymbolTable d) {
		// TODO Auto-generated method stub
		return binaryOp.getFirstOperand().accept(this, d) && binaryOp.getSecondOperand().accept(this, d);
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, MySymbolTable d) {
		// TODO Auto-generated method stub
		return binaryOp.getFirstOperand().accept(this, d) && binaryOp.getSecondOperand().accept(this, d);
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, MySymbolTable d) {
		// TODO Auto-generated method stub
		return unaryOp.getOperand().accept(this, d);
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, MySymbolTable d) {
		// TODO Auto-generated method stub
		return unaryOp.getOperand().accept(this, d);
	}

	@Override
	public Boolean visit(Literal literal, MySymbolTable d) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, MySymbolTable d) {
		// TODO Auto-generated method stub
		return expressionBlock.getExpression().accept(this, d);
	}

	@Override
	public Boolean visit(MethodType methodType, MySymbolTable d) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private boolean checkUserType(Type t){
		
		Map<String,MySymbolRecord> records = globalScope.getEntries();
		if(records.containsKey(t.getName()))
			return true;
		return false;
	}
	
	private boolean checkVariable(String varName, MySymbolTable scope){
		
		while(scope.getParent()!=null){
			Map<String,MySymbolRecord> records = scope.getEntries();
			if(records.containsKey(varName) && 
					(records.get(varName).getKind()== Kind.Local_Variable ||records.get(varName).getKind()== Kind.Parameter || records.get(varName).getKind()== Kind.Field ))
				return true;
			scope = scope.getParent();
		}
		return false;
	}
	
private boolean checkFunction(String varName, MySymbolTable scope){
		
		while(scope.getParent()!=null){
			Map<String,MySymbolRecord> records = scope.getEntries();
			if(records.containsKey(varName) && 
					(records.get(varName).getKind()== Kind.Virtual_Method || records.get(varName).getKind()== Kind.Static_Method) && 
					records.get(varName).isDeclared())
				return true;
			scope = scope.getParent();
		}
		return false;
	}
	
	
	private void putAllClasseScopes(MySymbolTable scope){
		classScopes.put(scope.getDescription(), scope);
		if(scope.getChildren().size() == 0)
			return;
		
		for(MySymbolTable child: scope.getChildren())
			putAllClasseScopes(child);
	}
	
	private boolean checkLoop(MySymbolTable scope){
		while(scope.getParent()!= null){
			if(scope.getDescription().startsWith("while"))
				return true;
			scope = scope.getParent();
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
