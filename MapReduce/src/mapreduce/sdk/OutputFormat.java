package mapreduce.sdk;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputFormat<K,V> {

	public void write(K key, V value, OutputStream out) throws IOException;
}
