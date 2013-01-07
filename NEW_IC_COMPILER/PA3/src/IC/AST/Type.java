package IC.AST;

/**
 * Abstract base class for data type AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Type extends ASTNode implements INameable,Comparable<Type> {

	/**
	 * Number of array 'dimensions' in data type. For example, int[][] ->
	 * dimension = 2.
	 */
	private int dimension = 0;

	/**
	 * Constructs a new type node. Used by subclasses.
	 * 
	 * @param line
	 *            Line number of type declaration.
	 */
	protected Type(int line) {
		super(line);
	}

	public abstract String getName();

	public int getDimension() {
		return dimension;
	}
	
	public String getFullName() {
		return getName() + dimPostFix();
	}
	
	public void setDimension(int dim) {
		dimension = dim;
	}

	public void incrementDimension() {
		++dimension;
	}
	
	/**
	 * The hash code for the type.
	 */
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + dimension;
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
		if(!(o instanceof Type)) {
			return false;
		}
		else {
			Type t = (Type)o;
			if(dimension == t.dimension && 
					getName().equals(t.getName())) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * @return The dimension of the type.
	 */
	public  String dimPostFix() {
		StringBuilder dimentionString = new StringBuilder();
		for (int i = 0; i < getDimension(); i++) {
			dimentionString.append("[]");
		}
		
		return dimentionString.toString();
	}
	
	/**
	 * Clone the object.
	 */
	public abstract Object clone();
	
	
}