package IC.myTypes;

public class MyArrayType extends MyType {
	
	private MyType elementType;
	
	public MyArrayType clone(){
		MyArrayType t = new MyArrayType();
		t.setName(this.getName());
		t.setUnique_id(this.getUnique_id());
		t.setElementType(this.getElementType().clone());
		t.setDimention(this.getDimention());
		t.setFullName();
		return t;
	}
	
	public MyArrayType(){
		this.setUnique_id(this.run_id++);
	}
	
	@Override
	public boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		if(!(type instanceof MyArrayType ))
			return false;
		if(!(elementType == ((MyArrayType)type).getElementType()))
				return false;
		if(getDimention() != ((MyArrayType)type).getDimention())
			return false;
		return true;
		
	}

	public MyType getElementType() {
		return elementType;
	}

	public void setElementType(MyType elementType) {
		this.elementType = elementType;
		setDimention(this.elementType.getDimention());
		this.elementType.setDimention(0);
	}

	public void setFullName(){
		StringBuilder dimentionString = new StringBuilder();
		for (int i = 0; i < getDimention(); i++) {
			dimentionString.append("[]");
		}
		this.setName((elementType.getName()+dimentionString));
	}	
	public String toString(){
		return this.getUnique_id()+": Array type: "+this.getName();
	}
	 
}
