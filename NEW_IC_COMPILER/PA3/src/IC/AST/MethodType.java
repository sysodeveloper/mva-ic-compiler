/**
 * 
 */
package IC.AST;

import java.util.List;

/**
 * AST method type.
 *
 */
public class MethodType extends Type {
	
	/**
	 * The return type of the method.
	 */
	private Type m_returnType;
	
	/**
	 * The list of formals for this method type.
	 */
	private List<Type> m_formalTypes;

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
	 * @param line Line number of type declaration.
	 * @param returnType The return type of the method type.
	 * @param formalTypes The list of formal type of the method type.
	 */
	public MethodType(int line, Type returnType, 
			List<Type> formalTypes) {
		super(line);
		setReturnType(returnType);
		setFormalTypes(formalTypes);
	}
	
	/**
	 * @return the m_returnType
	 */
	public Type getReturnType() {
		return m_returnType;
	}

	/**
	 * @param m_returnType the m_returnType to set
	 */
	public void setReturnType(Type m_returnType) {
		this.m_returnType = m_returnType;
	}
	
	/**
	 * @return the m_formalTypes
	 */
	public List<Type> getFormalTypes() {
		return m_formalTypes;
	}

	/**
	 * @param m_formalTypes the m_formalTypes to set
	 */
	public void setFormalTypes(List<Type> m_formalTypes) {
		this.m_formalTypes = m_formalTypes;
	}

	/**
	 * Clone the object.
	 */
	@Override
	public Object clone() {
		return new MethodType(line, m_returnType, m_formalTypes);
	}

	@Override
	public String getName() {
		StringBuilder formals = new StringBuilder();
		boolean first = true;
		for (Type fType : getFormalTypes()) {
			if(!first) {
				formals.append(", ");
			}
			formals.append(fType.getName());
			first = false;
		}
		return "{" + formals.toString()  + " -> " + 
			getReturnType().getName()  + "}";
	}
}
