package bullyv2;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ProcessController extends Thread{
	
	private ArrayList<Data> table;
	private Socket client;
	private int pid;
	private Process myProcess;
	// Quando for fazer eleicao sera necessario esse parametro msg
	@SuppressWarnings("unused")
	private String msg;
	
	public ProcessController(Socket client, String msg, Process p) {
		this.client = client;
		this.msg = msg;
		this.table = p.getMyTable();
		this.pid = p.getpID();
		myProcess = p;
	}
	
	@Override
	public void run(){
		
		
		// Fim da escuta, fecha o socket.
		try {
			listen();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void listen() throws IOException{
		byte[] buffer = new byte[100];
		client.getInputStream().read(buffer);
		
		
		String myBuffer = new String(buffer).trim();
		
		// Vou ter 4 possíveis mensagens recebidas:   
		//(?) Eleicao (!) Keep Alive para o Servidor (O) Resposta de um servidor ao keep alive
		//(Nb) Recebe msg do novo boss.
		
		String msgRcv = "";
		int start = 0;
		int end = 0;
		
		
		for(int i = 0; i < myBuffer.length(); i++){
			if(myBuffer.charAt(i) == '('){
				start = i;
			}
			if(myBuffer.charAt(i) == ')'){
				end = i;
				msgRcv = myBuffer.substring(start + 1, end -1);
				String pID = myBuffer.substring(start + 2, end);
				checkMensage(msgRcv,pID);
			}
		}
	}

	private void checkMensage(String msgRcv, String pID) {
		if(msgRcv.equals("!")){
			// Retorno Ok para o camarada. Caso "send"			
			Data serverData = Data.myData(Integer.valueOf(pID), table);
			try {
				System.out.println("Mandando Mensagem Para : " + serverData.getIp() + ": "+ serverData.getPort());
				
				//PROBLEMA CASO HAJA IP'S IGUAIS, DEVO PROCURAR PELO PID
				
				Socket socket = new Socket(serverData.getIp(), Integer.valueOf(serverData.getPort()));
				
				socket.getOutputStream().write(("(O"+ String.valueOf(pid) +")").getBytes());
				
				socket.close();
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			myProcess.setfLoop(false);
		}
		if(msgRcv.equals("?")){
			// Retorno Ok para o camarada. Caso "election"
			Data serverData = Data.myData(Integer.valueOf(pID), table);
			try {
				System.out.println(":> Mandando Mensagem Para : " + serverData.getIp() + ": "+ serverData.getPort());
				
				//PROBLEMA CASO HAJA IP'S IGUAIS, DEVO PROCURAR PELO PID
				
				Socket socket = new Socket(serverData.getIp(), Integer.valueOf(serverData.getPort()));
				
				socket.getOutputStream().write(("(N"+ String.valueOf(pid) +")").getBytes());
				
				socket.close();
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
		}
		if(msgRcv.equals("O")){
			// Não vai fazer nada. Caso "read"
			try {
				Data serverData = Data.myData(Integer.valueOf(pID), table);
				System.out.println(":> O Boss eh :" + serverData.getIp() + ": "+ serverData.getPort());
				
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(msgRcv.equals("N")){
			try {
				Data serverData = Data.myData(Integer.valueOf(pID), table);
				System.out.println(":>" + serverData.getIp() + ": "+ serverData.getPort() + 
						"  Me Avisou que NAO sou o novo boss");
				
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Descobre que não é o novo chefe.
		}
		if(msgRcv.equals("b")){
			try {
				Data serverData = Data.myData(Integer.valueOf(pID), table);
				System.out.println(":> O Boss eh : " + serverData.getIp() + ": "+ serverData.getPort());
				
				myProcess.setBoss(serverData);
				
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

}
