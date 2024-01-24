package BasicFileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		String nomfichier = "./src/file1.txt";
		dos.writeUTF(nomfichier);
		
		// recevoir code retour
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		int code = dis.readInt();
		switch (code) {
		case 401:
			System.out.println("File found : " + nomfichier);
			
			// recevoir fichier
			int length = dis.readInt();
			byte[] b = new byte[length];
			dis.readFully(b);
			
			// créer fichier et écrire
			FileOutputStream fos = new FileOutputStream(nomfichier+"Client");
			fos.write(b);
			
			fos.close();
			
			break;
		case 404:
			System.out.println("File not found : " + nomfichier);
			// Sortie de programme
			return;
		case 403:
			System.out.println("Access refused");
			return;
		default:
			return;
		}
	}
}
