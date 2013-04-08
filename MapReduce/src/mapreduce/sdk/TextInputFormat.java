package mapreduce.sdk;

import java.io.IOException;

public class TextInputFormat extends FileInputFormat<Text, Text> {

	

	

	@Override
	public RecordReader<Text, Text> getRecordReader(
			InputSplit input, JobConf conf, Reporter reporter) throws IOException {
		// TODO Auto-generated method stub
		return new LineRecordReader(input);
	}

	

}
