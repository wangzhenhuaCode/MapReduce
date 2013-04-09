package mapreduce.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class LineRecordReader implements RecordReader<Text, Text> {

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
		if(pos>0){
			while(pos<len){
				char c=(char) reader.read();
				if(c=='\n'||c=='\r'){
					pos++;
					break;
				}
				pos++;
			}
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
	public boolean next(Text key, Text value) throws IOException {
		if(empty)return !empty;
		while(true){
			if(pos>=reader.length()){
				reader.close();
				curFile++;
				pos=0;
				if(curFile>=input.getFileList().size()){
					return false;
				}else{
					file=input.getFileList().get(curFile);
					reader=new RandomAccessFile(new File(file.getPath()),"r");
				}
			}else{
				if(pos>file.getEnd())
					return false;
				String line=readLine();
				value.setValue(line);
				key.setValue(file.getPath()+":"+pos);
				return true;
			}
			
		}

		
	}

	@Override
	public Text createKey() {
		// TODO Auto-generated method stub
		return new Text("");
	}

	@Override
	public Text createValue() {
		// TODO Auto-generated method stub
		return new Text("");
	}

	private String readLine() throws IOException{
		StringBuffer line=new StringBuffer();
		while(true){
			char c=(char) reader.read();
			pos++;
			if(c=='\n'||c=='\r'||c==-1){
				break;
			}
			line.append(c);
		}
		return new String(line);
	}
	
	
	

}
