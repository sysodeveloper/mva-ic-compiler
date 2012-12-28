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
public class SemanticChecker implements Visitor<Boolean> {
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
	public void setUsedUserType(Set<UserType> usedUserType) {
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
	public SemanticChecker(int id, SymbolTable parent) {
		setBreakFlag(false);
		setNonStatic(false);
		setBreaks(new ArrayList<Break>());
		setContinues(new ArrayList<Continue>());
		setNonStaticNodes(new ArrayList<ASTNode>());
		setCurrentMethod(null);
		setUsedUserType(new HashSet<UserType>());
	}
	
	/**
	 * Create the general scope symbol table.
	 * @param id The id of the table.
	 */
	public SemanticChecker(int id) {
		this(id, null);
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
					new SemanticError("break outside loop.", b.getLine()));
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
					new SemanticError("continue outside loop.", c.getLine()));				
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
						new SemanticError(
				 "non-static variable this cannot be referenced from a static context", node.getLine()));
				} else if(node instanceof Field) {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError("non-static variable " + ((Field)node).getName() + 
									" cannot be referenced from a static context", node.getLine()));
				} else if(node instanceof Method) {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError("non-static method " + ((Method)node).getName() + 
									" cannot be referenced from a static context", node.getLine()));
				}
				else {
					SemanticAnalyse.getInstance().getErrors().add(
							new SemanticError(
					"non-static node cannot be referenced from a static context", node.getLine()));
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
	public Boolean visit(Program program) {
		Boolean isOk = true;
		for (ICClass icClass : program.getClasses()) {
			isOk &= icClass.accept(icClass.getInnerTable());
		}
			
		return isOk;
	}

	/**
	 * Accept the methods and fields so they create their symbol 
	 *   table and register.getTable
	 */
	@Override
	public Boolean visit(ICClass icClass) {
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
		return true;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(VirtualMethod method) {
		Boolean isOk = true;
		
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}
		isOk &= checkBreak();
		isOk &= checkContinue();
		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(StaticMethod method) {
		Boolean isOk = true;
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
			if(s instanceof StatementsBlock) {
				StatementsBlock sb = (StatementsBlock)s;
				setBreakFlag(getBreakFlag() || getBreakFlag());
				setContinueFlag(getContinueFlag() || getContinueFlag());
				getBreaks().addAll(getBreaks());
				getContinues().addAll(getContinues());
				setNonStatic(getNonStatic() || getNonStatic());
			}
		}
		isOk &= checkBreak();
		isOk &= checkContinue();
		isOk &= checkStatic();
		return isOk;
	}

	/**
	 * Add a method to the symbol table and recursively 
	 * call on it's statements.
	 */
	@Override
	public Boolean visit(LibraryMethod method) {
		Boolean isOk = true;
		
		method.getInnerTable().setCurrentMethod(method);
		for (Formal e : method.getFormals()) {
			e.accept(method.getInnerTable());
		}
		for (Statement s : method.getStatements()) {
			isOk &= s.accept(method.getInnerTable());
		}
		isOk &= checkBreak();
		isOk &= checkContinue();
		return isOk;
	}

	/**
	 * Add the formal to the table.
	 */
	@Override
	public Boolean visit(Formal formal) {
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
	public Boolean visit(Break breakStatement) {
		getBreaks().add(breakStatement);
		setBreakFlag(true);
		return true;
	}

	/**
	 * Handle continue.
	 */
	@Override
	public Boolean visit(Continue continueStatement) {
		getContinues().add(continueStatement);
		setContinueFlag(true);
		return true;
	}

	/**
	 * Handle statement block.
	 */
	@Override
	public Boolean visit(StatementsBlock statementsBlock) {
		Boolean isOk;
		
		for (Statement s : statementsBlock.getStatements()) {
			s.accept(statementsBlock.getInnerTable());
		}
		isOk = checkBreak();
		isOk &= checkContinue();
		return isOk;
	}

	/**
	 * Handle local variables.
	 */
	@Override
	public Boolean visit(LocalVariable localVariable) {
		Boolean isOk = true;
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
	public Boolean visit(This thisExpression) {
		setNonStatic(true);
		getNonStaticNodes().add(thisExpression);
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
