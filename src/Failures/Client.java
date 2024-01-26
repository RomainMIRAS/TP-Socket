package Failures;

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
 * Client -> Envoie du nombre de bloc de 512 lu si il viens d'un récupération d'erreur
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
		String dataPathClient = prop.getProperty("dataPathClient");
		int portServer = Integer.parseInt(prop.getProperty("portServer"));

		System.out.println("Client is running...");
		System.out.println("IP Server: " + ipServer);
		System.out.println("Port Server: " + portServer);
		
		// connect to server
		Socket soc = new Socket(ipServer, portServer);
		
		// envoie mon nom
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		String nomfichier = "bigfile.txt";
		dos.writeUTF(nomfichier);
		
		// recevoir code retour
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		int code = dis.readInt();
		switch (code) {
		case 401:
			System.out.println("File found : " + nomfichier);
			
			// lire block de 512 octets
			byte[] buffer = new byte[512];
			int nbOctetsLus = 0;
			File file = new File(dataPathClient + nomfichier);
			FileOutputStream fos = new FileOutputStream(file);

			while ((nbOctetsLus = dis.read(buffer)) != -1) {
				fos.write(buffer, 0, nbOctetsLus);
			}

			fos.close();
			System.out.println("Fin de Client");
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
