package server.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AdminLoginModel {
	 public boolean isValidCredentials(String username, char[] password) {
		  try {
				ClassLoader classLoader = getClass().getClassLoader();
				InputStream inputStream = classLoader.getResourceAsStream("server/model/users/admin.xml");

				// Check if the InputStream is null
				if (inputStream == null) {
					 System.err.println("Error: InputStream is null. Check if the XML file exists at the specified path.");
					 return false;
				}

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(inputStream);
				doc.getDocumentElement().normalize();

				NodeList adminNodes = doc.getElementsByTagName("admin");
				for (int i = 0; i < adminNodes.getLength(); i++) {
					 Node node = adminNodes.item(i);

					 if (node.getNodeType() == Node.ELEMENT_NODE) {
						  Element adminElement = (Element) node;

						  if (adminElement.getAttribute("type").equals("admin")) {
								String u = adminElement.getElementsByTagName("username").item(0).getTextContent();
								String p = adminElement.getElementsByTagName("password").item(0).getTextContent();

								// Check if the entered credentials match any of the admin users
								if (username.trim().equals(u) && Arrays.equals(password, p.toCharArray())) {
									 return true;
								}
						  }
					 }
				}
		  } catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
		  }
		  return false;
	 } // end of isValidCredentials method
} // end of AdminLoginModel class