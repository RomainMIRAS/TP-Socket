package ConsideringLargeFiles;

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
		dos.writeInt(0);
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
			int numberOfReadedBlocks = 0;

			do {
				try {
					nbOctetsLus = dis.read(buffer);
					numberOfReadedBlocks++;
				} catch (IOException e) { // si le serveur est mort 
					/**
					 * 1. Fermer le socket
					 * 2. Créer un nouveau socket
					 * 3. Envoyer le nombre de blocks déjà lus
					 * 4. Envoyer le nom du fichier
					 * 5. Attendre le code retour
					 * 6. Si code retour == 401, continuer la lecture
					 * 7. Sinon, afficher le message d'erreur et sortir du programme
					 */
					
					 /**
					  * Solution pas très élégante, mais qui marche
					  */
					System.out.println("Server is dead");
					soc.close();
					soc = new Socket(ipServer, portServer);
					os = soc.getOutputStream();
					dos = new DataOutputStream(os);
					dos.writeInt(numberOfReadedBlocks);
					dos.writeUTF(nomfichier);
					is = soc.getInputStream();
					dis = new DataInputStream(is);
					code = dis.readInt();
					if (code == 401) {
						nbOctetsLus = dis.read(buffer);
					} else {
						System.out.println("File not found : " + nomfichier);
						// Sortie de programme
						return;
					}
				}				
			} while (nbOctetsLus != -1);

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
