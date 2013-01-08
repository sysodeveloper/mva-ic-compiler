package IC;

public class MyStringType extends MyPrimitiveType {

	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MyStringType(){
		this.setName("string");
	}
}
