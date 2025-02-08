package server.controller.handlers;

import java.net.ServerSocket;

public class ServerHolder {
	 private static ServerSocket serverSocket;

	 public static void setServerSocket(ServerSocket servSocket) {
		  serverSocket = servSocket;
	 }

	 public static ServerSocket getServerSocket() {
		  return serverSocket;
	 }
}
