package bullyv2;

import java.io.IOException;
import java.net.Socket;

/**
 * Aceita as conecxoes feitas no servidor do meu processo.
 * */
public class ProcessManager extends Thread{
	
	private Process p;
	private String msg;
	
	public ProcessManager(Process process){
		p = process;
		msg  = null;
	}
	
	public ProcessManager(Process process, String msg){
		p = process;
		this.msg = msg;
	}
	
	
	@Override
	public void run(){
		while(true){
			try {
				System.out.println("Em Aguardo...");
				Socket client = p.getServer().accept();
				new ProcessController(client,msg,p).start();

			} 
			catch (IOException e) {
				e.printStackTrace();
				System.out.println();
				break;
			}
			
		}
	}

}
