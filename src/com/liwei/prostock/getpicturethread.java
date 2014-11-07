package com.liwei.prostock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class getpicturethread extends Thread {
	private Handler handle=null;
	private String GetUrl=null;
	private int getattr=1;
	ProgressDialog progressDialog=null;
	Context actionContext=null;
	public getpicturethread(Handler hander){
        handle=hander;
	}
	 public void doStart(String url,Context actionContext) {
         // TODO Auto-generated method stub
	    this.GetUrl=url;
	    this.actionContext=actionContext;
	   // progressDialog=ProgressDialog.show(actionContext, "网络连接","正在请求，请稍等......",true,true);
	    this.start();
	    
	 }//
	 @Override
     public void run() {
		 super.run();
		 try{
			 Bitmap resule=returnBitMap(GetUrl);
			 Message message=handle.obtainMessage();
	         Bundle bd=new Bundle();
	         message.what=1;
	         ByteArrayOutputStream os = new ByteArrayOutputStream(); 
	         resule.compress(Bitmap.CompressFormat.PNG, 80, os);            

	         byte[] array = os.toByteArray(); 
	         

	         
	         bd.putByteArray("picture", array);
	         
	         message.setData(bd);
	         handle.sendMessage(message);
		 
		 }catch(Exception ex){
			 Message message=handle.obtainMessage();
	         Bundle bd=new Bundle();
	         message.what=2;
	         bd.putString("error",ex.toString());
	         message.setData(bd);
	         handle.sendMessage(message);
		 }
		 
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
