package IC.AST;

import java.util.List;

import IC.MyArrayType;
import IC.MyClassType;
import IC.MyType;
import IC.SymbolTable;

/**
 * Class declaration AST node.
 * 
 * @author Tovi Almozlino
 */
public class ICClass extends ASTNode implements SymbolTableContainer, INameable {

	private String name;

	private String superClassName = null;
	
	private UserType m_userType;

	private List<Field> fields;

	private List<Method> methods;
	
	/** The symbol table of this class.
	 */
	private SymbolTable m_InnerTable;
	
	/**
	 * The static table of that class;
	 */
	private SymbolTable m_staticTable;

	/**
	 * @return The table.
	 */
	public SymbolTable getInnerTable() {
		return m_InnerTable;
	}

	/**
	 * @param table The table to set.
	 */
	public void setInnerTable(SymbolTable table) {
		m_InnerTable = table;
	}

	@Override
	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new class node.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, List<Field> fields,
			List<Method> methods) {
		super(line);
		this.name = name;
		this.fields = fields;
		this.methods = methods;
		setInnerTable(new SymbolTable(SymbolTable.getNextId(), null, this));
		setStaticTable(new SymbolTable(SymbolTable.getNextId(), null, this));
		setUserType(new UserType(this));
	}

	/**
	 * Constructs a new class node, with a superclass.
	 * 
	 * @param line
	 *            Line number of class declaration.
	 * @param name
	 *            Class identifier name.
	 * @param superClassName
	 *            Superclass identifier name.
	 * @param fields
	 *            List of all fields in the class.
	 * @param methods
	 *            List of all methods in the class.
	 */
	public ICClass(int line, String name, String superClassName,
			List<Field> fields, List<Method> methods) {
		this(line, name, fields, methods);
		this.superClassName = superClassName;
	}

	public String getName() {
		return name;
	}

	public boolean hasSuperClass() {
		return (superClassName != null);
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<Method> getMethods() {
		return methods;
	}
	
	/**
	 * @return the m_userType
	 */
	public UserType getUserType() {
		return m_userType;
	}

	/**
	 * @param m_userType the m_userType to set
	 */
	public void setUserType(UserType m_userType) {
		this.m_userType = m_userType;
	}
	
	/**
	 * @return the m_staticTable
	 */
	public SymbolTable getStaticTable() {
		return m_staticTable;
	}

	/**
	 * @param m_staticTable the m_staticTable to set
	 */
	public void setStaticTable(SymbolTable m_staticTable) {
		this.m_staticTable = m_staticTable;
	}
	
	public MyType getMyType(){
		MyClassType returnType = new MyClassType();
		returnType.setName(this.getName());
		returnType.setClassAST(this);
		if(this.hasSuperClass()){
			MyClassType superClass = new MyClassType();
			superClass.setName(getSuperClassName());
			returnType.setSuperClass(superClass);
		}
		if(this.getUserType().getDimension() ==0)
			return returnType;
		MyArrayType array = new MyArrayType();
		array.setDimention(this.getUserType().getDimension());
		returnType.setDimention(this.getUserType().getDimension());
		array.setElementType(returnType);
		array.setName(this.getName());
		array.setFullName();
		return array;
	}
	
	/**
	 * The hash code for the type.
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    char[] arrChar = getName().toCharArray();
	    for (char c : arrChar) {
	    	result = prime * result + c;
		}
	    
	    return result;
	}
	
	/**
	 * check if the types are equals.
	 */
	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof ICClass)) {
			return false;
		}
		else {
			ICClass c = (ICClass)o;
			if(getName() == c.getName()) {
				return true;
			}
			return false;
		}
	}	
}