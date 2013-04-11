package mapreduce.sdk;

import java.io.IOException;
import java.io.OutputStream;

public class TextOutputFormat<K,V> extends FileOutputFormat<K, V> {

	@Override
	public void write(K key, V value, OutputStream out) throws IOException {
		String str=key+" : "+value+"\n";
		out.write(str.getBytes());
		
	}


}
