package com.liwei.prostock;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;




import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GetStock extends Thread {
	private Handler handle=null;
	private String GetUrl=null;
	private int getattr=1;
	ProgressDialog progressDialog=null;
	Context actionContext=null;
	public GetStock(Handler hander){
        handle=hander;
	}
	 public void doStart(String url,int attr,Context actionContext) {
         // TODO Auto-generated method stub
	    this.GetUrl=url;
	    this.getattr=attr;
	    this.actionContext=actionContext;
	   // progressDialog=ProgressDialog.show(actionContext, "网络连接","正在请求，请稍等......",true,true);
	    this.start();
	    
	 }//end dostart
	 
	 @Override
     public void run() {
		 boolean getscuess=true;
             // TODO Auto-generated method stub
		 //1:列表获取成功,2:单个获取成功,3:单个获取失败,4:出错或不成功
         super.run();
         try{
              HttpGet hg=new HttpGet(this.GetUrl);
              HttpResponse httpResponse = new DefaultHttpClient().execute(hg);
              if(httpResponse.getStatusLine().getStatusCode() == 200)  
              { 
                String strResult = EntityUtils.toString(httpResponse.getEntity()); 
                strResult = strResult.replaceAll("\n", "");
                String a[]=strResult.substring(0,strResult.length()-1).split(";");
                switch(getattr){
                case 1:
	                
	                for(int i=0;i<a.length;i++){
	                	String tempstr=a[i];
	                	int tempindex1=tempstr.indexOf("hq_str_")+7;
	                	int tempindex2=tempstr.indexOf("=");
	                	String tempstockid=tempstr.substring(tempindex1,tempindex2);
	                	tempstr=tempstr.substring(tempindex2+1);
	                	getscuess=tempstr.equals("\"\"");
	                	tempstr=tempstr.replaceAll("\"", "");
	                	if(getscuess){
	                		continue;
	                	}
	                	tempstr=tempstockid+","+tempstr;
	                	
	                	a[i]=tempstr;
	                }
	                if(a.length==0){
	                	Message message=handle.obtainMessage();
		                Bundle bd=new Bundle();
		                message.what=4;
		                bd.putString("stockerror", "没获取到数据!");
		                message.setData(bd);
		                handle.sendMessage(message);
	                }else{
		                Message message=handle.obtainMessage();
		                Bundle bd=new Bundle();
		                message.what=1;
		                bd.putStringArray("stock", a);
		                message.setData(bd);
		                handle.sendMessage(message);
		                Log.d("stock", strResult);
	                }
	                break;
                
                case 2:
                	String tempstr=a[0];
                	int tempindex=tempstr.indexOf("=")+1;
                	String tempstockid=tempstr.substring(tempindex-6-1,tempindex-1);
                	tempstr=tempstr.substring(tempindex);
                	getscuess=tempstr.equals("\"\"");
                	tempstr=tempstockid+","+tempstr;
                	tempstr=tempstr.replaceAll("\"", "");
                	a[0]=tempstr;
                	if(getscuess){
 	                	Message message=handle.obtainMessage();
 		                Bundle bd=new Bundle();
 		                message.what=3;
 		                handle.sendMessage(message);
 	                }else{
 		                Message message=handle.obtainMessage();
 		                Bundle bd=new Bundle();
 		                message.what=2;
 		                bd.putStringArray("stock", a);
 		                message.setData(bd);
 		                handle.sendMessage(message);
 	                }
                	break;
                	
                }
                //progressDialog.dismiss();
              }
         
         }catch(Exception ex){
        	 Message message=handle.obtainMessage();
             Bundle bd=new Bundle();
             message.what=4;
             bd.putString("stockerror", ex.getMessage());
           
             message.setData(bd);
             handle.sendMessage(message);
             	
        	 
         }finally{
                 
         }
       }//end run 
}
