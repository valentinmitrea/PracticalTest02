package ro.pub.cs.systems.eim.practicaltest02.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import ro.pub.cs.systems.eim.practicaltest02.utils.Utilities;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class ClientThread extends Thread {
	
	private String ipAddress;
	private int port;
	private String command;
	private Activity mainActivity;
	private Socket socket;
	
	
	public ClientThread(String ipAddress, int port, String command, Activity mainActivity) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.command = command;
		this.mainActivity = mainActivity;
	}
	
	
	@Override
	public void run() {
		try {
			socket = new Socket(ipAddress, port);
			if (socket == null)
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter printWriter = Utilities.getWriter(socket);
			
			printWriter.println(command);
			printWriter.flush();
			final String result = bufferedReader.readLine();
			
			//afisam rezultatul pe thread-ul principal
			Handler handler = new Handler(mainActivity.getApplicationContext().getMainLooper());
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					Toast.makeText(mainActivity.getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				}
			};
			handler.post(runnable);
		}
		catch (IOException e) {
			Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
