package IC.myTypes;

public class MyIntType extends MyPrimitiveType {

		
	public MyIntType(){
		this.setName("int");
		this.setUnique_id(1);
	}
	
	public MyIntType clone(){
		MyIntType t = new MyIntType();
		t.setName(this.getName());
		t.setDimention(this.getDimention());
		t.setUnique_id(this.getUnique_id());
		return t;
	}
}
