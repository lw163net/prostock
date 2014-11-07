package com.liwei.prostock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.liwei.prostock.ShakeDetector.OnShakeListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class stock extends Activity implements OnItemClickListener,
	OnItemLongClickListener{
	GetStock thread=null;
//	String Stockid="sh000001,sz399001,sh000300";
	String Stockid="";
	String url="http://hq.sinajs.cn/list=";
	String strstocklist;//保存在数据库中的自选股票串,以","号分格
	ArrayList<stockdata> stocklist=null;//获取数后存adb入stockdata对象数组
	ListView stocklistview=null;//主list
	StockSimpleAdapter myAdapter=null;//主adapter
	stockdata stock1;
	private EditText etstock=null;
	private int getattr=1;
	private String[] longclickmenu=new String[]{"删除股票","分时图(大图)","K线图(大图)"};//长按listview选项后弹出的菜单
	static final int SUB_REQUEST=0;    
	static final int SEARCH_REQUEST=1;
	ProgressBar bar1=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//启动摇晃事件
		ShakeDetector sd=new ShakeDetector(this);
        sd.registerOnShakeListener(new OnShakeListener(){

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				Intent serviceIntent = new Intent();
	        	serviceIntent.setAction("com.liwei.prostock.speakService");
	        	startService(serviceIntent);
			}
			});
        //sd.start();
		
		
		stocklistview=(ListView)findViewById(R.id.ListView01);
		stocklistview.setOnItemClickListener((OnItemClickListener) this);
		stocklistview.setOnItemLongClickListener(this);
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		String Stockid=sp.getString("strstock", "");
		bar1=(ProgressBar)findViewById(R.id.barstock);
		strstocklist=Stockid;
		if(!Stockid.equals("")){
			Responsestock(url+strstocklist,getattr);
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("没有自选股票,是否添加?")
			.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Addstock();
				}
			}).setNegativeButton("不要", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		
	}
	//创建主菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//super.onCreateOptionsMenu(menu);
		//getMenuInflater().inflate(R.menu.stockmenu, menu);
		//return(super.onCreateOptionsMenu(menu));
		return true;
	}
	//主菜单点击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.item_ref:
	    	Responsestock(url+strstocklist,getattr);
	        return true;
	    case R.id.item_addstock:
	    	Addstock();
	        return true;
	    case R.id.item_exit:
	    	this.finish();
	    	return true;
	    case R.id.item_setup:
	    	Intent intentsetup=new Intent();
			intentsetup.setClass(stock.this, stocksetup.class);
			startActivity(intentsetup); 
			return true;
	    case R.id.item_about:
	    	 Builder builder=new AlertDialog.Builder(stock.this);  
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
	    return super.onOptionsItemSelected(item); 
	}
	//添加新股票
	public void Addstock(){
//		etstock = new EditText(stock.this);
//		final Builder  builder = new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_addstock))
//			.setMessage(getString(R.string.dialog_addmessage))
//			.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
//			     @Override
//			     public void onClick(DialogInterface arg0, int arg1) {
//			    	 String tempstr=etstock.getText().toString();
//			    	 String tt=tempstr.substring(0,1);
//			    	 if((tempstr.substring(0,1).equals("0"))||(tempstr.substring(0,1).equals("3")))
//			    		 tempstr="sz"+tempstr;
//			    	 else
//			    		 tempstr="sh"+tempstr;
//			    	 Stockid=tempstr;
//			    	 Responsestock(url+tempstr,2);
//			    	 
//			     }
//			    })
//			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			     @Override
//			     public void onClick(DialogInterface arg0, int arg1) {
//			    	 
//			     }
//			    });
//		builder.setView(etstock);
//		builder.show();
		Intent intent =new Intent();
		intent.setClass(stock.this, search.class);
		startActivityForResult(intent,SEARCH_REQUEST);

	}
	//远程取得股票数据
	public  void  Responsestock(String tempurl,int attr){
		bar1.setVisibility(View.VISIBLE);
        thread=new GetStock(handlerstock);
        
        thread.doStart(tempurl,attr,this);   
	}
	//构建Adapter参数中的item
	public List<Map<String, String>> fillmap(){
		List<Map<String, String>> items=new ArrayList<Map<String,String>>();
		for(int i=0;i<stocklist.size();i++){
			Map<String, String> tempmap=new HashMap<String, String>();
			tempmap.put("gpid", stocklist.get(i).getId());
			tempmap.put("gpmc", stocklist.get(i).getName().trim());
			tempmap.put("gpzxj",stocklist.get(i).getZxj());
			tempmap.put("gpzde", stocklist.get(i).getZde());
			tempmap.put("gpzdf", stocklist.get(i).getZdf());
			items.add(tempmap);
		}
		return items;
	}
	//按收消息(互联网访问后的)
	Handler handlerstock=new Handler(){
		public void handleMessage(Message m){
			bar1.setVisibility(View.GONE);
			switch(m.what){
			case 1://列表取得数据成功的消息
				String mylist[]=(String[])m.getData().getStringArray("stock");
				stocklist=new ArrayList<stockdata>();
				for(int i=0;i<mylist.length;i++){
					String tempstr=mylist[i];
					String a[]=tempstr.split(",");
					stockdata tempstock=new stockdata();
					tempstock.setId(a[0].substring(2));     			 tempstock.setName(a[1]);
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
				List<Map<String, String>> items=fillmap();
				myAdapter= new StockSimpleAdapter(stock.this,items,R.layout.stock
											 ,new String[]{"gpid","gpmc","gpzxj","gpzde","gpzdf"}
											 ,new int[]{R.id.gpid,R.id.gpmc,R.id.zxj,R.id.zde,R.id.zdf});
				
				stocklistview.setAdapter(myAdapter);
				break;
			case 2://单个股票取得成功的消息
				String mylist2[]=(String[])m.getData().getStringArray("stock");
				//stocklist=new ArrayList<stockdata>();
				stock1=new stockdata();
				for(int i=0;i<mylist2.length;i++){
					String tempstr=mylist2[i];
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
					stock1=tempstock;
				}
				Intent intent=new Intent();
			  	intent.setClass(stock.this, singlestock.class);
			  	intent.putExtra("KEY_STOCK", stock1);
			  	intent.putExtra("STRSTOCKLIST", strstocklist);
			  	//startActivity(intent); 
			  	startActivityForResult(intent,SUB_REQUEST);
				break;
			case 3://单个股票找不到的消息e
				new AlertDialog.Builder(stock.this)
                .setTitle("输入出错") 
                .setMessage("输入出错,请重输!") 
                .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
                  public void onClick(DialogInterface dlg, int sumthin) { 
                  } 
                }) 
                .show();
				break;
			case 4://网络连接不了的消息
				new AlertDialog.Builder(stock.this)
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
	};//end handler
	//点击主listview的事件
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		TextView txtgpid=(TextView)arg1.findViewById(R.id.gpid);
		String strgpid=txtgpid.getText().toString();
		getattr=2;
		if((strgpid.substring(0,1).equals("0"))||(strgpid.substring(0,1).equals("3")))
			strgpid="sz"+strgpid;
		else
			strgpid="sh"+strgpid;
		Responsestock(url+strgpid,getattr);
		Log.d("stock", arg1.toString());
	}
	//长按主listview的事件
	@Override
	public boolean onItemLongClick(AdapterView parent, final View view, final int position,
			long id) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle("操作股票").setItems(longclickmenu,
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						TextView txtgpid=(TextView)view.findViewById(R.id.gpid);
						String tempstr=txtgpid.getText().toString();
						if((tempstr.substring(0,1).equals("0"))||(tempstr.substring(0,1).equals("3")))
				    		 tempstr="sz"+tempstr;
				    	else
				    		 tempstr="sh"+tempstr;
						if(longclickmenu[which].equals("删除股票")){
							myAdapter.items.remove(position);
							stocklistview.setAdapter(myAdapter);
							int find=strstocklist.indexOf(tempstr);
							if(tempstr.length()==strstocklist.length()){
								strstocklist="";
							}else{
								if((find!=0)&&(find+tempstr.length()!=strstocklist.length()))
									strstocklist=strstocklist.substring(0,find-1)+ strstocklist.substring(find+tempstr.length());
								else{
									
									if(find==0)
										strstocklist=strstocklist.substring(tempstr.length()+1);
									else
										strstocklist=strstocklist.substring(0,find-1);
								}
							}
							SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
			    	    	Editor editor=sp.edit();
			    	    	editor.putString("strstock",strstocklist);
			    	    	editor.commit();
			    	    	Toast.makeText(stock.this, "删除成功", Toast.LENGTH_SHORT)
							.show();
						}

						if(longclickmenu[which].equals("分时图(大图)")){
							
							Intent intentfs=new Intent();
							intentfs.setClass(stock.this, getpicture.class);
							intentfs.putExtra("KEY_STOCK", tempstr);
							intentfs.putExtra("STOCK_IMAGE", "r");
							startActivity(intentfs); 
						}
						if(longclickmenu[which].equals("K线图(大图)")){
							
							Intent intentkx=new Intent();
							intentkx.setClass(stock.this, getpicture.class);
							intentkx.putExtra("KEY_STOCK", tempstr);
							intentkx.putExtra("STOCK_IMAGE", "k");
							startActivity(intentkx); 
						}
					}
				}).show();
		return false;
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if(requestCode==SUB_REQUEST){
			if(resultCode==RESULT_OK){
				strstocklist=(String)data.getCharSequenceExtra("strstocklist");
				getattr=1;
				Responsestock(url+strstocklist,getattr);
			}
		}
		if(requestCode==SEARCH_REQUEST){
			if(resultCode==RESULT_OK){
				String tempstr=(String)data.getCharSequenceExtra("search_stock");
				if((tempstr.substring(0,1).equals("0"))||(tempstr.substring(0,1).equals("3")))
		    		 tempstr="sz"+tempstr;
		    	else
		    		 tempstr="sh"+tempstr;
				
				//strstocklist=strstocklist+","+tempstr;
				Responsestock(url+tempstr,2);
				
			}
		}
	}

	
}
