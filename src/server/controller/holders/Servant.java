package server.controller.holders;

import server.controller.handlers.ServerHandler;

import javax.swing.*;

public class Servant extends SwingWorker<Void,Void> {

	 @Override
	 protected Void doInBackground ( ) {
		  ServerHandler.startServer();
		  return null;
	 }
}
