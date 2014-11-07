package com.liwei.prostock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import com.liwei.prostock.*;


public class stockwidget4 extends AppWidgetProvider {
	private static final String TAG="stockwidget4"; 
	private Timer timer=new Timer();
	GetStock thread=null;
	private int[] appWidgetIds;
	private AppWidgetManager appWidgetManager;
	private static Context context;
	private String Stockid=null;
	private String url="http://hq.sinajs.cn/list=";
	//final static String leftButton_actionName = "leftButton";
//	final static String rightButton_actionName = "rightButton";
	static int currentpage=0;
	static int pagesize=15;
	static ArrayList<stockdata> stocklist=new ArrayList<stockdata>();
	static ArrayList<stockindexdata> stockindexlist=new ArrayList<stockindexdata>();;
	static String str_speak_list="";
	public static final String ACTION_UPDATE_ALL = "com.liwei.prostock.START_STOCK_SERVICE4";
    @Override  
    public void onDeleted(Context context, int[] appWidgetIds) {   
    	super.onDeleted(context, appWidgetIds);
    	//停止service定时刷新
    	stocklist.clear();
    	//删除widget就应该把定时取消
    	 Intent updateIntent=new Intent(ACTION_UPDATE_ALL);   
        updateIntent.setClass(context, StockService4.class);   
        PendingIntent pending=PendingIntent.getService(context, 0, updateIntent, 0);
        AlarmManager alarm=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);   
        alarm.cancel(pending);
        
        //Log.d(TAG, "call onDeleted");
        
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
        context.stopService(new Intent(context, stockwidget4.class));
    }   
  
    @Override  
    public void onReceive(Context context, Intent intent) {   
        super.onReceive(context, intent);   
       // android.os.Debug.waitForDebugger();
	        RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.stockwidget4);
	        //Log.d(TAG, String.valueOf(currentpage));
	        
//	        SharedPreferences sp=context.getSharedPreferences("prostock",Context.MODE_PRIVATE);
//			String strtemp=sp.getString("stocklist", "");
//			Log.i(TAG, strtemp);
//			String a[]=strtemp.split(":");
//			Log.i(TAG, a[0]);
//			stocklist=getstocklist(a);
	        if(stocklist==null)
	        	Log.i(TAG, "null");
	        else{
	        	//Log.i(TAG, String.valueOf(stocklist.size()));
		        if (intent.getAction().equals("com.liwei.prostock.leftButton4")){
		        	//向左翻页
		        	if(currentpage!=0){
		        		if(fillstock(views,stocklist,currentpage-1,pagesize))
		        			currentpage=currentpage-1;
			        		ComponentName thisWidget = new ComponentName(context, stockwidget4.class);
			    	        AppWidgetManager manager =AppWidgetManager.getInstance(context);
			    	        manager.updateAppWidget(thisWidget, views);
		        		}
		        }else{
		        	//向右翻页
		        	if(intent.getAction().equals("com.liwei.prostock.rightButton4")){
		        		if(fillstock(views,stocklist,currentpage+1,pagesize))
		        			currentpage=currentpage+1;
		        		ComponentName thisWidget = new ComponentName(context, stockwidget4.class);
		    	        AppWidgetManager manager =AppWidgetManager.getInstance(context);
		    	        manager.updateAppWidget(thisWidget, views);
		        	}
		        }
		        //朗读行情tts
		        if(intent.getAction().equals("com.liwei.prostock.speakButton4")){
		        	int stock_list_len=stocklist.size();
		        	str_speak_list="";
		        	//读上证综指
		        	str_speak_list+="上证综指,";
		        	str_speak_list+=stockindexlist.get(0).getZsds();
		        	str_speak_list+="点,";
		        	
		        	float tempzd=Float.parseFloat(stockindexlist.get(0).getZszdl());
					if(!(tempzd==0)){
						if(tempzd>0.00f){
							str_speak_list=str_speak_list+"涨"+stockindexlist.get(0).getZszdl()+"%。";	    	
						}
						else{
							str_speak_list=str_speak_list+"跌"+stockindexlist.get(0).getZszdl().substring(1)+"%。";
						}
					}
					else
					{
						str_speak_list=str_speak_list+"平"+stockindexlist.get(0).getZszdl()+"%。";
					}
					//读深圳成指
					str_speak_list+="深圳成指,";
		        	str_speak_list+=stockindexlist.get(1).getZsds();
		        	str_speak_list+="点,";
		        	float tempzdsz=Float.parseFloat(stockindexlist.get(1).getZszdl());
					if(!(tempzdsz==0)){
						if(tempzdsz>0.00f){
							str_speak_list=str_speak_list+"涨"+stockindexlist.get(1).getZszdl()+"%。";	    	
						}
						else{
							str_speak_list=str_speak_list+"跌"+stockindexlist.get(1).getZszdl().substring(1)+"%。";
						}
					}
					else
					{
						str_speak_list=str_speak_list+"平"+stockindexlist.get(1).getZszdl()+"%。";
					}
					
					
		        	for(int i=0;i<stock_list_len;i++){
		        		String speak_one="";
		        		speak_one=speak_one+stocklist.get(i).getName()+",";
		        		speak_one=speak_one+stocklist.get(i).getZxj()+"元,";
		        		float tempzde=Float.parseFloat( stocklist.get(i).getZde());
						if(!(tempzde==0)){
							if(tempzde>0.00f){
								speak_one=speak_one+"涨"+stocklist.get(i).getZdf()+"%。";	    	
							}
							else{
								speak_one=speak_one+"跌"+stocklist.get(i).getZdf().substring(1)+"%。";
							}
						}
						else
						{
							speak_one=speak_one+"平"+stocklist.get(i).getZdf()+"%。";
						}
						str_speak_list=str_speak_list+speak_one;
		        	}
		        	str_speak_list=str_speak_list+"播报完毕";
		        	Log.i(TAG, str_speak_list);
		        	//服务是否启动，如果启动了则停止服务,如果没有启动则启动之
		        	if(classqg.isServiceRunning(context, "com.liwei.prostock.speakService")){
		        		Log.d(TAG,"stop speakserver");
		        		context.stopService(new Intent(context, speakService.class));
		        		
		        	}
		        		Log.d(TAG,"start speakserver");
			        	Intent serviceIntent = new Intent();
			        	serviceIntent.setAction("com.liwei.prostock.speakService");
			        	intent.putExtra("STR_STOCK_SPEAK", str_speak_list);
			        	context.startService(serviceIntent);
			        	
		        	
		        }
	        }

        }
     
	
	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds){
	    //  android.os.Debug.waitForDebugger();
		 this.appWidgetIds=appWidgetIds;
	     //启动服务
		 StockService4.updateAppWidgetIds(appWidgetIds);   
	     Intent serviceIntent = new Intent();
	     serviceIntent.setAction("com.liwei.prostock.START_STOCK_SERVICE4");
	     context.startService(serviceIntent);
	    // PendingIntent Pintent=PendingIntent.getService(context,0,serviceIntent,0); 
	}

	//定时刷新页面
	public static ArrayList<stockdata> getstocklist(String mylist[]){
		ArrayList<stockdata> ptrstocklist=null;
		if(mylist.length>0){
			ptrstocklist=new ArrayList<stockdata>();
			for(int i=0;i<mylist.length;i++){
				String tempstr=mylist[i];
				String a[]=tempstr.split(",");
				stockdata tempstock=new stockdata();
				tempstock.setId(a[0]);     			 tempstock.setName(a[1]);
				tempstock.setHqsj(a[31]+" "+a[32]);  tempstock.setZxj(a[4]);
				tempstock.setZsp(a[3]);	  			 tempstock.setJkp(a[2]);
				float zde=0.00f;
				if(!(a[4].equals("0.00")||a[4].equals("0.000")||a[4].equals("0.0"))){
					zde=Float.parseFloat(a[4])-Float.parseFloat(a[3]);
					tempstock.setZde(Float.toString((float)(Math.round(zde*100))/100));
				}
				else
					tempstock.setZde("0.00");
				tempstock.setZd(a[6]); 				 tempstock.setZg(a[5]);
				if(!(a[4].equals("0.00")||a[4].equals("0.000")||a[4].equals("0.0"))){
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
				ptrstocklist.add(tempstock);
			}
		
		}
		return ptrstocklist;
	}
	//刷新appwidget行情
	public static RemoteViews updateAppWidget(Context context,String mylist[],String indexlist[],int psize) {
		try{
			//Log.i(TAG, "updateAppWidget");
			//读一页大小
			pagesize=psize;
			RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.stockwidget4);
			//刷新时间
			Date d=new Date() ;
			d.getTime();
			Calendar c=Calendar.getInstance();		
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String time=format.format(c.getTime());

			views.setTextViewText(R.id.textconstate, "刷新:"+time.substring(11)+"  ");
			
			
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
			
			stocklist.clear();
			stocklist=getstocklist(mylist);
			fillstock(views,stocklist,currentpage,pagesize);
			if(stocklist.size()>0){
				String temptime=stocklist.get(0).getHqsj();
				int templength=temptime.length();
				views.setTextViewText(R.id.txttime, temptime.substring(templength-8,templength-3)+" ");
			}
			Intent intentClick = new Intent(context, prostock.class);
		    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
		                intentClick, 0);
		    
		    views.setOnClickPendingIntent(R.id.btnref, pendingIntent);
		    
		    Intent leftIntent = new Intent(context, stockwidget4.class);
		    leftIntent.setAction("com.liwei.prostock.leftButton4");
		    PendingIntent leftPendingIntent = PendingIntent.getBroadcast(context, 0, leftIntent, 0);
		    
		    views.setOnClickPendingIntent(R.id.btnplayleft, leftPendingIntent);
	
		    Intent rightIntent = new Intent(context, stockwidget4.class);
		    rightIntent.setAction("com.liwei.prostock.rightButton4");
		    PendingIntent rightPendingIntent = PendingIntent.getBroadcast(context, 0, rightIntent, 0);
		    views.setOnClickPendingIntent(R.id.btnplayright, rightPendingIntent);  
		    
		    Intent speakIntent = new Intent(context, stockwidget4.class);
		    speakIntent.setAction("com.liwei.prostock.speakButton4");
		    PendingIntent speakPendingIntent = PendingIntent.getBroadcast(context, 0, speakIntent, 0);
		    views.setOnClickPendingIntent(R.id.btnswicth, speakPendingIntent); 
		    return views;
		}
		catch(Exception ex){
			
           	return null;
       }
		
	}
	//根据页号currentpage填充自选股
	public static RemoteViews updateZt(Context context,String  netstate,String connectstate,boolean bar){
		RemoteViews viewzt=new RemoteViews(context.getPackageName(),R.layout.stockwidget4);
		viewzt.setTextViewText(R.id.textnetstate, "网络:"+netstate);
		
		return viewzt;
		
	}
	public static RemoteViews updateErr(Context context,String errstr,boolean ok){
		RemoteViews viewzt=new RemoteViews(context.getPackageName(),R.layout.stockwidget4);
		viewzt.setTextViewText(R.id.txtgeterr,errstr);
		if(ok)
			viewzt.setTextColor(R.id.txtgeterr, Color.GREEN);
		else
			viewzt.setTextColor(R.id.txtgeterr, Color.RED);
		return viewzt;
	}
	public static RemoteViews updateZtPro(Context context,boolean show){
		RemoteViews viewpro=new RemoteViews(context.getPackageName(),R.layout.stockwidget4);
		if(show){
			viewpro.setViewVisibility(R.id.linearLayoutstatusbar, View.VISIBLE);
		}
		else
		{
			viewpro.setViewVisibility(R.id.linearLayoutstatusbar, View.INVISIBLE);
		}
		return viewpro;
		
	}
	public static boolean fillstock(RemoteViews views,ArrayList<stockdata> stocklistptr,int page,int psize){
		
		//LinearLayout rssaddlayout=(LinearLayout)getLayoutInflater().inflate(R.layout.addrssurl, null);
		//RemoteViews contentView =new RemoteViews(context.getPackageName(), R.layout.stockwidget4);
		int[] dm=new int[]{R.id.dm1,R.id.dm2,R.id.dm3,R.id.dm4,R.id.dm5,R.id.dm6,R.id.dm7,R.id.dm8,R.id.dm9,R.id.dm10,R.id.dm11,R.id.dm12,R.id.dm13,R.id.dm14,R.id.dm15,R.id.dm16,R.id.dm17,R.id.dm18};
		int[] mc=new int[]{R.id.mc1,R.id.mc2,R.id.mc3,R.id.mc4,R.id.mc5,R.id.mc6,R.id.mc7,R.id.mc8,R.id.mc9,R.id.mc10,R.id.mc11,R.id.mc12,R.id.mc13,R.id.mc14,R.id.mc15,R.id.mc16,R.id.mc17,R.id.mc18};
		int[] xj=new int[]{R.id.xj1,R.id.xj2,R.id.xj3,R.id.xj4,R.id.xj5,R.id.xj6,R.id.xj7,R.id.xj8,R.id.xj9,R.id.xj10,R.id.xj11,R.id.xj12,R.id.xj13,R.id.xj14,R.id.xj15,R.id.xj16,R.id.xj17,R.id.xj18};
		int[] zdf=new int[]{R.id.zdf1,R.id.zdf2,R.id.zdf3,R.id.zdf4,R.id.zdf5,R.id.zdf6,R.id.zdf7,R.id.zdf8,R.id.zdf9,R.id.zdf10,R.id.zdf11,R.id.zdf12,R.id.zdf13,R.id.zdf14,R.id.zdf15,R.id.zdf16,R.id.zdf17,R.id.zdf18};
		//views.setViewVisibility(R.id.dm16, View.GONE);
		int start=psize*page;
		int end=start+psize;
		int stocklen=stocklistptr.size();
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
				for(int p=psize;p<18;p++){
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
					views.setTextViewText(dm[t], stocklistptr.get(k).getId());
					views.setTextViewText(mc[t], stocklistptr.get(k).getName());
					views.setTextViewText(xj[t], stocklistptr.get(k).getZxj());
					views.setTextViewText(zdf[t], stocklistptr.get(k).getZdf()+"%");
					views.setTextColor(mc[t], Color.YELLOW);
					float tempzde=Float.parseFloat( stocklistptr.get(k).getZde());
					if(!(tempzde==0)){
						if(tempzde>0.00f){
							views.setTextColor(xj[t],Color.RED);
							views.setTextColor(zdf[t],Color.RED);	    	
						}
						else{
							views.setTextColor(xj[t],Color.GREEN);
							views.setTextColor(zdf[t],Color.GREEN);
						}
					}else{
						views.setTextColor(xj[t],Color.WHITE);
						views.setTextColor(zdf[t],Color.WHITE);
					}
				}
				
				//views.setViewVisibility(R.id.ProBarZt, View.INVISIBLE);
				//views.setViewVisibility(R.id.ProgressBar01,View.GONE );
				return true;
		}
		else
			return false;
	}

}


