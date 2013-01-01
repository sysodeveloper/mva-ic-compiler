/**
 * 
 */
package IC;

import java.util.Set;

import IC.AST.ICClass;
import IC.AST.MethodType;
import IC.AST.PrimitiveType;
import IC.AST.Type;
import IC.AST.UserType;

/**
 * Prints the types.
 *
 */
public class TypeTablePrinter {
	/**
	 * The list of types to print.
	 */
	Set<Type> m_types;
	
	/**
	 * The file name.
	 */
	String m_fileName;
	
	/**
	 * Create the TypeTablePrinter.
	 */
	public TypeTablePrinter(Set<Type> type, String fileName) {
		m_types = type;
		m_fileName = fileName;
	}
	
	
	/**
	 * Return the string representation of the type table.
	 */
	@Override
	public String toString() {
		StringBuilder sbTypes = new StringBuilder();
		StringBuilder sbPrimitiveTypes = new StringBuilder();
		StringBuilder sbUserTypes = new StringBuilder();
		StringBuilder sbArrayTypes = new StringBuilder();
		StringBuilder sbMethodTypes = new StringBuilder();
		
		sbTypes.append("Type Table: " + m_fileName + "\n");
		for (Type type : m_types) {
			if(type.getDimension() >= 1) {
				sbArrayTypes.append("    " + type.getID() + ": Array type: " + type.getFullName() + "\n");
			}
			else {
				if(type instanceof UserType) {
					UserType ut = (UserType)type;
					ICClass superClass = null;
					if(ut.getICClass().hasSuperClass()) {
						for (ICClass c : UserType.getICClasses()) {
							if(ut.getICClass().getSuperClassName().equals(c.getName())) {
								superClass = c;
							}
						}
					}
					sbUserTypes.append("    " + ut.getICClass().getID() + ": Class: " + ut.getName() + 
							(ut.getICClass().hasSuperClass()?", Superclass ID: " + superClass.getID():"")
							+ "\n");
				}
				else if(type instanceof MethodType) {
					MethodType mt = (MethodType)type;
					sbMethodTypes.append("    " + type.getID() + ": Method type: " + mt.getName() + "\n");
				}
				else {
						sbPrimitiveTypes.append("    " + type.getID() + ": Primitive type: " + type.getName() + "\n");
				}
			}
		}
		
		sbTypes.append(sbPrimitiveTypes);
		sbTypes.append(sbUserTypes);
		sbTypes.append(sbArrayTypes);
		sbTypes.append(sbMethodTypes);
		
		return sbTypes.toString();
	}
}
