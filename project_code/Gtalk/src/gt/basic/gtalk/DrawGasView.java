package gt.basic.gtalk;


import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;

public class DrawGasView extends View {

	Bitmap bitmap;
	int[] buffer;
	int[] bufferOut;
	int Width=800;
	int Height=1280;
	Timer timer=new Timer();
	Paint paint;
	Thread thread;
	boolean runMark=true;
	public static int DrawPlus;
	public static boolean RenewBitmap; 
	public DrawGasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	
		this.Init();
	}
	
	public DrawGasView(Context context)
	{
		super(context);
		this.Init();
	}
	
	public void Init()
	{
		this.buffer=new int[this.Width*this.Height];
		this.bufferOut=new int[this.Width*this.Height];
		bitmap=Bitmap.createBitmap(this.Width, this.Height, Config.ARGB_8888);
		this.bitmap.getPixels(this.buffer, 0, this.Width, 0, 0, this.Width, this.Height);
		
		this.paint = new Paint();
		this.paint.setStyle(Style.FILL);
		this.paint.setColor(Color.GRAY);
		//this.paint.setAlpha(100);
		
		this.SetBitmapIntBuffer();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	
		canvas.drawBitmap(this.bitmap, 0,0, this.paint);
		//canvas.drawLine(0, 0, 500, 500, this.paint);
	}
	
	public void SetBitmapIntBuffer()
	{
		int red=100;
		int blue=100;
		int green=100;
		int alpha=0;
		for(int i=0;i<this.Height;i++)
		{
			for(int j=0;j<this.Width;j++)
			{
				//alpha=(int) (100*(Math.sin((i-this.Height/2)/200.0)*Math.sin((j-this.Width/2)/200.0)+1));
				alpha=(int) (300*Math.sin(Math.cos((j)/300.0)*((i)/300.0))*Math.cos((j)/300.0));
				//alpha=(int) (100*(Math.sin((i/400.0)*Math.sin(j/50.0))+1));
				//this.buffer[i*this.Width+j]=red+green<<8+blue<<16+alpha<<24;
				//this.buffer[i*this.Width+j]=0x00+0x00<<8+0x00<<16+(alpha<<24);
				/*
				if((alpha>>24)!=0)
				{
					alpha=0x00;
				}
				else if(alpha>255)
				{
					alpha=0xff;
				}
				*/
				if(alpha>255)
				{
					alpha=0xfe;
				}
				if(alpha<0)
				{
					alpha=0;
				}
				this.buffer[i*this.Width+j]=0xff|0xff<<8|0xff<<16|alpha<<24;
			}
		}
		this.bitmap.setPixels(this.buffer, 0, this.Width, 0, 0, this.Width, this.Height);
	}

	public void Start()
	{
		/*
		this.timer.schedule(new TimerTask() {
			 int count=-255;
			 boolean mark=true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//FunctionView3.this.ChangeAlpha(1);
				
				if(mark)
				{
					count=count+15;
					if(count>0)
					{
						mark=false;
					}
				}
				else
				{
					count=count-2;
					if(count<-255)
					{
						mark=true;
					}
					
				}
				
				
				DrawGasView.this.ChangeAlpha(count);
				DrawGasView.this.postInvalidate();
			}
		}, 0,30);
		*/
	}
	
	
	public void StartTimer()
	{
		this.timer.schedule(new TimerTask() {
			 int count=-260;
			 int min=-3;
			 boolean mark=true;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//FunctionView3.this.ChangeAlpha(1);
				if(count+DrawGasView.DrawPlus>-255)
				{
					count+=DrawGasView.DrawPlus;
					if(count+DrawGasView.DrawPlus>150)
					{
						count=150;
					}
					
				}
				if(count+min>-260)
				{
					count=count+min;
				}
				if(count>-255)
				{
					DrawGasView.this.ChangeAlpha(count);
					DrawGasView.this.postInvalidate();
				}
				if(DrawGasView.this.RenewBitmap)
				{
					DrawGasView.this.SetBitmapIntBuffer();
				}
				DrawGasView.DrawPlus=0;
			}
		}, 0,30);
	}
	public void ChangeAlpha(int alphaPlus)
	{
		int red=100;
		int blue=100;
		int green=100;
		int alpha=100;
		for(int i=0;i<this.Height;i++)
		{
			for(int j=0;j<this.Width;j++)
			{
				//alpha=(int) (255*Math.sin(i/50.0)*Math.sin(j/50.0));
				
				//this.buffer[i*this.Width+j]=this.buffer[i*this.Width+j]+(alphaPlus<<24);

				int alphaGet=(this.buffer[i*this.Width+j])>>>24;
			if(alphaGet>0)
			{
				int kk=0;
				kk++;
			}
			//	alphaGet=alphaGet&(0x00ffffff);
				int alphaResult=0;
				if(alphaPlus+alphaGet<0)
				{
					alphaResult=0x00;
				}
				else if(alphaPlus+alphaGet>255)
				{
					alphaResult=0xff;
				}
				else
				{
					alphaResult=alphaPlus+alphaGet;
				}
				this.bufferOut[i*this.Width+j]=(this.buffer[i*this.Width+j]&0x00ffffff)|(alphaResult<<24);
			}
		}
		this.bitmap.setPixels(this.bufferOut, 0, this.Width, 0, 0, this.Width, this.Height);
	}

	


}
