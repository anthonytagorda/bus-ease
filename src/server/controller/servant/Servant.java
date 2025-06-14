package server.controller.servant;

import server.controller.ServerController;
import server.model.ServerModel;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servant extends SwingWorker<Void, Void> {
	 private static final int PORT = 6969;
	 private static boolean isServerRunning =	false;

	 private static final List<PrintWriter> clientWriters = new ArrayList<>();
	 private static final List<ServerController> connectedClients = new ArrayList<>();

	 public static void startServer() {
		  if ( isServerRunning ) {
				JOptionPane.showMessageDialog(
						  null,
						  "Server is already running",
						  "Error",
						  JOptionPane.ERROR_MESSAGE);
				return;
		  }

		  try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				InetAddress localhost = InetAddress.getLocalHost();
				ServerModel.setServerSocket(serverSocket);
				isServerRunning = true;

				System.out.printf("Server started on %s%n", localhost);
				JOptionPane.showMessageDialog(null,
														"Server started successfully",
														"Server started",
														JOptionPane.INFORMATION_MESSAGE);
				while (isServerRunning) {
					 Socket clientSocket = serverSocket.accept();

					 System.out.printf("Client connected: %s%n", clientSocket.getInetAddress());

					 // Create a PrintWriter for the client and add it to the	 list
					 PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
					 clientWriters.add(clientWriter);
					 new ServerController(clientSocket);
				}
		  } catch (IOException e) {
				isServerRunning = false;
				JOptionPane.showMessageDialog(
						  null,
						  "Server stopped.",
						  "Stop",
						  JOptionPane.ERROR_MESSAGE);
				System.out.println("Server halted.");
		  }
	 } // end of startServer method

	 public static void stopServer(ServerSocket serverSocket) {
		  try {
				// Send a shutdown message to all connected clients
				for (PrintWriter writer : clientWriters) {
					 writer.println("Server has been shutdown.");
				}

				// Set the serverShutdown flag for each PassengerController
				for(ServerController serverController : connectedClients) {
					 serverController.setServerShutdown(true);
				}

				serverSocket.close();
				isServerRunning = false;
		  } catch (IOException | NullPointerException exception) {
				JOptionPane.showMessageDialog(
						  null,
						  "Server not yet started.",
						  "Server Error",
						  JOptionPane.ERROR_MESSAGE);
		  }
	 } // end of stopServer method

	 @Override
	 protected Void doInBackground ( ) throws Exception {
		  startServer();
		  return null;
	 }
} // end of Servant class
