package IC;

import IC.AST.Type;

public abstract class MyType {
	private int unique_id;
	public static int run_id = 6;
	private String name;	
	private int dimention=0;
	abstract boolean subtypeOf(MyType type) ;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof MyType)) {
			return false;
		}
		else {
			MyType t = (MyType)o;
			if(	getName().equals(t.getName())) {
				if(getDimention() == t.getDimention()){
					return true;	
				}else{
					return false;
				}
			}
			return false;
		}
	}
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + 7;
	    char[] arrChar = getName().toCharArray();
	    for (char c : arrChar) {
	    	result = prime * result + c;
		}
	    
	    return result;
	}

	public int getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(int unique_id) {
		this.unique_id = unique_id;
	}

	public int getDimention() {
		return dimention;
	}

	public void setDimention(int dimention) {
		this.dimention = dimention;
	}
	
}
