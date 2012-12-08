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
public class SymbolTable implements Visitor {
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
	 * Indicate if the symbol table contains a break not 
	 * yet under a while.
	 */
	private Boolean m_breakFlag;
	
	/**
	 * Indicate if the symbol table contains a continue not 
	 * yet under a while.
	 */
	private Boolean m_continueFlag;
	
	/**
	 * The breaks triggered break flag.
	 */
	private List<Break> m_breaks;
	
	/**
	 * The continues triggered break flag.
	 */
	private List<Continue> m_continues;

	/** The parent tableS.
	 */
	private SymbolTable m_parent;

	/**
	 * The entries of the table.
	 */
	private Map<String, SymbolRecord> m_entries;
	
	/**
	 * Hold all the used 
	 */
	private Set<UserType> m_usedUserType;
	
	/**
	 * indicate if the block of the current symbol table contain 
	 *   non static statements.
	 */
	private Boolean m_nonStatic;
	
	/**
	 * The non static nodes triggered the flag.
	 */
	private List<ASTNode> m_nonStaticNodes;
	
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
	 * @return The breakFlag.
	 */
	public Boolean getBreakFlag() {
		return m_breakFlag;
	}
	
	/**
	 * @param breakFlag The breakFlag to set.
	 */
	public void setBreakFlag(Boolean breakFlag) {
		m_breakFlag = breakFlag;
	}
	
	/**
	 * @return The breaks.
	 */
	public List<Break> getBreaks() {
		return m_breaks;
	}
	/**
	 * @param breaks The breaks to set.
	 */
	public void setBreaks(List<Break> breaks) {
		m_breaks = breaks;
	}
	
	/**
	 * @return The continueFlag.
	 */
	public Boolean getContinueFlag() {
		return m_continueFlag;
	}
	
	/**
	 * @param breakFlag The breakFlag to set.
	 */
	public void setContinueFlag(Boolean continueFlag) {
		m_continueFlag = continueFlag;
	}
	
	/**
	 * @return The continues.
	 */
	public List<Continue> getContinues() {
		return m_continues;
	}
	/**
	 * @param break The continue to set.
	 */
	public void setContinues(List<Continue> continues) {
		m_continues = continues;
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
	public void setusedUserType(Set<UserType> usedUserType) {
		m_usedUserType = usedUserType;
	}
	
	/**
	 * @return The nonStatic.
	 */
	public Boolean getNonStatic() {
		return m_nonStatic;
	}
	/**
	 * @param nonStatic The nonStatic to set.
	 */
	public void setNonStatic(Boolean nonStatic) {
		m_nonStatic = nonStatic;
	}
	
	/**
	 * @return The nonStaticNodes.
	 */
	public List<ASTNode> getNonStaticNodes() {
		return m_nonStaticNodes;
	}
	/**
	 * @param nonStaticNodes The nonStaticNodes to set.
	 */
	public void setNonStaticNodes(List<ASTNode> nonStaticNodes) {
		m_nonStaticNodes = nonStaticNodes;
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
		setBreakFlag(false);
		setNonStatic(false);
		setBreaks(new ArrayList<Break>());
		setContinues(new ArrayList<Continue>());
		setNonStaticNodes(new ArrayList<ASTNode>());
		setCurrentMethod(null);
		setusedUserType(new HashSet<UserType>());
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
	 * Check if there is a break not under a while.
	 * @return true if the check is ok else false.
	 */
	private Boolean checkBreak() {
		Boolean isOk = true;
		if(getBreakFlag()) {
			isOk = false;
			for (Break b : getBreaks()) {
				SemanticAnalyse.getInstance().getErrors().add(
					new SemanticError("Line: " + b.getLine() + 
							" break outside loop."));
			}
		}
		// Handle more than one break.
		getBreaks().clear();
		setBreakFlag(false);
		
		return isOk;
	}
	
	/**
	 * Check if there is a continue not under a while.
	 * @return true if the check is ok else false.
	 */
	private Boolean checkContinue() {
		Boolean isOk = true;
		if(getContinueFlag()) {
			isOk = false;
			for (Continue c : getContinues()) {
				SemanticAnalyse.getInstance().getErrors().add(
					new SemanticError("Line: " + c.getLine() + 
							" continue outside loop."));				
			}
		}
		// Handle more than one break.
		getContinues().clear();
		setContinueFlag(false);
		
		return isOk;
	}
	
	/**
	 * Check if the current method pass the static check.
	 * @return
	 */
	private Boolean checkStatic() {
		if(getNonStatic()) {
			for (ASTNode node : getNonStaticNodes()) {
				if(node instanceof This) {
				SemanticAnalyse.getInstance().getErrors().add(
						new SemanticError("Line: " + node.getLine() + 
								" non-static variable this cannot be referenced from a static context"));
				} else if(node instanceof Field) {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError("Line: " + node.getLine() + 
									" non-static variable " + ((Field)node).getName() + 
									" cannot be referenced from a static context"));
				} else if(node instanceof Method) {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError("Line: " + node.getLine() + 
									" non-static method " + ((Method)node).getName() + 
									" cannot be referenced from a static context"));
				}
				else {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError("Line: " + node.getLine() + 
									" non-static node cannot be referenced from a static context"));
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Create a new Symbol Record for each class.
	 */
	@Override
	public Object visit(Program program) {
		Boolean isOk = true;
		setParent(null);
		for (ICClass icClass : program.getClasses()) {
			getEntries().put(icClass.getName(), new SymbolRecord(getRecordId(), this, 
					icClass.getName(), Kind.CLASS, 
					new UserType(icClass.getLine(), icClass.getName())));
			icClass.getTable().setParent(this);
			isOk &= (Boolean)icClass.accept(icClass.getTable());
		}
			
		return isOk;
	}

	/**
	 * Accept the methods and fields so they create their symbol 
	 *   table and register.
	 */
	@Override
	public Object visit(ICClass icClass) {
		Boolean isOk = true;
		for (Field field : icClass.getFields()) {
			isOk &= (Boolean)field.accept(this);
		}
		for (Method method : icClass.getMethods()) {
			isOk &= (Boolean)method.accept(this);
		}
		
		return isOk;
	}

	/**
	 * Add the field to the symbol table.
	 */
	@Override
	public Object visit(Field field) {
		getEntries().put(field.getName(), new SymbolRecord(getRecordId(), 
			this, field.getName(), Kind.FIELD, field.getType()));

		return true;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Object visit(VirtualMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getTable().setCurrentMethod(method);
		method.getTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= (Boolean)s.accept(method.getTable());
		}
		isOk &= method.getTable().checkBreak();
		isOk &= method.getTable().checkContinue();
		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Object visit(StaticMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getTable().setCurrentMethod(method);
		method.getTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= (Boolean)s.accept(method.getTable());
			if(s instanceof StatementsBlock) {
				StatementsBlock sb = (StatementsBlock)s;
				setBreakFlag(sb.getTable().getBreakFlag() || getBreakFlag());
				setContinueFlag(sb.getTable().getContinueFlag() || getContinueFlag());
				getBreaks().addAll(sb.getTable().getBreaks());
				getContinues().addAll(sb.getTable().getContinues());
				setNonStatic(sb.getTable().getNonStatic() || getNonStatic());
			}
		}
		isOk &= method.getTable().checkBreak();
		isOk &= method.getTable().checkContinue();
		isOk &= method.getTable().checkStatic();
		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Object visit(LibraryMethod method) {
		Boolean isOk = true;
		getEntries().put(method.getName(), new SymbolRecord(getRecordId(),
				this, method.getName(), Kind.VIRTUAL_METHOD, method.getType(), null,
				method.getFormals()));
		method.getTable().setCurrentMethod(method);
		method.getTable().setParent(this);
		for (Formal e : method.getFormals()) {
			e.accept(method.getTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= (Boolean)s.accept(method.getTable());
		}
		isOk &= method.getTable().checkBreak();
		isOk &= method.getTable().checkContinue();
		return isOk;
	}

	/**
	 * Add the formal to the table.
	 */
	@Override
	public Object visit(Formal formal) {
		getEntries().put(formal.getName(), new SymbolRecord(getRecordId(), 
				this, formal.getName(), Kind.FORMAL, formal.getType()));
		return true;
	}

	/**
	 * Handle primitive type.
	 */
	@Override
	public Object visit(PrimitiveType type) {
		return true;
	}

	/**
	 * Handle user types.
	 */
	@Override
	public Object visit(UserType type) {
		getUsedUserType().add(type);
		return true;
	}

	/**
	 * Add assignment args to the table.
	 */
	@Override
	public Object visit(Assignment assignment) {
		Boolean isOk;
		isOk = (Boolean)assignment.getAssignment().accept(this);
		isOk &= (Boolean)assignment.getVariable().accept(this);
		return isOk;
	}

	/**
	 * Handle call statments.
	 */
	@Override
	public Object visit(CallStatement callStatement) {
		Boolean isOk = true;
		for (Expression e : callStatement.getCall().getArguments()) {
			isOk &= (Boolean)e.accept(this);
		}
		return isOk;
	}

	/**
	 * Handle the return statement.
	 */
	@Override
	public Object visit(Return returnStatement) {
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
	public Object visit(If ifStatement) {
		Boolean isOk;
		isOk = (Boolean)ifStatement.getCondition().accept(this);
		isOk &= (Boolean)ifStatement.getOperation().accept(this);
		isOk &= (Boolean)ifStatement.getElseOperation().accept(this);
		return isOk;
	}

	/**
	 * Handle while.
	 */
	@Override
	public Object visit(While whileStatement) {
		Boolean isOk;
		isOk = (Boolean)whileStatement.getCondition().accept(this);
		isOk &= (Boolean)whileStatement.getOperation().accept(this);
		setBreakFlag(false);
		setContinueFlag(false);
		getBreaks().clear();
		getContinues().clear();
		return isOk;
	}

	/**
	 * Handle break statement.
	 */
	@Override
	public Object visit(Break breakStatement) {
		getBreaks().add(breakStatement);
		setBreakFlag(true);
		return true;
	}

	/**
	 * Handle continue.
	 */
	@Override
	public Object visit(Continue continueStatement) {
		getContinues().add(continueStatement);
		setContinueFlag(true);
		return true;
	}

	/**
	 * Handle statement block.
	 */
	@Override
	public Object visit(StatementsBlock statementsBlock) {
		Boolean isOk;
		statementsBlock.getTable().setParent(this);
		for (Statement s : statementsBlock.getStatements()) {
			s.accept(statementsBlock.getTable());
		}
		isOk = checkBreak();
		isOk &= checkContinue();
		return isOk;
	}

	/**
	 * Handle local variables.
	 */
	@Override
	public Object visit(LocalVariable localVariable) {
		Boolean isOk = true;
		getEntries().put(localVariable.getName(), new SymbolRecord(
				getRecordId(), this, localVariable.getName(), 
				Kind.LOCAL_VARIABLE, localVariable.getType()));
		if(localVariable.hasInitValue()) {
			isOk = (Boolean)localVariable.getInitValue().accept(this);
		}
		return isOk;
	}

	/**
	 * Handle variable location.
	 */
	@Override
	public Object visit(VariableLocation location) {
		return location.getLocation().accept(this);
	}

	/**
	 * Handle array location.
	 */
	@Override
	public Object visit(ArrayLocation location) {
		Boolean isOk;
		isOk = (Boolean)location.getArray().accept(this);
		isOk &= (Boolean)location.getIndex().accept(this);
		return isOk;
	}

	/**
	 * Handle static call.
	 */
	@Override
	public Object visit(StaticCall call) {
		Boolean isOk = true;
		for (Expression e : call.getArguments()) {
			isOk &= (Boolean)e.accept(this);
		}
		return isOk;
	}

	/**
	 * Handle virtual method call.
	 */
	@Override
	public Object visit(VirtualCall call) {
		Boolean isOk = true;
		
		for (Expression e : call.getArguments()) {
			isOk &= (Boolean)e.accept(this);
		}
		
		if(!call.isExternal()) {
			setNonStatic(true);
			getNonStaticNodes().add(call);
		}
		else {
			call.getLocation().accept(this);
		}
		
		return isOk;
	}

	/**
	 * Handle this key word.
	 */
	@Override
	public Object visit(This thisExpression) {
		setNonStatic(true);
		getNonStaticNodes().add(thisExpression);
		return null;
	}

	/**
	 * Handle new class key word.
	 */
	@Override
	public Object visit(NewClass newClass) {
		return true;
	}

	/**
	 * Handle new array key word.
	 */
	@Override
	public Object visit(NewArray newArray) {
		return newArray.getType().accept(this);
	}

	/**
	 * The length key word.
	 */
	@Override
	public Object visit(Length length) {
		return length.getArray().accept(this);
	}

	/**
	 * Handle Math binary operation.
	 */
	@Override
	public Object visit(MathBinaryOp binaryOp) {
		Boolean isOk;
		isOk = (Boolean)binaryOp.getFirstOperand().accept(this);
		isOk &= (Boolean)binaryOp.getSecondOperand().accept(this);
		
		// TODO: Add operation validation. (How to get expression type?)
		return isOk;
	}

	/**
	 * Handle logical operation.
	 */
	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		Boolean isOk;
		isOk = (Boolean)binaryOp.getFirstOperand().accept(this);
		isOk &= (Boolean)binaryOp.getSecondOperand().accept(this);
		
		// TODO: Add operation validation. (How to get expression type?)
		return isOk;
	}

	/**
	 * Handle math unary operations.
	 */
	@Override
	public Object visit(MathUnaryOp unaryOp) {
		Boolean isOk;
		isOk = (Boolean)unaryOp.getOperand().accept(this);
		// TODO: Validate unary operation use.
		return isOk;
	}

	/**
	 * Handle logical unary operation.
	 */
	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		Boolean isOk;
		isOk = (Boolean)unaryOp.getOperand().accept(this);
		// TODO: Validate unary operation use.
		return isOk;
	}

	/**
	 * Handle literal. 
	 */
	@Override
	public Object visit(Literal literal) {
		return true;
	}

	/**
	 * Handle expression block.
	 */
	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		return expressionBlock.getExpression().accept(this);
	}
}
