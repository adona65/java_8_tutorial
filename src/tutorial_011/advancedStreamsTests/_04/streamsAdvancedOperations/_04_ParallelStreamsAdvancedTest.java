package tutorial_011.advancedStreamsTests._04.streamsAdvancedOperations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class _04_ParallelStreamsAdvancedTest {

	static List<Person> persons = Arrays.asList(
		    new Person("Max", 18),
		    new Person("Peter", 23),
		    new Person("Pamela", 23),
		    new Person("David", 12));
	
	public static void main(String[] args) {
		/*
		 * Streams can be executed in parallel to increase runtime performance on large amount of input elements. 
		 * Parallel streams use a common ForkJoinPool available via the static ForkJoinPool.commonPool() method. 
		 * The size of the underlying thread-pool depends on the amount of available physical CPU cores :
		 */
		ForkJoinPool commonPool = ForkJoinPool.commonPool();
		System.out.println(commonPool.getParallelism()); // 7

		System.out.println("=====================================");
		
		/*
		 * This value can be decreased or increased by setting the following JVM parameter :
		 * -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
		 * 
		 * Collections support the method parallelStream() to create a parallel stream of elements. Alternatively 
		 * you can call the intermediate method parallel() on a given stream to convert a sequential stream to a 
		 * parallel counterpart.
		 * 
		 * In order to understand the parallel execution behavior of a parallel stream the next example prints information 
		 * about the current thread :
		 */
		Arrays.asList("a1", "a2", "b1", "c2", "c1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n",
		        s, Thread.currentThread().getName())
		    );
		
		System.out.println("=====================================");
		
		/*
		 * By investigating the debug output we should get a better understanding which threads are actually used to execute the 
		 * stream operations :
		 * "
		 	filter: b1 [main]
			map: b1 [main]
			forEach: B1 [main]
			filter: c1 [ForkJoinPool.commonPool-worker-2]
			map: c1 [ForkJoinPool.commonPool-worker-2]
			filter: c2 [ForkJoinPool.commonPool-worker-4]
			filter: a2 [ForkJoinPool.commonPool-worker-1]
			map: a2 [ForkJoinPool.commonPool-worker-1]
			filter: a1 [ForkJoinPool.commonPool-worker-3]
			map: a1 [ForkJoinPool.commonPool-worker-3]
			forEach: A1 [ForkJoinPool.commonPool-worker-3]
			forEach: A2 [ForkJoinPool.commonPool-worker-1]
			map: c2 [ForkJoinPool.commonPool-worker-4]
			forEach: C1 [ForkJoinPool.commonPool-worker-2]
			forEach: C2 [ForkJoinPool.commonPool-worker-4]
		 * "
		 * 
		 * The parallel stream use all available threads from the common ForkJoinPool for executing the stream operations. The output
		 *  may differ in consecutive runs because the behavior of which particular thread is actually used is non-deterministic.
		 *  
		 *  Let's extend the example by an additional stream operation, sort :
		 */
		Arrays.asList("a1", "a2", "b1", "c2", "c1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .sorted((s1, s2) -> {
		        System.out.format("sort: %s <> %s [%s]\n",
		            s1, s2, Thread.currentThread().getName());
		        return s1.compareTo(s2);
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n",
		        s, Thread.currentThread().getName())
		    );
		
		System.out.println("=====================================");
		
		/*
		 * this output something like :
		 * "
		 	filter: c2 [ForkJoinPool.commonPool-worker-3]
			filter: c1 [ForkJoinPool.commonPool-worker-4]
			filter: b1 [main]
			map: b1 [main]
			filter: a2 [ForkJoinPool.commonPool-worker-1]
			map: a2 [ForkJoinPool.commonPool-worker-1]
			filter: a1 [ForkJoinPool.commonPool-worker-2]
			map: a1 [ForkJoinPool.commonPool-worker-2]
			map: c1 [ForkJoinPool.commonPool-worker-4]
			map: c2 [ForkJoinPool.commonPool-worker-3]
			sort: A2 <> A1 [main]
			sort: B1 <> A2 [main]
			sort: C2 <> B1 [main]
			sort: C1 <> C2 [main]
			sort: C1 <> B1 [main]
			sort: C1 <> C2 [main]
			forEach: B1 [main]
			forEach: C1 [ForkJoinPool.commonPool-worker-5]
			forEach: C2 [ForkJoinPool.commonPool-worker-3]
			forEach: A1 [ForkJoinPool.commonPool-worker-2]
			forEach: A2 [ForkJoinPool.commonPool-worker-6]
		 * "
		 * 
		 * It seems that sort is executed sequentially on the main thread only. Actually, sort on a parallel stream uses the new 
		 * Java 8 method Arrays.parallelSort() under the hood. As stated in Javadoc, this method decides depending on the length 
		 * of the array if sorting will be performed sequentially or in parallel : If the length of the specified array is less 
		 * than the minimum granularity, then it is sorted using the appropriate Arrays.sort method.
		 * 
		 * Coming back to a reduce example : we know that the combiner function is only called in parallel but not in sequential streams. 
		 * Let's see which threads are actually involved :
		 */
		persons.parallelStream()
		    .reduce(0,
		        (sum, p) -> {
		            System.out.format("accumulator: sum=%s; person=%s [%s]\n",
		                sum, p, Thread.currentThread().getName());
		            return sum += p.age;
		        },
		        (sum1, sum2) -> {
		            System.out.format("combiner: sum1=%s; sum2=%s [%s]\n",
		                sum1, sum2, Thread.currentThread().getName());
		            return sum1 + sum2;
		        }
		    );

		/*
		 * The console output reveals that both the accumulator and the combiner functions are executed in parallel on all available threads :
		 * "
		 	accumulator: sum=0; person=David [ForkJoinPool.commonPool-worker-2]
			accumulator: sum=0; person=Pamela [main]
			accumulator: sum=0; person=Peter [ForkJoinPool.commonPool-worker-6]
			accumulator: sum=0; person=Max [ForkJoinPool.commonPool-worker-3]
			combiner: sum1=23; sum2=12 [main]
			combiner: sum1=18; sum2=23 [ForkJoinPool.commonPool-worker-3]
			combiner: sum1=41; sum2=35 [ForkJoinPool.commonPool-worker-3]
		 * "
		 * 
		 * In summary, it can be stated that parallel streams can bring a nice performance boost to streams with a large amount of input elements. 
		 * But keep in mind that some parallel stream operations like reduce and collect need additional computations (combine operations) which 
		 * isn't needed when executed sequentially.
		 * 
		 * Furthermore be careful : as we saw that all parallel stream operations share the same JVM-wide common ForkJoinPool, we need avoid 
		 * implementing slow blocking stream operations since that could potentially slow down other parts of our applications which rely heavily 
		 * on parallel streams.
		 */
	}

}
