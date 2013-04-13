package mapreduce.sdk;

import java.io.Serializable;

/**
 * A interface to make class serializable and comparable.
 *
 * @param <T>
 */
public interface Writable<T> extends Comparable<T>, Serializable{
	public T getValue();
	public void setValue(T value);
}
