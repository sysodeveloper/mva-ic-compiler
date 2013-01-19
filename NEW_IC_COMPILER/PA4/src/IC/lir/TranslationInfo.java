package IC.lir;

import java.util.ArrayList;
import java.util.List;

public class TranslationInfo{
	public List<String> instructions;
	public List<Integer> usedRegisters;
	
	public TranslationInfo() {
		instructions = new ArrayList<String>();
		usedRegisters = new ArrayList<Integer>();
	}
}