package BasicFileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


			DataInputStream dis = null;
			DataOutputStream dos = null;


			InputStream is = soc.getInputStream();
			dis = new DataInputStream(is);
			String fileName = dis.readUTF();

			OutputStream os = soc.getOutputStream();
			FileInputStream fis = null;
			dos = new DataOutputStream(os);

			try {
				fis = new FileInputStream(dataPathServer+fileName);
				dos.writeInt(401);
				System.out.println("File found : " + fileName);
			} catch (FileNotFoundException e) {
				dos.writeInt(404);
				System.out.println("File not found : " + fileName);
				continue;
			} catch (SecurityException e) {
				dos.writeInt(403);
				System.out.println("Access refused : " + fileName);
				continue;
			} catch (NullPointerException e) {
				dos.writeInt(404);
				System.out.println("File not found : " + fileName);
				continue;
			}
			

			byte[] data = fis.readAllBytes();
			dos.writeInt(data.length);
			dos.write(data);
			
			fis.close();
			System.out.println("Sending " + fileName + " Done...");
		} 
	}

}
