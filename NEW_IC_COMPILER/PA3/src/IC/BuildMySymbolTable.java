package IC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import IC.MySymbolRecord.Kind;
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
import IC.AST.Type;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;

public class BuildMySymbolTable implements PropagatingVisitor<MySymbolTable, Boolean>{
	static int uniqueTable = 0;
	static int uniqueRecord = 0;	
	MyTypeTable typeTable;
	
	public MyTypeTable getTypeTable(){
		return typeTable;
	}
	
	public BuildMySymbolTable(){
		typeTable = new MyTypeTable();
	}
	
	
	@Override
	public Boolean visit(Program program, MySymbolTable d) {
		//build current symbol table
		MySymbolTable table = new MySymbolTable(uniqueTable++,"Global");
		table.setParent(null);
		boolean returnValue = true;
		//list to hold all the classes, each time we deal with a class we delete it
		List<ICClass> allClasses = new ArrayList<ICClass>();
		
		for(ICClass c : program.getClasses()){
			MySymbolRecord record =  new MySymbolRecord(uniqueRecord++,c,Kind.Class,new UserType(c.getLine(), c.getName()));
			if(!table.InsertRecord(c.getName(),record)){
				
				return Boolean.FALSE;
			}
			MyType mt = typeTable.insertType(c.getMyType());
			record.setMyType(mt);
			c.setRecord(record);
		}

		
		for(ICClass c : program.getClasses()){
			//directly connected classes
			if(!c.hasSuperClass()){
				returnValue &= c.accept(this, table);
			}else{
				allClasses.add(c);
			}
		}
		MySymbolRecord rec;
		boolean changed = false;
		List<ICClass> removeClasses = new ArrayList<ICClass>();
		while(allClasses.size() > 0){
			changed = false;
			for(ICClass c : allClasses){
				//connected to other classes
				if(c.hasSuperClass()){
					rec = table.Lookup(c.getSuperClassName());
					if(rec != null){
						//check if we already accepted
						//rec.getNode() is the parent class
						if(!allClasses.contains((ICClass)rec.getNode()))
						{
							returnValue &= c.accept(this,rec.getNode().enclosingScope());
							removeClasses.add(c);
							changed = true;
						}
					}else{
						System.out.println("Undefined Class " + c.getSuperClassName());
						return Boolean.FALSE;
					}
				}
			}
			if(!changed){
				StringBuffer str = new StringBuffer();
				if(allClasses.size() > 0){
					str.append("Inheretence cycle detected with class ");
					str.append(allClasses.get(0).getName());
					str.append(" at line ");
					str.append(allClasses.get(0).getLine());
				}else{
					str.append("Inheretence cycle detected");
				}
				System.out.println(str);
				return Boolean.FALSE;
			}else{
				//update list
				allClasses.removeAll(removeClasses);
				removeClasses.clear();
			}
		}
		program.setEnclosingScope(table);
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(ICClass icClass, MySymbolTable d) {
		
		
		//d is the symbol table of the parent symbol class
		boolean returnValue = true;
		MySymbolTable table = new MySymbolTable(uniqueTable++,icClass.getName());
		table.setParent(d);
		for(Field f : icClass.getFields()){
			MySymbolRecord record = new MySymbolRecord(uniqueRecord++,f,Kind.Field,f.getType());
			if(!table.InsertRecord(f.getName(),record )){				
				return Boolean.FALSE;
			}
			MyType mt = typeTable.insertType(f.getType().getMyType());
			record.setMyType(mt);
			f.setRecord(record);
		}		
		for(Field f : icClass.getFields()){
			returnValue &= f.accept(this,table);
		}
		for(Method m : icClass.getMethods()){
			returnValue &= m.accept(this,table);
		}				
		returnValue &= icClass.getUserType().accept(this, table);
		icClass.setEnclosingScope(table);
		d.addChild(table);
		try {
			String m = icClass.getName();
			checkShadowing(table);
		} catch (SemanticError e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return false;
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(Field field, MySymbolTable d) {
		boolean returnValue = field.getType().accept(this,d);
		field.setEnclosingScope(d);
		return returnValue;
	}

	public Boolean visit(Method method,MySymbolTable d, Kind kind){
		boolean returnValue = true;	
		MySymbolRecord trecord = new MySymbolRecord(uniqueRecord++,method,kind,method.getType());
		returnValue = d.InsertRecord(method.getName(),trecord) ;
		MyType methodType = typeTable.insertType(method.getMyType());
		trecord.setMyType(methodType);
		method.setRecord(trecord);	
		MyType retType = typeTable.insertType(method.getReturnType().getMyType());
		
		
		MySymbolTable table = new MySymbolTable(uniqueTable++,method.getName());
		table.setParent(d);
		MySymbolRecord returnRecord = new MySymbolRecord(uniqueRecord++, method, kind, method.getType());
		MyType returnMyType = typeTable.insertType(retType);
		returnRecord.setMyType(returnMyType);
		table.InsertRecord("$ret", returnRecord);
		returnValue &= method.getType().accept(this,table);
		for(Formal f : method.getFormals()){
			MySymbolRecord record = new MySymbolRecord(uniqueRecord++,f,Kind.Parameter,f.getType());
			if(!table.InsertRecord(f.getName(), record)){
				
				return false;
			}
			MyType mt = typeTable.insertType(f.getType().getMyType());
			record.setMyType(mt);				
			f.setRecord(record);
		}
		for(Statement s : method.getStatements()){
			returnValue &= s.accept(this, table);
		}
		
		method.setEnclosingScope(table);
		d.addChild(table);
		return returnValue;
	}
	
	@Override
	public Boolean visit(VirtualMethod method, MySymbolTable d) {
		return visit(method, d,Kind.Virtual_Method);
	}

	@Override
	public Boolean visit(StaticMethod method, MySymbolTable d) {
		return visit(method, d,Kind.Static_Method);
	}

	@Override
	public Boolean visit(LibraryMethod method, MySymbolTable d) {
		return visit(method, d,Kind.Library_Method);
	}

	@Override
	public Boolean visit(Formal formal, MySymbolTable d) {
		formal.setEnclosingScope(d);
		boolean returnValue = formal.getType().accept(this, d);
		if(returnValue){
			MySymbolRecord record = new MySymbolRecord(uniqueRecord++,formal,Kind.Parameter,formal.getType());
			record.setAsDeclared();
			d.InsertRecord(formal.getName(),record );
			MyType mt = typeTable.insertType(formal.getType().getMyType());
			record.setMyType(mt);					
			formal.setRecord(record);
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(PrimitiveType type, MySymbolTable d) {
		type.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(UserType type, MySymbolTable d) {
		type.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(Assignment assignment, MySymbolTable d) {
		assignment.setEnclosingScope(d);
		boolean var = assignment.getVariable().accept(this,d);
		boolean assign =  assignment.getAssignment().accept(this,d);
		if(assign){
			//Does not work!
			//setInitialization(((VariableLocation)assignment.getVariable()).getName(), d);
		}
		return var&&assign;
	}

	@Override
	public Boolean visit(CallStatement callStatement, MySymbolTable d) {
		callStatement.setEnclosingScope(d);
		boolean returnValue = callStatement.getCall().accept(this,d);
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(Return returnStatement, MySymbolTable d) {
		returnStatement.setEnclosingScope(d);
		boolean returnValue = true;
		if(returnStatement.hasValue()){
			returnValue &= returnStatement.getValue().accept(this, d);	
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(If ifStatement, MySymbolTable d) {
		ifStatement.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= ifStatement.getOperation().accept(this, d);
		if(ifStatement.hasElse()){
			returnValue &= ifStatement.getElseOperation().accept(this, d);
		}
		returnValue &= ifStatement.getCondition().accept(this, d);
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(While whileStatement, MySymbolTable d) {
		boolean returnValue = whileStatement.getCondition().accept(this, d);
		
		returnValue &= whileStatement.getOperation().accept(this, d);	
		//MySymbolTable table = new MySymbolTable(uniqueTable++,"while in " + d.getDescription());
		//table.setParent(d);
		whileStatement.setEnclosingScope(d);
		//d.getChildren().add(d);		
			
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(Break breakStatement, MySymbolTable d) {
		breakStatement.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(Continue continueStatement, MySymbolTable d) {
		continueStatement.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, MySymbolTable d) {
		boolean returnValue = true;
		//d is the symbol table of the parent symbol class
		MySymbolTable table = new MySymbolTable(uniqueTable++,"statement block in " + d.getDescription());
		table.setParent(d);
		
		for(Statement s : statementsBlock.getStatements()){
			returnValue &= s.accept(this, table);
		}
		statementsBlock.setEnclosingScope(table);
		d.addChild(table);
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(LocalVariable localVariable, MySymbolTable d) {
		localVariable.setEnclosingScope(d);
		MySymbolRecord record = new MySymbolRecord(uniqueRecord++,localVariable,Kind.Local_Variable,localVariable.getType());
		boolean returnValue = d.InsertRecord(localVariable.getName(), record);
		if(!returnValue){			
			System.out.println("ERROR INSERTING " + localVariable.getName() + " to table " + d.getId());
		}
		record.setAsDeclared();
		returnValue &= localVariable.getType().accept(this,d);
		if(localVariable.hasInitValue()){
			localVariable.getInitValue().accept(this, d);
			record.setAsInitialized();
		}
		if(returnValue){
			MyType mt= typeTable.insertType(localVariable.getType().getMyType());				
			record.setMyType(mt);
			localVariable.setRecord(record);			
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(VariableLocation location, MySymbolTable d) {
		location.setEnclosingScope(d);
		boolean returnValue = true;
		if(location.isExternal()){
			returnValue = location.getLocation().accept(this,d);
			if(!returnValue)
				return false;
		}
		try {
			checkVariableDeclaration(location, d);
		} catch (SemanticError e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return false;
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(ArrayLocation location, MySymbolTable d) {
		location.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= location.getArray().accept(this,d);
		returnValue &= location.getIndex().accept(this,d);
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(StaticCall call, MySymbolTable d) {
		call.setEnclosingScope(d);
		boolean returnValue = true;
		for(IC.AST.Expression e : call.getArguments()){
			returnValue &= e.accept(this, d);
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(VirtualCall call, MySymbolTable d) {
		call.setEnclosingScope(d);
		boolean returnValue = true;
		if(call.isExternal())
			returnValue &= call.getLocation().accept(this,d);
		for(IC.AST.Expression e : call.getArguments()){
			returnValue &= e.accept(this, d);
		}
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(This thisExpression, MySymbolTable d) {
		thisExpression.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(NewClass newClass, MySymbolTable d) {
		newClass.setEnclosingScope(d);
		return Boolean.TRUE;
	}

	@Override
	public Boolean visit(NewArray newArray, MySymbolTable d) {
		newArray.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= newArray.getSize().accept(this, d);
		returnValue &= newArray.getType().accept(this, d);
				
		return new Boolean(returnValue);
	}

	@Override
	public Boolean visit(Length length, MySymbolTable d) {
		length.setEnclosingScope(d);
		boolean returnValue = length.getArray().accept(this, d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, MySymbolTable d) {
		binaryOp.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= binaryOp.getFirstOperand().accept(this, d);
		returnValue &= binaryOp.getSecondOperand().accept(this, d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, MySymbolTable d) {
		binaryOp.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= binaryOp.getFirstOperand().accept(this, d);
		returnValue &= binaryOp.getSecondOperand().accept(this, d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, MySymbolTable d) {
		unaryOp.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= unaryOp.getOperand().accept(this,d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, MySymbolTable d) {
		unaryOp.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue &= unaryOp.getOperand().accept(this,d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(Literal literal, MySymbolTable d) {
		literal.setEnclosingScope(d);
		boolean returnValue = literal.getICType().accept(this,d);
		
		return  returnValue;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, MySymbolTable d) {
		expressionBlock.setEnclosingScope(d);
		boolean returnValue = true;
		returnValue = expressionBlock.getExpression().accept(this, d);
		return new Boolean(returnValue);	
	}

	@Override
	public Boolean visit(MethodType methodType, MySymbolTable d) {
		
		methodType.setEnclosingScope(d);
		boolean returnValue = true;
		for(Type t : methodType.getFormalTypes()){
			returnValue &= t.accept(this, d);
		}
		returnValue &= methodType.getReturnType().accept(this, d);
		
		return new Boolean(returnValue);
	}
	
	/**
	 * Shadowing semantic check in build time
	 * @throws SemanticError 
	 */
	private void checkShadowing(MySymbolTable classTable) throws SemanticError{
		
		MySymbolTable extendedClassTable = classTable.getParent();
		Map<String,MySymbolRecord> c_symbols = classTable.getEntries(); 
		Map<String,MySymbolRecord> ex_symbols ;
		Set<Entry<String,MySymbolRecord>> c_entries = c_symbols.entrySet();
		
		
		while(extendedClassTable.getParent() != null){
			//check fields and methods shadowing and correct overriding
			ex_symbols = extendedClassTable.getEntries();
			
			for(Entry<String,MySymbolRecord> symbol : c_entries){
				if(ex_symbols.containsKey(symbol.getKey())){ // field or method with the same name as in extended class
					if(symbol.getValue().getKind()==Kind.Field){
						throw new SemanticError("Field or method with the field name "+symbol.getKey()+" already defined in extended classes", 
								symbol.getValue().getNode().getLine());
						// shadowing with a filed name is not allowed 
					}
					if(symbol.getValue().getKind() == Kind.Virtual_Method || symbol.getValue().getKind() == Kind.Static_Method ){
						MySymbolRecord ex_symbol_rec = ex_symbols.get(symbol.getKey());
						MySymbolRecord symbol_rec = symbol.getValue();
						if(ex_symbol_rec.getKind()!=symbol_rec.getKind()) // method name shadows a field or method with different type(static/virtual)
							throw new SemanticError("Method or field with the method name "+symbol.getKey()+" already defined in extended classes", 
									symbol.getValue().getNode().getLine());						
						//check method signatures
						if(! symbol_rec.getType().equals(ex_symbol_rec.getType()))
							throw new SemanticError("Method with the method name "+symbol.getKey()+" already defined in extended classes. Overloading is not allowed.", 
									symbol.getValue().getNode().getLine());	
					}
				}
			}
			
			extendedClassTable = extendedClassTable.getParent();
		}
		
	}
	
	private void checkVariableDeclaration(VariableLocation var, MySymbolTable scope) throws SemanticError{
		String varName = var.getName();
		while(scope.getParent()!=null){
			if(scope.getEntries().containsKey(varName) && 
					(scope.getEntries().get(varName).getKind() == Kind.Local_Variable || scope.getEntries().get(varName).getKind() == Kind.Field || scope.getEntries().get(varName).getKind() == Kind.Parameter))
			{
				if(scope.getEntries().get(varName).getKind() == Kind.Local_Variable && !scope.getEntries().get(varName).isInitialized()) // check for initialization
					throw new SemanticError("variable "+varName+" has not been initialized",var.getLine());
				else
					return;
			}
			scope = scope.getParent();
		}
		
		throw new SemanticError("use of undefined variable "+varName,var.getLine());
	}
	
	private void setInitialization(String varName,  MySymbolTable scope ){
		while(scope.getParent()!=null){
			if(scope.getEntries().containsKey(varName)){
				scope.getEntries().get(varName).setAsInitialized();
				break;
			}
			scope = scope.getParent();
		}
	}
}
