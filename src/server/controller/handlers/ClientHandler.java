package server.controller.handlers;

public class ClientHandler implements Runnable {
	 private volatile boolean isServerShutdown = false;

	 @Override
	 public void run ( ) {

	 }

	 public void setServerShutdown (boolean serverShutdown) {
		  this.isServerShutdown = serverShutdown;
	 }
}
