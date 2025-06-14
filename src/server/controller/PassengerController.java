package server.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.model.users.Passenger;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PassengerController {
	 protected final String PASSENGER_CREDENTIAL_FILE_PATH;
	 private Passenger passenger;

	 public PassengerController ( ) {
		  PASSENGER_CREDENTIAL_FILE_PATH = "src/server/model/users/passenger.xml";
	 }

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
		  return "User does not exist";
	 } // end of validatePassengerLogin method

	 public String registerPassengerDetails (String username, String name, String password) {
		  try {
				// Load existing passenger.xml from the specified path
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(PASSENGER_CREDENTIAL_FILE_PATH);

				// Get the root element
				Element root = doc.getDocumentElement();

				// Get the last passenger's ID to increment to the next available ID
				int maxId = getMaxPassengerId(doc);

				// Check if the username already exist
				NodeList nodeList = doc.getElementsByTagName("passenger");
				for (int i = 0; i < nodeList.getLength(); i++) {
					 Element passengerElement = (Element) nodeList.item(i);
					 String existingUsername = passengerElement.getElementsByTagName("username").item(0).getTextContent();
					 if (existingUsername.equals(username)) {
						  return "false: Username already exists";
					 }
				}

				// If username does not exist, proceed with creation
				Element passenger = doc.createElement("passenger");
				passenger.setAttribute("id", Integer.toString(maxId + 1));
				passenger.setAttribute("isBanned", "false");
				passenger.setAttribute("type", "passenger");

				// Username
				Element usernameElement = doc.createElement("username");
				usernameElement.appendChild(doc.createTextNode(username));
				passenger.appendChild(usernameElement);

				// Password
				Element passwordElement = doc.createElement("password");
				passwordElement.appendChild(doc.createTextNode(password));
				passenger.appendChild(passwordElement);

				// Name
				Element nameElement = doc.createElement("name");
				nameElement.appendChild(doc.createTextNode(name));
				passenger.appendChild(nameElement);

				// Append client element to the root element
				root.appendChild(passenger);

				// Write the updated document back to the same xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("fix.xsl")));
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");

				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(PASSENGER_CREDENTIAL_FILE_PATH);
				transformer.transform(source, result);

				return "Registration Successful!";
		  } catch (ParserConfigurationException | IOException | SAXException e) {
				System.err.println("Registration Failed!");
				return "false:Error occurred while registering";
		  } catch (TransformerException e) {
				throw new RuntimeException(e);
		  }
	 } // end of registerPassengerDetails

	 private boolean isUsernameTaken (String newUsername) {
		  try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new File(PASSENGER_CREDENTIAL_FILE_PATH));
				doc.getDocumentElement().normalize();
				NodeList userNodes = doc.getElementsByTagName("passenger");

				for (int i = 0; i < userNodes.getLength(); i++) {
					 Node node = userNodes.item(i);

					 if (node.getNodeType() == Node.ELEMENT_NODE) {
						  Element element = (Element) node;
						  String xmlUsername = element.getElementsByTagName("username").item(0).getTextContent();

						  if (xmlUsername.equals(newUsername)) {
								return true;
						  }
					 }
				}
		  } catch (ParserConfigurationException | IOException | SAXException e) {
				throw new RuntimeException(e);
		  }
		  return false;
	 } // end of isUsernameTaken method

	 public Map<String, String> handleEditPassengerDetails (Map<String, String> editMap) {
		  Map<String, String> res = new HashMap<>();
		  if (editMap.get("oldPassword").equals(passenger.getPassword()) ) {
				if ( !isUsernameTaken(editMap.get("newUsername")) ) {
					 updateUsernameAndPassword(editMap.get("newUsername"), editMap.get("newPassword"));
					 res.put("value", "true");
					 res.put("msg", "Username and Password changed successfully.");
				} else {
					 res.put("value", "false");
					 res.put("msg", "Username is already taken. Please choose a different username.");
				}
		  } else {
				res.put("value", "false");
				res.put("msg", "Incorrect old password");
		  }
		  return res;
	 } // end of handleEditPassengerDetails

	 private void updateUsernameAndPassword(String newUsername, String newPassword) {
		  try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(PASSENGER_CREDENTIAL_FILE_PATH);
				doc.getDocumentElement().normalize();

				// Update the username password in the xml file for the "logged-in" user
				NodeList nodeList = doc.getElementsByTagName("passenger");
				for (int i = 0; i < nodeList.getLength(); i++) {
					 Node node = nodeList.item(i);
					 if (node.getNodeType() == Node.ELEMENT_NODE) {
						  Element element = (Element) node;
						  String xmlUsername = element.getElementsByTagName("username").item(0).getTextContent();
						  if (xmlUsername.equals(passenger.getUsername())) {
								element.getElementsByTagName("username").item(0).setTextContent(newUsername);
								element.getElementsByTagName("password").item(0).setTextContent(newPassword);
								break;
						  }
					 }
				}

				// Write the updated back to the file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("fix.xsl")));
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(PASSENGER_CREDENTIAL_FILE_PATH));
				transformer.transform(source,result);
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
	 } // end of updateUsernameAndPassword method


	 public void updatePassenger(String newUsername, String newName, String newPassword, int passengerId) {
		  try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new File(PASSENGER_CREDENTIAL_FILE_PATH));

				// Find the passenger with the specified ID
				NodeList passengerList = doc.getElementsByTagName("passenger");
				for (int i = 0; i < passengerList.getLength(); i++) {
					 Element passengerElement = (Element) passengerList.item(i);
					 int id = Integer.parseInt(passengerElement.getAttribute("id"));

					 if (id == passengerId) {
						  updateField(passengerElement, "username", newUsername);
						  updateField(passengerElement, "name", newName);
						  updateField(passengerElement, "password", newPassword);
						  break;
					 }
				}
				// Save changes to XML file
				saveDocumentToFile(doc, PASSENGER_CREDENTIAL_FILE_PATH);
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
	 }

	 private static void updateField (Element passengerElement, String fieldName, String newValue) {
		  Element fieldElement = (Element) passengerElement.getElementsByTagName(fieldName).item(0);
		  fieldElement.setTextContent(newValue);
	 }

	 public boolean deletePassenger(int passengerId) {
		  try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbf.newDocumentBuilder();
				Document doc = dBuilder.parse(new File(PASSENGER_CREDENTIAL_FILE_PATH));

				NodeList passengerList = doc.getElementsByTagName("passenger");
				for (int i = 0; i < passengerList.getLength(); i++) {
					 Element passengerElement = (Element) passengerList.item(i);
					 int id = Integer.parseInt(passengerElement.getAttribute("id"));

					 if (id == passengerId) {
						  // Delete the passenger element
						  passengerElement.getParentNode().removeChild(passengerElement);
						  // Save changes back to XML file
						  saveDocumentToFile(doc, PASSENGER_CREDENTIAL_FILE_PATH);
						  return true; // Deletion Successful
					 }
				}
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
		  return false; // Deletion Failed
	 }

	 public void changeBanStatus(int passengerId, boolean isBanned) {
		  try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new File(PASSENGER_CREDENTIAL_FILE_PATH));

				NodeList passengerList = doc.getElementsByTagName("passenger");
				for (int i = 0; i < passengerList.getLength(); i++) {
					 Element passengerElement = (Element) passengerList.item(i);
					 int id = Integer.parseInt(passengerElement.getAttribute("id"));

					 if (id == passengerId) {
						  passengerElement.setAttribute("banned", String.valueOf(!isBanned));
						  break; // Stop searching once the client is found and updated
					 }
				}
				// Save changes back to the XML file
				saveDocumentToFile(doc, PASSENGER_CREDENTIAL_FILE_PATH);
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
	 }

	 private int getMaxPassengerId (Document doc) {
		  NodeList nodeList = doc.getElementsByTagName("passenger");
		  int maxId = 0;
		  for (int i = 0; i < nodeList.getLength(); i++) {
				Element passengerElement = (Element) nodeList.item(i);
				int id = Integer.parseInt(passengerElement.getAttribute("id"));
				if ( id > maxId ) {
					 maxId = id;
				}
		  }
		  return maxId;
	 } // end of getMaxPassengerId method

	 private static void saveDocumentToFile (Document document, String filePath) throws TransformerException {
		  TransformerFactory tFactory = TransformerFactory.newInstance();
		  Transformer transformer = tFactory.newTransformer();

		  transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		  transformer.setOutputProperty(OutputKeys.INDENT, "no");
		  transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		  DOMSource source = new DOMSource(document);
		  StreamResult result = new StreamResult(new File(filePath));

		  transformer.transform(source, result);
		  JOptionPane.showMessageDialog(
					 null,
					 "XML file updated successful!",
					 "Update Success!",
					 JOptionPane.INFORMATION_MESSAGE);
	 }

}
