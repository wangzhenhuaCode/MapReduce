package mapreduce.sdk;

import java.io.File;
import java.io.IOException;

/**
 * Class for setting output file.
 *
 * @param <K>
 * @param <V>
 */
public abstract class FileOutputFormat<K ,V> implements OutputFormat<K,V> {
	/**
	 * @param conf JobConf instance
	 * @param outputDir output path. It could be either a file or a directory. If it is a directory the system will output the result to a "output.txt" file under that directory
	 * @throws IOException
	 */
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
			outputDir=f.getAbsolutePath();
		}else{
			outputDir=outputFile.getAbsolutePath();
		}
		conf.setOutputFile(outputDir);
	}

	
	
}
