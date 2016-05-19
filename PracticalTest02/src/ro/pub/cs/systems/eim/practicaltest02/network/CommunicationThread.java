package ro.pub.cs.systems.eim.practicaltest02.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import ro.pub.cs.systems.eim.practicaltest02.utils.TimerInformation;
import ro.pub.cs.systems.eim.practicaltest02.utils.Utilities;


public class CommunicationThread extends Thread {
	
	private ServerThread serverThread;
	private Socket socket;
	
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket = socket;
	}
	
	
	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter printWriter = Utilities.getWriter(socket);
			
			Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client!");
			String command = bufferedReader.readLine();
			Log.i(Constants.TAG, "[COMMUNICATION THREAD] Received command: " + command);
			String[] tokens = command.split(",");
			String result = "";
			
			//vedem ce comanda am primit
			if (tokens[0].equals("set")) {
				TimerInformation timer = new TimerInformation(tokens[1], tokens[2]);
				serverThread.addTimer(socket.getInetAddress().toString(), timer);
				
				result = "success";
			}
			else if (tokens[0].equals("poll")) {
				TimerInformation timer = serverThread.getTimer(socket.getInetAddress().toString());
				if (timer == null)
					result = "none";
				else if (timer.getStatus().equals("active"))
					result = "active";
				else {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String content = httpClient.execute(httpGet, responseHandler);
					
					//parsam timpul primit de la serviciul Web
					String time = content.substring(content.indexOf('T') + 1, content.indexOf('T') + 6);
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Current time is " + time);
					
					String hour = time.substring(0, 2);
					String minute = time.substring(3, 5);
					
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Current time is " + time);
					
					if (Integer.parseInt(hour) <= Integer.parseInt(timer.getHour()) && Integer.parseInt(minute) < Integer.parseInt(timer.getMinute()))
						result = "inactive";
					else {
						result = "active";
						timer.setStatus("active");
					}
				}
			}
			else if (tokens[0].equals("reset")) {
				serverThread.removeTimer(socket.getInetAddress().toString());
				
				result = "success";
			}
			
			Log.i(Constants.TAG, "[COMMUNICATION THREAD] Sending result: " + result);
			printWriter.println(result);
			printWriter.flush();
		}
		catch (IOException e) {
			Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
