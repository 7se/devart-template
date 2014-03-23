package gt.basic.gtalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ShowBackView extends View {

	Bitmap bitmapShow;
	private Paint mLoadPaint;
	public ShowBackView(Context context) {
		super(context);		
		// TODO Auto-generated constructor stub
		
		mLoadPaint = new Paint();
		mLoadPaint.setAntiAlias(true);
		mLoadPaint.setTextSize(10);
		mLoadPaint.setARGB(255, 255, 0, 0);
	
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	
	

}
