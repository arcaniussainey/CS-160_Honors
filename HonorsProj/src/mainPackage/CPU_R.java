package mainPackage;

/**
 * Class providing methods for working with registers. Provides type overloaded methods for convenience
 * 
 * -Originally a class made to implement all registers, was later broken into multiple methods. 
 * @author Brian Waltz
 *
 */
public class CPU_R {

	/**
	 * Converts two byte primitives into a short by shifting them. Maintains sign and order. 
	 * @param b1 byte primitive
	 * @param b2 byte primitive
	 * @return short primitive
	 */
	public static short twoBytesToShort(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
        /*
         * the AND by 0xFF is needed. 
         *     Java will 'promote' bits, aka
         *     it will convert them to another
         *     data type first via sign extension 
         *     (adding 1s). The AND by 0xFF
         *     will AND it against 1111 1111, which
         *     won't modify it at all, but will
         *     keep it the same by forcing java
         *     to not use a sign extension when 
         *     casting. 
         *     
         * byte one is shifted left, and byte two is
         *   kept the same. I believe that the '|' 
         *   is an OR operator, so this would essentially
         *   merge the bytes. 
         *   
         * The reason that likely works is the prior mentioned
         *   bit promotion, aka, 00000000 is a byte, but
         *   Java converts it to an int which would be 
         *   00000000 00000000 00000000 00000000, and then 
         *   operates on it. Thus if we had two bytes:
         *       b1 = 01100000
         *       b2 = 00001100
         *   Java would convert them to ints:
         *       b1 = 00000000 00000000 00000000 01100000
         *       b2 = 00000000 00000000 00000000 00001100
         *   Java would only do this for operations, noted for later. 
         *   Following this, we would do an operation on both. 
         *   b1 would be shifted left by a byte. b2 would be ANDed with 1111 1111
         *   
         *      b1 = 00000000 00000000 01100000 00000000
         *      							 <- 87654321 (moved 8 places left)
         *      b2 = 00000000 00000000 00000000 00001100
         *      								AND of 1111 1111 with 0000 11000 is 0000 1100
         *      
         *   The final step is to merge these data types, 
         *   and then retain them. 
         *   
         *   ORing two bits takes the higher, so 1 ORed with 0 is 1. 
         *   Look at the following, where '|' represents OR
         *       0110 | 1001 = 1111
         *       0100 | 0000 = 0100
         *       0111 | 1110 = 1111 
         *       etc;
         *       
         *   This holds all necessary features to merge our data,
         *   keeping present bits, without adding or removing any
         *   
         *   
         *   Let's OR them. b1 | b2 = bR, We'll represent downwards
         *   
         *   b1 = 00000000 00000000 01100000 00000000
         *   b2 = 00000000 00000000 00000000 00001100
         *   ========================================
         *   bR = 00000000 00000000 01100000 00001100
         *   
         *   Now we have our merged data, two bytes side by side, we need 
         *   to ensure that java doesn't convert this back to a byte, else
         *   it would cut off the first 3 bytes. 
         *   
         *   to do this, we cast to a short
         *   
         *   Instead of:
         *   	00000000 00000000 01100000 00001100 -> [00000000]
         *   We have:
         *   	00000000 00000000 01100000 00001100 -> [00000000 00000000]
         *   
         *   we don't care that the last two bytes get cut off. 
         *   
         * 
         */
	}
	
	/**Converts a short primitive into two bytes via shifting and bitmasking. 
	 * 
	 * @param sh_in short primitive
	 * @return byte[] primitive
	 */
	public static byte[] shortToTwoBytes(short sh_in) {
		byte high_bits = (byte)(sh_in>>8 & 0x00FF);
		byte low_bits = (byte)( sh_in & 0x00FF); 
		// shift and AND them with a mask. This will erase anything not on the side we want to keep, and 
		//		keep anything that is on that side. Ie:
		/*
		 * given short "1011100000111110"
		 *  shift right 8 spots
		 *     0011111010111000
		 * AND 0000000011111111
		 * EQU 0000000010111000
		 * Cast to byte, removing upper half
		 * 
		 *     1011100000111110
		 * AND 0000000011111111
		 * EQU 0000000000111110
		 * Cast to byte, removing upper half
		 * 
		 */
		return new byte[] {high_bits, low_bits};
	}
	
	/**
	 * Convert a byte to a binary string representation, with 0's
	 * @param b1 byte
	 * @return String
	 */
	public static String registerToBinString(byte b1) {
		String ret_str = "00000000000000000000000000000000";
		//iterate for length of data type-1 (we start at 0, end at length-1) to get all values
		for (var i=7; i>=0; i--) {
				switch (b1>>i & (byte) 1) {
				// use binary value shifted number of times we've looped (n)
				//		to get the nth bit, and use case to throw it in. 
				  case 1:
					  ret_str += "1";
					  break;
				  case 0:
					  ret_str += "0";
					  break;
			}
		}
		
		return ret_str;
	}
	
	/**
	 * Convert a short to a binary string representation, with 0's
	 * @param s1 short
	 * @return String
	 */
	public static String registerToBinString(short s1) {
		String ret_str = "0000000000000000";
		for (var i=15; i>=0; i--) {
				switch ((s1 & 0xFFFFFFFF)>>i & 1) {
				  case 1:
					  ret_str += "1";
					  break;
				  case 0:
					  ret_str += "0";
					  break;
			}
		}
		
		return ret_str;
	}
	
	/**
	 * Convert an int to a binary string representation, with 0's
	 * @param i1 int
	 * @return String
	 */
	public static String registerToBinString(int i1) {
		String ret_str = "";
		for (var i=31; i>=0; i--) {
				switch ((i1 & 0xFFFFFFFF)>>i & 1) {
				  case 1:
					  ret_str += "1";
					  break;
				  case 0:
					  ret_str += "0";
					  break;
			}
		}
		
		return ret_str;
	}
	
	/**
	 * Convert a byte to a hex string representation
	 * @param b1 byte
	 * @return String
	 */
	public static String registerToHexString (byte b1) {
		String ret_val = Integer.toHexString((int) b1 & 0xff);
		return (ret_val);
	}
	
	/**
	 * Convert a short to a hex string representation
	 * @param s1 short
	 * @return String
	 */
	public static String registerToHexString (short s1) {
		String ret_val = Integer.toHexString((int) s1 & 0xffff);
		return (ret_val);
	}
	
	/**
	 * Convert an int to a hex string representation
	 * @param i1 int
	 * @return String
	 */
	public static String registerToHexString (int i1) {
		String ret_val = Integer.toHexString((int) i1 & 0xffffffff);
		return (ret_val);
	}
	// Implement the registers of a 32-bit cpu
	
}
