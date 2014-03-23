package gt.basic.gtalk;

import org.jivesoftware.smack.Chat;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class FloatService2 extends Service {

	private boolean viewAdded = false;// 透明窗体是否已经显示  
	private WindowManager windowManager;  
	private WindowManager.LayoutParams layoutParams;  
	public static Chat chat;
	public static String userID;
	testView drawGasView;
	


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
