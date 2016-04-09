package cloudKon.client;

import java.io.IOException;

public class ClientThread extends Thread {
	private Client client;

	public ClientThread(Client client) {
		super();
		this.client = client;
	}

	@Override
	public void run() {
		try {
			client.doExperiment();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
