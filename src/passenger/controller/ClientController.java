package passenger.controller;

import passenger.view.LoginView;
import passenger.view.RegisterView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientController {
	 private final Socket socket;
	 private ObjectOutputStream writer;
	 private ObjectInputStream reader;

	 private LoginView loginView;
	 private RegisterView registerView;

	 private final Map<String, String> passengerDetails = new HashMap<>();

	 public ClientController (Socket socket) {
		  this.socket = socket;
		  initiateStream();
	 }

	 private void initiateStream() {
		  try {
				writer = new ObjectOutputStream(socket.getOutputStream());
				reader = new ObjectInputStream(socket.getInputStream());

				// Test 1: Send a message to server
				String message = "Hello Server!";
				writer.writeObject(message);
				writer.flush();

				// Test 2: Receive a response
				Object reply = reader.readObject();
				System.out.printf("Received from server: %s%n", reply);

				JFrame authFrame = new JFrame();
				showAuthView(authFrame);

				Thread clientThread = new Thread(() -> {
					 try {
						  boolean closeFlag = true;

						  while (closeFlag) {
								// Read Response from the server
								Map <String, String> input = (Map<String, String>) reader.readObject();

								// Process the response (update UI, perform actions, etc.)
								System.out.printf("Received from server: %s%n", input);
								String msg = input.get("value");

								switch (input.get("messType")) {
									 case "auth" -> {
										  boolean result = msg.isEmpty();
										  if ( result ) {
												loginView.showValidLoginDialog();
												passengerDetails.putAll(input);
										  } else {
												loginView.showInvalidLoginDialog(msg);
										  }
									 }
									 case "register" -> {
										  String[] registerResponse = msg.split(":");
										  if (Boolean.valueOf(registerResponse[0])) {
												registerView.showValidRegisterDialog(registerResponse[1]);
										  } else {
												registerView.showInvalidRegisterDialog(registerResponse[1]);
										  }
									 }
									 case "logout" -> closeFlag = false;
									 default -> System.out.println("Invalid server message.");
								}
						  }

						  try {
								authFrame.dispose();
								writer.close();
								reader.close();
								socket.close();
						  } catch (IOException e) {
								throw new RuntimeException(e);
						  }

					 } catch (IOException | ClassNotFoundException e) {
						  authFrame.dispose();
						  System.err.println("Server stopped");
					 }
				});
				clientThread.start();
				// end of clientThread
		  } catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(
						  null,
						  "Server is not available.",
						  "Error 504",
						  JOptionPane.ERROR_MESSAGE);
				System.err.println("Server is not running");
		  }
	 } // end of initiateStream method

	 public void sendToServer(Object object) {
		  try {
				writer.writeObject(object);
				writer.flush();
		  } catch (IOException e) {
				throw new RuntimeException(e);
		  }
	 } // end of sendToServer method

	 public Map<String, String> formatMessage(String messType, String message) {
		  Map<String, String> messages = new HashMap<>();
		  messages.put("messType", messType);
		  messages.put("value", message);
		  return messages;
	 } // end of formatMessage method

	 private void showAuthView(JFrame authFrame) {
		  authFrame.setTitle("Bus Ease | Passenger Authentication");
		  authFrame.setSize(960, 540);
		  authFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  authFrame.setResizable(false);
		  authFrame.setLocationRelativeTo(null);
		  ImageIcon icon = new ImageIcon("src/public/icons/bus_ease_icon.png");
		  authFrame.setIconImage(icon.getImage());

		  CardLayout authLayout = new CardLayout();
		  JPanel authPanel = new JPanel(authLayout);

		  loginView = new LoginView(authPanel, authLayout);
		  registerView = new RegisterView(authPanel, authLayout);

		  authPanel.add(loginView,"Login");
		  authPanel.add(registerView,"Register");

		  authLayout.show(authPanel, "Login"); // Default Panel

		  authFrame.add(authPanel);
		  authFrame.setVisible(true);

		  JButton loginButton = loginView.getLoginButton();
		  loginButton.addActionListener(_ -> {
				String username = loginView.getUsernameInput();
				char[] password = loginView.getPasswordInput();

				sendToServer(formatMessage(
								"auth",
								String.format("%s:::%s:::%s",
												  "passenger",
												  username,
												  new String(password)
								)
				));
		  }); // login button

		  JButton registerButton = registerView.getRegisterButton();
		  registerButton.addActionListener(_ -> {
				if (registerView.validateInput()) {
					 String username = registerView.getUsernameInput();
					 String name = registerView.getNameInput();
					 String password = new String(registerView.getPasswordInput());
					 sendToServer(formatMessage(
								"register",
								String.format(
										  "%s:::%s:::%s:::%s:::%s",
										  "passenger",
										  name,
										  username,
										  password
								)
					 ));
				}
		  }); // register button
	 } // end of showAuthView
} // end of ClientController class