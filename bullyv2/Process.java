package bullyv2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Process extends Thread{
	
	private ArrayList<Data> myTable;
	private ServerSocket server;
	private int pid;
	private Data boss;
	private boolean fBoss, fLoop;
	
	public Data getBoss(){
		return boss;
	}
	
	public void setBoss(Data d){
		boss = d;
	}
	
	public Process(String pid, ArrayList<Data> table) throws NumberFormatException, IOException, InterruptedException{
		fLoop = true;
		
		fBoss = false;
		
		sleep(1000);
		
		setMyTable(table);
		setpID(Integer.valueOf(pid));
		
		//Starting own server:
		Data myData = Data.myData(this.pid, myTable);
		server = new ServerSocket(Integer.valueOf(myData.getPort()));
		
		boss = myTable.get(myTable.size() - 1);
		
		new ProcessManager(this).start();
	}
	
	
	@Override
	public void run(){
		checkWhoIsTheBoss();
	}
	
	private void checkWhoIsTheBoss(){
		
		if(Integer.valueOf(boss.getId()) == pid){
			System.out.println("I AM THE BOSS!!! ");
			fBoss = true;
		}
		else{
			System.out.println("Senpai "+ boss.getIp()+":"+boss.getPort() + " is the boss...");
			while(true){
				if(!send())
					election(true,boss);
				if(!fLoop)
					break;
			}
			
		}
		
		System.out.println("Só lembrando...");
// No final de tudo...
//		try {
//			server.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}


	private void election(boolean election, Data boss) {
		
		if(election){
			myTable.remove(boss);
			for(Data process: myTable){
				if(Integer.valueOf(process.getId()) > getpID())
					try {
						sleep(1000);
						
						Socket socket = new Socket(process.getIp(), Integer.valueOf(process.getPort()));
						
						System.out.println("Mandando Mensagem Para : " + process.getIp() + ":"+ process.getPort());
						
						socket.getOutputStream().write(("(?"+ new Integer(getpID()) +")").getBytes());
									
						socket.close();
						
					} catch (NumberFormatException e) {
					} catch (UnknownHostException e) {
					} catch (IOException e) {
					} catch (InterruptedException e) {
					}	
				
			}
			
			if(pid == myTable.size()){
				if(fBoss == false)
					System.out.println("I AM THE NEW BOSS!");
				fBoss = true;
				for(Data process: myTable){
					if(Integer.valueOf(process.getId()) != getpID())
						try {
							sleep(1000);
							
							Socket socket = new Socket(process.getIp(), Integer.valueOf(process.getPort()));
							
							socket.getOutputStream().write(("(b"+ new Integer(getpID()) +")").getBytes());
										
							socket.close();
							
						} catch (NumberFormatException e) {
						} catch (UnknownHostException e) {
						} catch (IOException e) {
						} catch (InterruptedException e) {
						}	
					
				}
			}
			
			
			
		}
	}


	private boolean reply() {
		return false;
	}


	private boolean send() {
		try {
			sleep(3000);
			
			Socket socket = new Socket(boss.getIp(), Integer.valueOf(boss.getPort()));
			
			System.out.println("Mandando Mensagem Para : " + boss.getIp() + ":"+ boss.getPort());
			
			socket.getOutputStream().write(("(!"+ new Integer(getpID()) +")").getBytes());
						
			socket.close();
			
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

	public boolean isfLoop() {
		return fLoop;
	}

	public void setfLoop(boolean fLoop) {
		this.fLoop = fLoop;
	}

}
