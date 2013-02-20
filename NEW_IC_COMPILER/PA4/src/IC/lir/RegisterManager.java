package IC.lir;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class RegisterManager {
	ArrayList<Boolean> registers;
	Stack<List<Integer>> allocators;
	private String lastRegister;
	public RegisterManager(){
		registers = new ArrayList<Boolean>();
		allocators = new Stack<List<Integer>>();
		lastRegister = null;
	}
	
	public void addAllocator(){
		allocators.push(new ArrayList<Integer>());
	}
	
	public String nextRegister(){
	
		if(allocators.size() <= 0 ){
			addAllocator();
		}
		int r = 0;
		for(Boolean b : registers){
			if(b.booleanValue() == true){
				this.registers.set(r, false);
				return registerDescription(r); 
			}
			r++;
		}
		//no free register yet, create one
		
		registers.add(false);
		System.out.println(this.registers);
		//add to allocator stack
		allocators.peek().add(r);
		//save last register
		lastRegister = registerDescription(r);
		return lastRegister;
	}
	
	public void freeRegisters(){
		//frees all the register to the last allocator
		List<Integer> registersToFree = allocators.pop();
		for(Integer i : registersToFree){
			freeRegister(registerDescription(i));
		}
	}
	
	public void freeRegister(String register){
		if(register.length() >= 2){
			String number = register.substring(1);
			int regNumber = -1;
			try{
				regNumber = Integer.parseInt(number);
			}catch(NumberFormatException e){
				
			}
			if(regNumber >= 0){
				//free register at pos regNumber
				System.out.println("Free " + regNumber);
				this.registers.set(regNumber, true);
			}
		}
		System.out.println(this.registers);
	}
	
	public String lastRegisterUsed(){
		return lastRegister;
	}
	
	private String registerDescription(int registerNumber){
		return "R"+registerNumber;
	}
}
