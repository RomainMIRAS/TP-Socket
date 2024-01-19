package BabyStep;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Client {

	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();

		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		String ipServer = prop.getProperty("ipServer");
		int portServer = Integer.parseInt(prop.getProperty("portServer"));

		System.out.println("Server is running...");
		System.out.println("IP: " + ipServer);
		System.out.println("Port: " + portServer);
		
		// connect to server
		Socket soc = new Socket(ipServer, portServer);
		
		// envoie mon nom
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeUTF("Louane");
		
		// recevoir message
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		String message = dis.readUTF();
		System.out.println("Message: "+message);
	}

}
