package mainPackage;

/**
 * This class is a wrapper for a byte primitive
 * @author Brian Waltz
 *
 */
public class CPU_R_8 {
	// the byte this class actually wraps up. 
	/**
	 * byte wrapped
	 */
	private byte value; 

	/**
	 * Constructor to wrap byte with existing value
	 * @param b1, byte to wrap
	 */
	CPU_R_8(byte b1){
		this.value = b1;
	}
	
	/**
	 * Empty Constructor
	 */
	CPU_R_8() {
		this((byte) 0);
	}
	
	/** 
	 * return value as bte
	 * @return byte value of register
	 */
	public byte getValueB(){
		return this.value;
	}
	
	/**
	 * Return value as int
	 * @return int primitive
	 */
	public int getValueS(){
		return (int) this.value & 0xff;
	}

	/**
	 * Set value of register using byte
	 * @param bi, byte input
	 * @return boolean (success), false on failure
	 */
	public boolean setValue(byte bi){
		try{
			this.value = bi;
			return true;
		} catch (Throwable e){
			return false;
		}
	}
}

