package mapreduce.sdk;

import java.io.IOException;

public interface InputFormat<K,V>
 {

	public RecordReader<K,V> getRecordReader(String[] path,JobConf conf, Reporter reporter) throws IOException;
}
