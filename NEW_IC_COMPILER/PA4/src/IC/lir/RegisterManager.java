package IC.lir;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

public class RegisterManager {
	ArrayList<Boolean> registers;
	Stack<List<Integer>> allocators;
	
	public RegisterManager(){
		registers = new ArrayList<Boolean>();
		allocators = new Stack<List<Integer>>();
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
				return registerDescription(r); 
			}
			r++;
		}
		//no free register yet, create one
		registers.add(false);
		//add to allocator stack
		allocators.peek().add(r);
		return registerDescription(r);
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
			String number = register.substring(2);
			int regNumber = -1;
			try{
				regNumber = Integer.parseInt(number);
			}catch(NumberFormatException e){
				
			}
			if(regNumber >= 0){
				//free register at pos regNumber
				this.registers.set(regNumber, true);
			}
		}
	}
	
	private String registerDescription(int registerNumber){
		return "R"+registerNumber;
		
	}
}
