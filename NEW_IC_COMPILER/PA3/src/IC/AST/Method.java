package IC.AST;

import java.util.ArrayList;
import java.util.List;
import IC.*;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;

/**
 * Abstract base class for method AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Method extends ASTNode implements INameable {

	protected Type type;
	
	protected MethodType methodType;

	protected String name;

	protected List<Formal> formals;

	protected List<Statement> statements;
	

	/**
	 * Constructs a new method node. Used by subclasses.
	 * 
	 * @param type
	 *            Data type returned by method.
	 * @param name
	 *            Name of method.
	 * @param formals
	 *            List of method parameters.
	 * @param statements
	 *            List of method's statements.
	 */
	protected Method(Type type, String name, List<Formal> formals,
			List<Statement> statements) {
		super(type.getLine());
		this.type = type;
		this.name = name;
		this.formals = formals;
		this.statements = statements;
		List<Type> formalTypes = new ArrayList<Type>();
		for (Formal f : formals) {
			formalTypes.add(f.getType());
		}
		methodType = new MethodType(line, type, formalTypes);
	}

	public Type getType() {
		
		return methodType;
	}
	
	public Type getReturnType(){
		return this.type;
	}
	public String getName() {
		return name;
	}

	public List<Formal> getFormals() {
		return formals;
	}

	public List<Statement> getStatements() {
		return statements;
	}
	
	public MyType getMyType(){
		MyMethodType method = new MyMethodType();
		method.setName(this.getName());
		ArrayList<MyType> formalTypes = new ArrayList<MyType>();
		MyType returnType = this.getReturnType().getMyType();
		method.setReturnType(returnType);
		for(Formal f : this.getFormals()){
			formalTypes.add(f.getType().getMyType());
		}
		method.setParamTypes(formalTypes);
		method.setFullName();
		return method;
	}
}