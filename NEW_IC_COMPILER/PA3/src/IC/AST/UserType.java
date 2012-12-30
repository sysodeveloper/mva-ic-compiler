package IC.AST;

import java.util.HashSet;
import java.util.Set;

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
}
