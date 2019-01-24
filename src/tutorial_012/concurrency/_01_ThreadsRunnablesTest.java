package tutorial_012.concurrency;

import java.util.concurrent.TimeUnit;

public class _01_ThreadsRunnablesTest {

	public static void main(String[] args) {
		/*
		 * All modern operating systems support concurrency both via processes and threads. Processes are instances 
		 * of programs which typically run independently to each other, e.g. if you start a java program the operating 
		 * system spawns a new process which runs in parallel to other programs. Inside those processes we can utilize 
		 * threads to execute code concurrently, so we can make the most out of the available cores of the CPU.
		 * 
		 * Before starting a new thread you have to specify the code to be executed by this thread, often called the task. 
		 * This is done by implementing Runnable - a functional interface defining a single void no-args method run() :
		 */
		Runnable task = () -> {
		    String threadName = Thread.currentThread().getName();
		    System.out.println("Hello " + threadName);
		};

		task.run(); // Call task's run() method, that display "Hello main".
		
		Thread thread = new Thread(task);
		thread.start(); // Starting the thread cause the task's run() method call, that display "Hello Thread-0".

		System.out.println("Done!");
		
		// Used just for well separate the display of differents examples.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("=====================================");
		
		/*
		 * First we execute the runnable directly on the main thread before starting a new thread. The result on the console
		 *  might look like this :
		 *  "
		 	Hello main
			Hello Thread-0
			Done!
		 *  "
		 *  
		 *  or this :
		 *  
		 *  "
		 	Hello main
			Done!
			Hello Thread-0
  
		 *  "
		 *  
		 *  Due to concurrent execution we cannot predict if the runnable will be invoked before or after printing 'done'. The order 
		 *  is non-deterministic, thus making concurrent programming a complex task in larger applications.
		 *  
		 *  Threads can be put to sleep for a certain duration :
		 */
		Runnable runnable = () -> {
		    try {
		        String name = Thread.currentThread().getName();
		        System.out.println("Foo " + name);
		        TimeUnit.SECONDS.sleep(1);
		        System.out.println("Bar " + name);
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		};
		
		Thread thread2 = new Thread(runnable);
		thread2.start();

		/*
		 * When you run the above code you'll notice the one second delay between the first and the second print statement. TimeUnit is a 
		 * useful enum for working with units of time. Alternatively you can achieve the same by calling Thread.sleep(1000).
		 * 
		 * Working with the Thread class can be very tedious and error-prone. Due to that reason the Concurrency API has been introduced in 
		 * 2004 with the release of Java 5. The API is located in package java.util.concurrent and contains many useful classes for handling 
		 * concurrent programming. Since that time the Concurrency API has been enhanced with every new Java release and even Java 8 provides 
		 * new classes and methods for dealing with concurrency.
		 */
	}

}
