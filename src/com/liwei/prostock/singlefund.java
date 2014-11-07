package com.liwei.prostock;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class singlefund extends Activity {
	ImageView iv1=null;
	ImageView iv2=null;
	TextView tvdm=null;
	TextView tvmc=null;
	TextView tvxj=null;
	getpicturethread picthread1=null;
	getpicturethread picthread2=null;
	funddata tempfund=null;
	String tempstr=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		setContentView(R.layout.singlefund);
		iv1=(ImageView)findViewById(R.id.fundiv1);
		iv2=(ImageView)findViewById(R.id.fundiv2);
		tvdm=(TextView)findViewById(R.id.txtdm);
		tvmc=(TextView)findViewById(R.id.txtmc);
		tvxj=(TextView)findViewById(R.id.txtxj);
		Bundle bundle=getIntent().getExtras();
		tempfund=(funddata)bundle.getSerializable("KEY_FUND");
		float tempzf=Float.parseFloat(tempfund.getFundzf());
		if(tempzf==0||tempzf==0.0||tempzf==0.00){
	    	tvdm.setTextColor(Color.BLACK);
	    	tvxj.setTextColor(Color.BLACK);
	    }else{
	    	if(tempzf>0){
	    		tvdm.setTextColor(Color.RED);
	    		tvxj.setTextColor(Color.RED);
	    	}
	    	else
	    	{
	    		tvdm.setTextColor(Color.GREEN);
	    		tvxj.setTextColor(Color.GREEN);
	    	}
	    }
		
		tempstr=tempfund.getFundid();
		tvdm.setText(tempfund.getFundzf());
		tvmc.setText(tempfund.getFundname());
		tvxj.setText(tempfund.getFundzxjz());
		this.setTitle("基金:"+tempfund.getFundid());
		String picurl1="http://image.sinajs.cn/newchart/v5/fund/nav/ss/"+tempstr+".gif";
		String picurl2="http://image.sinajs.cn/newchart/v5/fundpre/min_s/"+tempstr+".gif";
		picthread1=new getpicturethread(handerpic1);
		picthread1.doStart(picurl1,this);
		picthread2=new getpicturethread(handerpic2);
		picthread2.doStart(picurl2,this);
         
	}
	Handler handerpic1=new Handler(){
		public void handleMessage(Message m){
			switch(m.what){
			case 1:
				byte[] tempbyte=m.getData().getByteArray("picture");	
				Bitmap bm=BitmapFactory.decodeByteArray(tempbyte, 0, tempbyte.length);
				iv1.setImageBitmap(Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight()));
			//	bar1.setVisibility(View.GONE);
				break;
			}
		}
	};
	Handler handerpic2=new Handler(){
		public void handleMessage(Message m){
			switch(m.what){
			case 1:
				byte[] tempbyte=m.getData().getByteArray("picture");	
				Bitmap bm=BitmapFactory.decodeByteArray(tempbyte, 0, tempbyte.length);
				iv2.setImageBitmap(Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight()));
			//	bar1.setVisibility(View.GONE);
				break;
			}
		}
	};
}
