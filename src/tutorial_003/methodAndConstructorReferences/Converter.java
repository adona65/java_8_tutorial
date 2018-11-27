package tutorial_003.methodAndConstructorReferences;

@FunctionalInterface
public interface Converter<F, T> {
	T convert(F from);
}
