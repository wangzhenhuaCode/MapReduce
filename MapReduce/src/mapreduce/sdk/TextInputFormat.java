package mapreduce.sdk;

import java.io.IOException;

public class TextInputFormat extends FileInputFormat<WrapObject<Long>, WrapObject<String>> {

	

	@Override
	public RecordReader<WrapObject<Long>, WrapObject<String>> getRecordReader(
			String[] path, JobConf conf, Reporter reporter) throws IOException {
		// TODO Auto-generated method stub
		return new LineRecordReader(path,1);
	}

	

}
