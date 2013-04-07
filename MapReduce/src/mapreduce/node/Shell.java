package mapreduce.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class Shell {
	private static String shellscript="if [ $* -eq 3 ]; then \n" +
			" java -jar $1 $2 $3 | java -classpath %JAR% "+Agent.class.getName() +" %HOST% %PORT% \n" +
					"else \n" +
					"echo Incorrect arguments. \n" +
					"fi";
	public static void createNewJobShell(){
		try {
			File jarFile = new File(Shell.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String jarName=jarFile.getName();
			shellscript=shellscript.replaceAll("%JAR%", jarName);
			shellscript=shellscript.replaceAll("%HOST%", NodeSystem.configuration.getMasterHostName());
			shellscript=shellscript.replaceAll("%PORT%", NodeSystem.configuration.getMasterPort().toString());
			try {
				FileOutputStream out=new FileOutputStream(jarFile.getParentFile().getPath()+"/MapReduce");
				out.write(shellscript.getBytes());
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Runtime run = Runtime.getRuntime();
			try {
				run.exec("chmod MapReduce u+x");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
