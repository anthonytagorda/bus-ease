package server.model;

import java.net.ServerSocket;

public class ServerModel {
	 private static ServerSocket serverSocket;
	 public static void setServerSocket(ServerSocket servSocket) {
		  serverSocket = servSocket;
	 }

	 public static ServerSocket getServerSocket() {
		  return serverSocket;
	 }
}
