package com.liwei.prostock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class stockwidget extends AppWidgetProvider {
	private static final String TAG="stockwidget"; 
	private Timer timer=new Timer();
	GetStock thread=null;
	private int[] appWidgetIds;
	private AppWidgetManager appWidgetManager;
	private Context context;
	private String Stockid=null;
	private String url="http://hq.sinajs.cn/list=";
	//final static String leftButton_actionName = "leftButton";
//	final static String rightButton_actionName = "rightButton";
	static int currentpage=0;
	static int pagesize=0;
	static ArrayList<stockdata> stocklist=null;
    @Override  
    public void onDeleted(Context context, int[] appWidgetIds) {   
    }   
  
    @Override  
    public void onDisabled(Context context) {   
        // TODO Auto-generated method stub   
        super.onDisabled(context);   
    }   
  
    @Override  
    public void onEnabled(Context context) {   
        // TODO Auto-generated method stub   
        super.onEnabled(context);   
    }   
  
    @Override  
    public void onReceive(Context context, Intent intent) {   
        super.onReceive(context, intent);   

	        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.stockwidget);
	        //Log.d(TAG, String.valueOf(currentpage));
	        if(stocklist==null)
	        	Log.i(TAG, "null");
	        else{
	        	Log.i(TAG, String.valueOf(stocklist.size()));
		        if (intent.getAction().equals("com.liwei.prostock.leftButton")){
		        	//向左翻页
		        	if(currentpage!=0){
		        		if(fillstock(views,stocklist,currentpage-1,pagesize))
		        			currentpage=currentpage-1;
			        		ComponentName thisWidget = new ComponentName(context, stockwidget.class);
			    	        AppWidgetManager manager =AppWidgetManager.getInstance(context);
			    	        manager.updateAppWidget(thisWidget, views);
		        		}
		        }else
		        	//向右翻页
		        	if(intent.getAction().equals("com.liwei.prostock.rightButton")){
		        		if(fillstock(views,stocklist,currentpage+1,pagesize))
		        			currentpage=currentpage+1;
		        		ComponentName thisWidget = new ComponentName(context, stockwidget.class);
		    	        AppWidgetManager manager =AppWidgetManager.getInstance(context);
		    	        manager.updateAppWidget(thisWidget, views);
		        }
	        }

        }
     
	
	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds){
	      //android.os.Debug.waitForDebugger();
		 this.appWidgetIds=appWidgetIds;
	     //启动服务
		 StockService.updateAppWidgetIds(appWidgetIds);   
	     Intent serviceIntent = new Intent();
	     serviceIntent.setAction("com.liwei.prostock.START_STOCK_SERVICE");
	     context.startService(serviceIntent);
	    // PendingIntent Pintent=PendingIntent.getService(context,0,serviceIntent,0); 
	}

	//定时刷新页面
	public static RemoteViews updateAppWidget(Context context,String mylist[],String indexlist[],int psize) {
		Log.i(TAG, "updateAppWidget");
		try{
			pagesize=psize;
			RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.stockwidget);
			ArrayList<stockindexdata> stockindexlist;
			stockindexlist=new ArrayList<stockindexdata>();
			for(int i=0;i<indexlist.length;i++){
				String tempstr=indexlist[i];
				tempstr=tempstr.substring(7);
				String a[]=tempstr.split(",");
				stockindexdata tempstock=new stockindexdata();
				tempstock.setZsmc(a[0]);
				tempstock.setZsds(a[1]);
				tempstock.setZsjg(a[2]);
				tempstock.setZszdl(a[3]);
			
				if(a.length<6){
					tempstock.setZssj(a[4]);
				}else{	
					int tempcje=Integer.parseInt(a[5])/10000;
					tempstock.setZssj(String.valueOf(tempcje)+"亿");
				}
				stockindexlist.add(tempstock);
			}
			
			float tempshzd=Float.parseFloat(stockindexlist.get(0).getZsjg());
			float tempszzd=Float.parseFloat(stockindexlist.get(1).getZsjg());
			views.setTextViewText(R.id.txtshindex, stockindexlist.get(0).getZsds()+"("+stockindexlist.get(0).getZszdl()+"%)");
			views.setTextViewText(R.id.txtszindex, stockindexlist.get(1).getZsds()+"("+stockindexlist.get(1).getZszdl()+"%)");
			if(!(tempshzd==0)){
				if(tempshzd>0.00f){
					views.setTextColor(R.id.txtshindex,Color.RED);
					views.setTextColor(R.id.txtshindex,Color.RED);	    	
				}
				else{
					views.setTextColor(R.id.txtshindex,Color.GREEN);
					views.setTextColor(R.id.txtshindex,Color.GREEN);
				}
			}
			if(!(tempszzd==0)){
				if(tempszzd>0.00f){
					views.setTextColor(R.id.txtszindex,Color.RED);
					views.setTextColor(R.id.txtszindex,Color.RED);	    	
				}
				else{
					views.setTextColor(R.id.txtszindex,Color.GREEN);
					views.setTextColor(R.id.txtszindex,Color.GREEN);
				}
			}
			
			
			stocklist=new ArrayList<stockdata>();
			for(int i=0;i<mylist.length;i++){
				String tempstr=mylist[i];
				String a[]=tempstr.split(",");
				stockdata tempstock=new stockdata();
				tempstock.setId(a[0]);     			 tempstock.setName(a[1]);
				tempstock.setHqsj(a[31]+" "+a[32]);  tempstock.setZxj(a[4]);
				tempstock.setZsp(a[3]);	  			 tempstock.setJkp(a[2]);
				float zde=0.00f;
				if(!a[4].equals("0.00")){
					zde=Float.parseFloat(a[4])-Float.parseFloat(a[3]);
					tempstock.setZde(Float.toString((float)(Math.round(zde*100))/100));
				}
				else
					tempstock.setZde("0.00");
				tempstock.setZd(a[6]); 				 tempstock.setZg(a[5]);
				if(!a[4].equals("0.00")){
					float zdf=zde/(Float.parseFloat(a[3]))*100;
					zdf=(float)(Math.round(zdf*100))/100;
					tempstock.setZdf(Float.toString(zdf));
				}
				else
					tempstock.setZdf("0.00");
				tempstock.setCjl(a[9]);   tempstock.setCje(a[10]);
				tempstock.setJmj(a[7]);   tempstock.setJmj2(a[8]);
				tempstock.setM1(a[11]+","+a[12]);
				tempstock.setM2(a[13]+","+a[14]);    tempstock.setM3(a[15]+","+a[16]);
				tempstock.setM4(a[17]+","+a[18]);    tempstock.setM5(a[19]+","+a[20]);
				tempstock.setMm1(a[21]+","+a[22]);   tempstock.setMm2(a[23]+","+a[24]);
				tempstock.setMm3(a[25]+","+a[26]);   tempstock.setMm4(a[27]+","+a[28]);
				tempstock.setMm5(a[29]+","+a[30]);
				stocklist.add(tempstock);
			}
			fillstock(views,stocklist,currentpage,pagesize);
			if(stocklist.size()>0){
				String temptime=stocklist.get(0).getHqsj();
				int templength=temptime.length();
				views.setTextViewText(R.id.txttime, temptime.substring(templength-8,templength-3));
			}
			Intent intentClick = new Intent(context,prostock.class);
		    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		                intentClick, 0);
		    
		    views.setOnClickPendingIntent(R.id.btnref, pendingIntent);
		    
		    Intent leftIntent = new Intent(context, stockwidget.class);
		    leftIntent.setAction("com.liwei.prostock.leftButton");
		    PendingIntent leftPendingIntent = PendingIntent.getBroadcast(context, 0, leftIntent, 0);
		    
		    views.setOnClickPendingIntent(R.id.btnplayleft, leftPendingIntent);
	
		    Intent rightIntent = new Intent(context, stockwidget.class);
		    rightIntent.setAction("com.liwei.prostock.rightButton");
		    PendingIntent rightPendingIntent = PendingIntent.getBroadcast(context, 0, rightIntent, 0);
		    views.setOnClickPendingIntent(R.id.btnplayright, rightPendingIntent);    
			return views;
		}
		catch(Exception ex){
			
	       	return null;
		}
	}
		//根据页号currentpage填充自选股
		public static boolean fillstock(RemoteViews views,ArrayList<stockdata> stocklist,int page,int psize){
			int[] dm=new int[]{R.id.dm1,R.id.dm2,R.id.dm3,R.id.dm4,R.id.dm5,R.id.dm6,R.id.dm7};
			int[] mc=new int[]{R.id.mc1,R.id.mc2,R.id.mc3,R.id.mc4,R.id.mc5,R.id.mc6,R.id.mc7};
			int[] xj=new int[]{R.id.xj1,R.id.xj2,R.id.xj3,R.id.xj4,R.id.xj5,R.id.xj6,R.id.xj7};
			int[] zdf=new int[]{R.id.zdf1,R.id.zdf2,R.id.zdf3,R.id.zdf4,R.id.zdf5,R.id.zdf6,R.id.zdf7};
			int start=psize*page;
			int end=start+psize;
			int stocklen=stocklist.size();
			if(stocklen>start){
					//清空widget
					for(int w=0;w<psize;w++){
						views.setTextViewText(dm[w], "");
						views.setTextViewText(mc[w], "");
						views.setTextViewText(xj[w], "");
						views.setTextViewText(zdf[w], "");
						views.setViewVisibility(dm[w], View.VISIBLE);
						views.setViewVisibility(mc[w], View.VISIBLE);
						views.setViewVisibility(xj[w], View.VISIBLE);
						views.setViewVisibility(zdf[w], View.VISIBLE);
					}
					for(int p=psize;p<7;p++){
						views.setViewVisibility(dm[p], View.GONE);
						views.setViewVisibility(mc[p], View.GONE);
						views.setViewVisibility(xj[p], View.GONE);
						views.setViewVisibility(zdf[p], View.GONE);
					}
					//自选数不够一页
					if(stocklen<end)
						end=stocklen;
					for(int k=start;k<end;k++){
						int t=k-start;//用于控作数组
						views.setTextViewText(dm[t], stocklist.get(k).getId());
						views.setTextViewText(mc[t], stocklist.get(k).getName());
						views.setTextViewText(xj[t], stocklist.get(k).getZxj());
						views.setTextViewText(zdf[t], stocklist.get(k).getZdf()+"%");
						views.setTextColor(mc[t], Color.YELLOW);
						float tempzde=Float.parseFloat( stocklist.get(k).getZde());
						if(!(tempzde==0)){
							if(tempzde>0.00f){
								views.setTextColor(xj[t],Color.RED);
								views.setTextColor(zdf[t],Color.RED);	    	
							}
							else{
								views.setTextColor(xj[t],Color.GREEN);
								views.setTextColor(zdf[t],Color.GREEN);
							}
						}
					}
					return true;
			}
			else
				return false;
		}
	
   
}
//	Handler handlerstock=new Handler(){
//		public void handleMessage(Message m){
//			
//			switch(m.what){
//			case 1://列表取得数据成功的消息
//				String mylist[]=(String[])m.getData().getStringArray("stock");
//				stocklist=new ArrayList<stockdata>();
//				for(int i=0;i<mylist.length;i++){
//					String tempstr=mylist[i];
//					String a[]=tempstr.split(",");
//					stockdata tempstock=new stockdata();
//					tempstock.setId(a[0]);     			 tempstock.setName(a[1]);
//					tempstock.setHqsj(a[31]+" "+a[32]);  tempstock.setZxj(a[4]);
//					tempstock.setZsp(a[3]);	  			 tempstock.setJkp(a[2]);
//					float zde=0.00f;
//					if(!a[4].equals("0.00")){
//						zde=Float.parseFloat(a[4])-Float.parseFloat(a[3]);
//						tempstock.setZde(Float.toString((float)(Math.round(zde*100))/100));
//					}
//					else
//						tempstock.setZde("0.00");
//					tempstock.setZd(a[6]); 				 tempstock.setZg(a[5]);
//					if(!a[4].equals("0.00")){
//						float zdf=zde/(Float.parseFloat(a[3]))*100;
//						zdf=(float)(Math.round(zdf*100))/100;
//						tempstock.setZdf(Float.toString(zdf));
//					}
//					else
//						tempstock.setZdf("0.00");
//					tempstock.setCjl(a[9]);   tempstock.setCje(a[10]);
//					tempstock.setJmj(a[7]);   tempstock.setJmj2(a[8]);
//					tempstock.setM1(a[11]+","+a[12]);
//					tempstock.setM2(a[13]+","+a[14]);    tempstock.setM3(a[15]+","+a[16]);
//					tempstock.setM4(a[17]+","+a[18]);    tempstock.setM5(a[19]+","+a[20]);
//					tempstock.setMm1(a[21]+","+a[22]);   tempstock.setMm2(a[23]+","+a[24]);
//					tempstock.setMm3(a[25]+","+a[26]);   tempstock.setMm4(a[27]+","+a[28]);
//					tempstock.setMm5(a[29]+","+a[30]);
//					stocklist.add(tempstock);
//				}
//					int[] dm=new int[]{R.id.dm1,R.id.dm2,R.id.dm3,R.id.dm4,R.id.dm5};
//					int[] mc=new int[]{R.id.mc1,R.id.mc2,R.id.mc3,R.id.mc4,R.id.mc5};
//					int[] xj=new int[]{R.id.xj1,R.id.xj2,R.id.xj3,R.id.xj4,R.id.xj5};
//					int[] zdf=new int[]{R.id.zdf1,R.id.zdf2,R.id.zdf3,R.id.zdf4,R.id.zdf5};
//					int n=appWidgetIds.length;
//					int t=5;
//					int stocklen=stocklist.size();
//					
//					for(int j=0;j<n;j++){
//						int appWidgetId=appWidgetIds[j];
//						RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.stockwidget);
//						//清空widget
//						for(int w=0;w<5;w++){
//							views.setTextViewText(dm[w], "");
//							views.setTextViewText(mc[w], "");
//							views.setTextViewText(xj[w], "");
//							views.setTextViewText(zdf[w], "");
//						}
//						//自选数不够
//						if(stocklen<5)
//							t=stocklen;
//						for(int k=0;k<t;k++){
//							views.setTextViewText(dm[k], stocklist.get(k).getId());
//							views.setTextViewText(mc[k], stocklist.get(k).getName());
//							views.setTextViewText(xj[k], stocklist.get(k).getZxj());
//							views.setTextViewText(zdf[k], stocklist.get(k).getZdf()+"%");
//							views.setTextColor(mc[k], Color.YELLOW);
//							float tempzde=Float.parseFloat( stocklist.get(k).getZde());
//							 if(!(tempzde==0)){
//								    if(tempzde>0.00f){
//								    	views.setTextColor(xj[k],Color.RED);
//								    	views.setTextColor(zdf[k],Color.RED);
//								    	
//								    }
//								    else{
//								    	views.setTextColor(xj[k],Color.GREEN);
//								    	views.setTextColor(zdf[k],Color.GREEN);
//
//
//								    }
//							    }
//						}
//						
//						appWidgetManager.updateAppWidget(appWidgetId, views);
//					}
//				
//	
//				break;
//			}
//		}
//	};

// private Handler handlertime=new Handler(){
//	public void handleMessage(Message msg){
//		switch(msg.what){
//		case 1:
//			
//			if(!Stockid.equals("")){
//				Responsestock(url+Stockid,1);
//			}
//			
//			
//			break;
//		}
//		super.handleMessage(msg);
//	}
//};
//private TimerTask timerTask=new TimerTask(){
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		Message message =new Message();
//		message.what=1;
//		handlertime.sendMessage(message);
//	}
//	
//};
	

