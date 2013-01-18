package IC.semanticChecks; 

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import IC.mySymbolTable.*;
import IC.mySymbolTable.MySymbolRecord.Kind;
import IC.AST.*;

public class MySemanticAnalyzer implements PropagatingVisitor<MySymbolTable, Boolean> {
	private MySymbolTable globalScope;
	private Map<String,MySymbolTable> classScopes = new LinkedHashMap<String, MySymbolTable>();
	private List<SemanticError> semanticErrors = new ArrayList<SemanticError>();
	
	//continue and break flags
	private int loopCounter = 0;
	@Override
	public Boolean visit(Program program, MySymbolTable d) {
		globalScope = program.enclosingScope();
		Boolean result = true;
		for(ICClass c:program.getClasses())
			putAllClasseScopes(c.enclosingScope());
		boolean g;
		
		for(ICClass c:program.getClasses())			
			result &= c.accept(this,program.enclosingScope());
		return result;
	}

	@Override
	public Boolean visit(ICClass icClass, MySymbolTable d) {
		MySymbolTable scope = icClass.enclosingScope();		
				
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
		if(returnStatement.hasValue())
			return returnStatement.getValue().accept(this, d);
		return true;
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
		boolean result = whileStatement.getCondition().accept(this, d);
		loopCounter++;
		result &= whileStatement.getOperation().accept(this, whileStatement.enclosingScope());
		loopCounter--;
		return result;
	}

	@Override
	public Boolean visit(Break breakStatement, MySymbolTable d) {
		if(loopCounter == 0){
			semanticErrors.add(new SemanticError("break statement not inside while scope ", breakStatement.getLine()));
			return false;
		}
		return true;
	}

	@Override
	public Boolean visit(Continue continueStatement, MySymbolTable d) {
		if(loopCounter == 0){
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
		/*boolean filed = checkVariable(location.getName(), d,Kind.Field);
		boolean param = checkVariable(location.getName(), d,Kind.Parameter);
		boolean local = checkVariable(location.getName(), d,Kind.Local_Variable);*/
		
		MySymbolRecord locationRecord = location.enclosingScope().Lookup(location.getName());
		if(locationRecord == null){
			return false;
		}
		boolean filed = false;
		boolean param = false;
		boolean local = false;
		switch(locationRecord.getKind()){
		case Field:
			filed = true;
			break;
		case Parameter:
			param = true;
			break;
		case Local_Variable:
			local = true;
			break;
		}
		if(location.isExternal()){
			boolean result = location.getLocation().accept(this, d);
			if(!result)
				return false;
			
			if(location.getLocation() instanceof This){
				if(!filed){
					semanticErrors.add(new SemanticError("undefined field with the name "+location.getName() , location.getLine()));
					return false;
				}
				if(filed){
					//check that it is not a call from static method
					String desc = d.getDescription();
					MySymbolTable enc = location.enclosingScope();
					MySymbolRecord rec = location.enclosingScope().Lookup(desc);
					if(rec != null){
						if(rec.getKind() == Kind.Static_Method || rec.getKind() == Kind.Library_Method){
							semanticErrors.add(new SemanticError("Cannot call field "+location.getName() + " from a static method" , location.getLine()));
							return false;						
						}
					}
				}
			}
			//field of another class - check in type rules
			return true;
		}	
		
		
		
		// direct reference
		else{	
			if(filed){
				//check that it is not a call from static method
				String desc = d.getDescription();
				MySymbolTable enc = location.enclosingScope();
				MySymbolRecord rec = location.enclosingScope().Lookup(desc);
				if(rec != null){
					if(rec.getKind() == Kind.Static_Method || rec.getKind() == Kind.Library_Method){
						semanticErrors.add(new SemanticError("Cannot call field "+location.getName() + " from a static method" , location.getLine()));
						return false;						
					}
				}
			}
			
			if(!(filed || param  || local )){
				semanticErrors.add(new SemanticError("undefined variable/field with the name "+location.getName() , location.getLine()));
				return false;
			}
		}
		
		return true;
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
		
		if(calledClass.getEntries().get(call.getName()).getKind() != Kind.Static_Method && calledClass.getEntries().get(call.getName()).getKind() != Kind.Library_Method){
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
			result = checkFunction(call.getName(), d, Kind.Virtual_Method);
			if(result){
				//virtual call cannot be made from a static scope
				String desc = d.getDescription();
				MySymbolTable enc = call.enclosingScope();
				MySymbolRecord rec = call.enclosingScope().Lookup(desc);
				if(rec != null){
					if(rec.getKind() == Kind.Static_Method || rec.getKind() == Kind.Library_Method){
						semanticErrors.add(new SemanticError("virtual call cannot be made from a static method" , call.getLine()));
						return false;						
					}
				}

			}
			result |= checkFunction(call.getName(), d, Kind.Static_Method);
			if(!result){
				semanticErrors.add(new SemanticError("call to undefined  function "+call.getName(), call.getLine()));			
				return false;
			}
			
		}
		else{
			result &= call.getLocation().accept(this, d); // check the location first
			if(!result)
				return false;
			
			if(call.getLocation() instanceof This){ // calling to virtual method of current class
				result = checkFunction(call.getName(), d, Kind.Virtual_Method);
				result |= checkFunction(call.getName(), d, Kind.Static_Method);
				if(!result){
					semanticErrors.add(new SemanticError("call to undefined virtual function "+call.getName(), call.getLine()));			
					return false;
				}
				
			}
			else{ // calling to virtual method of other class
				//System.out.println("classname = " + call.getLocation().toString());
				if(call.getLocation() instanceof ExpressionBlock){ 
					Expression newExp= ((ExpressionBlock)call.getLocation()).getExpression();
					if(newExp instanceof NewClass){// something like (new MyClass).m
						result = checkFunction(call.getName(), classScopes.get(((NewClass)newExp).getName()), Kind.Virtual_Method);
						if(!result){
							semanticErrors.add(new SemanticError("call to undefined virtual function "+call.getName()+ " of class "+ ((NewClass)newExp).getName(), call.getLine()));			
							return false;
						}
					}
					
					
						
				}
				else if(call.getLocation() instanceof VariableLocation){
					/* doesn't work yet  */
					/*
					MySymbolRecord r = call.getLocation().enclosingScope().Lookup(((VariableLocation)call.getLocation()).getName());
					MySymbolTable t = classScopes.get(r.getType().getName());
					if(! t.getEntries().containsKey(((VariableLocation)call.getLocation()).getName())){
						semanticErrors.add(new SemanticError("call to undefined virtual function "+call.getName()+ " of class "+ r.getType().getName(), call.getLine()));			
						return false;
					}*/
				}return true;
				
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
		//check that the scope is not static
		String desc = d.getDescription();
		MySymbolTable enc = thisExpression.enclosingScope();
		MySymbolRecord rec = thisExpression.enclosingScope().Lookup(desc);
		if(rec != null){
			if(rec.getKind() == Kind.Static_Method || rec.getKind() == Kind.Library_Method){
				semanticErrors.add(new SemanticError("Cannot use 'this' in a static method" , thisExpression.getLine()));
				return false;						
			}
		}
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
	
	private boolean checkVariable(String varName, MySymbolTable scope,Kind varKind){
		
		while(scope!=globalScope){
			Map<String,MySymbolRecord> records = scope.getEntries();
			if(records.containsKey(varName) && 
					(records.get(varName).getKind()==varKind))
				return true;
			scope = scope.getParent();
		}
		return false;
	}	
	
	
private boolean checkFunction(String varName, MySymbolTable scope ,Kind functionKind){
		
		while(scope.getParent()!=null){
			Map<String,MySymbolRecord> records = scope.getEntries();
			if(records.containsKey(varName) && 
					(records.get(varName).getKind()== functionKind) && 
					records.get(varName).isDeclared())
				return true;
			scope = scope.getParent();
		}
		return false;
	}
	
	
	private void putAllClasseScopes(MySymbolTable scope){
		classScopes.put(scope.getDescription(), scope);
		//System.out.println("puting scope "+scope.getDescription());
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
