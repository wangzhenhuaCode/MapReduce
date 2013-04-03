package mapreduce.sdk;

import java.io.File;

public class FileInputFormat implements InputFormat {
	
	public void setInputPaths(JobConf conf,String path){
		String inputPath="";
		File root = new File(path);
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
	private String getFile(File root){
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
}
