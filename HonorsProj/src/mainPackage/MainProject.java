package mainPackage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

	/** 
	 * Main class, executes.
	 * @author Brian Waltz
	 *
	 */
public class MainProject {
	/*
	 *  Emulator of a custom Computer
	 * 
	 * 
	 * Components of note
	 * 		- CPU
	 * 		- RAM
	 * 		- Disk
	 * 
	 * CPU:
	 * 	The CPU will likely be best simulated by a bitfield, or some
	 * 		equivalent, in its own object. 
	 * 
	 * RAM:
	 * 	The RAM will likely be best represented as an array of bytes in
	 * 		memory. 
	 * 
	 * Disk:
	 * 	Disk should be prototyped as an array of bytes, but at some point
	 * 		aim for actual bytes from a real disk. 
	 * 
	 */
	
	//private Byte[] Memory = new Byte[100];
	
	
	public static void main(String [] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		
		//System.out.println(CPU.TREG.retVal(CPU.AH, CPU.AL));
		//System.out.println(CPU.REG(CPU.BH, CPU.BL));
		CPU ourCPU = new CPU();
		System.out.println("COMP Reg to Bin INT: " + CPU_R.registerToBinString( (int) ourCPU.EAX.getValue()) );
		System.out.println("COMP INT class: " + Integer.toBinaryString( (int) ourCPU.EAX.getValue() ));
		
		
		ourCPU.EAX.setValue(16876813);
		System.out.println( Integer.toBinaryString( (int)(byte) 255 ) );
		System.out.println("Reg to Bin BYTE: " + CPU_R.registerToBinString( (byte) 255) );
		System.out.println("Reg to Bin Short: " + CPU_R.registerToBinString( (short) 11702) );
		System.out.println("Reg to Bin INT: " + CPU_R.registerToBinString( (int) 55028492) );
		System.out.println("INT class: " + Integer.toBinaryString( (int) 55028492 ));
		
		System.out.println("COMP Reg to Bin INT: " + CPU_R.registerToBinString( (int) ourCPU.EAX.getValue()) );
		System.out.println("COMP Reg to Bin INT: " + CPU_R.registerToBinString( (int) ourCPU.EAX.getLowHalf()) );
		System.out.println("COMP INT class: " + Integer.toBinaryString( (int) ourCPU.EAX.getValue() ));
		// Testing/demonstration
		
		System.out.println( Integer.toBinaryString( (int)9430025) );
		
		
		
		//setup instruction set
		InstructionSet mov_regs = new InstructionSet(
														(byte)0xb5, "movEAX",
														(byte)0xa5, "movEDX",
														(byte)0xd5, "movEBX"
													);
		
		byte[] instructions = new byte[] {
				(byte)0xc1, (byte) 0xb5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, 
				(byte)0xc1, (byte) 0xb5, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x16
				};
		
		System.out.println("Switching to Execution");
		
		ByteBuffer memory = ByteBuffer.wrap(instructions);
		
		Class<CPU> c = CPU.class;
		
		while (memory.hasRemaining()) {
			byte curr_ins = memory.get();
			switch(curr_ins) {
				case (byte) 0xc1:
					Class<?>[] argType = new Class[] {Integer.class};
					byte instruction = memory.get();
					// moving 4 bytes into register
					int value = memory.getInt();
					String command = mov_regs.searchINS((Byte) instruction);
					Method ins_com = c.getMethod(command, argType);
					// we don't handle possibility it might error out, CPU would be reading invalid bytecode then
					ins_com.invoke(ourCPU, argType[0].cast(value));
					System.out.println(ourCPU.EAX.getValue());
					System.out.println(": " + CPU_R.registerToBinString( (int) ourCPU.EAX.getValue()) );
			}
			
		
	}
	System.out.println(Byte.parseByte("111")); 

	}
}
