package tutorial_012.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static tutorial_012.concurrency.ConcurrentUtils.*;

public class _04_SynchronizedTest {

	static int count = 0;
	static int synchronizedCount = 0;
	
	public static void main(String[] args) {
		/*
		 * When writing multi-threaded code you have to pay particular attention when accessing shared mutable variables 
		 * concurrently from multiple threads. Let's just say we want to increment an integer which is accessible simultaneously 
		 * from multiple threads. When calling the method that increment this integer concurrently from multiple threads we're in 
		 * serious trouble :
		 */
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		Callable<Integer> incrementer = () -> {
			increment();
			return count;
		};

		IntStream.range(0, 10_000)
		    .forEach(i -> executor.submit(incrementer));

		stop(executor);

		System.out.println(count);  // A result different each time we call the code.
		
		/*
		 * Instead of seeing a constant result count of 10_000, the actual result varies with every execution of the above code. The reason 
		 * is that we share a mutable variable upon different threads without synchronizing the access to this variable which results in a 
		 * race condition.
		 * 
		 * Three steps have to be performed in order to increment the number : 
		 * - (i) read the current value
		 * - (ii) increase this value by one
		 * - (iii) write the new value to the variable. 
		 * 
		 * If two threads perform these steps in parallel it's possible that both threads perform step 1 simultaneously thus reading the same current 
		 * value. This results in lost writes so the actual result is lower.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * Java supports thread-synchronization since the early days via the synchronized keyword. We can utilize synchronized to fix the above race 
		 * conditions when incrementing the count :
		 */
		ExecutorService synchronizedExecutor = Executors.newFixedThreadPool(2);

		Callable<Integer> synchronizedIncrementer = () -> {
			synchronizedIncrement();
			return synchronizedCount;
		};
		
		IntStream.range(0, 10000)
		    .forEach(i -> synchronizedExecutor.submit(synchronizedIncrementer));

		stop(synchronizedExecutor);

		System.out.println(synchronizedCount);  // 10_000
		
		/*
		 * When using synchronizedIncrement() concurrently we get the desired result count of 10_000. No race conditions occur and the result is stable 
		 * with every execution of the code. 
		 * 
		 * In non static context, the synchronized keyword is also available as a block statement :
		 * "
		 	static void synchronizedIncrement() {
			 	synchronized (this) {
					synchronizedCount = synchronizedCount + 1;
				}
			}
		 * "
		 * 
		 * Internally, Java uses a so called monitor also known as monitor lock or intrinsic lock in order to manage synchronization. This monitor is 
		 * bound to an object, e.g. when using synchronized methods each method share the same monitor of the corresponding object.
		 * 
		 * All implicit monitors implement the reentrant characteristics. Reentrant means that locks are bound to the current thread. A thread can 
		 * safely acquire the same lock multiple times without running into deadlocks (e.g. a synchronized method calls another synchronized method 
		 * on the same object).
		 */
	}
	
	static void increment() {
	    count = count + 1;
	}
	
	synchronized static void synchronizedIncrement() {
		synchronizedCount = synchronizedCount + 1;
	}


}
