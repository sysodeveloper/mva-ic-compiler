package IC.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Build dot language representation of the parsed AST.
 *
 */
public class Graphviz implements Visitor<Integer>{
	/**
	 * Local copy of the root.
	 */
	private Program root;
	
	/**
	 * The name of the graph.
	 */
	private String name;	
	
	/**
	 * the nodes of the graph.
	 */
	private StringBuilder nodes;
	
	/**
	 * The edges.
	 */
	private StringBuilder edges;
	
	/**
	 * The map between id to label.
	 */
	private Map<Integer, String> labels;
	
	/**
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Create the Graphviz.
	 * @param name The name of the AST program file.
	 */
	public Graphviz(String name){
		setName(name);
		edges    = new StringBuilder();
		nodes    = new StringBuilder();
		labels   = new HashMap<Integer, String>();
	}
	
	/**
	 * @param program The root of the AST.
	 * @return Get the graph.
	 */
	public String getGraph(Program root) {
		this.root = root;
		root.accept(this);
		StringBuffer output = new StringBuffer();
		output.append("digraph \"" + getName() + "\"{\n");
		output.append(nodes);
		output.append(edges);
		output.append("}\n");
		return output.toString();
	}
	
	/**
	 * Build an edge.
	 *  @param id       The id of the node.
	 *  @param children The id's of the sub nodes.
	 * @return The edge string.
	 */
	private void addEdge(int id, List<Integer> children) {
		edges.append(id + " -> {" + join(children, ", ") + "}\n");
	}
	
	/**
	 * Build a node
	 * @param name     The name of the node.
	 * @return The node string.
	 */
	private void addNode(int id, String name) {
		labels.put(id, name);
		nodes.append(id + " " + "[label=\"" + name + "\"]\n");
	}
	
	/**
	 * Build a string from a list with separator between the strings.
	 * @param list The list to join.
	 * @param conjunction The separator.
	 * @return
	 */
	private String join(List<Integer> list, String separator)
	{
	   StringBuilder sb = new StringBuilder();
	   boolean first = true;
	   for(Integer item : list)
	   {
	      if (first) {
	         first = false;
	      }
	      else {
	         sb.append(separator);
	      }
	      sb.append(item);
	   }
	   return sb.toString();
	}
	
	/**
	 * Handle ICClass.
	 */
	@Override
	public Integer visit(ICClass icClass){
		List<Integer> children = new ArrayList<Integer>();		
		addNode(icClass.getID(), "ICClass '" + icClass.getName() + "'");
		
		for (Field f : icClass.getFields()) {
			children.add(f.accept(this));
		}
		
		for(Method m : icClass.getMethods()){
			children.add(m.accept(this));
		}
		addEdge(icClass.getID(), children);
		return icClass.getID();
	}

	/**
	 * Handle LibraryMethod.
	 */
	@Override
	public Integer visit(LibraryMethod method){
		List<Integer> children = new ArrayList<Integer>();
		addNode(method.getID(), "LibraryMethod '" + method.getName() + "'");
		children.add(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			children.add(f.accept(this));
		}
		for (Statement s : method.getStatements()) {
			children.add(s.accept(this));
		}
		addEdge(method.getID(), children);
		return method.getID();
	}

	/**
	 * Handle formal.
	 */
	@Override
	public Integer visit(Formal formal){
		List<Integer> children = new ArrayList<Integer>();
		addNode(formal.getID(), formal.getName());
		children.add(formal.getType().accept(this));
		addEdge(formal.getID(), children);
		return formal.getID();
	}

	/**
	 * Handle Primitive type.
	 */
	@Override
	public Integer visit(PrimitiveType type){
		StringBuffer output = new StringBuffer();
		output.append(type.getName());
		for(int i = 1; i <= type.getDimension(); ++i){
			output.append("[]");
		}
		addNode(type.getID(), output.toString());
		return type.getID();
	}

	/**
	 * Handle user type.
	 */
	@Override
	public Integer visit(UserType type){
		StringBuffer output = new StringBuffer();
		output.append(type.getName());
		for(int i = 1; i <= type.getDimension(); ++i){
			output.append("[]");
		}
		addNode(type.getID(), output.toString());
		return type.getID();
	}

	/**
	 * Handle assignment.
	 */
	@Override
	public Integer visit(Assignment assignment){
		List<Integer> children = new ArrayList<Integer>();
		addNode(assignment.getID(), "Assignment");
		children.add(assignment.getVariable().accept(this));
		children.add(assignment.getAssignment().accept(this));
		addEdge(assignment.getID() , children);
		return assignment.getID();
	}

	/**
	 * Handle literal.
	 */
	@Override
	public Integer visit(Literal literal){
		addNode(literal.getID(), "Literal " + 
				(literal.getType().toFormattedString(literal.getValue())).replace('\"', '\''));
		return literal.getID();
	}

	/**
	 * Handle the program.
	 */
	@Override
	public Integer visit(Program program) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(program.getID(), "Program '" + getName() + "'");
		for (ICClass c : program.getClasses()) {
			children.add(c.getID());
			c.accept(this);
		}
		addEdge(program.getID(), children);
		return program.getID();
	}
	
	/**
	 * Handle Field.
	 */
	@Override
	public Integer visit(Field field) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(field.getID(), "Field '" + 
		field.getName() + "'");
		children.add(field.getType().accept(this));
		addEdge(field.getID(), children);
		return field.getID();
	}
	
	/**
	 * Handle Virtual Method.
	 */
	@Override
	public Integer visit(VirtualMethod method) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(method.getID(), "VirtualMethod '" + method.getName() + "'");
		children.add(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			children.add(f.accept(this));
		}
		for (Statement s : method.getStatements()) {
			children.add(s.accept(this));
		}
		addEdge(method.getID(), children);
		return method.getID();
	}
	
	/**
	 * Handle Static Method
	 */
	@Override
	public Integer visit(StaticMethod method) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(method.getID(), "StaticMethod '" + method.getName() + "'");
		children.add(method.getType().accept(this));
		for(Formal f : method.getFormals()){
			children.add(f.accept(this));
		}
		for (Statement s : method.getStatements()) {
			children.add(s.accept(this));
		}
		addEdge(method.getID(), children);
		return method.getID();
	}
	
	/**
	 * Handle method call statement.
	 */
	@Override
	public Integer visit(CallStatement callStatement) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(callStatement.getID(), "CallStatement");
		children.add(callStatement.getCall().accept(this));
		addEdge(callStatement.getID(), children);
		return callStatement.getID();
	}
	
	@Override
	public Integer visit(Return returnStatement) {
		addNode(returnStatement.getID(), "Return "); 
		if(returnStatement.hasValue()) {
			List<Integer> children = new ArrayList<Integer>();
			children.add(returnStatement.getValue().accept(this));
			addEdge(returnStatement.getID(), children);
		}
		
		return returnStatement.getID();
	}
	
	/**
	 * Handle if statement.
	 */
	@Override
	public Integer visit(If ifStatement) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(ifStatement.getID(), "If");
		children.add(ifStatement.getCondition().accept(this));
		children.add(ifStatement.getOperation().accept(this));
		if(ifStatement.hasElse()) {
			children.add(ifStatement.getElseOperation().accept(this));
		}
		addEdge(ifStatement.getID(), children);
		return ifStatement.getID();
	}
	
	/**
	 * Handle while.
	 */
	@Override
	public Integer visit(While whileStatement) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(whileStatement.getID(), "While");
		children.add(whileStatement.getCondition().accept(this));
		children.add(whileStatement.getOperation().accept(this));
		addEdge(whileStatement.getID(), children);
		return whileStatement.getID();
	}
	
	/**
	 * Handle break.
	 */
	@Override
	public Integer visit(Break breakStatement) {
		addNode(breakStatement.getID(), "Break");
		return breakStatement.getID();
	}
	
	/**
	 * Handle continue.
	 */
	@Override
	public Integer visit(Continue continueStatement) {
		addNode(continueStatement.getID(), "Continue");
		return continueStatement.getID();
	}
	
	/**
	 * Handle statement block.
	 */
	@Override
	public Integer visit(StatementsBlock statementsBlock) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(statementsBlock.getID(), "StatementsBlock");
		for (Statement s : statementsBlock.getStatements()) {
			children.add(s.accept(this));
		}
		addEdge(statementsBlock.getID(), children);
		return statementsBlock.getID();
	}
	
	/**
	 * Handle local variables.
	 */
	@Override
	public Integer visit(LocalVariable localVariable) {
		addNode(localVariable.getID(), "LocalVariable '" + localVariable.getName() + "'");
		if(localVariable.hasInitValue()) {
			List<Integer> children = new ArrayList<Integer>();
			children.add(localVariable.getInitValue().accept(this));
			addEdge(localVariable.getID(), children);
		}
		return localVariable.getID();
	}
	
	/**
	 * Handle variable location.
	 */
	@Override
	public Integer visit(VariableLocation location) {
		addNode(location.getID(), "VariableLocation '" + location.getName() + "'");
		if(location.isExternal()) {
			List<Integer> children = new ArrayList<Integer>();
			children.add(location.getLocation().accept(this));
			addEdge(location.getID(), children);
		}
		
		return location.getID();
	}
	
	/**
	 * Handle array location.
	 */
	@Override
	public Integer visit(ArrayLocation location) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(location.getID(), "ArrayLocation ");
		children.add(location.getIndex().accept(this));
		children.add(location.getArray().accept(this));
		addEdge(location.getID(), children);
		return location.getID();
	}
	
	/**
	 * Handle static call.
	 */
	@Override
	public Integer visit(StaticCall call) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(call.getID(), "StaticCall '" + 
	  call.getClassName() + "." + call.getName() + "'");
		for (Expression e : call.getArguments()) {
			children.add(e.accept(this));
		}
		addEdge(call.getID(), children);
		return call.getID();
	}
	
	/**
	 * Handle virtual call.
	 */
	@Override
	public Integer visit(VirtualCall call) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(call.getID(), "VirtualCall '" + 
		  call.getName() + "'");
		for (Expression e : call.getArguments()) {
			children.add(e.accept(this));
		}
		addEdge(call.getID(), children);
		return call.getID();
	}
	
	/**
	 * Handle this.
	 */
	@Override
	public Integer visit(This thisExpression) {
		addNode(thisExpression.getID(), "This");
		return thisExpression.getID();
	}
	
	/**
	 * Handle new class.
	 */
	@Override
	public Integer visit(NewClass newClass) {
		addNode(newClass.getID(), "NewClass '" + newClass.getName() + "'");
		return newClass.getID();
	}
	
	/**
	 * Handle new array.
	 */
	@Override
	public Integer visit(NewArray newArray) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(newArray.getID(), "NewArray ");
		children.add(newArray.getSize().accept(this));
		children.add(newArray.getType().accept(this));
		addEdge(newArray.getID(), children);
		return newArray.getID();
	}
	
	/**
	 * Handle length.
	 */
	@Override
	public Integer visit(Length length) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(length.getID(), "Length");
		children.add(length.getArray().accept(this));
		addEdge(length.getID(), children);
		return length.getID();
	}
	
	/**
	 * Handle math binary operation.
	 */
	@Override
	public Integer visit(MathBinaryOp binaryOp) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(binaryOp.getID(), "MathBinaryOp '" + binaryOp.getOperator().getOperatorString() + "'");
		children.add(binaryOp.getFirstOperand().accept(this));
		children.add(binaryOp.getSecondOperand().accept(this));
		addEdge(binaryOp.getID(), children);
		return binaryOp.getID();
	}
	
	/**
	 * Handle logical binary operation.
	 */
	@Override
	public Integer visit(LogicalBinaryOp binaryOp) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(binaryOp.getID(), "LogicalBinaryOp '" + binaryOp.getOperator().getOperatorString() + "'");
		children.add(binaryOp.getFirstOperand().accept(this));
		children.add(binaryOp.getSecondOperand().accept(this));
		addEdge(binaryOp.getID(), children);
		return binaryOp.getID();
	}
	
	/**
	 * Handle math unary operation.
	 */
	@Override
	public Integer visit(MathUnaryOp unaryOp) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(unaryOp.getID(), "MathUnaryOp '" + unaryOp.getOperator().getOperatorString() + "'");
		children.add(unaryOp.getOperand().accept(this));
		addEdge(unaryOp.getID(), children);
		return unaryOp.getID();
	}
	
	/**
	 * Handle logical unary operation.
	 */
	@Override
	public Integer visit(LogicalUnaryOp unaryOp) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(unaryOp.getID(), "LogicalUnaryOp '" + unaryOp.getOperator().getOperatorString() + "'");
		children.add(unaryOp.getOperand().accept(this));
		addEdge(unaryOp.getID(), children);
		return unaryOp.getID();
	}
	
	/**
	 * Handle expression block.
	 */
	@Override
	public Integer visit(ExpressionBlock expressionBlock) {
		List<Integer> children = new ArrayList<Integer>();
		addNode(expressionBlock.getID(), "ExpressionBlock");
		children.add(expressionBlock.getExpression().accept(this));
		addEdge(expressionBlock.getID(), children);
		return expressionBlock.getID();
	}

	@Override
	public Integer visit(MethodType methodType) {
		StringBuffer output = new StringBuffer();
		output.append(methodType.getName());
		for(int i = 1; i <= methodType.getDimension(); ++i){
			output.append("[]");
		}
		addNode(methodType.getID(), output.toString());
		return methodType.getID();
	}
}