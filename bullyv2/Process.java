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
		
	}
	
	public void checkWhoIsTheBoss(){
		Data boss = myTable.get(myTable.size() - 1);
		if(Integer.valueOf(boss.getId()) == pid){
			System.out.println("I AM THE BOSS!!! ");
			//send();
		}
		else{
			System.out.println("Senpai "+ boss.getIp() + " is the boss...");
		}
		
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
