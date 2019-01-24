package tutorial_012.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class _03_ScheduledExecutors {

	public static void main(String[] args) {
		/*
		 * In order to periodically run common tasks multiple times, we can utilize scheduled thread pools. A ScheduledExecutorService
		 * is capable of scheduling tasks to run either periodically or once after a certain amount of time has elapsed.
		 * 
		 * This code sample schedules a task to run after an initial delay of three seconds :
		 */
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
		ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

		try {
			TimeUnit.MILLISECONDS.sleep(1337);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
		System.out.printf("Remaining Delay: %sms\n", remainingDelay);
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
		
		/*
		 * Scheduling a task produces a specialized future of type ScheduledFuture which - in addition to Future - provides the method getDelay() 
		 * to retrieve the remaining delay. After this delay has elapsed the task will be executed concurrently.
		 * 
		 * In order to schedule tasks to be executed periodically, executors provide the two methods scheduleAtFixedRate() and scheduleWithFixedDelay(). 
		 * The first method is capable of executing tasks with a fixed time rate, for example once every second :
		 */
		ScheduledExecutorService periodicExecutor = Executors.newScheduledThreadPool(1);

		Runnable periodicTask = () -> System.out.println("Periodic Executor Scheduling : " + System.nanoTime());

		int initialDelay = 0;
		int period = 1;
		periodicExecutor.scheduleAtFixedRate(periodicTask, initialDelay, period, TimeUnit.SECONDS);
		
		/*
		 * Additionally this method accepts an initial delay which describes the leading wait time before the task will be executed for the first time.
		 * Careful : keep in mind that scheduleAtFixedRate() doesn't take into account the actual duration of the task. So if you specify a period of one 
		 * second but the task needs 2 seconds to be executed then the thread pool will working to capacity very soon.
		 * 
		 * In that case you should consider using scheduleWithFixedDelay() instead. This method works just like the counterpart described above. The 
		 * difference is that the wait time period applies between the end of a task and the start of the next task. For example :
		 */
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(5000);
			periodicExecutor.shutdownNow();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
		
		ScheduledExecutorService fixedDelayExecutor = Executors.newScheduledThreadPool(1);

		Runnable fixedDelayTask = () -> {
		    try {
		        TimeUnit.SECONDS.sleep(1);
		        System.out.println("Fixed Delay Scheduling: " + System.nanoTime());
		    }
		    catch (InterruptedException e) {
		        System.err.println("task interrupted");
		    }
		};

		fixedDelayExecutor.scheduleWithFixedDelay(fixedDelayTask, 0, 100, TimeUnit.MILLISECONDS);
		
		/*
		 * This example schedules a task with a fixed delay of 100ms between the end of an execution and the start of the next execution. The initial delay 
		 * is zero and the tasks duration is one second. As you can see scheduleWithFixedDelay() is handy if you cannot predict the duration of the scheduled 
		 * tasks.
		 */
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(5000);
			fixedDelayExecutor.shutdownNow();
			Thread.sleep(1000); // Allow displaying of "task interrupted" before next prinln.
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
	}

}
