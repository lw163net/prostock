package com.liwei.prostock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class fund extends Activity implements OnItemClickListener,OnItemLongClickListener{
	GetStock thread=null;
	String strfundlist="";
	String url="http://hq.sinajs.cn/list=";
	ArrayList<funddata> fundlist=null;
	ListView fundlistview=null;
	ProgressBar bar1=null;
	private int getattr=1;
	FundAdapter myAdapter=null;
	EditText etstock=null;
	static final int SEARCH_REQUEST=1;
	private String[] longclickmenu=new String[]{"删除自选基金"};//长按listview选项后弹出的菜单
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		fundlistview=(ListView)findViewById(R.id.ListView01);
		bar1=(ProgressBar)findViewById(R.id.barstock);
		fundlistview.setOnItemClickListener(this);
		fundlistview.setOnItemLongClickListener(this);
//		SharedPreferences sd=getSharedPreferences("prostock",Context.MODE_PRIVATE);
//	    Editor editor=sd.edit();
//    	editor.putString("strfund", "");
//    	editor.commit();
		
		
		
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		strfundlist=sp.getString("strfund", "");
		
		if(!strfundlist.equals("")){
			Responsefund(url+strfundlist,getattr);
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("没有自选基金,是否添加?")
			.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Addfund();
				}
			}).setNegativeButton("不要", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();	
		
		}
	}
	public List<Map<String, String>> fillmap(){
		List<Map<String, String>> items=new ArrayList<Map<String,String>>();
		for(int i=0;i<fundlist.size();i++){
			Map<String, String> tempmap=new HashMap<String, String>();
			tempmap.put("fundid", fundlist.get(i).getFundid());
			tempmap.put("fundname", fundlist.get(i).getFundname());
			tempmap.put("fundzxjz",fundlist.get(i).getFundzxjz());
			tempmap.put("fundljjz", fundlist.get(i).getFundljjz());
			tempmap.put("fundzrjz", fundlist.get(i).getFundzrjz());
			tempmap.put("fundzf", fundlist.get(i).getFundzf()+"%");
			tempmap.put("fundrq", fundlist.get(i).getFundrq());
			items.add(tempmap);
		}
		return items;
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
	    	Responsefund(url+strfundlist,getattr);
	        return true;
	    case R.id.item_addstock:
	    	Addfund();
	        return true;
	    case R.id.item_exit:
	    	this.finish();
	    	return true;
	    case R.id.item_setup:
	    	Intent intentsetup=new Intent();
			intentsetup.setClass(fund.this, stocksetup.class);
			startActivity(intentsetup); 
			return true;
	    case R.id.item_about:
	    	 Builder builder=new AlertDialog.Builder(fund.this);  
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
	
	@Override
	public boolean onItemLongClick(AdapterView parent, final View view, final int position,long id) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("基金自选操作").setItems(longclickmenu,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						TextView txtjjid=(TextView)view.findViewById(R.id.fundid);
						String tempstr=txtjjid.getText().toString();
				    	tempstr="of"+tempstr;
						if(longclickmenu[which].equals("删除自选基金")){
							myAdapter.items.remove(position);
							fundlistview.setAdapter(myAdapter);
							int find=strfundlist.indexOf(tempstr);
							if(tempstr.length()==strfundlist.length()){
								strfundlist="";
							}else{
								if((find!=0)&&(find+tempstr.length()!=strfundlist.length()))
									strfundlist=strfundlist.substring(0,find-1)+ strfundlist.substring(find+tempstr.length());
								else{
									
									if(find==0)
										strfundlist=strfundlist.substring(tempstr.length()+1);
									else
										strfundlist=strfundlist.substring(0,find-1);
								}
							}
							SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
			    	    	Editor editor=sp.edit();
			    	    	editor.putString("strfund",strfundlist);
			    	    	editor.commit();
			    	    	Toast.makeText(fund.this, "删除成功", Toast.LENGTH_SHORT)
							.show();
						}


					}
				}).show();
		return false;
	}
	
	private void Addfund() {
		Intent intent =new Intent();
		intent.setClass(fund.this, search_fund.class);
		startActivityForResult(intent,SEARCH_REQUEST);		
	}
	private void Responsefund(String tempurl,int getattr2) {
		// TODO Auto-generated method stub
		bar1.setVisibility(View.VISIBLE);
		thread=new GetStock(handlerfund);
		thread.doStart(tempurl,getattr2,this);
		
	}
	
	Handler handlerfund=new Handler(){
		public void handleMessage(Message m){
			bar1.setVisibility(View.GONE);
			switch(m.what){
			case 1:
				String mylist[]=(String[])m.getData().getStringArray("stock");
				fundlist=new ArrayList<funddata>();
				for(int i=0;i<mylist.length;i++){
					String tempstr=mylist[i];
					String a[]=tempstr.split(",");
					funddata tempdata=new funddata();
					tempdata.setFundid(a[0].substring(2));
					tempdata.setFundname(a[1]);
					tempdata.setFundzxjz(a[2]);
					tempdata.setFundljjz(a[3]);
					tempdata.setFundzrjz(a[4]);
					tempdata.setFundzf(a[5]);
					tempdata.setFundrq(a[6]);
					fundlist.add(tempdata);
				}
				List<Map<String, String>> items=fillmap();
				myAdapter= new FundAdapter(fund.this,items,R.layout.fund
											 ,new String[]{"fundid","fundname","fundzxjz","fundljjz","fundzrjz","fundzf","fundrq"}
											 ,new int[]{R.id.fundid,R.id.fundname,R.id.fundzxjz,R.id.fundljjz,R.id.fundzrjz,R.id.fundzf,R.id.fundrq});
				
				fundlistview.setAdapter(myAdapter);
				break;
			case 2:
				String mylist2[]=(String[])m.getData().getStringArray("stock");
				if(fundlist==null)
					fundlist=new ArrayList<funddata>();
				String a[]=null;
				for(int i=0;i<mylist2.length;i++){
					String tempstr=mylist2[i];
					a=tempstr.split(",");
					funddata tempdata=new funddata();
					tempdata.setFundid(a[0]);
					tempdata.setFundname(a[1]);
					tempdata.setFundzxjz(a[2]);
					tempdata.setFundljjz(a[3]);
					tempdata.setFundzrjz(a[4]);
					tempdata.setFundzf(a[5]);
					tempdata.setFundrq(a[6]);
					fundlist.add(tempdata);
				}
				if(strfundlist.equals(""))
					strfundlist="of"+a[0];
				else
					strfundlist=strfundlist+",of"+a[0];		
				SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
	    	    Editor editor=sp.edit();
		    	editor.putString("strfund", strfundlist);
		    	editor.commit();
		    	List<Map<String, String>> items2=fillmap();
		    	myAdapter= new FundAdapter(fund.this,items2,R.layout.fund
						,new String[]{"fundid","fundname","fundzxjz","fundljjz","fundzrjz","fundzf","fundrq"}
						,new int[]{R.id.fundid,R.id.fundname,R.id.fundzxjz,R.id.fundljjz,R.id.fundzrjz,R.id.fundzf,R.id.fundrq});
				
		    	fundlistview.setAdapter(myAdapter);
		    	Toast.makeText(fund.this, "添加成功", Toast.LENGTH_SHORT)
				.show();
		    	    

					break;
			case 3:
				new AlertDialog.Builder(fund.this)
                .setTitle("输入出错") 
                .setMessage("输入出错,请重输!") 
                .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
                  public void onClick(DialogInterface dlg, int sumthin) { 
                  } 
                }) 
                .show();
				break;
			case 4:
				new AlertDialog.Builder(fund.this)
                .setTitle("通迅出错,错误如下:") 
                .setMessage(m.getData().getString("error")) 
                .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
                  public void onClick(DialogInterface dlg, int sumthin) { 
                  } 
                }) 
                .show();
				break;
			
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(fund.this, singlefund.class);
	  	intent.putExtra("KEY_FUND", fundlist.get(arg2));
	  	startActivity(intent);
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  

		if(requestCode==SEARCH_REQUEST){
			if(resultCode==RESULT_OK){
				String tempstr=(String)data.getCharSequenceExtra("search_fund");
				//strstocklist=strstocklist+","+tempstr;
				//Responsestock(url+tempstr,2);
				if(tempstr.length()==6){
					tempstr="of"+tempstr;
					Responsefund(url+tempstr,2);
				}
				else{
					new AlertDialog.Builder(fund.this)
									 .setTitle("添加不成功") 
									 .setMessage("输入代码出错,请重输!") 
									 .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
										 public void onClick(DialogInterface dlg, int sumthin) { 
										 } 
					 }) 
					.show();		    		 
				}
				
			}
		}
	}
}
