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

public class Client implements Serializable {
	 private int id;
	 private String type;
	 private String username;
	 private String password;
	 private String name;
	 private boolean isBanned;

	 public Client (int id, String type, String username, String password, String name, boolean isBanned) {
		  this.id = id;
		  this.type = type;
		  this.username = username;
		  this.password = password;
		  this.name = name;
		  this.isBanned = isBanned;
	 }

	 public static List<Client> parseCredentials(String filePath) {
		  List<Client> clientList = new ArrayList<>();

		  try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new File(filePath));
							doc.getDocumentElement().normalize();
				NodeList clientNodes = doc.getElementsByTagName("client");

				for (int i = 0; i < clientNodes.getLength(); i++) {
					 Element clientElement = (Element) clientNodes.item(i);

					 int id = Integer.parseInt(clientElement.getElementsByTagName("id").item(0).getTextContent());
					 String type = clientElement.getAttribute("type");
					 String username = clientElement.getElementsByTagName("username").item(0).getTextContent();
					 String password = clientElement.getElementsByTagName("password").item(0).getTextContent();
					 String name = clientElement.getElementsByTagName("name").item(0).getTextContent();
					 boolean isBanned = Boolean.parseBoolean(clientElement.getElementsByTagName("isBanned").item(0).getTextContent());

					 Client client = new Client(id, type, username, password, name, isBanned);
					 clientList.add(client);
				}
		  } catch (ParserConfigurationException | IOException | SAXException e) {
				throw new RuntimeException(e);
		  }
		  return clientList;
	 }

	 public int getId ( ) {
		  return id;
	 }

	 public void setId (int id) {
		  this.id = id;
	 }

	 public String getType ( ) {
		  return type;
	 }

	 public void setType (String type) {
		  this.type = type;
	 }

	 public String getUsername ( ) {
		  return username;
	 }

	 public void setUsername (String username) {
		  this.username = username;
	 }

	 public String getPassword ( ) {
		  return password;
	 }

	 public void setPassword (String password) {
		  this.password = password;
	 }

	 public String getName ( ) {
		  return name;
	 }

	 public void setName (String name) {
		  this.name = name;
	 }

	 public boolean isBanned ( ) {
		  return isBanned;
	 }

	 public Map<String, String> toMap() {
		  Map<String, String> map = new HashMap<>();
		  map.put("id", String.valueOf(id));
		  map.put("type", type);
		  map.put("username", username);
		  map.put("password", password);
		  map.put("name", name);
		  map.put("isBanned", String.valueOf(isBanned));
		  return map;
	 }
}
