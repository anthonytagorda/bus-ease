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

public class ClientHandler implements Runnable {
	 private volatile boolean isServerShutdown = false;
	 protected final String CLIENT_CREDENTIAL_FILE_PATH = "src/server/model/users/client.xml";

	 @Override
	 public void run ( ) {

	 }

	 public void setServerShutdown (boolean serverShutdown) {
		  this.isServerShutdown = serverShutdown;
	 }

	 public void updateClient(int clientId, String newType, String newUsername, String newPassword, String newName) {
		  try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new File(CLIENT_CREDENTIAL_FILE_PATH));

				// Find the client with the specified ID
				NodeList clientList = doc.getElementsByTagName("client");
				for (int i = 0; i < clientList.getLength(); i++) {
					 Element clientElement = (Element) clientList.item(i);
					 int id = Integer.parseInt(clientElement.getAttribute("id"));

					 if (id == clientId) {
						  updateField(clientElement, "type", newType);
						  updateField(clientElement, "username", newUsername);
						  updateField(clientElement, "name", newName);
						  updateField(clientElement, "password", newPassword);
						  break;
					 }
				}
				// Save changes to XML file
				saveDocumentToFile(doc, CLIENT_CREDENTIAL_FILE_PATH);
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
	 }

	 private static void updateField (Element clientElement, String fieldName, String newValue) {
		  Element fieldElement = (Element) clientElement.getElementsByTagName(fieldName).item(0);
		  fieldElement.setTextContent(newValue);
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

	 public boolean deleteClient(int clientId) {
		  try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbf.newDocumentBuilder();
				Document doc = dBuilder.parse(new File(CLIENT_CREDENTIAL_FILE_PATH));

				NodeList clientList = doc.getElementsByTagName("client");
				for (int i = 0; i < clientList.getLength(); i++) {
					 Element clientElement = (Element) clientList.item(i);
					 int id = Integer.parseInt(clientElement.getAttribute("id"));

					 if (id == clientId) {
						  // Delete the client element
						  clientElement.getParentNode().removeChild(clientElement);
						  // Save changes back to XML file
						  saveDocumentToFile(doc, CLIENT_CREDENTIAL_FILE_PATH);
						  return true; // Deletion Successful
					 }
				}
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
		  return false; // Deletion Failed
	 }

	 public void changeBanStatus(int clientId, boolean isBanned) {
		  try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new File(CLIENT_CREDENTIAL_FILE_PATH));

				NodeList clientList = doc.getElementsByTagName("client");
				for (int i = 0; i < clientList.getLength(); i++) {
					 Element clientElement = (Element) clientList.item(i);
					 int id = Integer.parseInt(clientElement.getAttribute("id"));

					 if (id == clientId) {
						  clientElement.setAttribute("banned", String.valueOf(!isBanned));
						  break; // Stop searching once the client is found and updated
					 }
				}
				// Save changes back to the XML file
				saveDocumentToFile(doc, CLIENT_CREDENTIAL_FILE_PATH);
		  } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
				throw new RuntimeException(e);
		  }
	 }

}
