package tutorial_011.advancedStreamsTests._04.streamsAdvancedOperations;

import java.util.Arrays;
import java.util.List;

public class _03_ReduceTest {

	static List<Person> persons =
		    Arrays.asList(
		        new Person("Max", 18),
		        new Person("Peter", 23),
		        new Person("Pamela", 23),
		        new Person("David", 12));
	
	public static void main(String[] args) {
		/*
		 * REDUCE : The reduction operation combines all elements of the stream into a single result. Java 8 s
		 * upports three different kind of reduce methods. The first one reduces a stream of elements to exactly 
		 * one element of the stream :
		 */
		persons.stream()
		    .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
		    .ifPresent(System.out::println); // Pamela

		System.out.println("=====================================");
		
		/*
		 * The reduce method accepts a BinaryOperator accumulator function. That's actually a BiFunction where both 
		 * operands share the same type, in that case Person. BiFunctions are like Function but accept two arguments.
		 * 
		 * The second reduce method accepts both an identity value and a BinaryOperator accumulator. This method can be
		 *  utilized to construct a new Person with the aggregated names and ages from all other persons in the stream :
		 */
		Person result = persons.stream()
					        .reduce(new Person("", 0), (p1, p2) -> {
					            p1.age += p2.age;
					            p1.name += p2.name;
					            return p1;
					        });

		System.out.format("name=%s; age=%s \n", result.name, result.age); // name=MaxPeterPamelaDavid; age=76
		
		System.out.println("=====================================");
		
		/*
		 * The third reduce method accepts three parameters : 
		 * - An identity value
		 * - A BiFunction accumulator
		 * - A combiner function of type BinaryOperator. 
		 * 
		 * Since the identity values type is not restricted to the Person type, we can utilize this reduction to determine the 
		 * sum of ages from all persons :
		 */
		Integer ageSum = persons.stream()
			    			.reduce(0, (sum, p) -> sum += p.age, (sum1, sum2) -> sum1 + sum2);

		System.out.println(ageSum);  // 76
		
		/*
		 * What's happening exactly under the hood ? Let's extend the above code by some debug output :
		 */
		@SuppressWarnings("unused")
		Integer ageSumDebug = persons.stream()
								    .reduce(0,
								        (sum, p) -> {
								            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
								            return sum += p.age;
								        },
								        (sum1, sum2) -> {
								            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
								            return sum1 + sum2;
								        }
								     );
									// accumulator: sum=0; person=Max
									// accumulator: sum=18; person=Peter
									// accumulator: sum=41; person=Pamela
									// accumulator: sum=64; person=David
		
		System.out.println("=====================================");
		
		/*
		 * The accumulator function does all the work. It first get called with the initial identity value 0 and the first person Max. 
		 * In the next three steps sum continually increases by the age of the last steps person up to a total age of 76.
		 * 
		 * So, what is the point of the combiner ? Executing the same stream in parallel will lift the secret :
		 */
		@SuppressWarnings("unused")
		Integer ageSumParallelized = persons.parallelStream()
										    .reduce(0,
										        (sum, p) -> {
										            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
										            return sum += p.age;
										        },
										        (sum1, sum2) -> {
										            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
										            return sum1 + sum2;
										        }
										     );
											// accumulator: sum=0; person=Pamela
											// accumulator: sum=0; person=David
											// accumulator: sum=0; person=Max
											// accumulator: sum=0; person=Peter
											// combiner: sum1=18; sum2=23
											// combiner: sum1=23; sum2=12
											// combiner: sum1=41; sum2=35
		
		/*
		 * Executing this stream in parallel results in an entirely different execution behavior. Now the combiner is actually called. 
		 * Since the accumulator is called in parallel, the combiner is needed to sum up the separate accumulated values.
		 */
	}

}
