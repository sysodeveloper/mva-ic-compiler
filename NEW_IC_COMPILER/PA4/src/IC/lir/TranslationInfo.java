package IC.lir;

import java.util.ArrayList;
import java.util.List;

public class TranslationInfo{
	public List<StringBuffer> instructions;
	public List<Integer> usedRegisters;
	
	public TranslationInfo() {
		instructions = new ArrayList<StringBuffer>();
		usedRegisters = new ArrayList<Integer>();
	}
}