package server;

import server.controller.AdminLoginController;
import server.view.AdminLoginView;

public class Server {
	 public static void main (String[] args) {
		  AdminLoginView adminLoginView = new AdminLoginView();
		  new AdminLoginController(adminLoginView);
	 }
}
