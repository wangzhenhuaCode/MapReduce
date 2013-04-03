package mapreduce.sdk;

import java.io.IOException;
import java.util.Iterator;

public interface Reducer<K2, V2, K3, V3> {
	public  void reduce(K2 key,
            Iterator<V2> values,
            OutputCollector<K3,V3> output,
            Reporter reporter)
            throws IOException;
}
