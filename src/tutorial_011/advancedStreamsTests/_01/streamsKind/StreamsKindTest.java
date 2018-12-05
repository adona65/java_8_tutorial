package tutorial_011.advancedStreamsTests._01.streamsKind;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsKindTest {

	public static void main(String[] args) {
		/*
		 * Most stream operations accept some kind of lambda expression parameter, a functional 
		 * interface specifying the exact behavior of the operation. Most of those operations must 
		 * be both non-interfering and stateless.
		 * 
		 * A function is non-interfering when it does not modify the underlying data source of the 
		 * stream, e.g. no used lambda expression does modify a list by adding or removing elements 
		 * from the collection on which stream is working.
		 * 
		 * A function is stateless when the execution of the operation is deterministic, e.g. no used 
		 * lambda expression depends on any mutable variables or states from the outer scope which might 
		 * change during execution.
		 * 
		 * Streams can be created from various data sources, especially collections. Lists and Sets support 
		 * new methods stream() and parallelStream() to either create a sequential or a parallel stream. 
		 * Parallel streams are capable of operating on multiple threads.
		 */
		
		Arrays.asList("a1", "a2", "a3")
		    .stream()
		    .findFirst()
		    .ifPresent(System.out::println);  // a1
		
		System.out.println("=====================================");
		
		/*
		 * Calling the method stream() on a list of objects returns a regular object stream. But we don't have to create 
		 * collections in order to work with streams. Just use Stream.of() to create a stream from a bunch of object 
		 * references :
		 */
		Stream.of("toto", "titi", "tutu")
		    .findFirst()
		    .ifPresent(System.out::println);  // toto
		
		System.out.println("=====================================");
		
		/*
		 * Besides regular object streams Java 8 ships with special kinds of streams for working with the primitive data 
		 * types int, long and double : IntStream, LongStream and DoubleStream.
		 * 
		 * IntStreams can replace the regular for-loop utilizing IntStream.range() :
		 */
		IntStream.range(1, 4)
			.forEach(System.out::println); // "1", "2", "3".
		
		/*
		 * All those primitive streams work just like regular object streams with the following differences : 
		 * - Primitive streams use specialized lambda expressions, e.g. IntFunction instead of Function or 
		 * 		IntPredicate instead of Predicate.
		 * - Primitive streams support the additional terminal aggregate operations sum() and average():
		 */
		Arrays.stream(new int[] {1, 2, 3})
		    .map(n -> 2 * n + 1)
		    .average()
		    .ifPresent(System.out::println);  // 5.0
		
		System.out.println("=====================================");
		
		/*
		 * Sometimes it's useful to transform a regular object stream to a primitive stream or vice versa. For that purpose 
		 * object streams support the special mapping operations mapToInt(), mapToLong() and mapToDouble :
		 */
		Stream.of("a1", "a2", "a3")
		    .map(s -> s.substring(1))
		    .mapToInt(Integer::parseInt)
		    .max()
		    .ifPresent(System.out::println);  // 3
		
		/*
		 * Primitive streams can be transformed to object streams via mapToObj() :
		 */
		IntStream.range(1, 4)
		    .mapToObj(i -> "a" + i)
		    .forEach(System.out::println); // "a1", "a2", "a3".

		/*
		 * Here's a combined example : the stream of doubles is first mapped to an int stream and than mapped to an object stream 
		 * of strings :
		 */
		Stream.of(1.0, 2.0, 3.0)
		    .mapToInt(Double::intValue)
		    .mapToObj(i -> "a" + i)
		    .forEach(System.out::println); // "a1", "a2", "a3".
	}
}
