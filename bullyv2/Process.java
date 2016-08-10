package bullyv1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.management.monitor.Monitor;

public class Process extends Thread{
	
	private ArrayList<Data> myTable;
	private ServerSocket server;
	private int pid;
	private Data boss;
	private boolean isBoss;
	private boolean keepAlive;
	private boolean isDown;
	private boolean electionACK;
	private Semaphore mutex;
	
	public Data getBoss(){
		return boss;
	}
	
	public void setBoss(Data d){
		boss = d;
	}
	
	public Process(String pid, ArrayList<Data> table) throws NumberFormatException, IOException, InterruptedException{
		mutex = new Semaphore(1);
		
		isDown = false;
		
		isBoss = false;
		
		sleep(1000);
		
		setMyTable(table);
		setpID(Integer.valueOf(pid));
		
		//Starting own server:
		Data myData = Data.myData(this.pid, myTable);
		server = new ServerSocket(Integer.valueOf(myData.getPort()));
		
		boss = myTable.get(myTable.size() - 1);
		
		new ProcessManager(this).start();
		new ProcessBreaker(this).start();

	}
	
	
	@Override
	public void run(){
		try {
			checkWhoIsTheBoss();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkWhoIsTheBoss() throws InterruptedException{
		
		if(Integer.valueOf(boss.getId()) == pid){
			System.out.println("I AM THE BOSS!!! ");
			isBoss = true;
			while(isBoss){
				//TODO Condicoes de queda...
			}
		}
		else{
			System.out.println("Senpai "+ boss.getIp()+":"+boss.getPort() + " is the boss...");
			while(true){
				if(!send() && !isBoss){
					//System.out.println("@@@Teste@@@");
					election(boss);
				}
			}
			
		}
		// No final de tudo...
		//waitToClose();
		
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public synchronized void checkProcess() throws InterruptedException{
		while(isDown())
			wait();
		
	}

	
	public synchronized void waitToDown() throws InterruptedException, IOException {
		// TODO Da uma olhada nesse tempo depois...
		// Lembra que se vc comentar a run do breaker, funciona tudo...
		
		wait((long)(Math.random()*10000));
		
		if(Math.random()*100 > 75){
			//System.out.println("Going to change State :");
			
			if(isDown == true){
				System.out.println(":> Going Up");
				isDown = false;
				//criar server
				Data myData = Data.myData(this.pid, myTable);
				server = new ServerSocket(Integer.valueOf(myData.getPort()));
				setIsBoss(false);
				election(boss);
				
			}
			else if(isDown == false){
				System.out.println(":> Going Down");
				isDown = true;
				setIsBoss(false);
				// fechar server;
				server.close();
			}
		}
		
		notifyAll();
	}

	private synchronized void election(Data boss) {
		
		
		setElectionACK(false);
		
		try {
			wait(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(":> " +"Iniciando eleicao...");
		
		for(Data process: myTable){
			if(Integer.valueOf(process.getId()) > getpID())
				try {
					
					Socket socket = new Socket(process.getIp(), Integer.valueOf(process.getPort()));
					
					System.out.println(":> " +"Mandando Eleicao Para : " + process.getIp() + ":"+ process.getPort());
					
					socket.getOutputStream().write(("(?"+ new Integer(getpID()) +")").getBytes());
								
					socket.close();
					
				} catch (NumberFormatException e) {
				} catch (UnknownHostException e) {
				} catch (IOException e) {
				}	
		}
		
		try {
			//Espera por resposta de alguem...
			wait(1000);
			
			if(isElectionACK()){
			//Espera pela msg do coordenador novo, se nao vinher ele vai repetir a eleicao
				
			}
			else{
			// Avisa que ele é o eleito...
				if(!isBoss)
					System.out.println("I'm the Boss!");
				setIsBoss(true);
				for(Data process: myTable){
					if(Integer.valueOf(process.getId()) < getpID()){
						Socket socket = new Socket(process.getIp(), Integer.valueOf(process.getPort()));
						
						System.out.println(":> " +"Mandando Eleito Para : " + process.getIp() + ":"+ process.getPort());
						
						socket.getOutputStream().write(("(b"+ new Integer(getpID()) +")").getBytes());
									
						socket.close();
					}
				}
				
			}
		} catch (InterruptedException | NumberFormatException | IOException e) {
		}
		
		
		
		
	}




	private synchronized boolean send() {
		if(!isDown && !isBoss)
		try {
			
			setKeepAlive(false);
			
			Socket socket = new Socket(boss.getIp(), Integer.valueOf(boss.getPort()));
			
			System.out.println(":> " +"Mandando Mensagem Para : " + boss.getIp() + ":"+ boss.getPort());
			
			socket.getOutputStream().write(("(!"+ new Integer(getpID()) +")").getBytes());
						
			socket.close();
			
			sleep(2000);
			
			if(keepAlive){
				//System.out.println("ACK recebido...");
				return true;
			}
			else{
				//System.out.println("ACK nao recebido recebido...");
				return false;
			}
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return true;
	}
	

	public int getpID() {
		return pid;
	}


	public void setpID(int pID) {
		this.pid = pID;
	}


	public ArrayList<Data> getMyTable() {
		return myTable;
	}


	public void setMyTable(ArrayList<Data> myTable) {
		this.myTable = myTable;
	}
	
	public ServerSocket getServer(){
		return server;
	}

	public boolean isBoss() {
		return isBoss;
	}

	public void setIsBoss(boolean fLoop) {
		this.isBoss = fLoop;
	}

	public Semaphore getMutex() {
		return mutex;
	}

	public void setMutex(Semaphore mutex) {
		this.mutex = mutex;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public boolean isElectionACK() {
		return electionACK;
	}

	public void setElectionACK(boolean election) {
		this.electionACK = election;
	}

}
