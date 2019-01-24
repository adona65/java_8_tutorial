package tutorial_014.variousChanges;

public class _03_MathTest {
	public static void main(String[] args) {
		/*
		 * The utility class Math has been enhanced by a couple of new methods for handling number overflows. What does that mean ? 
		 * We've already seen that all number types have a maximum value. So what happens when the result of an arithmetic operation 
		 * doesn't fit into this size? 
		 */
		System.out.println(Integer.MAX_VALUE);      // 2147483647
		System.out.println(Integer.MAX_VALUE + 1);  // -2147483648
		
		// Used only for well display of outputs.
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		/*
		 * As you can see a so called integer overflow happens which is normally not the desired behavior. Java 8 adds support for 
		 * strict math to handle this problem. Math has been extended by a couple of methods who all ends with exact, e.g. addExact. 
		 * Those methods handle overflows properly by throwing an ArithmeticException when the result of the operation doesn't fit 
		 * into the number type :
		 */
		try {
		    Math.addExact(Integer.MAX_VALUE, 1);
		}
		catch (ArithmeticException e) {
		    System.err.println(e.getMessage()); // => integer overflow
		}

		/*
		 * The same exception might be thrown when trying to convert longs to int via toIntExact :
		 */
		try {
		    Math.toIntExact(Long.MAX_VALUE);
		}
		catch (ArithmeticException e) {
		    System.err.println(e.getMessage()); // => integer overflow
		}

	}
}
