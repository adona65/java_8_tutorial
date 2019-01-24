package tutorial_013.atomicity;

import static tutorial_013.atomicity.ConcurrentUtils.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

public class _03_LongAccumulatorTest {
	public static void main(String[] args) {
		/*
		 * LongAccumulator is a more generalized version of LongAdder. Instead of performing simple add operations the class 
		 * LongAccumulator builds around a lambda expression of type LongBinaryOperator :
		 */
		LongBinaryOperator op = (x, y) -> 2 * x + y;
		LongAccumulator accumulator = new LongAccumulator(op, 1L);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 10)
		    .forEach(i -> executor.submit(() -> accumulator.accumulate(i)));

		stop(executor);

		System.out.println(accumulator.getThenReset());     // => 2539
		
		/*
		 * We create a LongAccumulator with the function 2 * x + y and an initial value of one. With every call to accumulate(i) 
		 * both the current result and the value i are passed as parameters to the lambda expression.
		 * 
		 * A LongAccumulator just like LongAdder maintains a set of variables internally to reduce contention over threads.
		 */
	}
}
