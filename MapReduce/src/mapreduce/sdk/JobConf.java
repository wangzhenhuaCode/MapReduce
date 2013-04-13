package mapreduce.sdk;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;



/**
 * Class for job configuration.
 *
 */
public class JobConf implements Serializable {
	private Map<String, String> configuration;
	/**
	 * @param exampleClass The class with main method.
	 * @throws IOException
	 */
	public JobConf(Class exampleClass) throws IOException{
		configuration=new HashMap<String,String>();
		findJarAddress(exampleClass);
	}
	private void findJarAddress(Class exampleClass) throws IOException{
		String path=null;
		try {
			File jarFile = new File(exampleClass.getProtectionDomain().getCodeSource().getLocation().toURI());
			configuration.put("mapreduce.jar", jarFile.getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
			
	}
	/**
	 * set the job name. Required for job running.
	 * @param name
	 */
	public void setJobName(String name){
		configuration.put("mapreduce.job.name", name);
	}
	/**
	 * set the output key's class. This class should be serializable. Required for job running.
	 * @param exampleClass
	 */
	public void setOutputKeyClass(Class<? extends Serializable> exampleClass){
		configuration.put("mapreduce.output.key.class", exampleClass.getName());
	}
	/**
	 * set the output value's class. This class should be serializable. Required for job running.
	 * @param exampleClass
	 */
	public void setOutputValueClass(Class<? extends Serializable> exampleClass){
		configuration.put("mapreduce.output.value.class", exampleClass.getName());
	}
	/**
	 * Set the map class. The class should implements Mapper. Required for job running.
	 * @param exampleClass
	 */
	public void setMapperClass(Class<? extends Mapper> exampleClass){
		configuration.put("mapreduce.map.class", exampleClass.getName());
	}
	/**
	 * Set the combine class. The class should implements Reducer. Required for job running.
	 * @param exampleClass
	 */
	public void setCombinerClass(Class<? extends Reducer> exampleClass){
		configuration.put("mapreduce.combiner.class", exampleClass.getName());
	}
	/**
	 * Set the reduce class. The class should implements Reducer. Required for job running.
	 * @param exampleClass
	 */
	public void setReducerClass(Class<? extends Reducer> exampleClass){
		configuration.put("mapreduce.reduce.class", exampleClass.getName());
	}
	/**
	 * Set the input format class. The class should implements InputFormat. Required for job running.
	 * @param exampleClass
	 */
	public void setInputFormat(Class<? extends InputFormat> exampleClass){
		configuration.put("mapreduce.input.format", exampleClass.getName());
	}
	/**
	 * Set the output format class. The class should implements OutputFormat. Required for job running.
	 * @param exampleClass
	 */
	public void setOutputFormat(Class<? extends OutputFormat> exampleClass){
		configuration.put("mapreduce.output.format", exampleClass.getName());
	}
	/**
	 * Set the input file path. It could be either a file or a directory. If it is a directory, the system will include all the non-hidden files under that directory as input. Required for job running.
	 * @param path
	 */
	protected void setInputFile(String path){
		configuration.put("mapreduce.input.path", path);
	}
	/**
	 * Set the output file path. It could be either a file or a directory. If it is a directory, the system will automatically generate a "output.txt" under that directory to store the result. Required for job running.
	 * @param path
	 */
	protected void setOutputFile(String path){
		configuration.put("mapreduce.output.path", path);
	}
	public Map<String, String> getConfiguration() {
		return configuration;
	}
	protected void setJobId(String id){
		configuration.put("mapreduce.job.id", id);
	}
	public void setWorkingDirectory(String path){
		configuration.put("mapreduce.workingDirectory", path);
	}
	/**
	 * Set the split block size in mapping. The minimum size is 128 bytes. The default size is 1024 bytes.
	 * @param size
	 */
	public void setSplitSize(Long size){
		if(size<128){
			size=128L;
		}
		configuration.put("mapreduce.blockSize", size.toString());
	}
	/**
	 * Set the number of tasks a reduce task can handle. The minimum ratio is 2. The default ratio is 10.
	 * @param ratio
	 */
	public void setMaxMapReduceRatio(Integer ratio){
		if(ratio<2){
			ratio=2;
		}
		configuration.put("mapreduce.mapReduceRatio", ratio.toString());
	}
}
