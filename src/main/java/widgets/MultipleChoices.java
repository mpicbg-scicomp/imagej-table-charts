package widgets;

import java.util.Set;

/**
 * @author Matthais Arzt
 */
public interface MultipleChoices<T> {

	void setChoices(Iterable<T> choices, PrettyPrinter<T> prettyPrinter);

	boolean get(T value);

	Set<T> get();

	interface PrettyPrinter<T> {
		String toString(T value);
	}

}
