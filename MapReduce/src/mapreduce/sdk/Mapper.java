package mapreduce.sdk;

import java.io.IOException;


public interface Mapper<K1, V1, K2 extends Writable<K2>, V2> {
	public void map(K1 key, V1 value,
	         OutputCollector<K2,V2> output,
	         Reporter reporter)
	         throws IOException;
}
