package mapreduce.sdk;

import java.io.Serializable;

public class WrapObject<T> implements Serializable {
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
		return value.equals(obj);
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
	
	
}
