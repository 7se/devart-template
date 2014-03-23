package gt.basic.gtalk;

import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import ca.uol.aig.fftpack.RealDoubleFFT;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TalkActivity extends Activity {

	//protected ChatManager chatManager;
	protected Chat chat;
	public static Chat chatOut;
	Button buttonSend;
	TextView textView;
	protected MessageListener messageListener;
	
	
	private Button button;
    private ImageView imageView;
    private int frequency = 8000;
    private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private RealDoubleFFT transformer;
    private int blockSize = 256;
    private boolean started = true;
	
	
	private Handler mHandler=new Handler()
	{

		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.arg1)
			{
				case 1:
				{
					TalkActivity.this.textView.setText(TalkActivity.this.textView.getText()+"\r\n"+(String)msg.obj);
				}
			
			}
		}
		
	};
	RecordAudio recordAudio;
	public TalkActivity()
	{
		this.messageListener=new MessageListener()
		{
			@Override
			public void processMessage(Chat arg0, Message arg1) {
				// TODO Auto-generated method stub
				String kk=arg1.getBody();
				int kk2=0;
				kk2++;
				if(kk!=null)
				{
					//TalkActivity.this.textView.setText(TalkActivity.this.textView.getText()+"\r\n"+kk);
					//TalkActivity.this.textView.invalidate();
					TalkActivity.this.mHandler.obtainMessage(0, 1, 1, kk).sendToTarget();
					
				}
			}
			
		};
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_talk);
		this.buttonSend=(Button)findViewById(R.id.sendButton);
		this.textView=(TextView)findViewById(R.id.receiveText);
		Intent intent = getIntent();
		String userJID = intent.getStringExtra(RostersActivity.KEY);

		AsyncTask<String, Integer, Chat> task = new AsyncTask<String, Integer, Chat>() {

			@Override
			protected Chat doInBackground(String... params) {
				// TODO Auto-generated method stub
				//chatManager=Connection.getChat(params[0]);
				//return Connection.getChat(params[0]);
				ChatManager chatManager=Connection.GetChatManager(params[0]);
				chat=chatManager.createChat(params[0], TalkActivity.this.messageListener);
				chatOut=chat;
				return chat;
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
		
		
		
		
		
		
		
	}

	private class ButtonListener implements OnTouchListener
	{

		RecordAudio recordAudio;
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				recordAudio=new RecordAudio();
				recordAudio.RunMark=true;
				recordAudio.execute();
			}
			if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				recordAudio.RunMark=false;
			}
			
			
			
			
			return false;
			
		}
		
	}
	
	private class RecordAudio extends AsyncTask<Void, double[], Void>
	{

		public boolean RunMark=false;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			 int bufferSize = AudioRecord.getMinBufferSize(frequency,
                     channelConfiguration, audioEncoding);
			 AudioRecord audioRecord = new AudioRecord(
                     MediaRecorder.AudioSource.MIC, frequency,
                     channelConfiguration, audioEncoding, bufferSize);
			 short[] buffer = new short[blockSize];
			 double[] toTransform = new double[blockSize];
			 audioRecord.startRecording();
			 while (RunMark) {
				 //将record的数据 读到buffer中，但是我认为叫做write可能会比较合适些。
				 int bufferResult = audioRecord.read(buffer, 0, blockSize);

				 for (int i = 0; i < bufferResult; i++) {
                     	toTransform[i] = (double) buffer[i] / Short.MAX_VALUE;
				 }
				 transformer.ft(toTransform);
				 publishProgress(toTransform);
			 }
			 audioRecord.stop();
			 return null;
		}

		@Override
		protected void onProgressUpdate(double[]... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
			//FloatService.chat.sendMessage(message)
			double size=0;
			for(int i=0;i<values[0].length/3;i++)
			{
				size+=values[0][i];
			}
			size=size/values[0].length;
			try {
				FloatService.chat.sendMessage(""+size);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
     }
	

		
		
	
	
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
	}

}
