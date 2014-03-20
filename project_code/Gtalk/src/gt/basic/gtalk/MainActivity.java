package gt.basic.gtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button connetButton;
	private EditText usernameEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connetButton = (Button) findViewById(R.id.connectButton);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);

		connetButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();

				AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {

					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						Connection.getConnect();
						Connection.login(params[0], params[1]);
						return null;
					}

					@Override
					protected void onPostExecute(String result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						showList();
					}

				};
				task.execute(username, password);

			}
		});
	}

	private void showList() {
		Intent intent = new Intent(getApplicationContext(),
				RostersActivity.class);
		this.startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
