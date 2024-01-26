package Failures;

import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ProdCons {
	
	private int sizeBuff;
	private Semaphore plein;
	private Semaphore vide;
	private Semaphore mutex;
	private Socket[] buffer;
	int in = 0;
	int out = 0;
	
	int totalM = 0;

	public ProdCons(int sizeBuff) {
		this.sizeBuff = sizeBuff;
		buffer = new Socket[sizeBuff];
		vide = new Semaphore(sizeBuff);
		plein = new Semaphore(0);
		mutex = new Semaphore(1);
	}

	public void put(Socket soc) throws InterruptedException {
		vide.acquire();
		mutex.acquire();
		buffer[in] = soc;
		in = (in + 1)% sizeBuff;
		totalM++;
		mutex.release();
		// one more not empty entry
		plein.release();
	}
	
	public synchronized Socket get() throws InterruptedException {
		plein.acquire();
		mutex.acquire();
		Socket soc = buffer[out];
		out = (out + 1)% sizeBuff;
		mutex.release();
		// one more not empty entry
		vide.release();
		
		return soc;
	}
	
}
