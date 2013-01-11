package IC;

public class MyArrayType extends MyType {
	
	private MyType elementType;
	private int arrayDimention;
	
	public MyArrayType(){
		this.setUnique_id(this.run_id++);
	}
	
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		if(!(type instanceof MyArrayType ))
			return false;
		if(! elementType.subtypeOf(((MyArrayType)type).getElementType()))
				return false;
		if(arrayDimention != ((MyArrayType)type).getDimantion())
			return false;
		return true;
		
	}

	public MyType getElementType() {
		return elementType;
	}

	public void setElementType(MyType elementType) {
		this.elementType = elementType;
	}

	public int getDimantion() {
		return arrayDimention;
	}

	public void setDimantion(int dimantion) {
		this.arrayDimention = dimantion;
	}
	
	public void setFullName(){
		StringBuilder dimentionString = new StringBuilder();
		for (int i = 0; i < getDimantion(); i++) {
			dimentionString.append("[]");
		}
		
		this.setName((elementType.getName()+dimentionString));
	}
	
	public String toString(){
		return this.getUnique_id()+": Array type: "+this.getName();
	}
}
