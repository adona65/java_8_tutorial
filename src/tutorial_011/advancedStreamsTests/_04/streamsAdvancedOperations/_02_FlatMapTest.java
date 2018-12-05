package tutorial_011.advancedStreamsTests._04.streamsAdvancedOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class _02_FlatMapTest {

	public static void main(String[] args) {
		/*
		 * FLATMAP : We transform the objects of a stream into another type of objects by utilizing the map 
		 * operation. Map is kinda limited because every object can only be mapped to exactly one other object. 
		 * But what if we want to transform one object into multiple others or none at all? This is where flatMap
		 * is useful.
		 * 
		 * FlatMap transforms each element of the stream into a stream of other objects. So each object will be transformed 
		 * into zero, one or multiple other objects backed by streams. The contents of those streams will then be placed into 
		 * the returned stream of the flatMap operation.
		 */
		List<Foo> foos = new ArrayList<>();

		// create foos
		IntStream
		    .range(1, 4)
		    .forEach(i -> foos.add(new Foo("Foo" + i)));

		// create bars
		foos.forEach(f ->
		    IntStream
		        .range(1, 4)
		        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
		
		/*
		 * Now we have a list of three foos each consisting of three bars. FlatMap accepts a function which has to return a stream 
		 * of objects. So in order to resolve the bar objects of each foo, we just pass the appropriate function :
		 */
		foos.stream()
		    .flatMap(f -> f.bars.stream())
		    .forEach(b -> System.out.println(b.name));
		
		System.out.println("=====================================");
		
		/*
		 * We have transformed the stream of three foo objects into a stream of nine bar objects.
		 * 
		 * The above code example can be simplified into a single pipeline of stream operations :
		 */
		IntStream.range(1, 4)
			    .mapToObj(i -> new Foo("Foo" + i))
			    .peek(f -> IntStream.range(1, 4)
							        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
							        .forEach(f.bars::add)
			     )
			    .flatMap(f -> f.bars.stream())
			    .forEach(b -> System.out.println(b.name));
		
		System.out.println("=====================================");
		
		/*
		 * FlatMap is also available for the Optional class introduced in Java 8. Optionals flatMap operation returns an optional object 
		 * of another type. So it can be utilized to prevent nasty null checks.
		 * 
		 * Think of a highly hierarchical structure like this :
		 */
		class Inner {
		    String foo;
		}
		
		class Nested {
		    Inner inner;
		}
		
		class Outer {
		    Nested nested;
		}
		
		/*
		 * In order to resolve the inner string foo of an outer instance you have to add multiple null checks to prevent possible 
		 * NullPointerExceptions :
		 */
		Outer outer = new Outer();
		
		if (outer != null && outer.nested != null && outer.nested.inner != null) {
		    System.out.println(outer.nested.inner.foo);
		} else {
			System.out.println("No inner's foo.");
		}

		/*
		 * The same behavior can be obtained by utilizing optionals flatMap operation :
		 */
		Optional.of(new Outer())
			    .flatMap(o -> Optional.ofNullable(o.nested))
			    .flatMap(n -> Optional.ofNullable(n.inner))
			    .flatMap(i -> Optional.ofNullable(i.foo))
			    .ifPresent(System.out::println);

		/*
		 * Each call to flatMap returns an Optional wrapping the desired object if present or null if absent.
		 */
	}

}
