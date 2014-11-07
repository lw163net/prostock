package com.liwei.prostock;


import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class StockService extends Service implements Runnable {
	private static final String TAG="StockService"; 
	private static Object sLock = new Object();   
	private static Queue<Integer> sAppWidgetIds=new LinkedList<Integer>();//所有widget id队列
	private static boolean sThreadRunning = false;   
	public static final String ACTION_UPDATE_ALL = "com.liwei.prostock.START_STOCK_SERVICE"; 
	String url="http://hq.sinajs.cn/list=";
	private static int pagesize=5;
  
    @Override  
    public IBinder onBind(Intent intent) {   
        return null;   
    }
    
    public static void updateAppWidgetIds(int[] appWidgetIds){   
        synchronized (sLock) {   
            for (int appWidgetId : appWidgetIds) {   
                sAppWidgetIds.offer(appWidgetId);   //加入元素
            }   
        }   
    } 
    
    public static int getNextWidgetId(){   
        synchronized (sLock) {   
            if (sAppWidgetIds.peek() == null) {   //检查第一个元素,但不移除
                return AppWidgetManager.INVALID_APPWIDGET_ID;   
            } else {   
                return sAppWidgetIds.poll();  //移出元素 
                   
            }   
        }   
    }   
    
    private static boolean hasMoreUpdates() {   
        synchronized (sLock) {   
            boolean hasMore = !sAppWidgetIds.isEmpty();   
            if (!hasMore) {   
                sThreadRunning = false;   
            }   
            return hasMore;   
        }   
    }   

    @Override  
    public void onCreate() {   
        super.onCreate();   
    //  android.os.Debug.waitForDebugger();
//      IntentFilter filter=new IntentFilter(); 
//      filter.addAction("com.liwei.prostock.serviceLeftButton"); 
//      filter.addCategory(Intent.CATEGORY_DEFAULT);
//    //  filter.addAction("com.liwei.prostock.rightButton");
//      this.registerReceiver(new BroadcastReceiver(){
//
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				// TODO Auto-generated method stub
//				if(intent.getAction().equals("com.liwei.prostock.serviceLeftButton")){
//					Log.d(TAG, "aa");
//				}
//			}
//      	
//      }, filter);
        
    }   
    
    @Override  
    public void onStart(Intent intent, int startId) {   
        super.onStart(intent, startId);   

        if (null != intent) {   
            if (ACTION_UPDATE_ALL.equals(intent.getAction())) {   
                AppWidgetManager widget = AppWidgetManager.getInstance(this);   
                updateAppWidgetIds(widget.getAppWidgetIds(new ComponentName(this, stockwidget.class)));   
            }   
        }   
        synchronized (sLock) {   
            if (!sThreadRunning) {   
                sThreadRunning=true;    
                new Thread(this).start();   
            }   
        }   
    }   
  
    
	@Override
public void run() {
		
		// TODO Auto-generated method stub
		String Stockid=null;
		Long widgetupdatetime;
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		Stockid=sp.getString("strstock", "0");
		
		//sp = PreferenceManager.getDefaultSharedPreferences(this);
		//SharedPreferences spc = this.getSharedPreferences("com.android.PreferenceActivity.Usage_preferences.xml", MODE_WORLD_READABLE );
		//读设置
		SharedPreferences spc= PreferenceManager.getDefaultSharedPreferences(this);
		widgetupdatetime=Long.parseLong(spc.getString("preference_widget", "0"))*60000;
		pagesize=Integer.parseInt(spc.getString("preference_widget2_count", "5"));
		try
		{
			AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);   
	        RemoteViews updateViews=null;   
	           
	        while (hasMoreUpdates()) {   
	            int appWidgetId=getNextWidgetId();   
	            String[] stocklist=null;
	            String[] indexlist=null;
	            if(!Stockid.equals(""))
	            	stocklist=getStockList(url+Stockid);
	            	indexlist=getIndexList(url+"s_sh000001,s_sz399001");
	            	if(stocklist !=null)
	            	   Log.d(TAG, "stocklist length="+String.valueOf(stocklist.length));
	            	else
	            		Log.d(TAG, "stocklist is null");
	            	if(indexlist !=null)
	             	   Log.d(TAG, "indexlist length="+String.valueOf(stocklist.length));
	             	else
	             		Log.d(TAG, "indexlist is null");
	            	
	            if ((stocklist != null)&&(indexlist!=null)) {   
	                updateViews = stockwidget.updateAppWidget(this, stocklist,indexlist,pagesize);   
	            }   
	            if (updateViews != null) {   
	                appWidgetManager.updateAppWidget(appWidgetId, updateViews);   
	            }   
	        }   
	        
	       // Date dt=new Date();
	       
	        Intent updateIntent=new Intent(ACTION_UPDATE_ALL);   
	        updateIntent.setClass(this, StockService.class);   
	        PendingIntent pending=PendingIntent.getService(this, 0, updateIntent, 0);   
	           
	        Time time = new Time();   
	        long nowMillis = System.currentTimeMillis();   
	        time.set(nowMillis+widgetupdatetime);   
	        long updateTimes = time.toMillis(true);   
	        Log.d(TAG, "request next update at "+updateTimes);   
	           
	        AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);   
	        alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);   //设置好下次开动服务的时间间隔
	        stopSelf();  // 完成了工作，停止自己
		}
		catch(Exception ex){
			Intent updateIntent=new Intent(ACTION_UPDATE_ALL);   
	        updateIntent.setClass(this, StockService4.class);   
	        PendingIntent pending=PendingIntent.getService(this, 0, updateIntent, 0);   
	           
	        Time time = new Time();   
	        long nowMillis = System.currentTimeMillis();   
	        time.set(nowMillis+widgetupdatetime);   
	        long updateTimes = time.toMillis(true);   
	        Log.d(TAG, "request next update at "+updateTimes);   
	           
	        AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);   
	        alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);   //设置好下次开动服务的时间间隔
	        stopSelf();  // 完成了工作，停止自己
		}
        

	}  
	//widget获得两大指数
	public String[] getIndexList(String strurl){
		try{
			boolean getscuess=true;
		
            HttpGet hg=new HttpGet(strurl);
            HttpResponse httpResponse = new DefaultHttpClient().execute(hg);
            if(httpResponse.getStatusLine().getStatusCode() == 200)  
            { 
	              String strResult = EntityUtils.toString(httpResponse.getEntity()); 
	              strResult = strResult.replaceAll("\n", "");
	              String a[]=strResult.substring(0,strResult.length()-1).split(";");
		                
		          for(int i=0;i<a.length;i++){
		               String tempstr=a[i];
		               int tempindex=tempstr.indexOf("=")+1;
		               String tempstockid=tempstr.substring(tempindex-6-1,tempindex-1);
		               tempstr=tempstr.substring(tempindex);
		               getscuess=tempstr.equals("\"\"");
		               if(getscuess){
		                	continue;
		               }
		               tempstr=tempstockid+","+tempstr;
		               tempstr=tempstr.replaceAll("\"", "");
		               a[i]=tempstr;
		          }
		          if(a.length==0){
		               return null;
		          }
		          return a;
            }

       }catch(Exception ex){
           	return null;
       }
	return null;
	}
	//获得自选股票信息
	public String[] getStockList(String strurl){
		try{
			boolean getscuess=true;
		
            HttpGet hg=new HttpGet(strurl);
            HttpResponse httpResponse = new DefaultHttpClient().execute(hg);
            if(httpResponse.getStatusLine().getStatusCode() == 200)  
            { 
	              String strResult = EntityUtils.toString(httpResponse.getEntity()); 
	              strResult = strResult.replaceAll("\n", "");
	              String b[]=strResult.substring(0,strResult.length()-1).split(";");
		                
		          for(int i=0;i<b.length;i++){
		               String tempstr=b[i];
		               int tempindex=tempstr.indexOf("=")+1;
		               String tempstockid=tempstr.substring(tempindex-6-1,tempindex-1);
		               tempstr=tempstr.substring(tempindex);
		               getscuess=tempstr.equals("\"\"");
		               if(getscuess){
		                	continue;
		               }
		               tempstr=tempstockid+","+tempstr;
		               tempstr=tempstr.replaceAll("\"", "");
		               b[i]=tempstr;
		          }
		          if(b.length==0){
		               return null;
		          }
		          return b;
            }

       }catch(Exception ex){
           	return null;
       }
	return null;

		
	}
	
	
}
