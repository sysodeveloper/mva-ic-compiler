package IC;

import IC.SymbolRecord.Kind;
import IC.AST.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.org.apache.bcel.internal.generic.PUTSTATIC;

/**
 * 
 * The symbol table of each scope.
 *
 */
public class SymbolTable implements Visitor<Boolean> {
	/**
	 * The id of the table.
	 */
	private int m_id; 
	
	/**
	 * The next id for table.
	 */
	private static int m_next_id = 0;
	
	/**
	 * The next record id.
	 */
	private int m_recordID;
	
	/**
	 * The parent node of this table.
	 */
	private SymbolTableContainer m_parentNode;
	
	/** The parent table.
	 */
	private SymbolTable m_parent;
	
	/**
	 * Get the direct children symbol tables.
	 */
	private List<SymbolTable> m_childTables;

	/**
	 * The entries of the table.
	 */
	
	private Map<String, SymbolRecord> m_entries;
	
	/**
	 * Hold all the used type. 
	 */
	private static Set<Type> m_usedType = new HashSet<Type>();
	
	/**
	 * The root symbol table of the program.
	 */
	private static SymbolTable m_root;
	
	/**
	 * The id's for the used types.
	 */
	private static int UsedTypeId = 1;
	
	/**
	 * Map all the defined classes.
	 */
	private static Map<String, ICClass> m_classTables = new HashMap<String, ICClass>();
	
	/**
	 * @return The id.
	 */
	public int getId() {
		return m_id;
	}
	/**
	 * @return the m_root
	 */
	public static SymbolTable getRoot() {
		return m_root;
	}
	
	/**
	 * @return the m_parentNode
	 */
	public SymbolTableContainer getParentNode() {
		return m_parentNode;
	}
	/**
	 * @param m_parentNode the m_parentNode to set
	 */
	public void setParentNode(SymbolTableContainer m_parentNode) {
		this.m_parentNode = m_parentNode;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		m_id = id;
	}
	
	/**
	 * @return The parent.
	 */
	public SymbolTable getParent() {
		return m_parent;
	}

	/**
	 * @param parent The m_parent to set.
	 */
	public void setParent(SymbolTable table) {
		m_parent = table;
	}

	/**
	 * @return The symbol entries.
	 */
	public Map<String, SymbolRecord> getEntries() {
		return m_entries;
	}
	
	public boolean putSymbol(String key, SymbolRecord record, ASTNode node) {
		if(getEntries().containsKey(key)){
			SemanticAnalyse.getInstance().getErrors().add(
					new SemanticError("Redefinition of " + key, node.getLine()));
			return false;
		}
		getEntries().put(key, record);
		return true;
	}
	
	/**
	 * Put the current type in the used type.
	 * Add also all the lower dimensions.
	 * @param type
	 * @return
	 */
	public void putUsedType(Type type) {
		for (int d = 0; d <= type.getDimension(); ++d) {
			Type t = (Type)type.clone();
			
			t.setDimension(d);
			if(!getUsedType().contains(t))
			{
				t.setID(UsedTypeId++);
				getUsedType().add(t);
			}
		}
	}
	
	/**
	 * @return The usedType.
	 */
	public static Set<Type> getUsedType() {
		return m_usedType;
	}
	/**
	 * @param usedType The usedType to set.
	 */
	public void setUsedType(Set<Type> usedType) {
		m_usedType = usedType;
	}
	
	/**
	 * Create the symbol table for the given record.
	 * @param id The id of the table.
	 * @param parent The parent of the scope.
	 * @param parentNode The node own the table.
	 */
	public SymbolTable(int id, SymbolTable parent, SymbolTableContainer parentNode) {
		m_recordID = 0;
		setId(id);
		setParent(parent);
		m_entries = new LinkedHashMap<String, SymbolRecord>();
		setChildTables(new ArrayList<SymbolTable>());
		setParentNode(parentNode);
	}
	
	/**
	 * Create the general scope symbol table.
	 * @param id The id of the table.
	 */
	public SymbolTable(int id) {
		this(id, null, null);
	}
	
	/**
	 * This function return the next id for this table.
	 * @return
	 */
	public static int getNextId() {
		return ++m_next_id;
	}
	
	/**
	 * This function return the next record id for this table.
	 * @return
	 */
	public int getRecordId() {
		return ++m_recordID;
	}
	
	/**
	 * @return the childTables
	 */
	public List<SymbolTable> getChildTables() {
		return m_childTables;
	}
	/**
	 * @param childTables the childTables to set
	 */
	public void setChildTables(List<SymbolTable> m_childTables) {
		this.m_childTables = m_childTables;
	}
	
	/**
	 * Reorder The classes so it could iterate as defined.
	 * @param classes The original list.
	 * @return The new list.
	 */
	private List<ICClass> orderClasses(List<ICClass> classes) {
		List<ICClass> tmpClasses = new ArrayList<ICClass>();
		List<ICClass> orderedClasses = new ArrayList<ICClass>();
		List<String> defined = new ArrayList<String>();
		ICClass tmpClass;
		// Keep the first class in case of an error.
		ICClass firstClass;
		
		for (ICClass icClass : classes) {
			tmpClasses.add(icClass);
		}
		
		while(!tmpClasses.isEmpty()) {
			tmpClass = null;
			firstClass = null;
			for (ICClass icClass : tmpClasses) {
				if(firstClass == null) {
					firstClass = icClass;
				}
				if(!icClass.hasSuperClass() || defined.contains(icClass.getSuperClassName())) {
					orderedClasses.add(icClass);
					defined.add(icClass.getName());
					tmpClass = icClass;
					break;
				}
			}
			
			if(tmpClass == null) {
				SemanticAnalyse.getInstance().getErrors().add(new SemanticError(
						"cannot find symbol\nsymbol: class " + firstClass.getName(), firstClass.getLine()));
				break;
			}
			tmpClasses.remove(tmpClass);
		}
		
		return orderedClasses;
	}

	/**
	 * Create a new Symbol Record for each class.
	 */
	@Override
	public Boolean visit(Program program) {
		Boolean isOk = true;
		m_root = this;
		setParent(null);
		setParentNode(program);
		program.setInnerTable(this);
		program.setOuterTable(null);
		for (ICClass icClass : orderClasses(program.getClasses())) {
			isOk &= putSymbol(icClass.getName(), new SymbolRecord(getRecordId(), icClass, this, 
					icClass.getName(), Kind.CLASS, 
					new UserType(icClass.getLine(), icClass.getName())), icClass);
			icClass.getInnerTable().setParent(this);
			isOk &= icClass.accept(this);
		}
			
		return isOk;
	}

	/**
	 * Accept the methods and fields so they create their symbol 
	 *   table and register.
	 */
	@Override
	public Boolean visit(ICClass icClass) {
		// TODO: register to parent in TypeTable.
		Boolean isOk = true;
		m_classTables.put(icClass.getName(), icClass);
		if(icClass.hasSuperClass()){
			SymbolTable superTable = (m_classTables.get(icClass.getSuperClassName())).getInnerTable();
			icClass.setOuterTable(superTable);
			superTable.getChildTables().add(icClass.getInnerTable());
		}
		else {
			icClass.setOuterTable(this);
			getChildTables().add(icClass.getInnerTable());
		}
		
		putUsedType(new UserType(icClass.getLine(), icClass.getName()));

		
		for (Field field : icClass.getFields()) {
			isOk &= field.accept(icClass.getInnerTable());
		}
		for (Method method : icClass.getMethods()) {
			isOk &= method.accept(icClass.getInnerTable());
		}
		
		return isOk;
	}

	/**
	 * Add the field to the symbol table.
	 */
	@Override
	public Boolean visit(Field field) {
		field.setOuterTable(this);
		return putSymbol(field.getName(), new SymbolRecord(getRecordId(), field, 
			this, field.getName(), Kind.FIELD, field.getType()), field);
	}
	
	private Boolean methodVisit(Method method, Kind kind) {
		Boolean isOk = true;
		method.setOuterTable(this);
		this.getChildTables().add(method.getInnerTable());
		putUsedType(method.getType());
		isOk &= putSymbol(method.getName(), new SymbolRecord(getRecordId(), method,
				this, method.getName(), kind, 
				method.getType()), method);
		method.getInnerTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}
		method.getType().accept(this);
		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(VirtualMethod method) {
		return methodVisit(method, Kind.VIRTUAL_METHOD);
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(StaticMethod method) {
		return methodVisit(method, Kind.STATIC_METHOD);
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(LibraryMethod method) {
		return methodVisit(method, Kind.LIBRARY_METHOD);
	}

	/**
	 * Add the formal to the table.
	 */
	@Override
	public Boolean visit(Formal formal) {
		formal.setOuterTable(this);
		Boolean retVal = putSymbol(formal.getName(), new SymbolRecord(getRecordId(), formal,
				this, formal.getName(), Kind.PARAMETER, formal.getType()), formal);
		retVal &= formal.getType().accept(this);
		return true;
	}

	/**
	 * Handle primitive type.
	 */
	@Override
	public Boolean visit(PrimitiveType type) {
		type.setOuterTable(this);
		putUsedType(type);
		return true;
	}

	/**
	 * Handle user types.
	 */
	@Override
	public Boolean visit(UserType type) {
		type.setOuterTable(this);
		putUsedType(type);
		return true;
	}

	/**
	 * Add assignment args to the table.
	 */
	@Override
	public Boolean visit(Assignment assignment) {
		Boolean isOk;
		assignment.setOuterTable(this);
		isOk = assignment.getAssignment().accept(this);
		isOk &= assignment.getVariable().accept(this);
		return isOk;
	}

	/**
	 * Handle call statments.
	 */
	@Override
	public Boolean visit(CallStatement callStatement) {
		Boolean isOk = true;
		callStatement.setOuterTable(this);
		for (Expression e : callStatement.getCall().getArguments()) {
			isOk &= e.accept(this);
		}
		return isOk;
	}

	/**
	 * Handle the return statement.
	 */
	@Override
	public Boolean visit(Return returnStatement) {
		returnStatement.setOuterTable(this);
		if(returnStatement.hasValue())
		{
			return returnStatement.getValue().accept(this);
		}
		return true;
	}

	/**
	 * Handle if statement.
	 */
	@Override
	public Boolean visit(If ifStatement) {
		Boolean isOk;
		ifStatement.setOuterTable(this);
		isOk = ifStatement.getCondition().accept(this);
		isOk &= ifStatement.getOperation().accept(this);
		if(ifStatement.hasElse()) {
			isOk &= ifStatement.getElseOperation().accept(this);
		}
		return isOk;
	}

	/**
	 * Handle while.
	 */
	@Override
	public Boolean visit(While whileStatement) {
		Boolean isOk;
		whileStatement.setOuterTable(this);
		isOk = whileStatement.getCondition().accept(this);
		isOk &= whileStatement.getOperation().accept(this);
		
		return isOk;
	}

	/**
	 * Handle break statement.
	 */
	@Override
	public Boolean visit(Break breakStatement) {
		breakStatement.setOuterTable(this);
		return true;
	}

	/**
	 * Handle continue.
	 */
	@Override
	public Boolean visit(Continue continueStatement) {
		continueStatement.setOuterTable(this);
		return true;
	}

	/**
	 * Handle statement block.
	 */
	@Override
	public Boolean visit(StatementsBlock statementsBlock) {
		Boolean isOk = true;
		statementsBlock.setOuterTable(this);
		statementsBlock.getInnerTable().setParent(this);
		for (Statement s : statementsBlock.getStatements()) {
			s.accept(statementsBlock.getInnerTable());
		}
		
		getChildTables().add(statementsBlock.getInnerTable());
		
		return isOk;
	}

	/**
	 * Handle local variables.
	 */
	@Override
	public Boolean visit(LocalVariable localVariable) {
		Boolean isOk = true;
		localVariable.setOuterTable(this);
		isOk &= putSymbol(localVariable.getName(), new SymbolRecord(
				getRecordId(), localVariable, this, localVariable.getName(), 
				Kind.LOCAL_VARIABLE, localVariable.getType()), localVariable);
		if(localVariable.hasInitValue()) {
			isOk = localVariable.getInitValue().accept(this);
		}
		
		localVariable.getType().accept(this);
		return isOk;
	}

	/**
	 * Handle variable location.
	 */
	@Override
	public Boolean visit(VariableLocation location) {
		location.setOuterTable(this);
		if (location.isExternal()) {
			return location.getLocation().accept(this);
		}
		return true;
	}

	/**
	 * Handle array location.
	 */
	@Override
	public Boolean visit(ArrayLocation location) {
		Boolean isOk;
		location.setOuterTable(this);
		isOk = location.getArray().accept(this);
		isOk &= location.getIndex().accept(this);
		return isOk;
	}

	/**
	 * Handle static call.
	 */
	@Override
	public Boolean visit(StaticCall call) {
		Boolean isOk = true;
		call.setOuterTable(this);
		for (Expression e : call.getArguments()) {
			isOk &= e.accept(this);
		}
		return isOk;
	}

	/**
	 * Handle virtual method call.
	 */
	@Override
	public Boolean visit(VirtualCall call) {
		Boolean isOk = true;
		call.setOuterTable(this);
		for (Expression e : call.getArguments()) {
			isOk &= e.accept(this);
		}
		
		if(call.isExternal()) {
			call.getLocation().accept(this);
		}
		
		return isOk;
	}

	/**
	 * Handle this key word.
	 */
	@Override
	public Boolean visit(This thisExpression) {
		thisExpression.setOuterTable(this);
		return true;
	}

	/**
	 * Handle new class key word.
	 */
	@Override
	public Boolean visit(NewClass newClass) {
		newClass.setOuterTable(this);
		return true;
	}

	/**
	 * Handle new array key word.
	 */
	@Override
	public Boolean visit(NewArray newArray) {
		newArray.setOuterTable(this);
		return newArray.getType().accept(this);
	}

	/**
	 * The length key word.
	 */
	@Override
	public Boolean visit(Length length) {
		length.setOuterTable(this);
		return length.getArray().accept(this);
	}

	/**
	 * Handle Math binary operation.
	 */
	@Override
	public Boolean visit(MathBinaryOp binaryOp) {
		Boolean isOk;
		binaryOp.setOuterTable(this);
		isOk = binaryOp.getFirstOperand().accept(this);
		isOk &= binaryOp.getSecondOperand().accept(this);
		
		// TODO: Add operation validation. (How to get expression type?)
		return isOk;
	}

	/**
	 * Handle logical operation.
	 */
	@Override
	public Boolean visit(LogicalBinaryOp binaryOp) {
		Boolean isOk;
		binaryOp.setOuterTable(this);
		isOk = binaryOp.getFirstOperand().accept(this);
		isOk &= binaryOp.getSecondOperand().accept(this);
		
		// TODO: Add operation validation. (How to get expression type?)
		return isOk;
	}

	/**
	 * Handle math unary operations.
	 */
	@Override
	public Boolean visit(MathUnaryOp unaryOp) {
		Boolean isOk;
		unaryOp.setOuterTable(this);
		isOk = unaryOp.getOperand().accept(this);
		// TODO: Validate unary operation use.
		return isOk;
	}

	/**
	 * Handle logical unary operation.
	 */
	@Override
	public Boolean visit(LogicalUnaryOp unaryOp) {
		Boolean isOk;
		unaryOp.setOuterTable(this);
		isOk = unaryOp.getOperand().accept(this);
		// TODO: Validate unary operation use.
		return isOk;
	}

	/**
	 * Handle literal. 
	 */
	@Override
	public Boolean visit(Literal literal) {
		literal.setOuterTable(this);
		literal.getICType().accept(this);
		return true;
	}

	/**
	 * Handle expression block.
	 */
	@Override
	public Boolean visit(ExpressionBlock expressionBlock) {
		expressionBlock.setOuterTable(this);
		return expressionBlock.getExpression().accept(this);
	}
	@Override
	public Boolean visit(MethodType methodType) {
		methodType.setOuterTable(this);
		
		for (Type t : methodType.getFormalTypes()) {
			t.accept(this);
		}
		methodType.getReturnType().accept(this);
		
		return true;
	}
}
