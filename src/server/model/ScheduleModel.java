package server.model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;

public class ScheduleModel {
	 public static Object[][] loadSchedules(String filePath) {
		  ArrayList<Object[]> schedules = new ArrayList<>();

		  try {
				File xmlFile = new File(filePath);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("schedule");

				for (int i = 0; i < nList.getLength(); i++) {
					 Element schedule = (Element) nList.item(i);
					 String busNo = schedule.getElementsByTagName("busNo").item(0).getTextContent();
					 String type = schedule.getElementsByTagName("type").item(0).getTextContent();
					 String date = schedule.getElementsByTagName("date").item(0).getTextContent();
					 String time = schedule.getElementsByTagName("time").item(0).getTextContent();
					 String origin = schedule.getElementsByTagName("origin").item(0).getTextContent();
					 String destination = schedule.getElementsByTagName("destination").item(0).getTextContent();
					 String distance = schedule.getElementsByTagName("distance").item(0).getTextContent();
					 String slots = schedule.getElementsByTagName("slots").item(0).getTextContent();

					 schedules.add(new Object[]{busNo, type, date, time, origin, destination, distance, slots});
				}
		  } catch (Exception e) {
				e.printStackTrace();
		  }

		  // Convert ArrayList to Object[][]
		  return schedules.toArray(new Object[0][]);
	 }
}
