package mapreduce.sdk;

import java.io.File;
import java.io.IOException;

public abstract class FileOutputFormat<K ,V> implements OutputFormat<K,V> {
	public static void setOutputPath(JobConf conf, String outputDir) throws IOException{
		File outputFile=new File(outputDir);
		if(!outputFile.isFile()&&!outputFile.isDirectory())
			throw new IOException("Can not find: "+outputDir);
		if(outputFile.isDirectory()){
			if(!outputDir.endsWith("/"))
				outputDir=outputDir+"/";
			outputDir=outputDir+"output.txt";
			File f=new File(outputDir);
			f.createNewFile();
		}
		conf.setOutputFile(outputDir);
	}

	
	
}
