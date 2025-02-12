package server.controller;

import server.view.ManageUsersView;
import server.view.ServerMainView;

public class ManageUsersController {
	 private final ManageUsersView manageUsersView;

	 public ManageUsersController (ManageUsersView manageUsersView) {
		  this.manageUsersView = manageUsersView;

		  // Manage Users Action Listeners
		  this.manageUsersView.addReturnButtonListener(e -> serverMain());
	 } // end of ManageUsersController constructor

	 private void serverMain() {
		  manageUsersView.dispose();
		  new ServerMainView();
	 }

} // end of ManageUsersController class
