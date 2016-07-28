package tracker;

import java.io.IOException;

public class MainTracker {
	public static void main(String[] args) {
		try {
			//Ao gerar o jar, mude a porta pra a mesma do cliente
			new Tracker(9799);
		} catch (IOException | InterruptedException e) {
			
			System.out.println("Finalizado com sucesso!!");
			
		}
	}
}
