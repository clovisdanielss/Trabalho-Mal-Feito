package bullyv2;

import java.io.IOException;
import java.util.Scanner;

public class MainClient {
	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.print("Entre com o numero de conexoes a serem feitas : ");
		int nC = scan.nextInt();
		SimpleClient client = new SimpleClient(nC);
		
		System.out.print("Eu sou "+ client.socket.getInetAddress().getHostAddress() +
				":" + client.socket.getLocalPort() + ":\n\n");
		
		
		System.out.println("List :");
		for(int i = 0; i < client.table.size(); i++){
			System.out.println("ip: " + client.table.get(i).getIp());
			System.out.println("port: " + client.table.get(i).getPort());
			client.table.get(i).setId(new Integer(i + 1).toString());
		}
		
		@SuppressWarnings("unused")
		Data myData = Data.myData(client.socket.getInetAddress().getHostAddress(),
				String.valueOf(client.socket.getLocalPort()), client.table);
		
		/** Treta ocorre se tirar isso...
		 * 
		 * 
		client.socket.close();		
		client.socket = null;
		System.gc();
		
		try {
			new Process(myData.getId(), client.table);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
		
	}
}
