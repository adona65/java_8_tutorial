package tutorial_008.map;

import java.util.HashMap;
import java.util.Map;

public class MapTest {

	public static void main(String[] args) {
		/*
		 * Maps doesn't support streams. Instead maps now support various new and useful methods 
		 * for doing common tasks. For example :
		 * - utIfAbsent prevents us from writing additional if checks.
		 * - forEach accepts a consumer to perform operations for each value of the map.
		 */
		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < 10; i++) {
			map.putIfAbsent(i, "val" + i);
		}

		map.forEach((key, val) -> System.out.println(val)); // Output each value in the map.
		
		System.out.println("=====================================");
		
		/*
		 * The next example shows how to compute code on the map by utilizing functions :
		 * - computeIfPresent(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)
		 *     If the value for the specified key is present and non-null, attempts to compute a new 
		 *     mapping given the key and its current mapped value.
		 * - computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)
		 *     If the specified key is not already associated with a value (or is mapped to null), attempts 
		 *     to compute its value using the given mapping function and enters it into this map unless null.
		 */
		map.computeIfPresent(3, (num, val) -> val + num);
		System.out.println(map.get(3)); // Ouput "val33".

		map.computeIfPresent(9, (num, val) -> null);
		System.out.println(map.containsKey(9)); // Ouput "false".

		map.computeIfAbsent(23, num -> "val" + num);
		System.out.println(map.containsKey(23)); // Ouput "true".

		map.computeIfAbsent(3, num -> "bam");
		System.out.println(map.get(3)); // Output "val33" because the "3" key is already present, so computeIfAbsent does nothing.
		
		System.out.println("=====================================");
		
		/*
		 * Next, we see how to remove entries for a a given key, only if it's currently mapped to a given value :
		 */
		map.remove(3, "val3");
		System.out.println(map.get(3)); // val33

		map.remove(3, "val33");
		System.out.println(map.get(3)); // null

		System.out.println("=====================================");
		
		/*
		 * Another helpful method (the name is explicit, and works on the keys of the map) :
		 */
		System.out.println(map.getOrDefault(42, "not found"));  // not found
		System.out.println(map.getOrDefault(4, "not found"));  // val4
		
		System.out.println("=====================================");
		
		/*
		 * Merging entries of a map is quite easy. Merge either put the key/value into the map if no entry 
		 * for the key exists, or the merging function will be called to change the existing value.
		 */
		map.merge(9, "valNew9", (value, newValue) -> value.concat(newValue)); // "9" key was removed previously with "map.computeIfPresent(9, (num, val) -> null);".
		System.out.println(map.get(9)); // valNew9
		
		map.merge(9, "concat", (value, newValue) -> value.concat(newValue));
		System.out.println(map.get(9)); // valNew9concat
	}

}
