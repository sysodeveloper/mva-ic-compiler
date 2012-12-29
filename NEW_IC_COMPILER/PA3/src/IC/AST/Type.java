package IC.AST;

/**
 * Abstract base class for data type AST nodes.
 * 
 * @author Tovi Almozlino
 */
public abstract class Type extends ASTNode {

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
		System.out.println("Equals called!");
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
	 * Clone the object.
	 */
	public abstract Object clone();
}