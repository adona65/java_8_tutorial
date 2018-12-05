package tutorial_011.advancedStreamsTests._04.streamsAdvancedOperations;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class _01_CollectTest {
	
	static List<Person> persons =
		    Arrays.asList(
		        new Person("Max", 18),
		        new Person("Peter", 23),
		        new Person("Pamela", 23),
		        new Person("David", 12));

	public static void main(String[] args) {
		/*
		 * Among the operations supported by Streams, we will see the more complexe operations,
		 * ie collect, flatMap and reduce.
		 * 
		 * COLLECT : Collect is an extremely useful terminal operation to transform the elements of 
		 * the stream into a different kind of result, e.g. a List, Set or Map. Collect accepts a Collector 
		 * which consists of four different operations: a supplier, an accumulator, a combiner and a finisher. 
		 * 
		 * Java 8 supports various built-in collectors via the Collectors class. So for the most common operations 
		 * you don't have to implement a collector yourself.
		 */
		List<Person> filteredList = persons.stream()
							        .filter(p -> p.name.startsWith("P"))
							        .collect(Collectors.toList());

		System.out.println(filteredList); // [Peter, Pamela]
		
		System.out.println("=====================================");
		
		/*
		 * Need a set instead of list - just use Collectors.toSet().
		 */
		Set<Person> filteredset = persons.stream()
								        .filter(p -> p.name.startsWith("P"))
								        .collect(Collectors.toSet());

		System.out.println(filteredset); // [Peter, Pamela]
		
		System.out.println("=====================================");
		
		/*
		 * The next example groups all persons by age :
		 */
		Map<Integer, List<Person>> personsByAge = persons.stream()
			    										.collect(Collectors.groupingBy(p -> p.age));

		personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
		// age 18: [Max]
		// age 23: [Peter, Pamela]
		// age 12: [David]
		
		System.out.println("=====================================");
		
		/*
		 * Collectors are extremely versatile. You can also create aggregations on the elements of the stream, 
		 * e.g. determining the average age of all persons :
		 */
		Double averageAge = persons.stream()
								.collect(Collectors.averagingInt(p -> p.age));

		System.out.println(averageAge);     // 19.0
		
		System.out.println("=====================================");
		
		/*
		 * If you're interested in more comprehensive statistics, the summarizing collectors return a special built-in 
		 * summary statistics object. So we can simply determine min, max and arithmetic average age of the persons as 
		 * well as the sum and count.
		 */
		IntSummaryStatistics ageSummary =persons.stream()
			        							.collect(Collectors.summarizingInt(p -> p.age));

		System.out.println(ageSummary); // IntSummaryStatistics{count=4, sum=76, min=12, average=19.000000, max=23}
		
		System.out.println("=====================================");
		
		/*
		 * The next example joins all persons into a single string :
		 */
		String phrase = persons.stream()
						    .filter(p -> p.age >= 18)
						    .map(p -> p.name)
						    .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

		System.out.println(phrase); // In Germany Max and Peter and Pamela are of legal age.
		
		System.out.println("=====================================");
		
		/*
		 * In order to transform the stream elements into a map, we have to specify how both the keys and the values should be 
		 * mapped. Keep in mind that the mapped keys must be unique, otherwise an IllegalStateException is thrown. You can optionally 
		 * pass a merge function as an additional parameter to bypass the exception.
		 */
		Map<Integer, String> map = persons.stream()
									    .collect(Collectors.toMap(
											        p -> p.age, // key
											        p -> p.name, // value
											        (name1, name2) -> name1 + ";" + name2 // merge function
											     )
									    );

		System.out.println(map); // {18=Max, 23=Peter;Pamela, 12=David}
		
		System.out.println("=====================================");
		
		/*
		 * Now let's build our own custom collector. We want to transform all persons of the stream into a single string consisting of all 
		 * names in upper letters separated by the | pipe character. In order to achieve this we create a new collector via Collector.of().
		 * We have to pass the four ingredients of a collector: a supplier, an accumulator, a combiner and a finisher.
		 */
		Collector<Person, StringJoiner, String> personNameCollector = Collector.of(
																	        () -> new StringJoiner(" | "), // supplier
																	        (j, p) -> j.add(p.name.toUpperCase()), // accumulator
																	        (j1, j2) -> j1.merge(j2), // combiner
																	        StringJoiner::toString // finisher
																	  );
		/*
		 *  Since strings are immutable, we need a helper class like StringJoiner to let the collector construct our string. 
		 *  The supplier initially constructs such a StringJoiner with the appropriate delimiter. 
		 *  The accumulator is used to add each persons upper-cased name to the StringJoiner. 
		 *  The combiner knows how to merge two StringJoiners into one. 
		 *  The finisher constructs the desired String from the StringJoiner.
		 */

		String names = persons.stream()
							.collect(personNameCollector);

		System.out.println(names);  // MAX | PETER | PAMELA | DAVID
	}

}
