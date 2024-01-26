package MultiThreadedFileServer.v1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;


/**
 * 
 * @author mirasr
 * 
 * Envoie des paquets : 
 * Client -> Demande d'un nom de fichier
 * Server -> Envoie un code de retour
 * 		401 - GOOD -> Envoie du fichier
 * 		404 - NOT FOUND -> FIN D'EXECUTION
 * 		403 - Access Refused -> FIN D'EXECUTION
 */
public class Server {

	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
		Properties prop = new Properties();
		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		int portServer = Integer.parseInt(prop.getProperty("portServer"));
		String dataPathServer = prop.getProperty("dataPathServer");
		
		System.out.println("Server is running...");

		ServerSocket listenSoc = new ServerSocket(portServer);
		while(true) {
			Socket soc = listenSoc.accept();
			if (soc == null) continue;;
			ServerThread t = new ServerThread(dataPathServer,soc);
			t.start();
		} 
	}

}
