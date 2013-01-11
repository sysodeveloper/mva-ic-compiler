package IC;

public class MyStringType extends MyPrimitiveType {

	
	
	public MyStringType(){
		this.setName("string");
		this.setUnique_id(4);
	}
	
	public MyStringType clone(){
		MyStringType t = new MyStringType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		return t;
	}
}
