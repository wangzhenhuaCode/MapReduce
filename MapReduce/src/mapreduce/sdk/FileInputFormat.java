package mapreduce.sdk;

import java.io.File;
import java.io.IOException;

public abstract class  FileInputFormat<K,V> implements InputFormat<K,V> {
	
	public static void setInputPaths(JobConf conf,String path) throws IOException{
		String inputPath="";
		File root = new File(path);
		if(!root.isFile()&&!root.isDirectory())
			throw new IOException("Can not find: "+path);
		if(root.isDirectory()){
			String subFile=getFile(root);
			if(!subFile.equals(""))
				inputPath=inputPath+subFile+",";
		}else{
			inputPath=path;
		}
		while(inputPath.endsWith(",")){
			inputPath=inputPath.substring(0, inputPath.length()-1);
		}
		conf.setInputFile(inputPath);
	}
	private static String getFile(File root){
		String path="";
		File[] files = root.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				String subFile=getFile(file);
				if(!subFile.equals(""))
					path=path+subFile+",";
			}else{
				path=path+file.getPath()+",";
			}
		}
		return path;
	}

	public abstract RecordReader<K, V> getRecordReader(String[] path,JobConf conf, Reporter reporter)throws IOException;

	
}
