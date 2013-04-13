package mapreduce.sdk;

import java.io.IOException;
import java.util.Iterator;

/**
 * Interface for reduce class and combine class
 *
 * @param <K2> output key class in the mapping process
 * @param <V2> output value class in the mapping process
 * @param <K3> final output key class
 * @param <V3> final output value class
 */
public interface Reducer<K2, V2, K3 extends Writable, V3> {
	/**
	 * method which will be invoked in the reduce and combine process
	 * @param key key of the mapping output
	 * @param values list of values which have the same key in the mapping output
	 * @param output collector for store the result.
	 * @param reporter reporter to log the information.
	 * @throws IOException
	 */
	public  void reduce(K2 key,
            Iterator<V2> values,
            OutputCollector<K3,V3> output,
            Reporter reporter)
            throws IOException;
}
