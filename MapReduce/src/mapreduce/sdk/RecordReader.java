package mapreduce.sdk;

import java.io.IOException;
import java.util.Map;

public interface RecordReader<K, V> {
	 public boolean next(K key,V value) throws IOException;
	 public K createKey();
	 public V createValue();
	
}
