package mapreduce.sdk;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface for output format class
 *
 * @param <K>  class for output key
 * @param <V>  class for output value
 */
public interface OutputFormat<K,V> {

	/**
	 * write the key, value pair to the output stream
	 * @param key
	 * @param value
	 * @param out
	 * @throws IOException
	 */
	public void write(K key, V value, OutputStream out) throws IOException;
}
