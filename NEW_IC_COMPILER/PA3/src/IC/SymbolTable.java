package IC;

import IC.SymbolRecord.Kind;
import IC.AST.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	/** The parent table.
	 */
	private SymbolTable m_parent;

	/**
	 * The entries of the table.
	 */
	
	private Map<String, SymbolRecord> m_entries;
	
	/**
	 * Hold all the used type. 
	 */
	private Set<UserType> m_usedUserType;
	
	/**
	 * The current handled method.
	 */
	private Method m_currentMethod;
	
	/**
	 * @return The id.
	 */
	public int getId() {
		return m_id;
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
	
	/**
	 * @return The usedUserType.
	 */
	public Set<UserType> getUsedUserType() {
		return m_usedUserType;
	}
	/**
	 * @param usedUserType The usedUserType to set.
	 */
	public void setUsedUserType(Set<UserType> usedUserType) {
		m_usedUserType = usedUserType;
	}
	
	/**
	 * @return The currentMethod.
	 */
	public Method getCurrentMethod() {
		return m_currentMethod;
	}
	/**
	 * @param currentMethod The currentMethod to set.
	 */
	public void setCurrentMethod(Method currentMethod) {
		m_currentMethod = currentMethod;
	}
	
	/**
	 * Create the symbol table for the given record.
	 * @param id The id of the table.
	 * @param parent The parent of the scope.
	 */
	public SymbolTable(int id, SymbolTable parent) {
		m_recordID = 0;
		setCurrentMethod(null);
		setUsedUserType(new HashSet<UserType>());
		setId(id);
		setParent(parent);
		m_entries = new HashMap<String, SymbolRecord>();
	}
	
	/**
	 * Create the general scope symbol table.
	 * @param id The id of the table.
	 */
	public SymbolTable(int id) {
		this(id, null);
	}
	
	/**
	 * This function return the next id for this table.
	 * @return
	 */
	public static int getNextId() {
		return m_next_id++;
	}
	
	/**
	 * This function return the next record id for this table.
	 * @return
	 */
	public int getRecordId() {
		return m_recordID++;
	}

	/**
	 * Create a new Symbol Record for each class.
	 */
	
	@Override
	public Boolean visit(Program program) {
		Boolean isOk = true;
		setParent(null);
		for (ICClass icClass : program.getClasses()) {
			getEntries().put(icClass.getName(), new SymbolRecord(getRecordId(), this, 
					icClass.getName(), Kind.CLASS, 
					new UserType(icClass.getLine(), icClass.getName())));
			icClass.getInnerTable().setParent(this);
			isOk &= icClass.accept(icClass.getInnerTable());
		}
			
		return isOk;
	}

	/**
	 * Accept the methods and fields so they create their symbol 
	 *   table and register.
	 */
	@Override
	public Boolean visit(ICClass icClass) {
		// TODO: Check name redefinition + register to parent in TypeTable.

		Boolean isOk = true;
		for (Field field : icClass.getFields()) {
			isOk &= field.accept(this);
		}
		for (Method method : icClass.getMethods()) {
			isOk &= method.accept(this);
		}
		
		return isOk;
	}

	/**
	 * Add the field to the symbol table.
	 */
	@Override
	public Boolean visit(Field field) {
		getEntries().put(field.getName(), new SymbolRecord(getRecordId(), 
			this, field.getName(), Kind.FIELD, field.getType()));

		return true;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(VirtualMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getInnerTable().setCurrentMethod(method);
		method.getInnerTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}

		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(StaticMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getInnerTable().setCurrentMethod(method);
		method.getInnerTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}

		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(LibraryMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getInnerTable().setCurrentMethod(method);
		method.getInnerTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}
		
		return isOk;
	}

	/**
	 * Add the formal to the table.
	 */
	@Override
	public Boolean visit(Formal formal) {
		getEntries().put(formal.getName(), new SymbolRecord(getRecordId(), 
				this, formal.getName(), Kind.FORMAL, formal.getType()));
		return true;
	}

	/**
	 * Handle primitive type.
	 */
	@Override
	public Boolean visit(PrimitiveType type) {
		return true;
	}

	/**
	 * Handle user types.
	 */
	@Override
	public Boolean visit(UserType type) {
		getUsedUserType().add(type);
		return true;
	}

	/**
	 * Add assignment args to the table.
	 */
	@Override
	public Boolean visit(Assignment assignment) {
		Boolean isOk;
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
		if(returnStatement.hasValue())
		{
			return returnStatement.accept(this);
		}
		return true;
	}

	/**
	 * Handle if statement.
	 */
	@Override
	public Boolean visit(If ifStatement) {
		Boolean isOk;
		isOk = ifStatement.getCondition().accept(this);
		isOk &= ifStatement.getOperation().accept(this);
		isOk &= ifStatement.getElseOperation().accept(this);
		return isOk;
	}

	/**
	 * Handle while.
	 */
	@Override
	public Boolean visit(While whileStatement) {
		Boolean isOk;
		isOk = whileStatement.getCondition().accept(this);
		isOk &= whileStatement.getOperation().accept(this);
		
		return isOk;
	}

	/**
	 * Handle break statement.
	 */
	@Override
	public Boolean visit(Break breakStatement) {
		return true;
	}

	/**
	 * Handle continue.
	 */
	@Override
	public Boolean visit(Continue continueStatement) {
		return true;
	}

	/**
	 * Handle statement block.
	 */
	@Override
	public Boolean visit(StatementsBlock statementsBlock) {
		Boolean isOk = true;
		statementsBlock.getInnerTable().setParent(this);
		for (Statement s : statementsBlock.getStatements()) {
			s.accept(statementsBlock.getInnerTable());
		}
		
		return isOk;
	}

	/**
	 * Handle local variables.
	 */
	@Override
	public Boolean visit(LocalVariable localVariable) {
		Boolean isOk = true;
		getEntries().put(localVariable.getName(), new SymbolRecord(
				getRecordId(), this, localVariable.getName(), 
				Kind.LOCAL_VARIABLE, localVariable.getType()));
		if(localVariable.hasInitValue()) {
			isOk = localVariable.getInitValue().accept(this);
		}
		return isOk;
	}

	/**
	 * Handle variable location.
	 */
	@Override
	public Boolean visit(VariableLocation location) {
		return location.getLocation().accept(this);
	}

	/**
	 * Handle array location.
	 */
	@Override
	public Boolean visit(ArrayLocation location) {
		Boolean isOk;
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
		return true;
	}

	/**
	 * Handle new class key word.
	 */
	@Override
	public Boolean visit(NewClass newClass) {
		return true;
	}

	/**
	 * Handle new array key word.
	 */
	@Override
	public Boolean visit(NewArray newArray) {
		return newArray.getType().accept(this);
	}

	/**
	 * The length key word.
	 */
	@Override
	public Boolean visit(Length length) {
		return length.getArray().accept(this);
	}

	/**
	 * Handle Math binary operation.
	 */
	@Override
	public Boolean visit(MathBinaryOp binaryOp) {
		Boolean isOk;
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
		isOk = unaryOp.getOperand().accept(this);
		// TODO: Validate unary operation use.
		return isOk;
	}

	/**
	 * Handle literal. 
	 */
	@Override
	public Boolean visit(Literal literal) {
		return true;
	}

	/**
	 * Handle expression block.
	 */
	@Override
	public Boolean visit(ExpressionBlock expressionBlock) {
		return expressionBlock.getExpression().accept(this);
	}
}
