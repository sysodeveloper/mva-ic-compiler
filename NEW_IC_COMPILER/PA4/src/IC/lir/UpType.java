package IC.lir;

public class UpType {
	
	
	private String targetRegister; 
	
	
	public UpType(){		
		targetRegister = null; 
	}
	
	public UpType(UpType param){
		this.targetRegister = param.targetRegister;
	}
	
	public UpType(String reg){
		this.targetRegister = reg;
	}
	
	
	public void setTarget(String register){
		this.targetRegister = register;
	}
	
	public String getTarget(){
		return targetRegister;
	}
}

