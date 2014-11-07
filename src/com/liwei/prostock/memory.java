package com.liwei.prostock;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.youmi.android.appoffers.YoumiOffersManager;

import com.liwei.youmi.MyPointsManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class memory extends Activity {
	GetStock thread=null;
	String url="http://hq.sinajs.cn/list=";
	ListView memorylistview=null;
	ProgressBar bar1=null;
	String strmemlist=null;
	ArrayList<moneydata> memdatalist=null;
	String[] Allmemlist=null;
	String[] tempa=null;
	ListView lv=null;
	private int getattr=1;
	MemoryAdapter myAdapter=null;
	private int fs=50;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		memorylistview=(ListView)findViewById(R.id.ListView01);
		if(!getok()){
			int jfq_fs=MyPointsManager.getInstance().queryPoints(memory.this);
			if(jfq_fs>=fs){
				int tempi=jfq_fs-fs;
				MyPointsManager.getInstance().spendPoints(memory.this, fs);
				setok();//设置为可以使用该功能
				new AlertDialog.Builder(memory.this).setTitle("您可以放心的使用该功能了!扣除积分"+Integer.toString(fs)+",你所积分剩余:"+Integer.toString(tempi))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
			}else{
				AlertDialog dialog=null;
				
				dialog=new AlertDialog.Builder(memory.this).setTitle("打开该功能需要50积分,请选择以下操作,你的积分为:"+Integer.toString(jfq_fs))
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
	
	
						YoumiOffersManager.showOffers(memory.this,
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
		}else{
		
			bar1=(ProgressBar)findViewById(R.id.barstock);
			
	//		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
	//    	Editor editor=sp.edit();
	//    	editor.putString("strmemory","");
	//    	editor.commit();
			
			
			SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
			strmemlist=sp.getString("strmemory", "");
			Allmemlist=this.getResources().getStringArray(R.array.stringmoney);
			if(!strmemlist.equals("")){
				Responsememory(url+strmemlist,getattr);
			}
			else
			{
				new AlertDialog.Builder(this).setTitle("没有自选外汇,是否添加?")
				.setPositiveButton("是的", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						AddMemory();
					}
				}).setNegativeButton("不要", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();	
			
			}	
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.stockmenu, menu);
		//return(super.onCreateOptionsMenu(menu));
		return true;
	}
	//主菜单点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.item_ref:
	    	Responsememory(url+strmemlist,getattr);
	        return true;
	    case R.id.item_addstock:
	    	AddMemory();
	        return true;
	    case R.id.item_exit:
	    	this.finish();
	    	return true;
	    case R.id.item_setup:
	    	Intent intentsetup=new Intent();
			intentsetup.setClass(this, stocksetup.class);
			startActivity(intentsetup); 
			return true;
	    case R.id.item_about:
	    	 Builder builder=new AlertDialog.Builder(this);  
	    	                 /** 设置对话框的标题*/  
	    	                 builder.setTitle(R.string.menu_about);  
	    	                 /** 设置对话框的内容*/  
	    	                 builder.setMessage(R.string.about_content);  
	    	                 /** 设置对话框的里按钮的Value值和设置按钮的点击事件并且弹出对话框*/  
	    	                 builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){  
	    	                         @Override  
	    	                         public void onClick(DialogInterface dialog, int which) {  
	    	                             /** 离开程序*/  
	    	                              
	    	                         }  
	    	                }).show();  
	    	 
			return true;

	    }
	    return false; //should never happen
	}
	
	private void setok(){
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		Editor editor=sp.edit();
    	editor.putBoolean("strmemoryok",true);
    	editor.commit();
	}
	private boolean getok(){
		
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		boolean isok=sp.getBoolean("strmemoryok", false);
		return isok;
	}
	private void AddMemory(){
		try{
			int memlen=Allmemlist.length;
			tempa=new String[memlen];
			String[] tempb=new String[memlen];
			boolean[] tempd=new boolean[memlen];
			for(int i=0;i<memlen;i++){
				String tempstr=Allmemlist[i];
				String[] tempc=tempstr.split(",");
				tempa[i]=tempc[0];
				tempb[i]=tempc[1];
				if(strmemlist.indexOf(tempc[0])==-1)
					tempd[i]=false;
				else
					tempd[i]=true;
				
			}
			AlertDialog ad=new AlertDialog.Builder(this).setTitle("选择外汇选项")
					.setMultiChoiceItems(tempb, tempd,
					new DialogInterface.OnMultiChoiceClickListener() {
						public void onClick(DialogInterface dialog, int which, boolean isChecked) {
							
						}
					}).setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
								
									strmemlist="";
										for(int j=0;j<tempa.length;j++){
											if(lv.getCheckedItemPositions().get(j)){
												strmemlist=strmemlist+tempa[j]+",";
											}
										}
										if(!strmemlist.equals(""))
											strmemlist=strmemlist.substring(0, strmemlist.length()-1);
										SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
						    	    	Editor editor=sp.edit();
						    	    	editor.putString("strmemory",strmemlist);
						    	    	editor.commit();
						    	    	if(!strmemlist.equals(""))
						    	    		Responsememory(url+strmemlist,getattr);
	
								}
							}).setNegativeButton("取消", null).create();
			lv=ad.getListView();

			ad.show();
		}
		catch(Exception ex){
			new AlertDialog.Builder(memory.this).setMessage(ex.getMessage()).show();
		}
	}
	private void Responsememory(String tempurl,int getattr2){
		bar1.setVisibility(View.VISIBLE);
		thread=new GetStock(handlermem);
		thread.doStart(tempurl,getattr2,this);
	}
	public List<Map<String, String>> fillmap(){
		List<Map<String, String>> items=new ArrayList<Map<String,String>>();
		for(int i=0;i<memdatalist.size();i++){
			Map<String, String> tempmap=new HashMap<String, String>();
			tempmap.put("memid", memdatalist.get(i).getM_id());
			tempmap.put("memname", memdatalist.get(i).getM_name());
			tempmap.put("memxj",memdatalist.get(i).getM_xj());
			tempmap.put("memzde", memdatalist.get(i).getM_zde().trim()+"("+memdatalist.get(i).getM_zdf()+"%)");
			tempmap.put("memjkp", memdatalist.get(i).getM_jkp());
			tempmap.put("memzsp", memdatalist.get(i).getM_zspj());
			tempmap.put("memzgj", memdatalist.get(i).getM_zgj());
			tempmap.put("memzdj", memdatalist.get(i).getM_zdj());
			tempmap.put("memtime", memdatalist.get(i).getM_time());
			items.add(tempmap);
		}
		return items;
	}
	Handler handlermem=new Handler(){
		public void handleMessage(Message m){
			bar1.setVisibility(View.GONE);
			switch(m.what){
			case 1:
				String mylist[]=(String[])m.getData().getStringArray("stock");
				memdatalist=new ArrayList<moneydata>();
				for(int i=0;i<mylist.length;i++){
					String tempstr=mylist[i];
					String a[]=tempstr.split(",");
					moneydata tempdata=new moneydata();
					tempdata.setM_id(a[0]);
					tempdata.setM_time(a[1]);
					tempdata.setM_mrj(a[2]);
					tempdata.setM_mcj(a[3]);
					tempdata.setM_zspj(a[4]);
					tempdata.setM_zf(a[5]);
					tempdata.setM_jkp(a[6]);
					tempdata.setM_zgj(a[7]);
					tempdata.setM_zdj(a[8]);
					tempdata.setM_xj(a[9]);
					tempdata.setM_name(a[10]);
					float m_zde=Float.parseFloat(a[9])-Float.parseFloat(a[4]);
					m_zde=(float)(Math.round(m_zde*10000))/10000;
					tempdata.setM_zde(String.format("%.4f ",m_zde));
					float m_zdf=m_zde*100/Float.parseFloat(a[4]);
					m_zdf=(float)(Math.round(m_zdf*10000))/10000;
					tempdata.setM_zdf(Float.toString(m_zdf));
					memdatalist.add(tempdata);				
				}
				List<Map<String, String>> items=fillmap();
				myAdapter= new MemoryAdapter(memory.this,items,R.layout.moneyitem
						 ,new String[]{"memname","memxj","memzde","memjkp","memzsp","memzgj","memzdj","memtime"}
						 ,new int[]{R.id.txtmemmc,R.id.txtmemxj,R.id.txtmemzde,R.id.txtmemjkp,R.id.txtmemzsp,R.id.txtmemzgj,R.id.txtmemzdj,R.id.txtmemtime});

				memorylistview.setAdapter(myAdapter);
				break;
			case 2:
				break;
			case 3:
				break;
			}
		}
	};
	
}
