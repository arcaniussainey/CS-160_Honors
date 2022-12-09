package mainPackage;

public class CPU_R_32 {
	// Reference/pointer to CPU_R_16 object
	private CPU_R_16 LH = null; // low half of bits.
	// Bits for high half
	private short HH = 0; // high half

	// Names for accessing the registers by a string
	private String LH_NAME = "LH_NAME";
	private String LH_NAME_H = "LH_NAME_H";
	private String LH_NAME_L = "LH_NAME_L";
	private String ALL_R = "ALL_R"; 
	
	// Binary values to correlate with above names. Must be final as case tables are static. 
	private final byte LH_NAMEv = 0;
	private final byte LH_NAME_Hv = 1;
	private final byte LH_NAME_Lv = 2;
	private final byte ALL_Rv = 3;
	
	// Converts the name of a register or subregister to its flag value
	/**
	 * Converts the given string into a flag value using stored register names. Returns 255 if fails. 
	 * @param name String
	 * @return byte Flag_Value
	 */
	private byte nameToFlag(String name) {
		// convert stored names to their final flags
		// could use map, but no. 
		if (name == LH_NAME) {
			return LH_NAMEv;
		} else if (name == LH_NAME_H) {
			return LH_NAME_Hv;
		} else if(name == LH_NAME_L) {
			return LH_NAME_Lv;
		} else if (name == ALL_R) {
			return ALL_Rv;
		} else {
			return (byte) 255;
		}
	}
	
	// Constructor allowing CPU Register to be made with already existing register. 
	// 	  could potentially be used to enable mode-switching like behavior (16-bit real to 32-bit protected?)
	/**
	 * Creates a CPU_R_32 with reference to existing object
	 * @param Lh CPU_R_16 Object
	 */
	CPU_R_32(CPU_R_16 Lh){
		this.LH = Lh; // set the low half to an object reference
	}

	/**
	 * Empty constructor, creates empty CU_R_16 object.
	 */
	CPU_R_32(){
		this(new CPU_R_16());
		// supporting empty here allows an entire register series to be created from top down
	}

	/**
	 * Returns value of register as Integer object
	 * @return Integer
	 */
	public Integer getValue(){
		return ( ((int) HH << 16 & 0xFFFFFFFF) | ((int) this.LH.getValue() & 0xFFFFFFFF));
		// 32 bit registers don't support high half only
	}

	/**
	 * Returns lower bytes of register as short primitive
	 * @return short
	 */
	public short getLowHalf(){
		return this.LH.getValue();
	}

	/**
	 * Sets value of register using Integer Object. Returns false on failure
	 * @param value Integer
	 * @return boolean (success/failure)
	 */
	public boolean setValue(Integer value){
		try{
			this.HH = (short) (value>>16 & 0x0000FFFF);
			this.LH.setValue((short) ((int) value & 0x0000FFFF));
			return true;
		} catch(Throwable e){
			return false;
		}
	}

	/**
	 * Sets value of register using int primitive. Returns false on failure
	 * @param value int primitive
	 * @return boolean (success/failure)
	 */
	public boolean setValue(int value){
		try{
			this.HH = (short) ((int) value>>16 & 0x0000FFFF);
			this.LH.setValue((short) ((int) value & 0x0000FFFF));
			return true;
		} catch(Throwable e){
			return false;
		}
	}

	/**
	 * Sets lower bytes of object using short value. Returns false on failure.
	 * @param Lh short primitive
	 * @return boolean (success)
	 */
	public boolean setLowHalf(short Lh){
		try {
			// calls the setValue operation of the lower half
			this.LH.setValue(Lh);
			return true;
		} catch(Throwable e){
			return false;
		}
	}

	/**
	 * Sets lower bytes of object using two byte values. Returns false on failure.
	 * @param Lh_HB byte, Lh_LB byte
	 * @return boolean (success)
	 */
	public boolean setLowHalf(byte Lh_HB, byte Lh_LB){
		try {
			this.LH.setValue(Lh_HB, Lh_LB);
			return true;
		} catch(Throwable e){
			return false;
		}
	}

	/**
	 * Reads register or sub-register value by assigned name, and attempts cast to given type T. Returns null on failure. Prints exception
	 * @param <T> Generic Type T
	 * @param name String
	 * @param target Generic Type T
	 * @return
	 */
	public <T> T readByName(String name, Class<T> target){
		// technically, this should "throw" the error, instead of catching and handling it. 
		try {
			// get the fags byte value, if it's even there
			byte val = this.nameToFlag(name);
			switch(val){
				// allow access by name, via case matching of returned flag value
				case LH_NAMEv:
					return target.cast(LH.getValue());
					// call target getValue method, and 
				case LH_NAME_Hv:
						return target.cast(LH.getHigh());
				case LH_NAME_Lv:
						return target.cast(LH.getLow());
				case ALL_Rv:
						return target.cast(this.getValue());
				default:
						try {
							return target.cast(null);
						} catch(Throwable e){
							return null;
						}
			}
		} catch(Throwable t){
			System.out.println(t);
			return null;
		}
	}
	
	/**
	 * Write to a register or sub-register by its name. Return false on failure
	 * @param name String
	 * @param args Object series
	 * @return boolean (success)
	 */
	public boolean WriteByName(String name, Object... args) {
		// we could've done what reflection does and had the user pass in
		// 		a function signature/parameter list, and matched the function
		// 		using that. 
		try {
			byte val = this.nameToFlag(name);
			switch(val){
			// same switch logic as read, get flag val and then switch on
				// allow access by name
				case LH_NAMEv:
					if (args.length==1) {
						// decide which to use
						return(this.setLowHalf((short) args[0]));
					} else {
						return(this.setLowHalf((byte) args[0], (byte) args[1]));
					}
					
				case LH_NAME_Hv:
					return (this.LH.setHigh((byte) args[0]));
					
				case LH_NAME_Lv:
					return (this.LH.setLow((byte) args[0]));
					
				case ALL_Rv:
					return (this.setValue((Integer) args[0]));
					
				default:
					try {
						return false;
					} catch(Throwable e){
						return false;
					}
			}
		} catch(Throwable t){
			System.out.println(t);
			return false;
		}
	}

	public void setName(byte flagVal, String name) {
		// set the name of a flagvalue by its byte val, to the new string name. 
		switch(flagVal) {
			case LH_NAMEv:
				LH_NAME = name;
				break;
			case LH_NAME_Hv:
				LH_NAME_H = name;
				break;
			case LH_NAME_Lv:
				LH_NAME = name;
				break;
			case ALL_Rv:
				ALL_R = name;
				break;
		}
	}
	
	/**
	 * Bitwise AND
	 * @param <T> Type castable to int
	 * @param inVal Generic, castable to int primitive
	 */
	public <T> void AND_OP(T inVal) {
		this.setValue(((this.getValue() & 0xffffffff) & ((int) inVal)) );
		// perform AND with bit preservation
	}
	
	/**
	 * Bitwise OR
	 * @param <T> Type castable to int
	 * @param inVal Generic, castable to int primitive
	 */
	public <T> void OR_OP(T inVal) {
		this.setValue(((this.getValue() & 0xffffffff) | ((int) inVal)) );
		// perform OR with bit preservation
	}
	public
	
	/**
	 * Bitwise XOR
	 * @param <T> Type castable to int
	 * @param inVal Generic, castable to int primitive
	 */<T> void XOR_OP(T inVal) {
		this.setValue(((this.getValue() & 0xffffffff) ^ ((int) inVal)) );
		// perform XOR with bit preservation 
	}
	
	/**
	 * Bitwise SHIFT LEFT
	 * @param <T> Type castable to int
	 * @param inVal Generic, castable to int primitive
	 */
	public <T> void SHIFTL_OP(T inVal) {
		this.setValue(((this.getValue() & 0xffffffff) << ((int) inVal)) );
		// perform SHIFTL with bit preservation
	}
	
	/**
	 * Bitwise SHIFT RIGHT
	 * @param <T> Type castable to int
	 * @param inVal Generic, castable to int primitive
	 */
	public <T> void SHIFTR_OP(T inVal) {
		this.setValue(((this.getValue() & 0xffffffff) >> ((int) inVal)) );
		// perform SHIFTR with bit preservation
	}
}

