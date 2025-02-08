package server;

import server.controller.AdminLoginController;
import server.model.AdminLoginModel;
import server.view.AdminLoginView;

public class Server {
	 public static void main (String[] args) {
		  AdminLoginView adminLoginView = new AdminLoginView();
		  AdminLoginModel adminLoginModel = new AdminLoginModel();
		  new AdminLoginController(adminLoginView, adminLoginModel);
	 }
}
