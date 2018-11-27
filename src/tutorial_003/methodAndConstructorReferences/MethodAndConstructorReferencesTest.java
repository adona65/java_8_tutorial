package tutorial_003.methodAndConstructorReferences;

public class MethodAndConstructorReferencesTest {
	
	public static void main(String args[]) {
		/*
		 * Java 8 enables you to pass references of methods or constructors via the :: keyword. 
		 * For example, the two following lines do exactly the same thing by utilizing 
		 * static method references :
		 */
		Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
		Converter<String, Integer> converterReferences = Integer::valueOf;
		
		System.out.println(converter.convert("123")); // Output "123".
		System.out.println(converterReferences.convert("123")); // Output "123".
		
		/*
		 * We can also reference object methods :
		 */
		Something something = new Something();
		Converter<String, String> converterObject = something::startsWith;
		System.out.println(converterObject.convert("Java")); // "J"
		
		/*
		 * The :: keyword also works for constructors. For illustrate this, we will use a bean of "Person" type with different 
		 * constructors that will be created by the PersonFactory interface. Instead of implementing the factory manually, 
		 * we glue everything together via constructor references. We create a reference to the Person constructor via Person::new. 
		 * The Java compiler automatically chooses the right constructor by matching the signature of PersonFactory.create.
		 */
		PersonFactory<Person> personFactory = Person::new;
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person.firstName + " " + person.lastName); // Output "Peter Parker".
	}
}

class Something {
    String startsWith(String s) {
        return String.valueOf(s.charAt(0));
    }
}

class Person {
    String firstName;
    String lastName;

    Person() {}

    Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

interface PersonFactory<P extends Person> {
    P create(String firstName, String lastName);
}