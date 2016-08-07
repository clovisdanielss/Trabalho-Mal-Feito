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
		
		System.out.print("Eu sou "+ client.getMyIp() +
				":" + new Integer(client.getSocket().getLocalPort() + 1) + ":\n\n");
		
		
		System.out.println("List :");
		for(int i = 0; i < client.getTable().size(); i++){
			System.out.println("ip: " + client.getTable().get(i).getIp()+";");
			System.out.println("port: " + client.getTable().get(i).getPort()+";");
			client.getTable().get(i).setId(new Integer(i + 1).toString());
		}
		System.out.println("\n");
		
		Data myData = Data.myData(client.getMyIp(),String.valueOf(client.getSocket().getLocalPort() + 1),
				client.getTable());		

		try {
			new Process(myData.getId(), client.getTable()).start();
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
		
	}
}
