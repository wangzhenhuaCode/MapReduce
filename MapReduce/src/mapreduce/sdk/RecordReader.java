package mapreduce.sdk;

import java.io.IOException;
import java.util.Map;

public interface RecordReader<K, V> {
	 public Map<K, V> generateKeyValue() throws IOException;
	
}
