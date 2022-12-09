package mainPackage;



public class CPU {
	/*
	 * 
	 * 
	 */
	
	// Create registers with default empty constructor (it will create sub-objects from there.
	public CPU_R_32 ESI = new CPU_R_32();
	public CPU_R_32 EAX = new CPU_R_32();
	public CPU_R_32 EDX = new CPU_R_32();
	public CPU_R_32 EBX = new CPU_R_32();
	
	// Define methods under class, referencing this class' data. Allows reflection API to grab them. 
	public void movEAX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movEDX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movEBX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movESI(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	
	
}
