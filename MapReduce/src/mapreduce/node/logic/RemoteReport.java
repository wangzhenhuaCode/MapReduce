package mapreduce.node.logic;

import java.util.Date;

import mapreduce.node.NodeSystem;
import mapreduce.sdk.Reporter;

public class RemoteReport implements Reporter {
	private String log;
	public RemoteReport(){
		log="";
	}
	@Override
	public void log(String logInfo) {
		// TODO Auto-generated method stub
		String logitem=(new Date()).toString()+"\t"+NodeSystem.configuration.getLocalHostName()+"\t"+logInfo+"\n";
		log=log+logitem;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public void systemLog(String logInfo){
		log=log+logInfo;
	}

}
