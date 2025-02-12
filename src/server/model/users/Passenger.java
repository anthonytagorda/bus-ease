package server.model.users;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Passenger implements Serializable {
	 private int id;
	 private boolean isBanned;
	 private String type;
	 private String status;
	 private String username;
	 private String name;
	 private String password;

	 public Passenger (int id, String type, String status, String username, String password, String name, boolean isBanned) {
		  this.id = id;
		  this.isBanned = isBanned;
		  this.type = type;
		  this.status = status;
		  this.username = username;
		  this.name = name;
		  this.password = password;
	 }

	 public int getId ( ) {
		  return id;
	 }

	 public void setId (int id) {
		  this.id = id;
	 }

	 public boolean isBanned ( ) {
		  return isBanned;
	 }

	 public void setBanned (boolean banned) {
		  isBanned = banned;
	 }

	 public String getType ( ) {
		  return type;
	 }

	 public String getUsername ( ) {
		  return username;
	 }

	 public void setUsername (String username) {
		  this.username = username;
	 }

	 public String getName ( ) {
		  return name;
	 }

	 public void setName (String name) {
		  this.name = name;
	 }

	 public String getPassword ( ) {
		  return password;
	 }

	 public void setPassword (String password) {
		  this.password = password;
	 }

	 public static List<Passenger> parseCredentials(String filePath) {
		  List<Passenger> passengers = new ArrayList<>();

		  try {
				File xmlFile = new File(filePath);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(xmlFile);
							doc.getDocumentElement().normalize();
				NodeList passengerNodes = doc.getElementsByTagName("passenger");

				for (int i = 0; i < passengerNodes.getLength(); i++) {
					 Element passengerElement = (Element) passengerNodes.item(i);

					 int id = Integer.parseInt(passengerElement.getAttribute("id"));
					 boolean banned = Boolean.parseBoolean(passengerElement.getAttribute("isBanned"));
					 String type = passengerElement.getAttribute("type");
					 String status = passengerElement.getAttribute("status");
					 String username = passengerElement.getElementsByTagName("username").item(0).getTextContent();
					 String name = passengerElement.getElementsByTagName("name").item(0).getTextContent();
					 String password = passengerElement.getElementsByTagName("password").item(0).getTextContent();

					 Passenger passenger = new Passenger(id, type, status, username, password, name, banned);
					 passengers.add(passenger);
				}
		  } catch (ParserConfigurationException | IOException | SAXException e) {
				throw new RuntimeException(e);
		  }
		  return passengers;
	 }
}
