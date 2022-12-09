package mainPackage;

public class HardwareHandlers {
	// Implement features of our CPU here, in different bytefields
	
	
	
	
	
	
	Short FLAGS = 0;// this is 16 bits, each representing a flag
	/*
	 * FLAG TABLE
	 * ====================
	 * 1:			|2:
	 * 3:			|4:
	 * 5:			|6:
	 * 7:			|8:
	 * 9:			|10:
	 * 11:			|12:
	 * 13:			|14:
	 * 15:			|16:
	 */
	
	public Short flagValue(int shiftAmnt) {
		/*  @param - Number of bits to shift by/ FlagNum to get
		 *  @return - Return either a one or zero, result of flag.
		 *  
		 *  Examples: 
		 *  FLAGS = 1001 0000 0000 0000
		 *  Want the 13th value, n=13
		 *  shift right n-1, or 12
		 *   -----------------
		 *   1001|000000000000
		 *   0001              AND
		 *   0001              Value of 1
		 */ 

		return( (short)(FLAGS >> (shiftAmnt-1) & 1 ) ); 
	}
	
}
