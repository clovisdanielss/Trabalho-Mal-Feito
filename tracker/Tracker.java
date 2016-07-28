package tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Tracker {
	
	public ServerSocket server;
	public ArrayList<Socket> clients;
	public Integer totalClients;
	public int threadCounter;
	public boolean loop;
	public Semaphore semaphore = new Semaphore(1);
	
	
	public Tracker(int port) throws InterruptedException, IOException{
		server = new ServerSocket(port);
		clients = new ArrayList<Socket>();
		loop = true;
		totalClients = new Integer(1);
		threadCounter = 0;
		
		while(loop){
			System.out.println("Aguardando Client...");
			Socket client = null;
			client = server.accept();
			new ClientManager(client,this).start();
			
			/*Uso de semaforo devido a recurso compartilhado.*/
			
			semaphore.acquire();
			newClient(client);
			semaphore.release();
			System.out.println("Client Conectado...");
			
			totalClients++;
			
		}
		
	}
	
	public synchronized void newClient(Socket client){
		clients.add(client);
	}
	


}
