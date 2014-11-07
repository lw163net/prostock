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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class qh extends Activity {
	static String TAG="qh";
	GetStock thread=null;
	ListView gjqhlistview=null;
	ListView grqhlistview=null;
	ArrayList<qhdata> gjqhlist=null;
	ArrayList<qhdata> grqhlist=null;
	String url="http://hq.sinajs.cn/list=";
	String strgjqhlist="hf_GC,hf_CL";
	String strgrqhlist=null;
	QhAdapter myAdapter=null;
	String[] Allindexlist=null;
	String[] tempa=null;
	int getattr=1;
	ProgressBar bar1=null;
	ListView lv=null;
	static final int SUB_REQUEST=0;   
	Button btn_grqh=null;
	Button btn_gjqh=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qh);
		//取得自选国内国际期货列表串
		SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		String qhid=sp.getString("strqh", "");
		strgrqhlist=sp.getString("strgrqh", "");
		strgjqhlist=qhid;
		
		Allindexlist=this.getResources().getStringArray(R.array.stringgjqh);
		gjqhlistview=(ListView)findViewById(R.id.ListViewgjqh);
		Log.d(TAG, "create");
		bar1=(ProgressBar)findViewById(R.id.qhbar);
		btn_grqh=(Button)findViewById(R.id.btn_qh_grqh);
		btn_gjqh=(Button)findViewById(R.id.btn_qh_gjqh);
		//响应click事件
		btn_grqh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!strgrqhlist.equals("")){
					ResponseGrqh(url+strgrqhlist,getattr);
				}
			}});
		btn_gjqh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!strgjqhlist.equals("")){
					ResponseGjqh(url+strgjqhlist,getattr);
				}
			}});
		
		if(!strgjqhlist.equals("")){
			ResponseGjqh(url+strgjqhlist,getattr);
		}
		else
		{
			new AlertDialog.Builder(this).setTitle("没有自选国际期货,是否添加?")
			.setPositiveButton("是的", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Addgjqh();
				}
			}).setNegativeButton("不要", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.qhmenu, menu);
		//return(super.onCreateOptionsMenu(menu));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.item_ref:
	        return true;
	    case R.id.item_qh_addgjqh:
	    	Addgjqh();
	        return true;
	    case R.id.item_qh_addgrqh:
	    	Addgrqh();
	        return true;   
	    case R.id.item_exit:
	    	this.finish();
	    	return true;
	    case R.id.item_setup:
	    	Intent intentsetup=new Intent();
			intentsetup.setClass(qh.this, stocksetup.class);
			startActivity(intentsetup); 
			
			return true;
	    case R.id.item_about:
	    	 Builder builder=new AlertDialog.Builder(qh.this);  
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if(requestCode==SUB_REQUEST){
			if(resultCode==RESULT_OK){
				strgrqhlist=(String)data.getCharSequenceExtra("strgrqhlist");
				Log.d("grqh", strgrqhlist);
				//Toast.makeText(qh.this, strgrqhlist, Toast.LENGTH_SHORT)
				//.show();
				//getattr=1;
				//Responsestock(url+strstocklist,getattr);
				if(!strgrqhlist.equals("")){
					ResponseGrqh(url+strgrqhlist,getattr);
				}
			}
		}
	}
	private void Addgrqh(){
		Intent intent=new Intent();
		intent.setClass(qh.this, searchqh.class);
		//startActivity(intent);
		startActivityForResult(intent,SUB_REQUEST);
	}
	private void Addgjqh() {
		// TODO Auto-generated method stub
		try{
				int indexlength=Allindexlist.length;
				tempa=new String[indexlength];
				String tempb[]=new String[indexlength];
				boolean tempd[]=new boolean[indexlength];
				for(int i=0;i<Allindexlist.length;i++){
					String tempstr=Allindexlist[i];
					String[] tempc=tempstr.split(",");
					tempa[i]=tempc[0];
					tempb[i]=tempc[1];	
					if(strgjqhlist.indexOf(tempc[0])==-1)
						tempd[i]=false;
					else
						tempd[i]=true;
				}
				
				AlertDialog ad=new AlertDialog.Builder(qh.this).setTitle("选择国际期货")
								.setMultiChoiceItems(tempb, tempd,
								new DialogInterface.OnMultiChoiceClickListener() {
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										
									}
								}).setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												// TODO Auto-generated method stub
											
												strgjqhlist="";
													for(int j=0;j<tempa.length;j++){
														if(lv.getCheckedItemPositions().get(j)){
															strgjqhlist=strgjqhlist+tempa[j]+",";
														}
													}
													if(!strgjqhlist.equals(""))
														strgjqhlist=strgjqhlist.substring(0, strgjqhlist.length()-1);
													SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
									    	    	Editor editor=sp.edit();
									    	    	editor.putString("strqh",strgjqhlist);
									    	    	editor.commit();
									    	    	if(!strgjqhlist.equals(""))
									    	    		ResponseGjqh(url+strgjqhlist,getattr);
				
											}
										}).setNegativeButton("取消", null).create();
				lv=ad.getListView();
		
				ad.show();
		}
		catch(Exception ex){
			new AlertDialog.Builder(qh.this).setMessage(ex.getMessage()).show();
		}
	}
	public String findname(String qhdm){
		
		int indexlength=Allindexlist.length;
		String[] tempa=new String[indexlength];
		String resule=null;
		for(int i=0;i<Allindexlist.length;i++){
			String tempstr=Allindexlist[i];
			String[] tempc=tempstr.split(",");
			if(tempc[0].equals(qhdm)){
				resule=tempc[1];
				break;
			}	

		}
		return resule;
	}
	public List<Map<String, String>> fillmap(ArrayList<qhdata> qhlist){
		List<Map<String, String>> items=new ArrayList<Map<String,String>>();
		for(int i=0;i<qhlist.size();i++){
			Map<String, String> tempmap=new HashMap<String, String>();

			tempmap.put("qh_zmc", qhlist.get(i).getZmc());
			tempmap.put("qh_zxj",qhlist.get(i).getZxj());
			tempmap.put("qh_zde", qhlist.get(i).getZde());
			tempmap.put("qh_zf", "("+qhlist.get(i).getZf()+"%)");
			//tempmap.put("qh_zdf", gjqhlist.get(i).);
			tempmap.put("qh_kpj", qhlist.get(i).getKpj());
			tempmap.put("qh_zrj", qhlist.get(i).getZrj());
			tempmap.put("qh_sj", qhlist.get(i).getSj());
			items.add(tempmap);
		}
		return items;
	}
	private void ResponseGjqh(String tempurl,int getattr2) {
		// TODO Auto-generated method stub
		bar1.setVisibility(View.VISIBLE);
		thread=new GetStock(handlergjqh);
		thread.doStart(tempurl,getattr2,this);
		
	}
	private void ResponseGrqh(String tempurl,int getattr2){
		bar1.setVisibility(View.VISIBLE);
		thread=new GetStock(handlergrqh);
		thread.doStart(tempurl, getattr2, this);
	}
	Handler handlergjqh=new Handler(){
		public void handleMessage(Message m){
			switch(m.what){
			case 1:
				String mylist[]=(String[])m.getData().getStringArray("stock");
				gjqhlist=new ArrayList<qhdata>();
				for(int i=0;i<mylist.length;i++){
					String tempstr=mylist[i];
					String a[]=tempstr.split(",");
					qhdata tempdata=new qhdata();
					tempdata.setDm(a[0]);
					tempdata.setZmc(findname(a[0]));
					float f_zxj=Float.parseFloat(a[1]);
					f_zxj=(float)(Math.round(f_zxj*100))/100;
					tempdata.setZxj(Float.toString(f_zxj));
					
					tempdata.setMj(a[3]);
					tempdata.setMmj(a[4]);
					
					float f_zgj=Float.parseFloat(a[5]);
					f_zgj=(float)(Math.round(f_zgj*100))/100;
					tempdata.setZgj(Float.toString(f_zgj));
					
					float f_zdj=Float.parseFloat(a[6]);
					f_zdj=(float)(Math.round(f_zdj*100))/100;
					tempdata.setZdj(Float.toString(f_zdj));
					
					float f_zrj=Float.parseFloat(a[8]);
					f_zrj=(float)(Math.round(f_zrj*100))/100;
					tempdata.setZrj(Float.toString(f_zrj));

					float f_kpj=Float.parseFloat(a[9]);
					f_kpj=(float)(Math.round(f_kpj*100))/100;
					tempdata.setKpj(Float.toString(f_kpj));

					tempdata.setCcl(a[10]);
					tempdata.setMl(a[11]);
					tempdata.setMml(a[12]);
					tempdata.setSj(a[13]+" "+a[7]);
	
					float f_zdf=Float.parseFloat(a[2]);
					f_zdf=(float)(Math.round(f_zdf*100))/100;
					tempdata.setZf(Float.toString(f_zdf));
					
					float f_zde=f_zxj-f_zrj;
					f_zde=(float)(Math.round(f_zde*100))/100;
					
					if(f_zrj>f_zxj)
						tempdata.setZde(Float.toString(f_zde));
					else
						tempdata.setZde(Float.toString(f_zde));
					
					
					

					gjqhlist.add(tempdata);
				}
				List<Map<String, String>> items=fillmap(gjqhlist);
				myAdapter= new QhAdapter(qh.this,items,R.layout.qhlist
											 ,new String[]{"qh_zmc","qh_zxj","qh_zde","qh_zf","qh_kpj","qh_zrj","qh_sj"}
											 ,new int[]{R.id.qh_txt_mc,R.id.qh_txt_zxj,R.id.qh_txt_zde,R.id.qh_txt_zf,
												R.id.qh_txt_kpj,R.id.qh_txt_zrj,R.id.qh_txt_sj});
				
				gjqhlistview.setAdapter(myAdapter);
				bar1.setVisibility(View.GONE);
				break;
			case 2:
				
					break;
			case 3:
				new AlertDialog.Builder(qh.this)
                .setTitle("输入出错") 
                .setMessage("输入出错,请重输!") 
                .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
                  public void onClick(DialogInterface dlg, int sumthin) { 
                  } 
                }) 
                .show();
				break;
			case 4:
				new AlertDialog.Builder(qh.this)
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
	Handler handlergrqh=new Handler(){
		public void handleMessage(Message m){
			switch(m.what){
			case 1:
				String mylist[]=(String[])m.getData().getStringArray("stock");
				grqhlist=new ArrayList<qhdata>();
				for(int i=0;i<mylist.length;i++){
					String tempstr=mylist[i];
					String a[]=tempstr.split(",");
					qhdata tempdata=new qhdata();
					tempdata.setDm(a[0]);
					tempdata.setZmc(a[1]);
					float f_zxj=Float.parseFloat(a[9]);
					f_zxj=(float)(Math.round(f_zxj*100))/100;
					tempdata.setZxj(Float.toString(f_zxj));
					
					tempdata.setMj(a[7]);
					tempdata.setMmj(a[8]);
					
					float f_zgj=Float.parseFloat(a[4]);
					f_zgj=(float)(Math.round(f_zgj*100))/100;
					tempdata.setZgj(Float.toString(f_zgj));
					
					float f_zdj=Float.parseFloat(a[5]);
					f_zdj=(float)(Math.round(f_zdj*100))/100;
					tempdata.setZdj(Float.toString(f_zdj));
					
					float f_zrj=Float.parseFloat(a[11]);
					f_zrj=(float)(Math.round(f_zrj*100))/100;
					tempdata.setZrj(Float.toString(f_zrj));

					float f_kpj=Float.parseFloat(a[3]);
					f_kpj=(float)(Math.round(f_kpj*100))/100;
					tempdata.setKpj(Float.toString(f_kpj));

					tempdata.setCcl(a[14]);
					tempdata.setMl(a[12]);
					tempdata.setMml(a[13]);
					tempdata.setSj(a[18]);
					float f_zde=Float.parseFloat(a[9])-Float.parseFloat(a[11]);
					f_zde=(float)(Math.round(f_zde*100))/100;
					tempdata.setZde(Float.toString(f_zde));
					float f_zdf=(float) 0.00;
					f_zdf=f_zde/f_zrj*100;						
					f_zdf=(float)(Math.round(f_zdf*100))/100;
					
					tempdata.setZf(Float.toString(f_zdf));
				
					grqhlist.add(tempdata);
					List<Map<String, String>> items=fillmap(grqhlist);
					myAdapter= new QhAdapter(qh.this,items,R.layout.qhlist
												 ,new String[]{"qh_zmc","qh_zxj","qh_zde","qh_zf","qh_kpj","qh_zrj","qh_sj"}
												 ,new int[]{R.id.qh_txt_mc,R.id.qh_txt_zxj,R.id.qh_txt_zde,R.id.qh_txt_zf,
													R.id.qh_txt_kpj,R.id.qh_txt_zrj,R.id.qh_txt_sj});
					
					gjqhlistview.setAdapter(myAdapter);
					bar1.setVisibility(View.GONE);
				}
				break;
			case 3:
				new AlertDialog.Builder(qh.this)
                .setTitle("输入出错") 
                .setMessage("输入出错,请重输!") 
                .setNeutralButton("Close", new DialogInterface.OnClickListener() { 
                  public void onClick(DialogInterface dlg, int sumthin) { 
                  } 
                }) 
                .show();
				break;
			case 4:
				new AlertDialog.Builder(qh.this)
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
}
