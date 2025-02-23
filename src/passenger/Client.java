package passenger;

import passenger.controller.ClientController;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client {
	 private static final String SERVER_IP = "192.168.100.104";
	 private static final int SERVER_PORT = 6969;

	 public Client() {
		  connectToServer(SERVER_IP,SERVER_PORT);
	 }

	 public static void main(String[] args) {
		  new Client();
	 }

	 private void connectToServer(String serverIP, int serverPort) {
		  try {
				Socket clientSocket = new Socket(serverIP, serverPort);

				System.out.printf("Connected to the server at : %s%n", serverIP);
				new ClientController(clientSocket);

		  } catch (IOException e) {
				JOptionPane.showMessageDialog(null,
														"Server is not online. Please try to login again.",
														"Error",
														JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
		  }
	 }
}