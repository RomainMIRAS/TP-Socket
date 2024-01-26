package MultiThreadedFileServer.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	String dataPathServer;
	Socket soc;

	public ServerThread(String dataPathServer, Socket soc) {
		this.dataPathServer = dataPathServer;
		this.soc = soc;
	}

	@Override
	public void run(){

		DataInputStream dis = null;
		DataOutputStream dos = null;
		
		try {
			
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
				return;
			} catch (SecurityException e) {
				dos.writeInt(403);
				System.out.println("Access refused : " + fileName);
				return;
			} catch (NullPointerException e) {
				dos.writeInt(404);
				System.out.println("File not found : " + fileName);
				return;
			}

			byte[] data = fis.readAllBytes();
			dos.writeInt(data.length);
			dos.write(data);
			dos.flush();
			fis.close();
			System.out.println("Sending " + fileName + " Done...");
			
		} catch (Exception e) {
			System.err.println("Should Not Happens ! ");
		} finally {
			try {
				dis.close();
				dos.close();
				soc.close();
			} catch (Exception e) {
				System.err.println("Should Not Happens ! ");
			}
		}
	}
}
