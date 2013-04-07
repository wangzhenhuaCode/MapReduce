package mapreduce.sdk;

import java.io.IOException;

public interface OutputCollector<K , V> {
	public void collect(K key,V value)throws IOException;
}
