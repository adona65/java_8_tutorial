package tutorial_012.concurrency;

import static tutorial_012.concurrency.ConcurrentUtils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class _05_LocksTest {
	
	static ReentrantLock reentrantLock = new ReentrantLock();
	static int reentrantCount = 0;

	public static void main(String[] args) {
		/*
		 * Instead of using implicit locking via the synchronized keyword, the Concurrency API supports various explicit 
		 * locks specified by the Lock interface. Locks support various methods for finer grained lock control thus are 
		 * more expressive than implicit monitors. Multiple lock implementations are available in the standard JDK.
		 * 
		 * REENTRANT LOCK : The class ReentrantLock is a mutual exclusion lock with the same basic behavior as the implicit 
		 * monitors accessed via the synchronized keyword but with extended capabilities. As the name suggests, this lock 
		 * implements reentrant characteristics just as implicit monitors.
		 * 
		 * Look at reentrantIncrement() : a lock is acquired via lock() and released via unlock(). It's important to wrap your 
		 * code into a try/finally block to ensure unlocking in case of exceptions. This method is thread-safe just like the 
		 * synchronized counterpart. If another thread has already acquired the lock, subsequent calls to lock() pause the current 
		 * thread until the lock has been unlocked. Only one thread can hold the lock at any given time.
		 * 
		 * Locks support various methods for fine grained control as seen in the next sample :
		 */
		ExecutorService reentrantExecutor = Executors.newFixedThreadPool(2);
		ReentrantLock lock = new ReentrantLock();

		reentrantExecutor.submit(() -> {
		    lock.lock();
		    try {
		        sleep(1);
		    } finally {
		        lock.unlock();
		    }
		});

		reentrantExecutor.submit(() -> {
		    System.out.println("Locked: " + lock.isLocked());
		    System.out.println("Held by me: " + lock.isHeldByCurrentThread());
		    boolean locked = lock.tryLock();
		    System.out.println("Lock acquired: " + locked);
		});

		stop(reentrantExecutor);
		
		/*
		 * While the first task holds the lock for one second, the second task obtains different informations about the current state 
		 * of the lock.
		 * 
		 * The method tryLock() is an alternative to lock() that tries to acquire the lock without pausing the current thread. The boolean 
		 * result must be used to check if the lock has actually been acquired before accessing any shared mutable variables.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * READWRITELOCK : The interface ReadWriteLock specifies another type of lock maintaining a pair of locks for read and write access. 
		 * The idea behind read-write locks is that it's usually safe to read mutable variables concurrently as long as nobody is writing to 
		 * this variable. So the read-lock can be held simultaneously by multiple threads as long as no threads hold the write-lock. This can 
		 * improve performance and throughput in case that reads are more frequent than writes.
		 */
		ExecutorService readWriteExecutor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		
		readWriteExecutor.submit(() -> {
			readWriteLock.writeLock().lock();
		    try {
		        sleep(1);
		        System.out.println("Write to map.");
		        map.put("foo", "bar");
		        System.out.println("Writing finished.");
		    } finally {
		    	readWriteLock.writeLock().unlock();
		    }
		});
		
		Runnable readTask = () -> {
			readWriteLock.readLock().lock();
		    try {
		        System.out.println(map.get("foo"));
		        sleep(1);
		        System.out.println("Sleeping finished.");
		    } finally {
		    	readWriteLock.readLock().unlock();
		    }
		};

		readWriteExecutor.submit(readTask);
		readWriteExecutor.submit(readTask);

		stop(readWriteExecutor);
		
		/*
		 * In the above example, a first task acquires a write-lock in order to put a new value to the map after sleeping for one second. 
		 * Before this task has finished, two other tasks are being submitted trying to read the entry from the map then sleep for two seconds.
		 * 
		 * When you execute this code sample, you'll notice that both read tasks have to wait the whole second until the write task has finished. 
		 * After the write lock has been released, both read tasks are executed in parallel and print the result simultaneously to the console. 
		 * They don't have to wait for each other to finish because read-locks can safely be acquired concurrently as long as no write-lock is held 
		 * by another thread.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * STAMPEDLOCK : Java 8 ships with a new kind of lock called StampedLock, which also support read and write locks just like in the above example.
		 * In contrast to ReadWriteLock, the locking methods of a StampedLock return a stamp represented by a long value. You can use these stamps to 
		 * either release a lock or to check if the lock is still valid. Additionally, stamped locks support another lock mode called optimistic locking.
		 * 
		 * Let's rewrite the last example code to use StampedLock instead of ReadWriteLock :
		 */
		
		ExecutorService stampedExecutor = Executors.newFixedThreadPool(2);
		Map<String, String> stampedMap = new HashMap<>();
		StampedLock stampedLock = new StampedLock();

		stampedExecutor.submit(() -> {
		    long stamp = stampedLock.writeLock();
		    try {
		        sleep(1);
		        System.out.println("Write to map.");
		        stampedMap.put("foo", "bar");
		        System.out.println("Writing finished, stamp => " + stamp);
		    } finally {
		    	stampedLock.unlockWrite(stamp);
		    }
		});

		Runnable stampedReadTask = () -> {
		    long stamp = stampedLock.readLock();
		    try {
		        System.out.println(stampedMap.get("foo"));
		        sleep(1);
		        System.out.println("Sleeping finished, stamp => " + stamp);
		    } finally {
		    	stampedLock.unlockRead(stamp);
		    }
		};

		stampedExecutor.submit(stampedReadTask);
		stampedExecutor.submit(stampedReadTask);

		stop(stampedExecutor);
		
		/*
		 * Obtaining a read or write lock via readLock() or writeLock() returns a stamp which is later used for unlocking within the finally 
		 * block. Keep in mind that stamped locks don't implement reentrant characteristics. Each call to lock returns a new stamp and blocks 
		 * if no lock is available even if the same thread already holds a lock. So you have to pay particular attention not to run into deadlocks.
		 * 
		 * Just like in the previous ReadWriteLock example, both read tasks have to wait until the write lock has been released. Then both read tasks 
		 * print to the console simultaneously because multiple reads doesn't block each other as long as no write-lock is held.
		 */
		
		System.out.println("=====================================");
		
		/*
		 * The next example demonstrates optimistic locking : 
		 */
		ExecutorService optimisticExecutor = Executors.newFixedThreadPool(2);
		StampedLock optimisticLock = new StampedLock();

		optimisticExecutor.submit(() -> {
		    long stamp = optimisticLock.tryOptimisticRead();
		    try {
		        System.out.println("Optimistic Lock Valid: " + optimisticLock.validate(stamp));
		        sleep(1);
		        System.out.println("Optimistic Lock Valid: " + optimisticLock.validate(stamp));
		        sleep(2);
		        System.out.println("Optimistic Lock Valid: " + optimisticLock.validate(stamp));
		    } finally {
		    	optimisticLock.unlock(stamp);
		    }
		});

		optimisticExecutor.submit(() -> {
		    long stamp = optimisticLock.writeLock();
		    try {
		        System.out.println("Write Lock acquired");
		        sleep(2);
		    } finally {
		    	optimisticLock.unlock(stamp);
		        System.out.println("Write done");
		    }
		});

		stop(optimisticExecutor);

		/*
		 * An optimistic read lock is acquired by calling tryOptimisticRead(), which always returns a stamp without blocking the current 
		 * thread, no matter if the lock is actually available. If there's already a write lock active, the returned stamp equals zero. 
		 * You can always check if a stamp is valid by calling lock.validate(stamp).
		 * 
		 * Executing the above code results in the following output :
		 * "
			Optimistic Lock Valid: true
			Write Lock acquired
			Optimistic Lock Valid: false
			Write done
			Optimistic Lock Valid: false
		   "
		 * The optimistic lock is valid right after acquiring the lock. In contrast to normal read locks, an optimistic lock doesn't prevent 
		 * other threads to obtain a write lock instantaneously. After sending the first thread to sleep for one second, the second thread obtains 
		 * a write lock without waiting for the optimistic read lock to be released. From this point, the optimistic read lock is no longer valid. 
		 * Even when the write lock is released the optimistic read locks stays invalid.
		 * 
		 * So when working with optimistic locks, you have to validate the lock every time after accessing any shared mutable variable to make sure 
		 * the read was still valid.
		 * 
		 * Sometimes it's useful to convert a read lock into a write lock without unlocking and locking again. StampedLock provides the method 
		 * tryConvertToWriteLock() for that purpose as seen in the next sample :
		 */
		ExecutorService changeLockExecutor = Executors.newFixedThreadPool(2);
		StampedLock changeLock = new StampedLock();

		changeLockExecutor.submit(() -> {
			int count = 0;
		    long stamp = changeLock.readLock();
		    try {
		        if (count == 0) {
		            stamp = changeLock.tryConvertToWriteLock(stamp);
		            if (stamp == 0L) {
		                System.out.println("Could not convert to write lock");
		                stamp = changeLock.writeLock();
		            }
		            count = 23;
		        }
		        System.out.println(count);
		    } finally {
		    	changeLock.unlock(stamp);
		    }
		});

		stop(changeLockExecutor);
		
		/*
		 * The task first obtains a read lock. If the current valueof count is zero we want to assign a new value of 23. We first have to convert the 
		 * read lock into a write lock to not break potential concurrent access by other threads. Calling tryConvertToWriteLock() doesn't block but 
		 * may return a zero stamp indicating that no write lock is currently available. In that case we call writeLock() to block the current thread 
		 * until a write lock is available.
		 */
	}

	static void reentrantIncrement() {
		reentrantLock.lock();
	    try {
	    	reentrantCount++;
	    } finally {
	    	reentrantLock.unlock();
	    }
	}

}
