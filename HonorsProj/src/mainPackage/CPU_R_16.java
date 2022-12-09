package mainPackage;


public class CPU_R_16 {
	// DO NOT initialize these. We need them to be references/pointers to other objects, lol. 
	private CPU_R_8 LB = null;
	private CPU_R_8 HB = null;

	//Construct object with pre-existing two byte objects
	/**
	 * Creates a CPU_R_16 using two existing CPU_R_8 Objects.
	 * @param Hb high byte object
	 * @param Lb low byte object
	 */
	CPU_R_16(CPU_R_8 Hb, CPU_R_8 Lb) {
		this.HB = Hb;
		this.LB = Lb;
	}

	/**
	 * Default constructor, creates two byte objects
	 */
	CPU_R_16(){
		this(new CPU_R_8(), new CPU_R_8());
		// allow empty initialization 
	}
	
	

	/**
	 * Sets the value. Returns false on failure.
	 * @param high, the high byte value
	 * @param low, the low byte value
	 * @return success (boolean)
	 */
	public boolean setValue(byte high, byte low){
		try {
			HB.setValue(high);
			LB.setValue(low);
			return true;
		} catch (Throwable e){
			return false;
		}
	}
	
	/**
	 * Converts a short primitive into two bytes, and uses them to set HB and LB. Returns false on failure.
	 * @param _16bit, short to parse high and low bytes out of. 
	 * @return success (boolean)
	 */
	public boolean setValue(short _16bit){
		try {
			HB.setValue((byte)(_16bit << 8 & 0x00FF));
			LB.setValue((byte)(_16bit & 0x00FF));
			return true;
		} catch (Throwable e){
			return false;
		}
	}

	/**
	 * Returns the value of the register, with bit retainment. XORs to combine them. 
	 * @return
	 */
	public short getValue(){
		return (short) (( ( (short) HB.getValueS()) << 8 & 0xFFFF)^((short) LB.getValueS() & 0xFFFF ));
			// Shift the bits into alignment and XOR them, ie:
			/*
				Cast byte to short
				0000000011001111
				0000000011111100
				Shift left
				1100111100000000
				0000000011111100
				XOR
				1100111111111100
				POG

			*/
	}

	/**
	 * Return the high half
	 * @return, High byte
	 */
	public byte getHigh(){
		return (HB.getValueB());
		// call the lower level getter
	}

	/**
	 * Return the low half
	 * @return, Low byte
	 */
	public byte getLow(){
		return (LB.getValueB());
	}

	/**
	 * Set the high byte. Returns false on failure. 
	 * @param bi, byte value to be set to
	 * @return success (boolean)
	 */
	public boolean setHigh(byte bi){
		try {
			HB.setValue(bi); // byte-in
			return true;
		} catch (Throwable e){
			return false;
		}
	}

	/**
	 * Set the low byte. Returns false on failure. 
	 * @param bi, byte value to be set to
	 * @return success (boolean)
	 */
	public boolean setLow(byte bi){
		try {
			LB.setValue(bi); // byte-in
			return true;
		} catch (Throwable e){
			return false;
		}
	}
}
