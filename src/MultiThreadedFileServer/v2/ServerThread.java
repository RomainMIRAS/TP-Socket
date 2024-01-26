package MultiThreadedFileServer.v2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	String dataPathServer;
	ProdCons clientsBuffer;

	public ServerThread(String dataPathServer, ProdCons clientsBuffer) {
		this.dataPathServer = dataPathServer;
		this.clientsBuffer = clientsBuffer;
	}

	@Override
	public void run(){
		while (true) {

			Socket soc;
			try {
				soc = clientsBuffer.get();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				continue;
			}
			
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
}
