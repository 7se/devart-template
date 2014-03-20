package gt.basic.gtalk;

import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TalkActivity extends Activity {

	protected Chat chat;
	Button buttonSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_talk);
		this.buttonSend=(Button)findViewById(R.id.sendButton);
		Intent intent = getIntent();
		String userJID = intent.getStringExtra(RostersActivity.KEY);

		AsyncTask<String, Integer, Chat> task = new AsyncTask<String, Integer, Chat>() {

			@Override
			protected Chat doInBackground(String... params) {
				// TODO Auto-generated method stub
				chat=Connection.getChat(params[0]);
				return Connection.getChat(params[0]);
			}

			@Override
			protected void onPostExecute(Chat result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				chat = result;
			}

		};

		task.execute(userJID);

		try {
			task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(chat.getParticipant());
		
		this.buttonSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					TalkActivity.this.chat.sendMessage("test-kk");
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

}
