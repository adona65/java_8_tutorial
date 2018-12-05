package tutorial_011.advancedStreamsTests._02.processingOrder;

import java.util.stream.Stream;

public class ProcessingOrderTest {

	public static void main(String[] args) {
		/*
		 * Let's dive deeper into how stream operations are processed under the hood. An important 
		 * characteristic of intermediate operations is laziness. Look at this sample where a terminal
		 *  operation is missing :
		 */
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return true;
		    }); // Output nothing.
		
		/*
		 * When executing this code snippet, nothing is printed. That's because intermediate operations will 
		 * be executed only when a terminal operation is present. Let's extend the above example :
		 */
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return true;
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		
		/*
		 * Executing this code snippet results in the desired output on the console :
		 * "
		 	filter:  d2
			forEach: d2
			filter:  a2
			forEach: a2
			filter:  b1
			forEach: b1
			filter:  b3
			forEach: b3
			filter:  c
			forEach: c
		 * "
		 * 
		 * The order of the result might be surprising. A naive approach would be to execute the operations horizontally 
		 * one after another on all elements of the stream. But instead each element moves along the chain vertically. The
		 * first string "d2" passes filter then forEach, only then the second string "a2" is processed, and so on.
		 * 
		 * This behavior can reduce the actual number of operations performed on each element :
		 */
		System.out.println("=====================================");
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .anyMatch(s -> {
		        System.out.println("anyMatch: " + s);
		        return s.startsWith("A");
		    });
		
		/*
		 * It output :
		 * "
		 	 map:      d2
			 anyMatch: D2
			 map:      a2
			 anyMatch: A2
		 * " 
		 * 
		 * The operation anyMatch returns true as soon as the predicate applies to the given input element. This is true for the 
		 * second element passed "A2". Due to the vertical execution of the stream chain, map has only to be executed twice in this case. 
		 * So instead of mapping all remaining elements of the stream, map will be called as few as possible.
		 */
		System.out.println("=====================================");

		/*
		 * Order also matter for stream processing order. For example :
		 */
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("A");
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		
		/*
		 * This output :
		 * "
		 	 map:     d2
			 filter:  D2
			 map:     a2
			 filter:  A2
			 forEach: A2
			 map:     b1
			 filter:  B1
			 map:     b3
			 filter:  B3
			 map:     c
			 filter:  C
		 * "
		 * 
		 * Both map and filter are called five times for every string in the underlying collection whereas forEach is only called once.
		 * We can greatly reduce the actual number of executions if we change the order of the operations, moving filter to the beginning 
		 * of the chain :
		 */
		System.out.println("=====================================");
		
		Stream.of("d2", "a2", "b1", "b3", "c")
	    .filter(s -> {
	        System.out.println("filter: " + s);
	        return s.startsWith("a");
	    })
	    .map(s -> {
	        System.out.println("map: " + s);
	        return s.toUpperCase();
	    })
	    .forEach(s -> System.out.println("forEach: " + s));

		/*
		 * This output :
		 * "
		 	 filter:  d2
			 filter:  a2
			 map:     a2
			 forEach: A2
			 filter:  b1
			 filter:  b3
			 filter:  c
		 * "
		 * 
		 * Now, map is only called once so the operation pipeline performs much faster for larger numbers of input elements. Keep that in mind 
		 * when composing complex method chains.
		 * 
		 * Let's extend the above example by an additional operation, sorted :
		 */
		System.out.println("=====================================");
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .sorted((s1, s2) -> {
		        System.out.printf("sort: %s; %s\n", s1, s2);
		        return s1.compareTo(s2);
		    })
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("a");
		    })
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		
		/*
		 * This output :
		 * "
		 	sort:    a2; d2
			sort:    b1; a2
			sort:    b1; d2
			sort:    b1; a2
			sort:    b3; b1
			sort:    b3; d2
			sort:    c; b3
			sort:    c; d2
			filter:  a2
			map:     a2
			forEach: A2
			filter:  b1
			filter:  b3
			filter:  c
			filter:  d2
		 * "
		 * 
		 * Sorting is a special kind of intermediate operation. It's a so called stateful operation since in order to sort a 
		 * collection of elements you have to maintain state during ordering.
		 * 
		 * First, the sort operation is executed on the entire input collection. In other words sorted is executed horizontally. 
		 * So in this case sorted is called eight times for multiple combinations on every element in the input collection.
		 * 
		 * Once again we can optimize the performance by reordering the chain:
		 */
		System.out.println("=====================================");
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("a");
		    })
		    .sorted((s1, s2) -> {
		        System.out.printf("sort: %s; %s\n", s1, s2);
		        return s1.compareTo(s2);
		    })
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.println("forEach: " + s));

		/*
		 * This output :
		 * "
			filter:  d2
			filter:  a2
			filter:  b1
			filter:  b3
			filter:  c
			map:     a2
			forEach: A2 
		 * "
		 * 
		 * In this example sorted is never been called because filter reduces the input collection to just one element. So the performance 
		 * is greatly increased for larger input collections.
		 */
	}
}
