package tutorial_010.annotations;

import java.lang.annotation.Repeatable;

@Repeatable(value = Hints.class)
public @interface Hint {
	String value();
}
