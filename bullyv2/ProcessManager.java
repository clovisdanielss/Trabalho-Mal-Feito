package bullyv1;

import java.io.IOException;
import java.net.Socket;

/**
 * Aceita as conecxoes feitas no servidor do meu processo.
 * */
public class ProcessManager extends Thread{
	
	private Process myProcess;
	private String msg;
	
	public ProcessManager(Process process){
		myProcess = process;
		msg  = null;
	}
	
	public ProcessManager(Process process, String msg){
		myProcess = process;
		this.msg = msg;
	}
	
	
	@Override
	public void run(){
		while(true){
			try {
				//myProcess.checkProcess();
				Socket client = myProcess.getServer().accept();
				new ProcessController(client,msg,myProcess).start();

			} 
			catch (IOException e) {
			}
			
		}
	}
	
	
	
}
