package mapreduce.sdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.Configuration;

/**
 * Class for File input format. 
 *
 * @param <K> key's type
 * @param <V> value's type
 */
public abstract class  FileInputFormat<K,V> implements InputFormat<K,V> {
	
	/**
	 * @param conf JobConf instance
	 * @param path the input path. It could be either a file or a directory. If it is a directory, all the files under that directory will be regarded as input
	 * @throws IOException
	 */
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
			inputPath=root.getAbsolutePath();
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
				if(!file.isHidden())
				path=path+file.getAbsolutePath()+",";
			}
		}
		return path;
	}

	public abstract RecordReader<K, V> getRecordReader(InputSplit input,JobConf conf, Reporter reporter)throws IOException;
	public InputSplit[] getSplit(JobConf conf, String[] path){
		Long blockSize=Long.valueOf(conf.getConfiguration().get("mapreduce.blockSize"));
		Long available=blockSize;
		List<InputSplit> list=new ArrayList<InputSplit>();
		InputSplit inputsplit=new InputSplit();;
		for(int i=0;i<path.length;i++){
			File file=new File(path[i]);
			long pos=0;
			while(pos+available<file.length()){
				FileSplit split=new FileSplit(path[i],pos,pos+blockSize);
				inputsplit.getFileList().add(split);
				list.add(inputsplit);
				inputsplit=new InputSplit();
				pos+=blockSize;
				available=blockSize;
			}
			long occupied=file.length()-1-pos;
			available=blockSize-occupied;
			if(occupied>0){
				FileSplit split=new FileSplit(path[i],pos,pos+occupied);
				inputsplit.getFileList().add(split);
			}
		
		}
		list.add(inputsplit);
		InputSplit[] input=new InputSplit[list.size()];
		for(int i=0;i<list.size();i++){
			input[i]=list.get(i);
		}
		return input;
		
	}
	
}
