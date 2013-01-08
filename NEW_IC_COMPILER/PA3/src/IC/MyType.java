package IC;

import IC.AST.Type;

public abstract class MyType {
	private String name;	
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
				return true;
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
	
}
