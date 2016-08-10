package bullyv1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SimpleClient {

	private String myIp;
	private ArrayList<Data> table;
	private int totalConnections;
	private Socket socket;
	private boolean loop;
	


	public SimpleClient(int totalConnections) {
		loop = true;
		
		setMyIp("");

		this.totalConnections = totalConnections;
		
		table = new ArrayList<Data>();

		System.out.println("Client.");

		socket = null;
		
		//Ao gerar o Jar mude para o IP do camarada e a porta
		try {
			socket = new Socket("localhost", 9799);
			
			if (socket.isConnected()) {
				new Sender(this).start();
				while (true) {
					if (!Read(socket))
						break;
				}
			}		
		
		}catch (IOException e) {
			System.out.println("Finalizado com Sucesso!!");
			
		}

		

	}

	private boolean Read(Socket socket) throws IOException {
		// System.out.println("Lendo msg");
		if(!checkLoop())
			return false;
		
		byte[] buffer = new byte[100];
		InputStream in = socket.getInputStream();
		in.read(buffer);

		String msg = new String(buffer);
		msg = msg.trim();

		int ipMark = 0;
		int portMark = 0;
		int lastMark = 0;
		int selfMark = 0;
		int endSelfMark = 0;
		
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == 'i') {
				ipMark = i;
			}
			if (msg.charAt(i) == 'p'){
				portMark = i;
			}
			if (msg.charAt(i) == 's'){
				selfMark = i;
			}
			if (msg.charAt(i) == '!'){
				endSelfMark = i;
				setMyIp(msg.substring(selfMark + 1,endSelfMark));
			}
			if(msg.charAt(i) == ';'){
				
				lastMark = i;
				String ip = msg.substring(ipMark + 1, portMark);
				String port = msg.substring(portMark + 1,lastMark);
				
				if(table.isEmpty())
					table.add(new Data(ip,port));
				else if(!Data.containsIp(ip, port, table)){
					table.add(new Data(ip,port));
				}
			}
		}

		return true;
	}
	
	public synchronized boolean checkLoop(){
		return loop;
	}

	public String getMyIp() {
		return myIp;
	}

	public void setMyIp(String myIp) {
		this.myIp = myIp;
	}

	public ArrayList<Data> getTable(){
		return table;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public boolean getLoop(){
		return loop;
	}
	
	public void setLoop(boolean b){
		loop = b;
	}
	
	public int getTotalConnections(){
		return totalConnections;
	}
}
