package mapreduce.sdk;

import java.io.Serializable;

public class WrapObject<T extends Comparable<T>> implements Writable<T>{
	private T value;

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return value.equals(((WrapObject)obj).value);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value.toString();
	}

	public WrapObject(T value) {
		super();
		this.value = value;
	}

	public int compareTo(T arg0) {
		// TODO Auto-generated method stub
		return arg0.compareTo(value);
	}
	
	
}
