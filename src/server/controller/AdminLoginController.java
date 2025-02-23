package server.controller;

import server.model.AdminLoginModel;
import server.view.AdminLoginView;
import server.view.ServerMainView;

public class AdminLoginController extends AdminLoginModel {
	 private final AdminLoginView adminLoginView;

	 public AdminLoginController (AdminLoginView adminLoginView) {
		  this.adminLoginView = adminLoginView;

		  // Admin Login Action Listeners
		  this.adminLoginView.addLoginButtonListener(_ -> login());
	 }

	 private void login() {
		  String username = adminLoginView.getUsername();
		  char[] password = adminLoginView.getPassword();

		  if (isValidCredentials(username, password)) {
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
