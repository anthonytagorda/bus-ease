package server.controller;

import server.controller.handlers.ServerHandler;
import server.controller.handlers.ServerHolder;
import server.view.AdminLoginView;
import server.view.ManageUsersView;
import server.view.ServerMainView;

import javax.swing.*;
import java.net.ServerSocket;

public class ServerMainController {
	 private final ServerMainView serverMainView;

	 public ServerMainController(ServerMainView serverMainView) {
		  this.serverMainView = serverMainView;

		  // Server Main Action Listeners
		  this.serverMainView.addManageUsersButtonListener(e -> manageUsers());
		  this.serverMainView.addClearButtonListener(e -> clearLog());
		  this.serverMainView.addStartServerButtonListener(e -> startServer());
		  this.serverMainView.addStopServerButtonListener(e -> stopServer());
		  this.serverMainView.addLogoutButtonListener(e -> logout());
	 } // end of ServerMainController constructor

	 private void manageUsers() {
		  serverMainView.dispose();
		  new ManageUsersView();
	 } // end of manageUsers method

	 private void clearLog() {
		  serverMainView.clearLogText();
	 } // end of clearLog method

	 private void startServer() {
		  Servant server = new Servant();
		  server.execute();
	 } // end of startServer method

	 private void stopServer() {
		  ServerSocket serverSocket = ServerHolder.getServerSocket();
		  ServerHandler.stopServer(serverSocket);
	 } // end of stopServer method

	 private void logout() {
		  int result = JOptionPane.showConfirmDialog(serverMainView,
																	" Are you sure you want to logout?",
																	"Logout?",
																	JOptionPane.YES_NO_OPTION,
																	JOptionPane.QUESTION_MESSAGE);
		  if (result == JOptionPane.YES_OPTION) {
				new AdminLoginView();
				serverMainView.dispose();
		  }
	 } // end of logout method
}
