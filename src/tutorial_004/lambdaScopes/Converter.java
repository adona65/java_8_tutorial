package tutorial_004.lambdaScopes;

@FunctionalInterface
public interface Converter<F, T> {
	T convert(F from);
}
