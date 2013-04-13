package mapreduce.sdk;

import java.io.IOException;

/**
 * Interface for Collector. It will collect the result in the map reduce.
 *
 * @param <K>
 * @param <V>
 */
public interface OutputCollector<K , V> {
	/**
	 * Collect the result
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void collect(K key,V value)throws IOException;
}
