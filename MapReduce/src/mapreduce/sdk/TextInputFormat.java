package mapreduce.sdk;

import java.io.IOException;

public class TextInputFormat extends FileInputFormat<WrapObject<String>, WrapObject<String>> {

	

	

	@Override
	public RecordReader<WrapObject<String>, WrapObject<String>> getRecordReader(
			InputSplit input, JobConf conf, Reporter reporter) throws IOException {
		// TODO Auto-generated method stub
		return new LineRecordReader(input);
	}

	

}
