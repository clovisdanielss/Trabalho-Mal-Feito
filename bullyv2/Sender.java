package bullyv2;

import java.io.IOException;

public class Sender extends Thread {
	
	public SimpleClient client;
	
	public Sender(SimpleClient c){
		client = c;		
	}

	@Override
	public void run() {
		while(client.loop){
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
		if(client.table.size() == client.totalConnections){
			sleep(3000);
			
			client.socket.getOutputStream().write("close".getBytes());
			
			client.loop = false;
			
			client.socket.close();
			
			return true;
			
		}
		else{
			client.socket.getOutputStream().write("ok".getBytes());
			return false;
		}
		
	}
	
}
