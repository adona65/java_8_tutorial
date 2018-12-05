package tutorial_011.advancedStreamsTests._03.reusingStreams;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReusingStreamsTest {

	public static void main(String[] args) {
		/*
		 * Java 8 streams cannot be reused. As soon as you call any terminal operation the stream is closed.
		 */

		Stream<String> stream =
			    Stream.of("d2", "a2", "b1", "b3", "c");
			        //.filter(s -> s.startsWith("a"));

		stream.anyMatch(s -> { 
			System.out.println(s);
			return s.equals("b3");	
		}); // Works.
		
		try {
			stream.noneMatch(s -> true);   // Throw an exception.
		} catch(IllegalStateException e) {
			System.out.println("Stream already terminated => " + e.getMessage());
			e.printStackTrace();
		}
		
		
		/*
		 * To overcome this limitation we have to to create a new stream chain for every terminal operation we want 
		 * to execute, e.g. we could create a stream supplier to construct a new stream with all intermediate operations 
		 * already set up :
		 */
		
		System.out.println("=====================================");
		
		Supplier<Stream<String>> streamSupplier = ( 
			    () -> Stream.of("d2", "a2", "b1", "b3", "c")
			            .filter(s -> s.startsWith("a"))
		);
		
		streamSupplier.get().anyMatch(s -> { 
			System.out.println(s);
			return s.equals("a2");	
		}); // Works.
		
		System.out.println("=====================================");
		
		streamSupplier.get().noneMatch(s -> { 
			System.out.println(s);
			return s.equals("b1");	
		}); // Works.
		
		/*
		 * Each call to get() constructs a new stream on which we are save to call the desired terminal operation.
		 */
	}

}
