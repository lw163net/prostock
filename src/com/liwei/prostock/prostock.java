package com.liwei.prostock;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import net.youmi.android.appoffers.CheckStatusNotifier;
import net.youmi.android.appoffers.YoumiOffersManager;
import net.youmi.android.appoffers.YoumiPointsManager;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.liwei.rssread.rssnews;
import com.liwei.youmi.*;






public class prostock extends ActivityGroup implements CheckStatusNotifier  {
	//int menuImages[] = {R.drawable.stock_s,R.drawable.stock,R.drawable.fund,R.drawable.futures,R.drawable.stock_n,R.drawable.message};
	//private String menuStrs[] = {"指数","股票","基金","期货","新闻","提醒"};
	int menuImages[] = {R.drawable.stock_s,R.drawable.stock,R.drawable.fund,R.drawable.futures,R.drawable.meney,R.drawable.stock_n};
	private String menuStrs[] = {"指数","股票","基金","期货","外汇","新闻"};
	private LinearLayout container = null;
	private TextView txthometitle=null;
	private Button btnqgg=null;
	private GridView gridView;
	private int ggfs=20;
	private static AdView adv=null;
	private boolean getggbool(){
		String strqgg=null;
		boolean bool=true;
	
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		strqgg=sp.getString("strqgg", "");
		String strnow=getdate();
		if(strqgg.equals("")){
			Editor editor=sp.edit();
	    	editor.putString("strqgg",strnow);
	    	editor.commit();
	    	return true;
		}
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1=df.parse(strnow);
			Date dt2=df.parse(strqgg);
			Long days=getDistDates(dt2,dt1);
			if(days>0)
				bool= false;
			else
				bool= true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return true;
		}
		return bool;
	}
	public long getDistDates(Date startDate,Date endDate)    
	    {  
	        long totalDate = 0;  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(startDate);  
	        long timestart = calendar.getTimeInMillis();  
	        calendar.setTime(endDate);  
	        long timeend = calendar.getTimeInMillis(); 
	        totalDate = Math.abs((timeend - timestart))/(1000*60*60*24);  
	        return totalDate;  
	    }   

	private String getdate(){
		Date now = new Date(); 
	    Calendar cal = Calendar.getInstance(); 
	    DateFormat d1=new SimpleDateFormat("yyyy-MM-dd");
	    String str1 = d1.format(now);
	    return str1;
	}
	private void setggdate(){
		Date now = new Date(); 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(now);
		long t1 = cal.getTimeInMillis(); 
		t1+=(1000*60*60*24*16);
		DateFormat d1=new SimpleDateFormat("yyyy-MM-dd");
		Date dt = new Date(t1);  
		String sDateTime = d1.format(dt);  
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		Editor editor=sp.edit();
    	editor.putString("strqgg",sDateTime);
    	editor.commit();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.prohome);
    	AdManager.init(prostock.this,"c12d7e09626e0fee", "4e6dc1daca47c346", 30, false);
        
    	adv=(AdView)findViewById(R.id.adView);
        //有米广告
        if(getggbool()){
        	
        	//adv.setVisibility(View.VISIBLE);
        	btnqgg=(Button)findViewById(R.id.btnqgg);
        	btnqgg.setText("去除广告");
        	btnqgg.setOnClickListener(new View.OnClickListener(){
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				int jfq_fs=MyPointsManager.getInstance().queryPoints(prostock.this);
    				if(jfq_fs>=ggfs){
    					int tempi=jfq_fs-ggfs;
    					MyPointsManager.getInstance().spendPoints(prostock.this, ggfs);
    					setggdate();
    					new AlertDialog.Builder(prostock.this).setTitle("扣除积分"+Integer.toString(ggfs)+",你所积分剩余:"+Integer.toString(tempi))
    					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							// TODO Auto-generated method stub
    						}
    					}).show();
    				}else{
    					AlertDialog dialog=null;
    					
    					dialog=new AlertDialog.Builder(prostock.this).setTitle("你的积分不足,请选择以下操作,你的积分为:"+Integer.toString(jfq_fs))
    					.setPositiveButton("获取积分", new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							// TODO Auto-generated method stub
    							//Addfund();
    							try { 
    								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
    								field.setAccessible(true); 
    								field.set(dialog, false); 
    								} 
    							catch (Exception e) 
    							{ 
    								e.printStackTrace(); 
    							}
    							//展示积分强
    							YoumiOffersManager.showOffers(prostock.this,
    									YoumiOffersManager.TYPE_REWARD_OFFERS,MyPointsManager.getInstance());
    						}
    					}).setNegativeButton("退出", new DialogInterface.OnClickListener() {
    						
    						@Override
    						public void onClick(DialogInterface dialog, int which) {
    							// TODO Auto-generated method stub

    								//关闭对话框
    								try {
    								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
    								field.setAccessible(true);
    								field.set(dialog, true);
    								} catch (Exception e) {
    									e.printStackTrace();
    								}
    						}
    					}).show();
    				}
    			}});
        }
        else{
        	adv.setVisibility(View.GONE);
        	btnqgg=(Button)findViewById(R.id.btnqgg);
        	btnqgg.setText("更多应用");
        	btnqgg.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					YoumiOffersManager.checkStatus(prostock.this,
							prostock.this);
					
					YoumiOffersManager.showOffers(prostock.this,
							YoumiOffersManager.TYPE_REWARD_OFFERS,MyPointsManager.getInstance());
				}});
        	//LinearLayout l1=(LinearLayout)findViewById(R.id.llgg);
        	//l1.setVisibility(View.GONE);
        }
        
        //有米积分
        YoumiOffersManager.init(this,"c12d7e09626e0fee","4e6dc1daca47c346");
        YoumiPointsManager.setUserID("lw163net@foxmail.com");                                
        container = (LinearLayout)findViewById(R.id.llfield);
        txthometitle=(TextView)findViewById(R.id.txthometitle);
        
        LoadMenu();
        txthometitle.setText("指数");
        container.removeAllViews();
        LocalActivityManager m = getLocalActivityManager();
        Intent intent = new Intent(prostock.this, stockindex.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Window w = m.startActivity("stockindex", intent);
        View v = w.getDecorView();
        
        container.addView(v);
	}
	public boolean onPrepareOptionsMenu(Menu menu) {    
		//super.onPrepareOptionsMenu(menu);     
		menu.clear();
		String classname=getLocalActivityManager().getCurrentActivity().getLocalClassName();
		if(classname.equals("stockindex"))
			getMenuInflater().inflate(R.menu.stockmenu, menu);
		if(classname.equals("stock"))
			getMenuInflater().inflate(R.menu.stockmenu, menu);
		if(classname.equals("fund"))
			getMenuInflater().inflate(R.menu.stockmenu, menu);
		if(classname.equals("qh"))
			getMenuInflater().inflate(R.menu.qhmenu, menu);
		if(classname.equals("memory"))
			getMenuInflater().inflate(R.menu.stockmenu, menu);
		getLocalActivityManager().getCurrentActivity().onCreateOptionsMenu(menu);
		//return getLocalActivityManager().getCurrentActivity().onCreateOptionsMenu(menu); 
		return(super.onCreateOptionsMenu(menu));
	}  
	public boolean onCreateOptionsMenu(Menu menu) {     
		//super.onCreateOptionsMenu(menu);     
		////getMenuInflater().inflate(R.menu.stockmenu, menu);
		
		
		Log.d("prostock", getLocalActivityManager().getCurrentActivity().getLocalClassName());
		//return getLocalActivityManager().getCurrentActivity().onCreateOptionsMenu(menu); 
		return(super.onCreateOptionsMenu(menu));
	}  
	public boolean onMenuItemSelected(int featureId, MenuItem item) {     
		//super.onMenuItemSelected(featureId, item);     
		
		return getLocalActivityManager().getCurrentActivity().onMenuItemSelected(featureId, item); 
	} 
	
	
	public void LoadMenu(){
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);//取得屏幕参数

		gridView = (GridView) findViewById(R.id.gridViewhome);
		gridView.setNumColumns(menuImages.length);//设置gridview个数
		gridView.setColumnWidth((dm.widthPixels) / 5);//每列宽度
		LinearLayout.LayoutParams liner = (LinearLayout.LayoutParams) gridView
				.getLayoutParams();//取得gridview参数
		
		liner.width = menuImages.length*(dm.widthPixels) / 5;//设置gridview 宽度
		gridView.setLayoutParams(liner);
		class ItemView extends BaseAdapter {
			public int position;
			private Context context;

			public ItemView(Context context) {
				this.context = context;
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return menuImages.length;
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				this.position = position;
				return this.position;
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				this.position = position;
				return this.position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				this.position = position;
				convertView = LayoutInflater.from(context).inflate(
						R.layout.griditem, null);
				ImageView itemIcon = (ImageView) convertView
						.findViewById(R.id.imageitem);
				//itemIcon.setBackgroundResource(menuImages[position]);
				//itemIcon.setAdjustViewBounds(true);
				//itemIcon.setMaxHeight(48);
				//itemIcon.setMaxWidth(64);
				
				itemIcon.setBackgroundResource(menuImages[position]);
				
				itemIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
				//itemIcon.setImageDrawable(menuImages[position]);
				//itemIcon.setImageBitmap(drawableToBitmap(menuImages[position]));
				//
				itemIcon.setPadding(8, 8, 8, 8);
				TextView itemText = (TextView) convertView
						.findViewById(R.id.textitem);
				itemText.setText(menuStrs[position]);

				return convertView;
			}

			public int getPosition() {
				return (int) getItemId(position);
			}

		}
		ItemView itemView = new ItemView(this);
		gridView.setAdapter(itemView);
		gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				container = (ScrollView) findViewById(R.id.containerBody);
				txthometitle.setText(menuStrs[arg2]);
				if(menuStrs[arg2].equals("指数")){
//					container.removeAllViews();
//	                container.addView(getLocalActivityManager().startActivity(
//	                        "stockindex",
//	                        new Intent(prohome.this, stockindex.class)
//	                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//	                        .getDecorView());
					container.removeAllViews();
	                LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, stockindex.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stockindex", intent);
	                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	                View v = w.getDecorView();
	                
	        
	                
	                container.addView(v);
	                
	                

				}
				if(menuStrs[arg2].equals("股票")){
					container.removeAllViews();
					LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, stock.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stock", intent);
	                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	                View v = w.getDecorView();
	           
	                container.addView(v);
	                
	               
				
				}
				if(menuStrs[arg2].equals("基金")){
					container.removeAllViews();
					LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, fund.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stock", intent);
	                View v = w.getDecorView();
	           
	                container.addView(v);
	               
				
				}
				if(menuStrs[arg2].equals("期货")){
					container.removeAllViews();
					LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, qh.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stock", intent);
	                View v = w.getDecorView();
	           
	                container.addView(v);
	               
				
				}
				if(menuStrs[arg2].equals("外汇")){
					container.removeAllViews();
					LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, memory.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stock", intent);
	                View v = w.getDecorView();
	           
	                container.addView(v);
	               
				
				}
				if(menuStrs[arg2].equals("新闻")){
					container.removeAllViews();
					LocalActivityManager m = getLocalActivityManager();
	                Intent intent = new Intent(prostock.this, rssnews.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                Window w = m.startActivity("stock", intent);
	                View v = w.getDecorView();
	           
	                container.addView(v);
	               
				
				}
				
			}});

	}
//	@Override
//	public void onCheckStatusConnectionFailed(Context arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void onCheckStatusResponse(Context arg0, boolean arg1, boolean arg2,
//			boolean arg3) {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public void onCheckStatusConnectionFailed(Context arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCheckStatusResponse(Context arg0, boolean arg1, boolean arg2,
			boolean arg3) {
		// TODO Auto-generated method stub
		
	}
}
