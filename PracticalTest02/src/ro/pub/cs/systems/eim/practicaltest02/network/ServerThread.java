package ro.pub.cs.systems.eim.practicaltest02.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import ro.pub.cs.systems.eim.practicaltest02.utils.TimerInformation;


public class ServerThread extends Thread {
	
	private HashMap<String, TimerInformation> data;
	private ServerSocket serverSocket;
	
	
	public ServerThread(int port) {
		this.data = new HashMap<String, TimerInformation>();
		
		try {
			this.serverSocket = new ServerSocket(port);
		}
		catch (IOException e) {
			Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Log.i(Constants.TAG, "[SERVER] Waiting for a connection...");
				Socket socket = serverSocket.accept();
				Log.i(Constants.TAG, "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				
				CommunicationThread communicationThread = new CommunicationThread(this, socket);
				communicationThread.start();
			}
		}
		catch (ClientProtocolException e) {
			Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void stopThread() {
		if (serverSocket != null) {
			this.interrupt();
			try {
				serverSocket.close();
			}
			catch (IOException e) {
				Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	public synchronized void addTimer(String ipAddress, TimerInformation timer) {
		data.put(ipAddress, timer);
	}
	
	
	public synchronized TimerInformation getTimer(String ipAddress) {
		return data.get(ipAddress);
	}
	
	
	public synchronized void removeTimer(String ipAddress) {
		data.remove(ipAddress);
	}
	
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

}
