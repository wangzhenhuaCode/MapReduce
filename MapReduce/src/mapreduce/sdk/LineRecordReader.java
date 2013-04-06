package mapreduce.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class LineRecordReader implements RecordReader<WrapObject<String>, WrapObject<String>> {

	private InputSplit input;
	private RandomAccessFile reader;
	private int curFile;
	private FileSplit file;
	private long pos;
	private boolean empty;


	
	public LineRecordReader(InputSplit input) throws IOException {
		this.input=input;
		curFile=0;
		empty=false;
		file=input.getFileList().get(curFile);
		reader=new RandomAccessFile(new File(file.getPath()),"r");
		pos=file.getStart();
		reader.seek(pos);
		long len=reader.length();
		while(pos<len){
			if((reader.read())=='\n'){
				pos++;
				break;
			}
			pos++;
		}
		if(pos>=len-1){
			pos=0;
			curFile++;
			reader.close();
			if(curFile>=input.getFileList().size()){
				empty=true;
			}else{
				file=input.getFileList().get(curFile);
				
				reader=new RandomAccessFile(new File(file.getPath()),"r");
			}
		}
		
	
	}

	@Override
	public boolean next(WrapObject<String> key, WrapObject<String> value) throws IOException {
		if(empty)return !empty;
		if(pos<file.getEnd()){
			String v=reader.readLine();
			value.setValue(v);
			pos+=v.getBytes().length;
			key.setValue(file.getPath()+":"+pos);
			return true;
		}else{
			reader.close();
			curFile++;
			pos=0;
			if(curFile>=input.getFileList().size()){
				empty=true;
				return !empty;
			}else{
				file=input.getFileList().get(curFile);
				reader=new RandomAccessFile(new File(file.getPath()),"r");
				String v=reader.readLine();
				value.setValue(v);
				pos+=v.getBytes().length;
				key.setValue(file.getPath()+":"+pos);
				return true;
			}
		}
		
	}

	
	
	
	

}
