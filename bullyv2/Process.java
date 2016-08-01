package bullyv2;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Process extends Thread{
	
	private ArrayList<Data> myTable;
	private ServerSocket server;
	private int pid;
	
	public Process(String pid, ArrayList<Data> table) throws NumberFormatException, IOException, InterruptedException{
		sleep(1000);
		
		setMyTable(table);
		setpID(Integer.valueOf(pid));
		
		//Starting own server:
		Data myData = Data.myData(this.pid, myTable);
		server = new ServerSocket(Integer.valueOf(myData.getPort()));
		
	}
	
	
	@Override
	public void run(){
		checkWhoIsTheBoss();
	}
	
	public void checkWhoIsTheBoss(){
		Data boss = myTable.get(myTable.size() - 1);
		if(Integer.valueOf(boss.getId()) == pid){
			System.out.println("I AM THE BOSS!!! ");
			
		}
		else{
			System.out.println("Senpai "+ boss.getIp()+":"+boss.getPort() + " is the boss...");
			send();
		}
		
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void send() {
		
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

}
