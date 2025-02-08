package server.controller;

import server.model.AdminLoginModel;
import server.view.AdminLoginView;
import server.view.ServerMainView;

public class AdminLoginController {
	 private final AdminLoginView adminLoginView;
	 private final AdminLoginModel adminLoginModel;

	 public AdminLoginController (AdminLoginView adminLoginView, AdminLoginModel adminLoginModel) {
		  this.adminLoginView = adminLoginView;
		  this.adminLoginModel = adminLoginModel;

		  // Admin Login Action Listeners
		  this.adminLoginView.addLoginButtonListener(e -> login());
	 }

	 private void login() {
		  String username = adminLoginView.getUsername();
		  char[] password = adminLoginView.getPassword();

		  if ( adminLoginModel.isValidCredentials(username, password)) {
				// Successful Login
				ServerMainView serverMainView = new ServerMainView();
				new ServerMainController(serverMainView);
				adminLoginView.dispose();
		  } else {
				// Failed Login
				adminLoginView.showError("Login Failed");
		  }

		  // Clear password field for security
		  adminLoginView.clearPassword();
	 }
}
