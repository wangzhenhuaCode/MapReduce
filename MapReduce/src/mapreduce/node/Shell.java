package mapreduce.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

import mapreduce.sdk.MasterInfo;

public class Shell {
	private static String newJobScript="#! /bin/bash\n" +
			"if [ $# -eq 3 ]; then \n" +
			"cp %JARPAR%/config.cg ${1%/*}\n" +
			" java -jar $1 $2 $3\n" +
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
			newJobScript=newJobScript.replaceAll("%JARPAR%", jarFile.getParentFile().getPath());
			shutDownScript=replace(shutDownScript,jarName);
			jobMgtScript=replace(jobMgtScript,jarName);
			try {
				FileOutputStream out=new FileOutputStream(jarFile.getParentFile().getPath()+"/new-job");
				out.write(newJobScript.getBytes());
				out.close();
				out=new FileOutputStream(jarFile.getParentFile().getPath()+"/shut-down");
				out.write(shutDownScript.getBytes());
				out.close();
				out=new FileOutputStream(jarFile.getParentFile().getPath()+"/admin");
				out.write(jobMgtScript.getBytes());
				out.close();
				ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(jarFile.getParentFile().getPath()+"/config.cg"));
				oout.writeObject(new MasterInfo(NodeSystem.configuration.getMasterHostName(),NodeSystem.configuration.getMasterPort()));
				oout.close();
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
				run.exec("chmod u+x admin");
				
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
