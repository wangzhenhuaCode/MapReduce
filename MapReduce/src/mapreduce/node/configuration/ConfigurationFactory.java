package mapreduce.node.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;



public class ConfigurationFactory {
	static ConfigurationFactory instance;

	public Configuration getConfiguration(String url) throws Exception{
		FileInputStream fis = null;
		Properties porperty;
		try {
			fis = new FileInputStream(url);
		
			porperty =new Properties();
			porperty.load(fis);
		} catch (FileNotFoundException e1) {
			
			throw new Exception("Cannot find file: "+url);
		} catch (IOException e) {
			fis.close();
			throw new Exception("Cannot read file: "+url);
		}
		Configuration configuration =new Configuration();
		String isMasterString=porperty.getProperty("isMaster");
		if(isMasterString==null){
			fis.close();
			throw new Exception("Cannot find property: isMaster");
			
		}
		try{
		configuration.setIsMaster(Boolean.valueOf(isMasterString));
		}catch(Exception e){
			fis.close();
			throw new Exception("Wrong configuration for property: isMaster");
		}
		
		
		String localPortString=porperty.getProperty("localPort");
		if(localPortString==null){
			fis.close();
			throw new Exception("Cannot find property: localPort");
			
		}
		try{
		configuration.setLocalPort(Integer.valueOf(localPortString));
		}catch(Exception e){
			fis.close();
			throw new Exception("Wrong configuration for property: localPort");
		}
		
		
		String capabilityString=porperty.getProperty("capability");
		if(capabilityString==null){
			fis.close();
			throw new Exception("Cannot find property: capability");
			
		}
		try{
		configuration.setCapability(Integer.valueOf(capabilityString));
		}catch(Exception e){
			fis.close();
			throw new Exception("Wrong configuration for property: capability");
		}
		
		String messageMaxPoolSize=porperty.getProperty("messageMaxPoolSize");
		if(messageMaxPoolSize==null){
			fis.close();
			throw new Exception("Cannot find property: messageMaxPoolSize");
			
		}
		try{
		configuration.setMessageMaxPoolSize(Integer.valueOf(messageMaxPoolSize));
		}catch(Exception e){
			fis.close();
			throw new Exception("Wrong configuration for property: messageMaxPoolSize");
		}
		
		
		if(!configuration.getIsMaster()){
			String masterHostNameString=porperty.getProperty("masterHostName");
			if(masterHostNameString==null){
				fis.close();
				throw new Exception("Cannot find property: masterHostName");
				
			}
			try{
			configuration.setMasterHostName(masterHostNameString);
			}catch(Exception e){
				fis.close();
				throw new Exception("Wrong configuration for property: masterHostName");
			}
			
			
			
			String masterPortString=porperty.getProperty("masterPort");
			if(masterPortString==null){
				fis.close();
				throw new Exception("Cannot find property: masterPort");
				
			}
			try{
			configuration.setMasterPort(Integer.valueOf(masterPortString));
			}catch(Exception e){
				fis.close();
				throw new Exception("Wrong configuration for property: masterPort");
			}
			
			
			
		}else{
			configuration.setMasterPort(configuration.getLocalPort());
		}
		
		configuration.setLocalHostName(InetAddress.getLocalHost().getHostName());
		if(configuration.getIsMaster()){
			configuration.setMasterHostName(configuration.getLocalHostName());
		}
		return configuration;
	}
	public static ConfigurationFactory getConfigurationFactory()  {
		if(instance==null)
			instance=new ConfigurationFactory();
		return instance;
	}
	

}
