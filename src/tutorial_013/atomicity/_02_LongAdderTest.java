package tutorial_013.atomicity;

import static tutorial_013.atomicity.ConcurrentUtils.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class _02_LongAdderTest {
	public static void main(String[] args) {
		/*
		 * The class LongAdder can be used as an alternative to AtomicLong to consecutively add values to a number.
		 */
		ExecutorService executor = Executors.newFixedThreadPool(2);

		LongAdder adder = new LongAdder();
		
		IntStream.range(0, 1000)
		    .forEach(i -> executor.submit(adder::increment));

		stop(executor);

		System.out.println(adder.sumThenReset());   // => 1000
		
		/*
		 * LongAdder provides methods add() and increment() just like the atomic number classes and is also thread-safe. But instead of 
		 * summing up a single result this class maintains a set of variables internally to reduce contention over threads. The actual 
		 * result can be retrieved by calling sum() or sumThenReset().
		 * 
		 * This class is usually preferable over atomic numbers when updates from multiple threads are more common than reads. This is often 
		 * the case when capturing statistical data, e.g. you want to count the number of requests served on a web server. The drawback of 
		 * LongAdder is higher memory consumption because a set of variables is held in-memory.
		 */
	}
}
