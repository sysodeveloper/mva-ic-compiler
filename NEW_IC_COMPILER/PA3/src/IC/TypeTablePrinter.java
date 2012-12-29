/**
 * 
 */
package IC;

import java.util.Set;

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
		sbTypes.append("Type Table: " + m_fileName + "\n");
		for (Type type : m_types) {
			if(type instanceof UserType) {
				UserType ut = (UserType)type;
				sbTypes.append("    " + type.getID() + ": Class: " + ut.getName() + "\n");
			}
			else if(type instanceof MethodType) {
				MethodType mt = (MethodType)type;
				sbTypes.append("    " + type.getID() + ": Method type: " + mt + "\n");
			}
			else {
				if(type.getDimension() >= 1) {
					StringBuilder dimentionString = new StringBuilder();
					for (int i = 0; i < type.getDimension(); i++) {
						dimentionString.append("[]");
					}
					sbTypes.append("    " + type.getID() + ": Array type: " + type.getName() + dimentionString + "\n");
				}
				else {
					sbTypes.append("    " + type.getID() + ": Primitive type: " + type.getName() + "\n");
				}
			}
		}
		
		return sbTypes.toString();
	}
}
