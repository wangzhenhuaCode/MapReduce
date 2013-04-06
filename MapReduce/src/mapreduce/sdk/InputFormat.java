package mapreduce.sdk;

import java.io.IOException;

public interface InputFormat<K,V>
 {
	public InputSplit[] getSplit(JobConf conf, String[] path);
	public RecordReader<K,V> getRecordReader(InputSplit input,JobConf conf, Reporter reporter) throws IOException;
}
