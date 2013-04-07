package mapreduce.node;

import mapreduce.node.configuration.ConfigurationFactory;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		NodeSystem.configuration=ConfigurationFactory.getConfigurationFactory().getConfiguration(args[0]);
		NodeSystem.node=Node.createNode();
		Shell.createNewJobShell();
		}catch(Exception e){
			System.out.println(e.toString());
			System.exit(0);
		}
	}

}
