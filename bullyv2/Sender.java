package bullyv1;

import java.io.IOException;

public class Sender extends Thread {
	
	public SimpleClient client;
	
	public Sender(SimpleClient c){
		client = c;		
	}

	@Override
	public void run() {
		while(client.getLoop()){
			try {
				sleep(1000);
				if(sendMensage())
					break;
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean sendMensage() throws IOException, InterruptedException{
		if(client.getTable().size() == client.getTotalConnections()){
			sleep(3000);
			
			client.getSocket().getOutputStream().write("close".getBytes());
			
			client.setLoop(false);
			
			client.getSocket().close();
			
			return true;
			
		}
		else{
			client.getSocket().getOutputStream().write("ok".getBytes());
			return false;
		}
		
	}
	
}
