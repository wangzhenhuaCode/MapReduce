package mapreduce.sdk;

import java.io.IOException;

/**
 * Interface for input format class
 *
 * @param <K> class for input key
 * @param <V> class for input value
 */
public interface InputFormat<K,V>
 {
	/**
	 * To get the data split in mapping
	 * @param conf JobConf instance
	 * @param path input path array. Each element should be a file.
	 * @return InputSplit Array.
	 */
	public InputSplit[] getSplit(JobConf conf, String[] path);
	/**
	 * To get a RecordReader
	 * @param input
	 * @param conf 
	 * @param reporter
	 * @return a RecordReader instance which can handle the boundary and get key value pair for mapping
	 * @throws IOException
	 */
	public RecordReader<K,V> getRecordReader(InputSplit input,JobConf conf, Reporter reporter) throws IOException;
}
