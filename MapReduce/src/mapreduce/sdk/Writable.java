package mapreduce.sdk;

import java.io.Serializable;

public interface Writable<T> extends Comparable<T>, Serializable{
	public T getValue();
	public void setValue(T value);
}
