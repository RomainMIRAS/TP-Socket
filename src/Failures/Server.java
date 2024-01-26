package Failures;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;


/**
 * 
 * @author mirasr
 * 
 * Envoie des paquets : 
 * Client -> Envoie du nombre de bloc de 512 lu si il viens d'un récupération d'erreur
 * Client -> Demande d'un nom de fichier
 * Server -> Envoie un code de retour
 * 		401 - GOOD -> Envoie du fichier
 * 		404 - NOT FOUND -> FIN D'EXECUTION
 * 		403 - Access Refused -> FIN D'EXECUTION
 */
public class Server {

	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException, InterruptedException {
		Properties prop = new Properties();
		prop.loadFromXML(Server.class.getClassLoader().getResourceAsStream("info.xml"));
		int portServer = Integer.parseInt(prop.getProperty("portServer"));
		int numberServerThread = Integer.parseInt(prop.getProperty("numberServerThread"));
		int buffSize = Integer.parseInt(prop.getProperty("buffSize"));
		String dataPathServer = prop.getProperty("dataPathServer");

		System.out.println("Server is running...");

		ProdCons pc = new ProdCons(buffSize); // Can use ArrayBlockingQueue instead

		ServerSocket listenSoc = new ServerSocket(portServer);
		for (int i = 0; i < numberServerThread; i++) {
			ServerThread t = new ServerThread(dataPathServer,pc);
			t.start();
		}
		
		while(true) {
			Socket soc = listenSoc.accept();
			if (soc == null) continue;;
			pc.put(soc);
		} 
	}

}
