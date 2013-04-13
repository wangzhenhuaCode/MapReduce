package mapreduce.sdk;

import java.io.IOException;
import java.util.Map;

/**
 * Interface for get the key value pairs in the mapping.
 *
 * @param <K> class for input key
 * @param <V> class for input value
 */
public interface RecordReader<K, V> {
	 /**
	  * Get the next key value pair from the input data. This method will set the value in the key and value variable.
	 * @param key
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public boolean next(K key,V value) throws IOException;
	 /**
	  * create a new key instance
	 * @return
	 */
	public K createKey();
	 /**
	  * create a new value instance
	 * @return
	 */
	public V createValue();
	
}
