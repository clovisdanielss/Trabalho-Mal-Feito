package bullyv1;

import java.io.IOException;
import java.net.SocketException;

public class ProcessBreaker extends Thread{
	
	private Process myProcess;
	
	public ProcessBreaker(Process process){
		myProcess = process;
	}
	
	
	@Override
	public void run(){
		while(true){
			try {
				myProcess.waitToDown();

			} 
			catch (InterruptedException | IOException e) {
				System.out.println("Fechando...");
				break;
			}
			
		}
	}
	

}