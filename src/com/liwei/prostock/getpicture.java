package com.liwei.prostock;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class getpicture extends Activity {
	String imageUrlr = "http://hqpicr.eastmoney.com/r/";  
	String imageUrlk = "http://hqpick.eastmoney.com/k/";  

	Bitmap bmImg;  
	ImageView imView;  
	String stockid=null;
	String stockatt=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.getpicture);
		Bundle bundle=getIntent().getExtras();
		stockid=(String)bundle.getString("KEY_STOCK");
		stockatt=(String)bundle.getString("STOCK_IMAGE");
		imView = (ImageView) findViewById(R.id.ImageView01);  
		 
		Matrix matrix=new Matrix();
		matrix.setRotate(90);
		stockid=stockid.substring(2);
		String tempstr=stockid.substring(0, 1);
		Bitmap tempmap=null;
		if(tempstr.equals("6"))
			if(stockatt.equals("r"))
				tempmap=returnBitMap(imageUrlr+stockid+"1.png");
			else
				tempmap=returnBitMap(imageUrlk+stockid+"1.png");
		else
			
			if(stockatt.equals("r"))
				tempmap=returnBitMap(imageUrlr+stockid+"2.png");
			else
				tempmap=returnBitMap(imageUrlk+stockid+"2.png");
		
		imView.setImageBitmap(Bitmap.createBitmap(tempmap,0,0,tempmap.getWidth(),tempmap.getHeight(),matrix,true)); 
		
		
	}
	public Bitmap returnBitMap(String url) {  
		URL myFileUrl = null;  
		Bitmap bitmap = null;  
		try {  
			myFileUrl = new URL(url);  
		} catch (MalformedURLException e) {  
			e.printStackTrace();  
		}  
		try {  
			HttpURLConnection conn = (HttpURLConnection) myFileUrl  
			  .openConnection();  
			conn.setDoInput(true);  
			conn.connect();  
			InputStream is = conn.getInputStream();  
			bitmap = BitmapFactory.decodeStream(is);  
			is.close();  
		} catch (IOException e) {  
			e.printStackTrace();  
		  }  
		  return bitmap;  
		}  
}
