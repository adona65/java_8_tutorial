package tutorial_014.variousChanges;

public class _02_NumbersTest {
	public static void main(String[] args) {
		/*
		 * Java 8 adds additional support for working with unsigned numbers. Numbers in Java had always been signed. Let's look at 
		 * Integer for example. An int represents a maximum of 2³² binary digits. Numbers in Java are per default signed, so the last 
		 * binary digit represents the sign (0 = positive, 1 = negative). Thus the maximum positive signed int is 2³¹ - 1 starting with 
		 * the decimal zero. You can access this value via Integer.MAX_VALUE :
		 */
		System.out.println(Integer.MAX_VALUE);      // 2147483647
		System.out.println(Integer.MAX_VALUE + 1);  // -2147483648
		
		System.out.println("=====================================");
		
		/*
		 * Java 8 adds support for parsing unsigned ints.
		 */
		long maxUnsignedInt = (1l << 32) - 1;
		String string = String.valueOf(maxUnsignedInt);
		System.out.println(string); // 4294967295
		
		int unsignedInt = Integer.parseUnsignedInt(string, 10);
		String string2 = Integer.toUnsignedString(unsignedInt, 10);
		System.out.println(string2); // 4294967295
		
		/*
		 * As you can see it's now possible to parse the maximum possible unsigned number 2³² - 1 into an integer. And you can also convert 
		 * this number back into a string representing the unsigned number.
		 * 
		 * This wasn't possible before with parseInt as this example demonstrates :
		 */
		try {
		    Integer.parseInt(string, 10);
		}
		catch (NumberFormatException e) {
			// The number is not parseable as a signed int because it exceeds the maximum of 2³¹ - 1.
		    System.err.println("Could not parse signed int of " + maxUnsignedInt);
		}

	}
}
