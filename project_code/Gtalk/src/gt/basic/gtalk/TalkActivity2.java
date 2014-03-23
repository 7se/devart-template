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
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TalkActivity2 extends Activity{

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
	
	public TalkActivity2()
	{
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_talk);
		this.transformer= new RealDoubleFFT(blockSize);
		this.buttonSend=(Button)findViewById(R.id.sendButton);
		this.textView=(TextView)findViewById(R.id.receiveText);
		Intent intent = getIntent();
		String userJID = intent.getStringExtra(RostersActivity.KEY);

		Intent intentStart=new Intent(getApplicationContext(), FloatService.class);
		intentStart.putExtra("userID", userJID);
		FloatService.userID=userJID;
		this.startService(intentStart);
		
		/*
		this.buttonSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					FloatService.chat.sendMessage("1");
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		*/
		this.buttonSend.setOnTouchListener(new ButtonListener());
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
				//recordAudio.cancel(false);
				
			}
			
			
			
			
			return false;
			
		}
		
	}
	
	private class RecordAudio extends AsyncTask<Void, double[], Void>
	{
		AudioRecord audioRecord;
		 int bufferSize;
		 
		public RecordAudio()
		{
			
		}
		
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
				 try
                 {
                 	transformer.ft(toTransform);
                 	publishProgress(toTransform);
                 }
                 catch(Exception ee)
                 {
                 	int kk=0;
                 	kk++;
                 }
			 }
			 audioRecord.stop();
			 audioRecord.release();
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
