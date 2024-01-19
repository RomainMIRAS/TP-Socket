package BabyStep;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Server {
	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();
		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		int portServer = Integer.parseInt(prop.getProperty("portServer"));

		System.out.println("Server is running...");

		ServerSocket listenSoc = new ServerSocket(portServer);

		Socket soc = listenSoc.accept();
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try {
			InputStream is = soc.getInputStream();
			dis = new DataInputStream(is);
			String clientName = dis.readUTF();
			
			OutputStream os = soc.getOutputStream();
			dos = new DataOutputStream(os);
			dos.writeUTF("Hello " + clientName);
		} catch ( Exception e ) {

		} finally {

			if (dis != null) try { dis.close();} catch (Exception e) {}

		}
	}

}
