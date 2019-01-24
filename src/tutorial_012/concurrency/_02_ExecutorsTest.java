package tutorial_012.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class _02_ExecutorsTest {

	public static void main(String[] args) {
		/*
		 * The Concurrency API introduces the concept of an ExecutorService as a higher level replacement for working 
		 * with threads directly. Executors are capable of running asynchronous tasks and typically manage a pool of 
		 * threads, so we don't have to create new threads manually. All threads of the internal pool will be reused 
		 * under the hood for revenant tasks, so we can run as many concurrent tasks as we want throughout the life-cycle 
		 * of our application with a single executor service.
		 * 
		 * This is how the first thread-example of _01_ThreadsRunnablesTest class looks like using executors :
		 */
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
		    String threadName = Thread.currentThread().getName();
		    System.out.println("Hello " + threadName);
		});
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
		
		/*
		 * The class Executors provides convenient factory methods for creating different kinds of executor services. In this 
		 * sample we use an executor with a thread pool of size one.
		 * 
		 * The result looks similar to the _01_ThreadsRunnablesTest class sample but when running the code you'll notice an important 
		 * difference : the java process never stops ! Executors have to be stopped explicitly - otherwise they keep listening for new 
		 * tasks.
		 * 
		 * An ExecutorService provides two methods for that purpose : shutdown() waits for currently running tasks to finish while 
		 * shutdownNow() interrupts all running tasks and shut the executor down immediately.
		 */
		try {
		    System.out.println("attempt to shutdown executor");
		    executor.shutdown();
		    executor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		}
		finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow();
		    System.out.println("shutdown finished");
		}
		
		/*
		 * The executor shuts down softly by waiting a certain amount of time for termination of currently running tasks. After a maximum 
		 * of five seconds the executor finally shuts down by interrupting all running tasks.
		 */
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
		
		/*
		 * CALLABLES and FUTURES : In addition to Runnable, executors support another kind of task named Callable. Callables are functional 
		 * interfaces just like runnables but instead of being void they return a value.
		 */
		Callable<Integer> task = () -> {
		    try {
		        TimeUnit.SECONDS.sleep(1);
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		};
		
		/*
		 * Callables can be submitted to executor services just like runnables. But what about the callables result ? Since submit() doesn't 
		 * wait until the task completes, the executor service cannot return the result of the callable directly. Instead the executor 
		 * returns a special result of type Future which can be used to retrieve the actual result at a later point in time.
		 */
		//ExecutorService executor = Executors.newFixedThreadPool(1);
		ExecutorService executor2 = Executors.newFixedThreadPool(1);
		Future<Integer> future = executor2.submit(task);

		System.out.println("future done? " + future.isDone());

		Integer result = 0;
		try {
			result = future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("future done? " + future.isDone());
		System.out.println("result: " + result);
		
		/*
		 * After submitting the callable to the executor, we first check if the future has already been finished execution via isDone(). It's pretty 
		 * sure this isn't the case since the above callable sleeps for one second before returning the integer.
		 * 
		 * Calling the method get() blocks the current thread and waits until the callable completes before returning the actual result 123. Now the 
		 * future is finally done and we see the following result.
		 * 
		 * Futures are tightly coupled to the underlying executor service. Keep in mind that every non-terminated future will throw exceptions if you 
		 * shutdown the executor.
		 * 
		 * Also, we use newFixedThreadPool(1) to create an executor service backed by a thread-pool of size one. This is equivalent to newSingleThreadExecutor() 
		 * but we could later increase the pool size by simply passing a value larger than one.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * TIMEOUTS : Any call to future.get() will block and wait until the underlying callable has been terminated. In the worst case a callable runs 
		 * forever - thus making your application unresponsive. You can simply counteract those scenarios by passing a timeout :
		 */
		Future<Integer> timeoutedFuture = executor2.submit(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(2);
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		});

		try {
			timeoutedFuture.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.out.println("Reached timeout.");
			e.printStackTrace();
		}
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("=====================================");
		
		/*
		 * INVOKEALL : Executors support batch submitting of multiple callables at once via invokeAll(). This method accepts a collection of callables 
		 * and returns a list of futures. In this example we utilize streams in order to process all futures returned by the invocation of invokeAll.
		 */
		ExecutorService batchExecutor = Executors.newWorkStealingPool();

		List<Callable<String>> callables = Arrays.asList(
		        () -> "task1",
		        () -> "task2",
		        () -> "task3");

		try {
			batchExecutor.invokeAll(callables)
					    .stream()
					    .map(futur -> {
					        try {
					            return futur.get();
					        }
					        catch (Exception e) {
					            throw new IllegalStateException(e);
					        }
					    })
					    .forEach(System.out::println);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * The above example uses yet another type of executor created via newWorkStealingPool(). This factory method is part of Java 8 and returns 
		 * an executor of type ForkJoinPool which works slightly different than normal executors. Instead of using a fixed size thread-pool ForkJoinPools
		 * are created for a given parallelism size which per default is the number of available cores of the hosts CPU.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * INVOKEANY : Another way of batch-submitting callables is the method invokeAny() which works slightly different to invokeAll(). Instead of 
		 * returning future objects this method blocks until the first callable terminates and returns the result of that callable.
		 * 
		 * In order to test this behavior we use the helper callable() method (see below) to simulate callables with different durations. 
		 */
		ExecutorService invokeAnyExecutor = Executors.newWorkStealingPool();

		List<Callable<String>> invokeAnyCallables = Arrays.asList(
		    callable("task1", 2),
		    callable("task2", 1),
		    callable("task3", 3));

		String invokeAnyResult = "";
		try {
			invokeAnyResult = invokeAnyExecutor.invokeAny(invokeAnyCallables);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(invokeAnyResult); // => task2
	}
	
	/**
	 * Returns a callable that sleeps for a certain amount of time until returning the given result.
	 */
	static Callable<String> callable(String result, long sleepSeconds) {
	    return () -> {
	        TimeUnit.SECONDS.sleep(sleepSeconds);
	        return result;
	    };
	}

}
