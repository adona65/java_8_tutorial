package tutorial_007.parallelStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ParallelStreamsTest {

	public static void main(String[] args) {
		/*
		 * Streams can be either sequential or parallel. Operations on sequential streams are 
		 * performed on a single thread while operations on parallel streams are performed concurrent 
		 * on multiple threads.
		 * 
		 * The following example demonstrates how easy it is to increase the performance by using parallel 
		 * streams by measuring the time it takes to sort a stream of this collection. 
		 */
		int max = 1_000_000;
		List<String> intList = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
		    UUID uuid = UUID.randomUUID();
		    intList.add(uuid.toString());
		}

		/*
		 * Firstly we try with basic stream :
		 */
		long t0 = System.nanoTime();

		long count = intList.stream().sorted().count();
		System.out.println(count);

		long t1 = System.nanoTime();

		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("Sequential sort took: %d ms", millis));

		/*
		 * Then we try with parallel stream :
		 */
		
		long t2 = System.nanoTime();

		long parallelCount = intList.parallelStream().sorted().count();
		System.out.println(parallelCount);

		long t3 = System.nanoTime();

		long parallelMillis = TimeUnit.NANOSECONDS.toMillis(t3 - t2);
		System.out.println(String.format("Parallel sort took: %d ms", parallelMillis));
		
		/*
		 * As you can see both code snippets are almost identical but the parallel sort is roughly 50% faster. 
		 * All you have to do is change stream() to parallelStream().
		 */
	}
	
}
