package IC.AST;

import java.util.HashSet;
import java.util.Set;

import IC.DataTypes;
import IC.*;
import IC.mySymbolTable.*;
import IC.myTypes.*;
import IC.semanticChecks.*;

/**
 * User-defined data type AST node.
 * 
 * @author Tovi Almozlino
 */
public class UserType extends Type {

	/**
	 * The name of the type.
	 */
	private String name;
	
	/**
	 * The ICClass of that type.
	 */
	private static Set<ICClass> m_icclasses = new HashSet<ICClass>();

	public <UpType> UpType accept(Visitor<UpType> visitor) {
		return visitor.visit(this);
	}
	
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType d) {
		return visitor.visit(this, d);
	}

	/**
	 * Constructs a new user-defined data type node.
	 * 
	 * @param line
	 *            Line number of type declaration.
	 * @param name
	 *            Name of data type.
	 */
	public UserType(int line, String name) {
		super(line);
		this.name = name;
	}
	
	/**
	 * Create and bind the user type with the ICClass - 
	 *   This instance is the main type of the class.
	 */
	public UserType(ICClass c) {
		this(c.getLine(), c.getName());
		getICClasses().add(c);
		
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the class
	 */
	public ICClass getICClass() {
		ICClass retClass = null;
		
		for (ICClass c : getICClasses()) {
			if(c.getName().equals(getName())) {
				retClass = c;
			}
		}
		
		return retClass;
	}
	
	/**
	 * @return the classes
	 */
	public static Set<ICClass> getICClasses() {
		return m_icclasses;
	}

	/**
	 * @param classes the m_class to set
	 */
	public static void setICClasses(Set<ICClass> classes) {
		m_icclasses = classes;
	}

	/**
	 * Clone the object.
	 */
	@Override
	public Object clone() {
		return new UserType(line, name);
	}

	@Override
	public int compareTo(Type o) {
		// TODO Auto-generated method stub
		if(o.equals(new PrimitiveType(o.getLine(), null)))
			return 1;
		if(this.equals(o))
			return 0;
		if(this.getDimension() > 0 || o.getDimension() > 0)
			return -1;
		if(o.enclosingScope().Lookup(this.getName())==null)
			return -1;
		
		return 1;
		
	}

	@Override
	public MyType getMyType() {
		// TODO Auto-generated method stub
		MyArrayType arrayType = null;
		MyClassType baseType = new MyClassType();
		baseType.setName(this.getName());	
		
		if(this.getDimension()==0){
			return baseType;
		}
		else{
			arrayType = new MyArrayType();
			baseType.setDimention(this.getDimension());
			arrayType.setElementType(baseType);
			arrayType.setName(this.getName());
			arrayType.setFullName();
		}
		return arrayType;
	}
}
