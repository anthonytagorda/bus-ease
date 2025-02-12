package server.controller.handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.io.File;
import java.io.IOException;

public class PassengerHandler {
	 protected final String PASSENGER_CREDENTIAL_FILE_PATH;

	 public PassengerHandler ( ) {
		  PASSENGER_CREDENTIAL_FILE_PATH = "src/server/model/users/passenger.xml";
	 }

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
