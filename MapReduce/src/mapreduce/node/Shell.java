package mapreduce.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class Shell {
	private static String newJobScript="#! /bin/bash\n" +
			"if [ $# -eq 3 ]; then \n" +
			" java -jar $1 $2 $3 | java -classpath %JAR% "+Agent.class.getName() +" %HOST% %PORT% \n" +
					"else \n" +
					"echo Incorrect arguments. \n" +
					"fi";
	private static String shutDownScript="#! /bin/bash\n" +
			"java -classpath %JAR% "+MgtAgent.class.getName() +" %LOCAL% %HOST% %PORT% %MGTP% shutDown\n";
	private static String jobMgtScript="#! /bin/bash\n" +
			"java -classpath %JAR% "+MgtAgent.class.getName() +" %LOCAL% %HOST% %PORT% %MGTP% jobMgt\n";
	public static void createShell(){
		try {
			File jarFile = new File(Shell.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			String jarName=jarFile.getAbsolutePath();
			newJobScript=newJobScript.replaceAll("%JAR%", jarName);
			newJobScript=newJobScript.replaceAll("%HOST%", NodeSystem.configuration.getMasterHostName());
			newJobScript=newJobScript.replaceAll("%PORT%", NodeSystem.configuration.getMasterPort().toString());
			shutDownScript=replace(shutDownScript,jarName);
			jobMgtScript=replace(jobMgtScript,jarName);
			try {
				FileOutputStream out=new FileOutputStream(jarFile.getParentFile().getPath()+"/new-job");
				out.write(newJobScript.getBytes());
				out.close();
				out=new FileOutputStream(jarFile.getParentFile().getPath()+"/shut-down");
				out.write(shutDownScript.getBytes());
				out.close();
				out=new FileOutputStream(jarFile.getParentFile().getPath()+"/management");
				out.write(jobMgtScript.getBytes());
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
				run.exec("chmod u+x new-job");
				run.exec("chmod u+x shut-down");
				run.exec("chmod u+x management");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static String replace(String script,String jarName){
		script=script.replaceAll("%JAR%", jarName);
		script=script.replaceAll("%HOST%", NodeSystem.configuration.getMasterHostName());
		script=script.replaceAll("%PORT%", NodeSystem.configuration.getMasterPort().toString());
		script=script.replaceAll("%LOCAL%", NodeSystem.configuration.getLocalPort().toString());
		script=script.replaceAll("%MGTP%", NodeSystem.configuration.getManagementPort().toString());
		return script;
	}
}
