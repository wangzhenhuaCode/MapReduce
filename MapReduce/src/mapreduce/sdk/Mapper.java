package mapreduce.sdk;

import java.io.IOException;


/**
 * Interface for map class
 *
 * @param <K1> input key class
 * @param <V1> input value class
 * @param <K2> output key class in the mapping process
 * @param <V2> output value class in the mapping process
 */
public interface Mapper<K1, V1, K2 extends Writable, V2> {
	/**
	 * Map method that will be invoked in the map method
	 * @param key input key
	 * @param value input value
	 * @param output collector for store the map result.
	 * @param reporter reporter for log
	 * @throws IOException
	 */
	public void map(K1 key, V1 value,
	         OutputCollector<K2,V2> output,
	         Reporter reporter)
	         throws IOException;
}
