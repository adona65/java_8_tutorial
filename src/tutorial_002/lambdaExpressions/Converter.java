package tutorial_002.lambdaExpressions;

@FunctionalInterface
public interface Converter<F, T> {
	T convert(F from);
}
