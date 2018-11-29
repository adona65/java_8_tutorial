package tutorial_010.annotations;


public class AnnotationsTest {

	public static void main(String[] args) {
		/*
		 * Annotations in Java 8 are repeatable. Let's dive directly into an example to figure that out.
		 * First, we define a wrapper annotation which holds an array of the actual annotations :
		 * - We define a "Hint" annotation interface annoted with "@Repeatable(Hints.class)".
		 * - We define the wrapper "Hints" annotation interface that use an array of Hint.
		 * 
		 * By doing so, Java 8 enables us to use multiple annotations of the same type by declaring the 
		 * annotation @Repeatable.
		 * 
		 * Now, we can use this Repeatable annotation by two ways :
		 */

		// Variant 1: Using the container annotation (old school).
		/*
			@Hints({@Hint(value = "oldHint1"), @Hint(value = "oldHint2")})
			class Person {}
		*/
		
		// Variant 2: Using repeatable annotations (new school).
		/*
			@Hint("hint1")
			@Hint("hint2")
			class RepeatablePerson {}
		*/

		/*
		 * Using variant 2 the java compiler implicitly sets up the @Hints annotation under the hood. 
		 * That's important for reading annotation informations via reflection. Although we never declared 
		 * the @Hints annotation on the Person class, it's still readable via getAnnotation(Hints.class). 
		 * However, the more convenient method is getAnnotationsByType which grants direct access to all 
		 * annotated @Hint annotations.
		 */
		Hint oldHint = Person.class.getAnnotation(Hint.class);
		System.out.println(oldHint); // null

		Hints oldHint1 = Person.class.getAnnotation(Hints.class);
		System.out.println(oldHint1.value().length); // 2
		for(Hint hint: oldHint1.value()){
			System.out.println(hint.value()); // "oldHint1", "oldHint2".
		}

		Hint[] oldHint2 = Person.class.getAnnotationsByType(Hint.class);
		System.out.println(oldHint2.length); // 2
		for(Hint hint : oldHint2) {
			System.out.println(hint.value()); // "oldHint1", "oldHint2".
		}
		
		System.out.println("=====================================");
		
		/*
		 * Of course, previous example works for the new way :
		 */
		
		Hint repeatableHint = RepeatablePerson.class.getAnnotation(Hint.class);
		System.out.println(repeatableHint); // null

		Hints repeatableHint1 = RepeatablePerson.class.getAnnotation(Hints.class);
		System.out.println(repeatableHint1.value().length); // 2
		for(Hint hint: repeatableHint1.value()){
			System.out.println(hint.value()); // "repeatableHint1", "repeatableHint2".
		}

		Hint[] repeatableHint2 = RepeatablePerson.class.getAnnotationsByType(Hint.class);
		System.out.println(repeatableHint2.length); // 2
		for(Hint hint : repeatableHint2) {
			System.out.println(hint.value()); // "repeatableHint1", "repeatableHint2".
		}
	    
		/*
		 * Furthermore the usage of annotations in Java 8 is expanded to two new targets:
		 */
		
		/*
		 	@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
			@interface MyAnnotation {}
		 */
	}

}


