package tutorial_005.builtInFunctionnalInterfaces;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BuiltInFunctionnalInterfacesTest {

	public static void main(String args[]) {
		/*
		 * The java 8 version Some of them are well known from older versions of Java like Comparator or Runnable. 
		 * Those existing interfaces are extended to enable Lambda support via the @FunctionalInterface annotation.
		 * But the Java 8 API is also full of new functional interfaces. We will test some of them.
		 */
		
		/*
		 * PREDICATES : Predicates are boolean-valued functions of one argument. The interface contains various default 
		 * methods for composing predicates to complex logical terms (and, or, negate)
		 */
		Predicate<String> predicate = (s) -> s.length() > 0;

		System.out.println(predicate.test("foo")); // true
		System.out.println(predicate.negate().test("foo")); // false

		Boolean tutu = null;
		
		Predicate<Boolean> isNull = Objects::isNull;
		System.out.println(isNull.test(tutu)); // true 
		
		Predicate<Boolean> nonNull = Objects::nonNull;
		System.out.println(nonNull.test(tutu)); // false
		tutu = false;
		System.out.println(nonNull.test(tutu)); // true
		

		Predicate<String> isEmpty = String::isEmpty;
		System.out.println(isEmpty.test("")); // true
		System.out.println(isEmpty.test(" ")); // false
		Predicate<String> isNotEmpty = isEmpty.negate();
		System.out.println(isNotEmpty.test("")); // false
		System.out.println(isNotEmpty.test(" ")); // true
		
		System.out.println("=====================================");
		
		/*
		 * FUNCTIONS : Functions accept one argument and produce a result. 
		 * Default methods can be used to chain multiple functions together (compose, andThen).
		 */
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<String, String> backToString = toInteger.andThen(String::valueOf);

		System.out.println(toInteger.apply("123")); // 123
		System.out.println(backToString.apply("123")); // 123
		
		System.out.println("=====================================");
		
		/*
		 * SUPPLIERS : Suppliers produce a result of a given generic type. 
		 * Unlike Functions, Suppliers don't accept arguments.
		 */
		Supplier<Person> personSupplier = Person::new;
		Person person = personSupplier.get();
		System.out.println(person);

		System.out.println("=====================================");
		
		/*
		 * COMPARATOS : Comparators are well known from older versions of Java. 
		 * Java 8 adds various default methods to the interface.
		 */
		Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

		Person p1 = new Person("John", "Doe");
		Person p2 = new Person("Alice", "Wonderland");

		System.out.println(comparator.compare(p1, p2));             // > 0
		System.out.println(comparator.reversed().compare(p1, p2));  // < 0
		
		System.out.println("=====================================");
		
		/*
		 * OPTIONALS : Optionals are not functional interfaces, instead it's a nifty utility to prevent NullPointerException.
		 * Optional is a simple container for a value which may be null or non-null. Think of a method which may return a non-null 
		 * result but sometimes return nothing. Instead of returning null you return an Optional in Java 8. 
		 */
		
		Optional<String> optional = Optional.of("bam");

		System.out.println(optional.isPresent()); // true
		System.out.println(optional.get()); // "bam"
		System.out.println(optional.orElse("fallback")); // "bam"

		optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
	}
}

class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
