package mapreduce.node;

public class Slave extends Node {

	public Slave(){
		try {
			run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread update=new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					
				}
			}
			
		});
		update.start();
	}
	
}
