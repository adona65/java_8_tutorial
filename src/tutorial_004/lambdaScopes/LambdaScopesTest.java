package tutorial_004.lambdaScopes;

public class LambdaScopesTest {

	public static void main(String args[]) {
		/*
		 * Accessing outer scope variables from lambda expressions is very similar to anonymous objects. 
		 * You can access final variables from the local outer scope as well as instance fields and static variables.
		 * 
		 * We can read final local variables from the outer scope of lambda expressions:
		 */
		final int finalNum = 1;
		Converter<Integer, String> stringConverterFinal = (from) -> String.valueOf(from + finalNum);

		System.out.println(stringConverterFinal.convert(2)); // Output "3".
		
		/*
		 * But different to anonymous objects the variable num does not have to be declared final. 
		 * This code is also valid :
		 */
		final int num = 1;
		Converter<Integer, String> stringConverter = (from) -> String.valueOf(from + num);

		System.out.println(stringConverter.convert(2)); // Output "3".
		
		/*
		 * BEWARE : However num must be implicitly final for the code to compile. The following code does not compile :
		 */
		// int nonFinalNum = 1;
		// Converter<Integer, String> tutu = (from) -> String.valueOf(from + nonFinalNum);
		// nonFinalNum = 3;
		
		/*
		 * In contrast to local variables we have both read and write access to instance fields and static variables 
		 * from within lambda expressions. This behaviour is well known from anonymous objects :
		 */
		Lambda4 lambda = new Lambda4();
		lambda.testScopes();
		
		/*
		 * BEWARE : Interface Formula defines a default method sqrt which can be accessed from each formula instance including anonymous objects. 
		 * This does not work with lambda expressions. Default methods cannot be accessed from within lambda expressions. 
		 * The following code does not compile:
		 */
		//Formula formula = (a) -> sqrt( a * 100);
	}
}

class Lambda4 {
    static int outerStaticNum;
    int outerNum;

    public void testScopes() {
    	System.out.println("outerStaticNum : " + outerStaticNum);
    	System.out.println("outerNum : " + outerNum);
    	
        Converter<Integer, String> stringConverter1 = (from) -> {
            outerNum = 23;
            System.out.println("outerNum inside lambda : " + outerNum);
            return String.valueOf(from);
        };

        System.out.println("Call stringConverter1 : " + stringConverter1.convert(1));
        System.out.println("outerNum : " + outerNum);
        
        Converter<Integer, String> stringConverter2 = (from) -> {
            outerStaticNum = 72;
            System.out.println("outerStaticNum inside lambda : " + outerStaticNum);
            return String.valueOf(from);
        };
        
        System.out.println("Call stringConverter2 : " + stringConverter2.convert(2));
        System.out.println("outerStaticNum : " + outerStaticNum);
    }
}
