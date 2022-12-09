package mainPackage;
import java.util.*;

public class InstructionSet {
	HashMap<Byte, String> InstructionDescriptorTable = new HashMap<>();
	
	InstructionSet(Byte code, String func){
		InstructionDescriptorTable.put(code, func);
	}
	
	InstructionSet(Object... args){
		if (args.length % 2 == 0) {
			for (var i=0; i<args.length/2;i++) {
				InstructionDescriptorTable.put((Byte) args[2*i], (String) args[2*i +1]);
				// creates our list of instructions
			}
		}
	}
	
	public String searchINS(Byte code) {
		return (InstructionDescriptorTable.get(code));
	}
}
