package ro.pub.cs.systems.eim.practicaltest02;

import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;
import ro.pub.cs.systems.eim.practicaltest02.utils.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class PracticalTest02MainActivity extends Activity {

	private boolean isServerStarted;
	private ServerThread serverThread;
	
	private EditText textPort, textServerIP, textServerPort, textCommand;

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.button_connect) {
				if (isServerStarted)
					Toast.makeText(getApplicationContext(), "Server-ul este deja pornit!", Toast.LENGTH_SHORT).show();
				else if (textPort.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "Port-ul nu a fost completat!", Toast.LENGTH_SHORT).show();
				else {
					int port = Integer.parseInt(textPort.getText().toString());
					
					//cream socket-ul pentru server
					serverThread = new ServerThread(port);
					if (serverThread.getServerSocket() != null) {
						serverThread.start();
						isServerStarted = true;
						Toast.makeText(getApplicationContext(), "Server-ul a fost pornit!", Toast.LENGTH_SHORT).show();
					}
					else
						Log.e(Constants.TAG, "Could not create server thread!");
				}
			}
			else if (v.getId() == R.id.button_send_command) {
				if (!isServerStarted)
					Toast.makeText(getApplicationContext(), "Server-ul nu este pornit!", Toast.LENGTH_SHORT).show();
				else if (textServerIP.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "Adresa IP a server-ului nu a fost completata!", Toast.LENGTH_SHORT).show();
				else if (textServerPort.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "Port-ul server-ului nu a fost completat!", Toast.LENGTH_SHORT).show();
				else if (textCommand.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "Comanda de trimis nu a fost completata!", Toast.LENGTH_SHORT).show();
				else {
					ClientThread clientThread = new ClientThread(textServerIP.getText().toString(),
						Integer.parseInt(textServerPort.getText().toString()),
						textCommand.getText().toString(),
						PracticalTest02MainActivity.this);
					clientThread.start();
					
					textCommand.setText("");
				}
			}
		}
		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_main);
		
		textPort = (EditText)findViewById(R.id.text_port);
		textServerIP = (EditText)findViewById(R.id.text_server_ip);
		textServerPort = (EditText)findViewById(R.id.text_server_port);
		textCommand = (EditText)findViewById(R.id.text_command);
		
		//atasam listener-ul la butoane
		findViewById(R.id.button_connect).setOnClickListener(onClickListener);
		findViewById(R.id.button_send_command).setOnClickListener(onClickListener);
	}


	@Override
	public void onDestroy() {
		if (serverThread != null) {
			serverThread.stopThread();
			isServerStarted = false;
			serverThread = null;
		}
		
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_main, menu);
		
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		
		return super.onOptionsItemSelected(item);
	}

}
