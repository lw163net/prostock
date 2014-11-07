package com.liwei.prostock;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActivityManager;
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
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class StockService4 extends Service implements Runnable {
	private static final String TAG="StockService4"; 
	private static Object sLock = new Object();   
	private static Queue<Integer> sAppWidgetIds=new LinkedList<Integer>();//所有widget id队列
	private static boolean sThreadRunning = false;   
	public static final String ACTION_UPDATE_ALL = "com.liwei.prostock.START_STOCK_SERVICE4"; 
	String url="http://hq.sinajs.cn/list=";
	private static int pagesize=15;
	private static boolean first=true;
	private String netstate=null;
	private String connectstate="连接状态";
	private int runcount=0;

   
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
            if (sAppWidgetIds.peek() == null) {   
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
 //     android.os.Debug.waitForDebugger();
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
                updateAppWidgetIds(widget.getAppWidgetIds(new ComponentName(this, stockwidget4.class)));   
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
		Long widgetupdatetime;
		String Stockid=null;
		//检查网络状态
		checkNetworkInfo();
		runcount++;
		//classqg.WriteSettings("count="+String.valueOf(runcount));

		
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		Stockid=sp.getString("strstock", "0");
		
		//sp = PreferenceManager.getDefaultSharedPreferences(this);
		//SharedPreferences spc = this.getSharedPreferences("com.android.PreferenceActivity.Usage_preferences.xml", MODE_WORLD_READABLE );
		//读设置
		SharedPreferences spc= PreferenceManager.getDefaultSharedPreferences(this);
		widgetupdatetime=Long.parseLong(spc.getString("preference_widget", "5"))*60000;
		pagesize=Integer.parseInt(spc.getString("preference_widget4_count", "15"));
		
		// TODO Auto-generated method stub
	try{
		
		AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);   
        RemoteViews updateViews=null;   
        RemoteViews updateState=null; 
        RemoteViews updatepro=null;
         //这里得保证所有的appwidget需遍历到
        while (hasMoreUpdates()) {   
            int appWidgetId=getNextWidgetId();   
            updateState=stockwidget4.updateZt(this, netstate, connectstate, false);
            if (updateState != null) {   
                appWidgetManager.updateAppWidget(appWidgetId, updateState);   
              } 

            String[] stocklist=null;
            String[] indexlist=null;
            //判断是否开市或是服务第一次
            if(!netstate.equals("没网络")){
            	  boolean ks=isKs();
            	  boolean isnull=(stockwidget4.stocklist.size()==0);
            	 
	            if(ks||isnull){
	            	//classqg.WriteSettings("开市或没数据正常请求");
	              boolean showpro=true;
	                updatepro=stockwidget4.updateZtPro(this, showpro);
	              	if(updatepro!=null){
	              		appWidgetManager.updateAppWidget(appWidgetId, updatepro);
	              	}
	            	if(!Stockid.equals("")){
	                	stocklist=getStockList(url+Stockid);
	                	if(stocklist!=null){
		        	    	String strstocklist="";
		        	    	for(int i=0;i<stocklist.length-1;i++){
		        	    		strstocklist=strstocklist+stocklist[i]+":";
		        	    	}
		        	    	strstocklist=strstocklist+stocklist[stocklist.length-1];
	                	}
	            	}
	              indexlist=getIndexList(url+"s_sh000001,s_sz399001");
	              if((indexlist!=null)&&(stocklist!=null)){
			              synchronized (sLock) {  
					            if ((stocklist.length>0)&&(indexlist.length>0)) {   
					                updateViews = stockwidget4.updateAppWidget(this, stocklist,indexlist,pagesize);   
					            }   
			                }
			              RemoteViews updateerr=stockwidget4.updateErr(this,"通信正常 ",true);
		                	if(updateerr!=null){
		                		appWidgetManager.updateAppWidget(appWidgetId, updateerr);
		                	}
	                }else{
	                	RemoteViews updateerr=stockwidget4.updateErr(this,"通信失败 ",false);
	                	if(updateerr!=null){
	                		appWidgetManager.updateAppWidget(appWidgetId, updateerr);
	                	}
	              
	                }
		          if (updateViews != null) {   
		                appWidgetManager.updateAppWidget(appWidgetId, updateViews);   
		            } 
	                
	
	            }else{
//	              if(!ks)
//	            		classqg.WriteSettings("没开市,没请求");
//	            	if(!isnull)
//	            		classqg.WriteSettings("有数据,没请求");
	            		
	            //	Log.d(TAG,"sizeof="+String.valueOf(stockwidget4.stocklist.size()));
	            }
	           
            }
           updatepro=stockwidget4.updateZtPro(this, false);
        	if(updatepro!=null){
        		appWidgetManager.updateAppWidget(appWidgetId, updatepro);
        	}
            
        }   
       
//        Intent updateIntent=new Intent(ACTION_UPDATE_ALL);   
//        updateIntent.setClass(this, StockService4.class);   
//        PendingIntent pending=PendingIntent.getService(this, 0, updateIntent, 0);   
//           
//        Time time = new Time();   
//        long nowMillis = System.currentTimeMillis();   
//        time.set(nowMillis+widgetupdatetime);   
//        long updateTimes = time.toMillis(true);   
//        Log.d(TAG, "request next update at "+updateTimes);     
//   	     classqg.WriteSettings("ok"+":serviceok");
//        AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);   
//        alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);   //设置好下次开动服务的时间间隔
//        stopSelf();  // 完成了工作，停止自己
	}
	catch(Exception ex){  
		// classqg.WriteSettings(ex.getMessage()+":serviceerror");
		
	}
	finally{
		 Intent updateIntent=new Intent(ACTION_UPDATE_ALL);   
	     updateIntent.setClass(this, StockService4.class);   
	     PendingIntent pending=PendingIntent.getService(this, 0, updateIntent, 0);   
	           
	     Time time = new Time();   
	     long nowMillis = System.currentTimeMillis();   
	     time.set(nowMillis+widgetupdatetime);   
	     long updateTimes = time.toMillis(true);   
	     Log.d(TAG, "request next update at "+updateTimes);   
	        
	     AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
	     alarm.cancel(pending);
	     alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);   //设置好下次开动服务的时间间隔
	     stopSelf();  // 完成了工作，停止自己
		
	
	}

	}  
	//widget获得两大指数
	public String[] getIndexList(String strurl) throws Exception{
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
	public String[] getStockList(String strurl) throws Exception{
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
	//判断开市情况
	public boolean isKs(){
		//判断是否是第一次运行

		//叛断星期六天
		Date d=new Date() ;
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		int w=c.get(Calendar.DAY_OF_WEEK)-1;
		if(w==0)w=7;
		if(w==6||w==7)
			return false;
		//判断交易时
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		//Log.d(TAG, Integer.toString(hour));
		if (hour >= 9 && hour < 16) {
			
			if(hour==9){
				if (minute>15)
					return true;
				else
					return false;
			}
			if(hour==11){
				if(minute<=30)
					return true;
				else
					return false;
			}
			if(hour==12)
				return false;
			if(hour==15){
				if(minute<=10)
					return true;
				else
					return  false;
			}
			return true;
		} else {
			return false;
		}


	}
	public boolean IsTimeIn(Date time,Date begin,Date end){        
		return time.getTime()>=begin.getTime() && time.getTime()<=end.getTime();    
	}
	
	
	private void checkNetworkInfo()
	{
	    ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    //mobile 3G Data Network
	    State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	    if(mobile==State.CONNECTED||mobile==State.CONNECTING){
	    	TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
	    	int nettemp =tm.getNetworkType();
	    	if(nettemp==tm.NETWORK_TYPE_UNKNOWN)
	    		netstate="UNKNOWN";
	    	if(nettemp==tm.NETWORK_TYPE_GPRS)
	    		netstate="GPRS";
	    	if(nettemp==tm.NETWORK_TYPE_EDGE)
	    		netstate="EDGE";
	    	if(nettemp==tm.NETWORK_TYPE_UMTS)
	    		netstate="UMTS";
	    	if(nettemp==tm.NETWORK_TYPE_HSDPA)
	    		netstate="HSDPA";
	    	if(nettemp==tm.NETWORK_TYPE_HSUPA)
	    		netstate="HSUPA";
	    	if(nettemp==tm.NETWORK_TYPE_HSPA)
	    		netstate="HSPA";
	    	if(nettemp==tm.NETWORK_TYPE_CDMA)
	    		netstate="CDMA";
	    	if(nettemp==tm.NETWORK_TYPE_EVDO_0)
	    		netstate="EVDO_O";
	    	if(nettemp==tm.NETWORK_TYPE_EVDO_A)
	    		netstate="EVDO_A";
	    	if(nettemp==tm.NETWORK_TYPE_1xRTT)
	    		netstate="1xRTT";
	    	if(nettemp==tm.NETWORK_TYPE_UNKNOWN)
	    		netstate="unknown";
	    }else
	    	netstate="没网络";
	    
	    //NETWORK_TYPE_UNKNOWN  网络类型未知  0
	    //NETWORK_TYPE_GPRS     GPRS网络  1
	   // NETWORK_TYPE_EDGE     EDGE网络  2
	   // NETWORK_TYPE_UMTS     UMTS网络  3
	   // NETWORK_TYPE_HSDPA    HSDPA网络  8 
	   // NETWORK_TYPE_HSUPA    HSUPA网络  9
	   // NETWORK_TYPE_HSPA     HSPA网络  10
	   // NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
	   // NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
	   // NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
	   // NETWORK_TYPE_1xRTT    1xRTT网络  7
		
	    State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	    if(wifi==State.CONNECTED||wifi==State.CONNECTING)
	    	netstate="WIFI";
	    
		}
	//取得系统中所有服务的名称
	private String getServiceClassName(List<ActivityManager.RunningServiceInfo> mServiceList){  
	    String res = "";  
	    for(int i = 0; i < mServiceList.size(); i ++){  
	        res+=mServiceList.get(i).service.getClassName()+ " /n";  
	    }  
      
	    return res;  
	}  
	//判断服务是否启动
	 private boolean StockServiceIsStart(List<ActivityManager.RunningServiceInfo> mServiceList,String className){  
         
	        for(int i = 0; i < mServiceList.size(); i ++){  
	            if(className.equals(mServiceList.get(i).service.getClassName())){  
	                return true;  
	            }  
	        }  
	        return false;  
	    } 
}


