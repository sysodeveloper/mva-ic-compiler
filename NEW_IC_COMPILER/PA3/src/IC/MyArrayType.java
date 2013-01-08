package IC;

public class MyArrayType extends MyType {
	
	private MyType elementType;
	private int dimantion;
	
	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}

	public MyType getElementType() {
		return elementType;
	}

	public void setElementType(MyType elementType) {
		this.elementType = elementType;
	}

	public int getDimantion() {
		return dimantion;
	}

	public void setDimantion(int dimantion) {
		this.dimantion = dimantion;
	}
	
	public void setFullName(){
		StringBuilder dimentionString = new StringBuilder();
		for (int i = 0; i < getDimantion(); i++) {
			dimentionString.append("[]");
		}
		
		this.setName((elementType.getName()+dimentionString));
	}
	
	public String toString(){
		return "Array type: "+this.getName();
	}
}
