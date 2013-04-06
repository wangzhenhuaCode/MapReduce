package mapreduce.sdk;

import java.io.IOException;

public interface OutputCollector<K extends Comparable<K>, V> {
	public void collect(K key,V value)throws IOException;
}
