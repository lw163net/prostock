package com.liwei.prostock;



import com.liwei.rssread.rssnews;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class prostockbak extends TabActivity implements OnTabChangeListener{
    /** Called when the activity is first created. */
	private TabHost mTabhost;
	TabHost.TabSpec stockindexspec;
	private static final int TAB_INDEX_STOCK=0;
	private static final int TAB_INDEX_FUND=1;
	private  String currenttab=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        //自定义标题
//        SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
//    	Editor editor=sp.edit();
//    	editor.putString("strstock","");
//    	editor.commit();
    	
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //自定义标题
        this.requestWindowFeature( Window.FEATURE_NO_TITLE ); 
        setContentView(R.layout.tabmainface);       
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);   
        //setContentView(R.layout.tabmainface); 
        
       // Button titleRef = (Button)findViewById(R.id.title_button);  
        //TextView strTitle=(TextView)findViewById(R.id.TxtTitle);
       // strTitle.setText(R.string.title);
        
        mTabhost=this.getTabHost();
//        titleRef.setOnClickListener(new View.OnClickListener() {		
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(currenttab.equals("stockindex")){
//					Intent intent = new Intent(prostock.this,stockindex.class);
//					 Activity activity = getLocalActivityManager().getActivity(currenttab);
//					// getLocalActivityManager().destroyActivity(getLocalActivityManager().getCurrentId(), true);
//					// getLocalActivityManager().startActivity("stockindex", intent);
//				}
//			}
//			});
        
        
        setupIndex();
        setupStockTab();
        setupFundTab();
        setupQhTab();
       // setupRateTab();
        setupNewTab();
        mTabhost.setOnTabChangedListener(this);
        this.currenttab="stockindex";
        
    }

	private void setupIndex() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,stockindex.class);
		stockindexspec=mTabhost.newTabSpec("stockindex")
				.setIndicator("指数",this.getResources().getDrawable(R.drawable.stock_s))
				.setContent(intent);
		mTabhost.addTab(stockindexspec);
	}

	private void setupNewTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,rssnews.class);
		mTabhost.addTab(mTabhost.newTabSpec("rssnews")
				.setIndicator("新闻",this.getResources().getDrawable(R.drawable.stock_n))
				.setContent(intent));
	
	}
	private void setupFundTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,fund.class);
		mTabhost.addTab(mTabhost.newTabSpec("fund")
				.setIndicator("基金",this.getResources().getDrawable(R.drawable.fund))
				.setContent(intent));
	
	}
//	private void setupHkStockTab() {
//		// TODO Auto-generated method stub
//		Intent intent = new Intent(this,fund.class);
//		mTabhost.addTab(mTabhost.newTabSpec("fund")
//				.setIndicator("港股",this.getResources().getDrawable(R.drawable.myspace))
//				.setContent(intent));
//	
//	}
	private void setupQhTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,qh.class);
		mTabhost.addTab(mTabhost.newTabSpec("qh")
				.setIndicator("期货",this.getResources().getDrawable(R.drawable.futures))
				.setContent(intent));
	
	}
	private void setupRateTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,fund.class);
		mTabhost.addTab(mTabhost.newTabSpec("exchangerate")
				.setIndicator("汇率",this.getResources().getDrawable(R.drawable.futures))
				.setContent(intent));
	
	}
	private void setupStockTab() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,stock.class);
		mTabhost.addTab(mTabhost.newTabSpec("stock")
				.setIndicator("股票",this.getResources().getDrawable(R.drawable.stock))
				.setContent(intent));
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		this.currenttab=tabId;
		 Activity activity = getLocalActivityManager().getActivity(tabId);
	        if (activity != null) {
	            activity.onWindowFocusChanged(true);
	            
	        }
	}
}