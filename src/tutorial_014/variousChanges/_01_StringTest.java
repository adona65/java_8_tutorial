package tutorial_014.variousChanges;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _01_StringTest {
	public static void main(String[] args) {
		/*
		 * The most important changes in Java 8 are things like lambda expressions and functional streams. But many existing classes have 
		 * been enhanced in the JDK 8 API with useful features and methods. We will see examples of it in this package's class.
		 */
		
		/*
		 * Two new methods are available on the String class :join and chars. 
		 * 
		 * JOIN : Join any number of strings into a single string with the given delimiter :
		 */
		System.out.println(String.join(":", "foobar", "foo", "bar")); // => foobar:foo:bar

		System.out.println("=====================================");
		
		/*
		 * CHARS : Creates a stream for all characters of the string, so you can use stream operations upon those characters :
		 */
		System.out.println(
			"foobar:foo:bar"
			    .chars()
			    .distinct()
			    .mapToObj(c -> String.valueOf((char)c))
			    .sorted()
			    .collect(Collectors.joining())
		); // => :abfor
	
		System.out.println("=====================================");
		
		/*
		 * Not only strings but also regex patterns now benefit from streams. Instead of splitting strings into streams for each character, 
		 * we can split strings for any pattern and create a stream to work upon :
		 */
		System.out.println(
			Pattern.compile(":")
			    .splitAsStream("foobar:foo:bar")
			    .filter(s -> s.contains("bar"))
			    .sorted()
			    .collect(Collectors.joining(":"))
		); // => bar:foobar
		
		System.out.println("=====================================");
		
		/*
		 * Additionally, regex patterns can be converted into predicates. Those predicates can for example be used to filter a stream of strings :
		 */
		Pattern pattern = Pattern.compile(".*@gmail\\.com");
		System.out.println(
			Stream.of("bob@gmail.com", "alice@hotmail.com")
			    .filter(pattern.asPredicate())
			    .count()
		); // => 1	
	}
}
