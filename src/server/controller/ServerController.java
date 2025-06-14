package server.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.model.users.Passenger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerController extends PassengerController implements Runnable {
	 private final Socket clientSocket;
	 private final Thread serverThread;
	 private Passenger passenger;
	 private int userType = - 1; // 0 = passenger
	 private volatile boolean isServerShutdown = false;

	 public ServerController (Socket clientSocket) {
		  super();
		  this.clientSocket = clientSocket;
		  this.serverThread = new Thread(this);
		  this.serverThread.start();
	 }

	 @Override
	 public void run ( ) {
		  try {
				ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

				// Read input from client
				Object objectInput = objectInputStream.readObject();
				System.out.printf("Received from client: %s%n", objectInput);

				// Write response to client
				String response = "Server received: %s\n".formatted(objectInput);
				objectOutputStream.writeObject(response);
				objectOutputStream.flush();

				while (! clientSocket.isClosed() && ! isServerShutdown) {
					 try {
						  // Read an object from the client
						  Map<String, String> input = (Map<String, String>) objectInputStream.readObject();
						  if ( input == null ) {
								break; // Client disconnected
						  }

						  System.out.printf("Received from client: %s\n%n", input);
						  String msg = input.get("value");

						  switch (input.get("messType")) {
								case "auth" -> {
									 String[] loginInput = msg.split(":::");

									 String result = "";
									 if ( loginInput[0].equals("passenger") ) {
										  result = validatePassengerLogin(loginInput[1], loginInput[2]);
										  userType = 0;
									 }

									 Map<String, String> resp = formatSimpleMessage("auth", result);
									 if ( result.isEmpty() ) {
										  if ( userType == 0 ) {
												resp.putAll(passenger.toMap()); // append passenger details to xml file
										  }
										  resp.remove("password"); // do not send password
									 }
									 objectOutputStream.writeObject(resp);
									 objectOutputStream.flush();
								} // end of "auth" case

								case "register" -> {
									 String[] regInput = msg.split(":::");

									 if ( regInput[4].equals("passenger") ) {
										  objectOutputStream.writeObject(formatSimpleMessage("register", registerPassengerDetails(regInput[1], regInput[2], regInput[3])));
									 }

									 objectOutputStream.flush();

								} // end of "register" case

								case "logout" -> {
									 objectOutputStream.writeObject(formatSimpleMessage("logout", "true"));
									 objectOutputStream.flush();

									 if ( msg.equals("passenger") )
										  System.out.printf("Logging out: %s%n", passenger.getName());
									 objectInputStream.close();
									 objectOutputStream.close();
									 this.serverThread.interrupt();
									 return;
								} // end of "logout" case

								case "edit" -> {
									 Map<String, String> editResult = handleEditPassengerDetails(input);
									 editResult.put("messType", "edit");
									 objectOutputStream.writeObject(editResult);
									 objectOutputStream.flush();
								}

								case "getSchedules" -> {
									 // TODO -> get all schedules
								}

								case "newSchedule" -> {
									 // TODO -> create new schedule
								}

								case "getAvailableSchedule" -> {
									 // TODO -> get available schedules
								}

								default -> {

								}
						  } // end of switch statement
					 } catch (ClassNotFoundException ce) {
						  throw new RuntimeException(ce);
					 }
				}
		  } catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException(e);
		  } finally {
				try {
					 clientSocket.close();
				} catch (IOException e) {
					 new RuntimeException(e);
				}
		  }
	 } // end of run method

	 private Map<String, String> formatSimpleMessage (String messType, String message) {
		  Map<String, String> object = new HashMap<>();
		  object.put("messType", messType);
		  object.put("value", message);
		  return object;
	 } // end of formatSimpleMessage method

	 public void setServerShutdown (boolean serverShutdown) {
		  this.isServerShutdown = serverShutdown;
	 } // end of setServerShutdown method

	 public String validatePassengerLogin (String username, String password) {
		  try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document document = dBuilder.parse(new File(PASSENGER_CREDENTIAL_FILE_PATH));
				document.getDocumentElement().normalize();
				NodeList passengerNodes = document.getElementsByTagName("passenger");

				for (int i=0; i<passengerNodes.getLength(); i++) {
					 Node passengerNode = passengerNodes.item(i);

					 if (passengerNode.getNodeType() == Node.ELEMENT_NODE) {
						  Element passengerElement = (Element) passengerNode;

						  int id = Integer.parseInt(passengerElement.getAttribute("id"));
						  String user = passengerElement.getElementsByTagName("username").item(0).getTextContent();
						  String pass = passengerElement.getElementsByTagName("password").item(0).getTextContent();
						  String name = passengerElement.getElementsByTagName("name").item(0).getTextContent();
						  String type = passengerElement.getAttribute("type");
						  String status = passengerElement.getAttribute("status");

						  if (username.trim().equals(user) && password.trim().equals(pass)) {
								boolean isBanned = Boolean.parseBoolean(passengerElement.getAttribute("isBanned"));

								if ( isBanned ) {
									 return "Banned. Cannot login account. ";
								}

								passenger = new Passenger(id, isBanned, type, status, user, pass, name);
								return "";
						  }
					 }
				}
		  } catch (ParserConfigurationException | IOException | SAXException e) {
				throw new RuntimeException(e);
		  }
		  return "Passenger does not exist";
	 } // end of validatePassengerLogin method
} // end of ServerController class
