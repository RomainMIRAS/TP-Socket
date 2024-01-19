package BabyStep;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Server {
	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();
		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		String ipServer = prop.getProperty("ipServer");
		int portServer = Integer.parseInt(prop.getProperty("portServer"));
		
		System.out.println("Server is running...");
		System.out.println("IP: " + ipServer);
		System.out.println("Port: " + portServer);
	}

}
