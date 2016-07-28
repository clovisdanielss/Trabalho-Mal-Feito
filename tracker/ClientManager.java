package tracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientManager extends Thread{
	
	public Socket client;
	public Integer id;
	public Tracker tracker;
	
	public ClientManager(Socket socket, Tracker tracker){
		this.client = socket;
		this.tracker = tracker;
	}
	
	@Override
	public void run(){
		while(tracker.loop){
			try {
				sleep(1000);
				
				tracker.semaphore.acquire();
				toAllClients();
				tracker.semaphore.release();
				
				if(!checkConnection())
					break;
				
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}

	public synchronized void toAllClients(){
		for(Socket socket : tracker.clients){
			try {
				sendMensage(this.client.getOutputStream(),socket);
;
			}
			catch (IOException e) {
				System.err.println("Erro Na Conexao!");
			}
		}
	}
	
	private boolean checkConnection() throws IOException {
		byte[] buffer = new byte[100];
		InputStream in = client.getInputStream();
		in.read(buffer);
		
		String msg = new String(buffer);
		msg = msg.trim();
		
		if(msg.equals("close")){
			tracker.clients.remove(client);
			client.close();
			tracker.threadCounter++;
			if(tracker.threadCounter - tracker.totalClients == -1){
				System.out.println("Fechando Servidor...");
				tracker.loop = false;
				tracker.server.close();
			}					
			return false;
		}
		
		/* "Qualquer outra msg, retorna verdade" */
		
		return true;
		
	}

	private void sendMensage(OutputStream out, Socket socket) throws IOException {

		String msg = "i"+ socket.getInetAddress().getHostAddress()
				+"p"+ socket.getPort() + ";";
		out.write(msg.getBytes());			
	}
}