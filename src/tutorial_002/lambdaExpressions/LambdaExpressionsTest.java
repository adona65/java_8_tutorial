package tutorial_002.lambdaExpressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tutorial_002.lambdaExpressions.Converter;

public class LambdaExpressionsTest {

	public static void main(String args[]) {
		// Let's start with a simple example of how to sort a list of strings in prior versions of Java :
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

		Collections.sort(names, new Comparator<String>() {
		    @Override
		    public int compare(String a, String b) {
		        return b.compareTo(a);
		    }
		});
		
		System.out.println(names); // Output "[xenia, peter, mike, anna]".
		
		/*
		 * The static utility method Collections.sort accepts a list and a comparator in order to sort the elements of the given list. 
		 * You often find yourself creating anonymous comparators and pass them to the sort method. Instead of it, we may now use
		 * lambda expressions.
		 */
		List<String> names4Lambda = Arrays.asList("peter", "anna", "mike", "xenia");
		
		Collections.sort(names4Lambda, (String a, String b) -> {
		    return b.compareTo(a);
		});
		
		System.out.println(names4Lambda); // Output "[xenia, peter, mike, anna]", as for previous example.
		
		
		// This sorting may be performed in a shorter way :
		Collections.sort(names4Lambda, (String a, String b) -> b.compareTo(a));
		
		/*
		 * And even shorted, because for one line method bodies you can skip both 
		 * the braces {} and the return keyword. But it gets even more shorter:
		 */
		Collections.sort(names, (a, b) -> b.compareTo(a));
		
		/*
		 * How does lambda expressions fit into Javas type system? Each lambda corresponds to a given type, 
		 * specified by an interface. A so called functional interface must contain exactly one abstract method 
		 * declaration. Each lambda expression of that type will be matched to this abstract method. Since default 
		 * methods are not abstract you're free to add default methods to your functional interface.
		 * 
		 * We can use arbitrary interfaces as lambda expressions as long as the interface only contains one abstract method. 
		 * To ensure that your interface meet the requirements, you should add the @FunctionalInterface annotation. The compiler 
		 * is aware of this annotation and throws a compiler error as soon as you try to add a second abstract method declaration 
		 * to the interface.
		 * 
		 * Then, when you wan't use this method, you can define it functionment in the lambda expression call.
		 * 
		 * The following example show this :
		 */
		
		/*
		 * The Converter Functionnal interface wait two types, one as input of the lambda expression, one as output.
		 * In the following, we can see that :
		 * - We define the input of the expression will be a String.
		 * - We define the output of the expression will be a String.
		 * - The implementation of the abstract method will be the call to the Integer.valueOf() method, with a
		 * 		variable named "from" as input. This input, as explain before, must be of String type.
		 */
		Converter<String, Integer> converterTest = (from) -> Integer.valueOf(from);
		System.out.println(converterTest.convert("123")); // Output "123".
		/*
		 * The following commented example won't compile, because we defined that the convert()
		 * method of "converterTest" object wait a String as input.
		 */
		// System.out.println(converter.convert(123));
	}
}
