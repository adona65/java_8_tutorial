package tutorial_013.atomicity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class _05_ConcurrentHashMapTest {
	public static void main(String[] args) {
		/*
		 * ConcurrentHashMap is an important implementation of the ConcurrentMap interface.It has been enhanced with a couple 
		 * of new methods to perform parallel operations upon the map. Just like parallel streams, those methods use a special 
		 * ForkJoinPool available via ForkJoinPool.commonPool() in Java 8. This pool uses a preset parallelism which depends 
		 * on the number of available cores :
		 */
		System.out.println(ForkJoinPool.getCommonPoolParallelism());  // Indicate the numer of parallelism.
		
		/*
		 * This value can be decreased or increased by setting the following JVM parameter : 
		 * -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
		 */
		
		System.out.println("=====================================");
		
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.put("r2", "d2");
		map.put("c3", "p0");
		
		/*
		 * Java 8 introduces three kinds of parallel operations : forEach, search and reduce. Each of those operations are available 
		 * in four forms accepting functions with keys, values, entries and key-value pair arguments. All of those methods use a common 
		 * first argument called parallelismThreshold. This threshold indicates the minimum collection size when the operation should be 
		 * executed in parallel. E.g. if you pass a threshold of 500 and the actual size of the map is 499 the operation will be performed 
		 * sequentially on a single thread. In the next examples we use a threshold of one to always force parallel execution for demonstrating 
		 * purposes.
		 * 
		 * FOREACH : The method forEach() is capable of iterating over the key-value pairs of the map in parallel. The lambda expression of 
		 * type BiConsumer is called with the key and value of the current iteration step. In order to visualize parallel execution we print 
		 * the current threads name to the console.
		 */
		
		// 1 = parallelismThreshold
		// The output will depend's on the thread's name and the size of possible parallelism actions (depends on the machine's CPU cores).
		map.forEach(1, (key, value) -> System.out.printf("key: %s; value: %s; thread: %s\n", key, value, Thread.currentThread().getName()));
		
		System.out.println("=====================================");
		
		/*
		 * SEARCH : The method search() accepts a BiFunction returning a non-null search result for the current key-value pair or null if the 
		 * current iteration doesn't match the desired search criteria. As soon as a non-null result is returned, further processing is 
		 * suppressed. Keep in mind that ConcurrentHashMap is unordered. The search function should not depend on the actual processing order 
		 * of the map. If multiple entries of the map match the given search function the result may be non-deterministic.
		 */
		String result = map.search(1, (key, value) -> {
			// Here too the output will depend's on the thread's name and the size of possible parallelism actions (depends on the machine's 
			// CPU cores).
		    System.out.println(Thread.currentThread().getName());
		    if ("foo".equals(key)) {
		        return value;
		    }
		    return null;
		});
		System.out.println("Result: " + result);
		
		System.out.println("=====================================");
		
		/*
		 * Here's another example searching solely on the values of the map :
		 */
		String result2 = map.searchValues(1, value -> {
		    System.out.println(Thread.currentThread().getName());
		    if (value.length() > 3) {
		        return value;
		    }
		    return null;
		});

		System.out.println("Result: " + result2);
		
		System.out.println("=====================================");
		
		/*
		 * REDUCE : The method reduce() (already known from Java 8 Streams) accepts two lambda expressions of type BiFunction. The first 
		 * function transforms each key-value pair into a single value of any type. The second function combines all those transformed 
		 * values into a single result, ignoring any possible null values.
		 */
		String result3 = map.reduce(1,
			    (key, value) -> {
			        System.out.println("Transform: " + Thread.currentThread().getName());
			        return key + "=" + value;
			    },
			    (s1, s2) -> {
			        System.out.println("Reduce: " + Thread.currentThread().getName());
			        return s1 + ", " + s2;
			    });

		System.out.println("Result: " + result3);
	}
}
