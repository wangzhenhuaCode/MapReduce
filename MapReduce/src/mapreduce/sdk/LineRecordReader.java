package mapreduce.sdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class LineRecordReader implements RecordReader<WrapObject<Long>, WrapObject<String>> {
	private long lineStart;
	private long pos;
	private Long keyId=0l;
	private String[] path;
	private RandomAccessFile reader;
	private int lineNumber;
	private int curFile;
	private boolean filechanged;
	private long lineEnd;

	
	public LineRecordReader(String[] path, int lineNumber) throws IOException {
		this.path = path;
		this.lineNumber = lineNumber;
		curFile=0;
		pos=0;
		reader=new RandomAccessFile(new File(path[curFile]),"r");
	
	}

	@Override
	public Map<WrapObject<Long>, WrapObject<String>> generateKeyValue() throws IOException {
		byte[] buffer = new byte[1024];
        int count = 0;
        int readChars = 0;
        StringBuffer str=new StringBuffer();
        filechanged=false;
        lineStart=0;
        Map<WrapObject<Long>, WrapObject<String>> map=new HashMap<WrapObject<Long>, WrapObject<String>>();
	    while(hasNext()){
	    	
	        while ((readChars = reader.read(buffer)) != -1) {
	          
	            for (int i = 0; i < readChars; ++i) {
	                if (buffer[i] == '\n') {
	                    count++;

	                    if(count==lineNumber){
	                    	count=0;
	                    	lineEnd=pos+i;
	                    	str.append(path[curFile]+";"+lineStart+";"+lineEnd+",");
	                    	lineStart=lineEnd;
	                    	String v=new String(str);
	                    	map.put(new WrapObject<Long>(keyId),new WrapObject<String>(v));
	                    	keyId++;
	                    	str=new StringBuffer();
	                    }
	                    
	                    	
	                }
	            }
	            pos+=readChars;
	        }
	        lineEnd=pos;
	        if(lineStart<lineEnd){
	        	count++;
	        	lineEnd=pos;
            	str.append(path[curFile]+";"+lineStart+";"+lineEnd+",");
            	lineStart=0;
	        	if(count==lineNumber){
                	count=0;
                	String v=new String(str);
                	map.put(new WrapObject<Long>(keyId),new WrapObject<String>(v));
                	keyId++;
                	str=new StringBuffer();
                }
	        }
	        
	        
	    }
	    return map;
		
	}
	private boolean hasNext() throws IOException{
	
		
			curFile++;
			if(curFile>=path.length){
				return false;
			}
			pos=0;
			reader.close();
			reader=new RandomAccessFile(new File(path[curFile]),"r");
			
			filechanged=true;
			
		
		return true;
	}

	
	
	
	

}
