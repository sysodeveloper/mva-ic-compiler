package IC.lir;

public class UpType {
	
	
	private String targetRegister; 
	private int nextFreeRegister; // the fist register in the list of all free registers
	
	public UpType(){
		nextFreeRegister = 0;
		targetRegister = null; 
	}
	
	public UpType(UpType prevUp){
		this.nextFreeRegister = prevUp.getNextFreeRegister();
	}
	
	private String registerDescription(int registerNumber){
		return "R"+registerNumber;
	}
	
	public String nextRegister(){
		String next = registerDescription(nextFreeRegister++);
		return next;
	}
	
	public void setTarget(String register){
		this.targetRegister = register;
	}
	
	public void freeRegister(){
		nextFreeRegister--;
	}
	
	public void freeRegisters(int numToFree){
		nextFreeRegister-=numToFree;
	}
	
	public int getNextFreeRegister(){
		return nextFreeRegister;
	}
}

