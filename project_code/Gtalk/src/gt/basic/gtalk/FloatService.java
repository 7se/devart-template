package gt.basic.gtalk;


import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;

public class FloatService extends Service {

	public static String userID;
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		userID=intent.getStringExtra("userID");
	}


	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		userID=intent.getStringExtra("userID");
		return super.onStartCommand(intent, flags, startId);
	}




	private DrawGasView drawGasView;
	private boolean viewAdded = false;// 透明窗体是否已经显示  
	private WindowManager windowManager;  
	private WindowManager.LayoutParams layoutParams;  
	public static Chat chat;
	
	private MessageListener messageListener=new MessageListener()
	{

		@Override
		public void processMessage(Chat arg0,
				org.jivesoftware.smack.packet.Message arg1) {
			// TODO Auto-generated method stub
			DrawGasView.DrawPlus=300;
		}
		
	};
	
	
	private Handler mHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
	

	//private FunctionViewCocos functionViewCoco;
	
	
	int width=800;
	int height=1280;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		 this.drawGasView=new DrawGasView(this);
		
		
		layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,  
                LayoutParams.FILL_PARENT, LayoutParams.TYPE_SYSTEM_ERROR,  
                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);  
	
		layoutParams.flags=WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		
		this.windowManager=(WindowManager)this.getSystemService(WINDOW_SERVICE);
		
		
		 this.drawGasView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				//onBackPressed();
				return true;
			}  
	            
	            }  
	        );
	        
		
		this.drawGasView.setFocusable(false);
		this.drawGasView.setFocusable(false);
		this.drawGasView.setClickable(false);
		this.drawGasView.setKeepScreenOn(false);
		this.drawGasView.setLongClickable(false);
		this.drawGasView.setFocusableInTouchMode(false);
		this.drawGasView.StartTimer();
		//testView2 view=new testView2(this,);
		
		
		
		this.windowManager.addView(this.drawGasView, this.layoutParams);
		 /*
		
		this.functionViewCoco=new FunctionViewCocos(this);
		this.functionViewCoco.mGLSurfaceView.setFocusable(false);
		this.functionViewCoco.mGLSurfaceView.setFocusable(false);
		this.functionViewCoco.mGLSurfaceView.setClickable(false);
		this.functionViewCoco.mGLSurfaceView.setKeepScreenOn(false);
		this.functionViewCoco.mGLSurfaceView.setLongClickable(false);
		this.functionViewCoco.mGLSurfaceView.setFocusableInTouchMode(false);
		this.windowManager.addView(this.functionViewCoco.mGLSurfaceView, this.layoutParams);
		*/
		
		this.InitChat();
	}
	
	private void InitChat()
	{
		
		

		AsyncTask<String, Integer, Chat> task = new AsyncTask<String, Integer, Chat>() {

			@Override
			protected Chat doInBackground(String... params) {
				// TODO Auto-generated method stub
				//chatManager=Connection.getChat(params[0]);
				//return Connection.getChat(params[0]);
				ChatManager chatManager=Connection.GetChatManager(params[0]);
				chat=chatManager.createChat(params[0], FloatService.this.messageListener);
				//chatOut=chat;
				return chat;
			}

			@Override
			protected void onPostExecute(Chat result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				chat = result;
			}

		};

		task.execute(userID);

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


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}	
}
