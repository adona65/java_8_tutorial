package tutorial_001.defaultMethods;

import tutorial_001.defaultMethods.Formula;

/*
 * Java 8 enables us to add non-abstract method implementations to interfaces by utilizing the default keyword. 
 * This feature is also known as Extension Methods. 
 */

/*
 * Formula interface own two methods : 
 * - calculate() as an abstract method.
 * - sqrt() as a default method.
 * So, concrete class aren't forced to implement this method. Only the abstracts ones.
 */
class ImplementedFormula implements Formula {
    @Override
    public double calculate(int a) {
        return sqrt(a * 100);
    }
};

public class DefaultMethodsTest {
	
	public static void main(String args[]) {
		ImplementedFormula formulaTest = new ImplementedFormula();
		// Call the implemented method from ImplementedFormula class.
		System.out.println(formulaTest.calculate(100)); // Output "100.0".
		// Call the default method from Formula interface.
		System.out.println(formulaTest.sqrt(16)); // Output "4.0".
	}
}
