package tutorial_013.atomicity;

import static tutorial_013.atomicity.ConcurrentUtils.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class _01_AtomicIntegerTest {
	public static void main(String[] args) {
		/*
		 * Atomic Variables and Concurrent Maps have been greatly improved with the introduction of lambda expressions and 
		 * functional programming in Java 8 release.
		 * 
		 * The package java.concurrent.atomic contains many useful classes to perform atomic operations. An operation is atomic 
		 * when you can safely perform the operation in parallel on multiple threads without using the synchronized keyword or 
		 * locks. Internally, the atomic classes make heavy use of compare-and-swap (CAS), an atomic instruction directly supported 
		 * by most modern CPUs (wikipedia link to CAS : https://en.wikipedia.org/wiki/Compare-and-swap). Those instructions usually 
		 * are much faster than synchronizing via locks. So it's better to prefer atomic classes over locks in case you just have to 
		 * change a single mutable variable concurrently.
		 * 
		 * Let's use the atomic classes AtomicInteger for few examples purposes.
		 */
		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
		    .forEach(i -> executor.submit(atomicInt::incrementAndGet));

		stop(executor);

		System.out.println(atomicInt.get()); // => 1000
		
		/*
		 * By using AtomicInteger as a replacement for Integer, we are able to increment the number concurrently in a thread-safe manor 
		 * without synchronizing the access to the variable. The method incrementAndGet() is an atomic operation so we can safely call 
		 * this method from multiple threads.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * AtomicInteger supports various kinds of atomic operations. The method updateAndGet() accepts a lambda expression in order to 
		 * perform arbitrary arithmetic operations upon the integer :
		 */
		AtomicInteger atomicInt2 = new AtomicInteger(0);

		ExecutorService executor2 = Executors.newFixedThreadPool(2);

		Runnable task2 = () -> atomicInt2.updateAndGet(n -> n + 2);
		
		IntStream.range(0, 1000)
		    .forEach(i -> executor2.submit(task2));

		stop(executor2);

		System.out.println(atomicInt2.get()); // => 2000
		
		System.out.println("=====================================");
		
		/*
		 * The method accumulateAndGet() accepts another kind of lambda expression of type IntBinaryOperator. We use this method to sum up all 
		 * values from 0 to 1000 concurrently in the next sample :
		 */
		AtomicInteger atomicInt3 = new AtomicInteger(0);

		ExecutorService executor3 = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
	    .forEach(i -> {
	    	// We don't have to reinstantiate the task for each loop. We do it here only for getting an easy-to-read example.
	        Runnable task = () -> atomicInt3.accumulateAndGet(i, (n, m) -> n + m);
	        executor3.submit(task);
	    });

		stop(executor3);

		System.out.println(atomicInt3.get()); // => 499500
		
		/*
		 * Other useful atomic classes are AtomicBoolean, AtomicLong and AtomicReference.
		 */
	}
}
