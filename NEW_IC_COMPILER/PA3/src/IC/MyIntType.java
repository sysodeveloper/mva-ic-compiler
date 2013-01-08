package IC;

public class MyIntType extends MyPrimitiveType {

	@Override
	boolean subtypeOf(MyType type) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MyIntType(){
		this.setName("int");
	}
}
