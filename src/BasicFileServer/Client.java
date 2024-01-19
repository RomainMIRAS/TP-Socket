package BasicFileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import BabyStep.Server;

public class Client {
	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();

		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		String ipServer = prop.getProperty("ipServer");
		int portServer = Integer.parseInt(prop.getProperty("portServer"));

		System.out.println("Client is running...");
		System.out.println("IP Server: " + ipServer);
		System.out.println("Port Server: " + portServer);
		
		// connect to server
		Socket soc = new Socket(ipServer, portServer);
		
		// envoie mon nom
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		String nomfichier = "";
		dos.writeUTF(nomfichier);
		
		// recevoir fichier
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		int length = dis.readInt();
		byte[] b = new byte[length];
		dis.readFully(b);
		
		FileOutputStream fos = new FileOutputStream("new"+nomfichier);
		fos.write(b);
		
		fos.close();
	}
}
