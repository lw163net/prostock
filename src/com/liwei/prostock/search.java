package com.liwei.prostock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class search extends Activity{
	private static final String TAG="search"; 
	String GetUrl="http://suggest3.sinajs.cn/suggest/type=11&key=";
	EditText edit; 
	GetList thread=null;
	String[] a=null;
	ListView listviewsearch=null;
	ProgressBar bar1=null;
	Button btnsearch=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setTheme(R.style.Translucent);   
		 setContentView(R.layout.search); 
		 bar1=(ProgressBar)findViewById(R.id.barsearchstock);
		 edit=(EditText)findViewById(R.id.txtsearch);
		 listviewsearch=(ListView)findViewById(R.id.ListViewSearch);
		 btnsearch=(Button)findViewById(R.id.btnser);
		 listviewsearch.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Log.d(TAG, String.valueOf(arg2));
//				Intent intent=new Intent();
//			  	intent.setClass(search.this, singlestock.class);
//			  	intent.putExtra("KEY_STOCK", stock1);
//			  	intent.putExtra("STRSTOCKLIST", strstocklist);
//			  	//startActivity(intent); 
//			  	startActivityForResult(intent,SUB_REQUEST);
				String	b[]=arg0.getItemAtPosition(arg2).toString().split(",");
				String tempstr=b[1];
				Bundle bundle = new Bundle();    
    	    	bundle.putString("search_stock",tempstr); 
    	    	Intent mIntent = new Intent();     
    	    	mIntent.putExtras(bundle);    
    	    	setResult(RESULT_OK, mIntent); 
    	    	finish();
				
			}});
		 btnsearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bar1.setVisibility(View.VISIBLE);
				String tempstr=edit.getText().toString().trim();
				if(!tempstr.equals("")){
			        thread=new GetList(handlerSearch);     
			        thread.doStart(tempstr);   
				}
			}
		});
	}

	Handler handlerSearch=new Handler(){
		public void handleMessage(Message m){
			bar1.setVisibility(View.GONE);
			switch(m.what){
			case 1://列表取得数据成功的消息
				a=(String[])m.getData().getStringArray("search");	
				
				List<String> items = new ArrayList<String>(); 
				
				if(a.length==0){
					items.add("没有找到股票!");
				}else{
					for(int i=0;i<a.length;i++){
	
						String tempstr=a[i];
						String b[]=tempstr.split(",");
						tempstr=b[0]+","+b[2]+","+b[4];
						items.add(tempstr);
					}
				}
				ArrayAdapter<String> tempArray=new ArrayAdapter<String>(search.this, android.R.layout.simple_list_item_1, items);
				listviewsearch.setAdapter(tempArray);


				
				
				
				break;
				
			}
		}
	};

}
class GetList extends Thread
{
	String[] tempdata=null;
	String inputchar =null;
	private Handler handle=null;
	public GetList(Handler hander){
		handle=hander;
	}

	 public void doStart(String inputchar) {
         // TODO Auto-generated method stub
		 this.inputchar=inputchar;
	    this.start();
	    
	 }//end dostart
	@Override
	public void run() {
		// TODO Auto-generated method stub
			tempdata=getstocklist(inputchar);
			if(tempdata!=null){
				Message message=handle.obtainMessage();
	            Bundle bd=new Bundle();
	            message.what=1;
	            bd.putStringArray("search", tempdata);
	            message.setData(bd);
	            handle.sendMessage(message);
			}
	}
	public String[] getstocklist(String s){
		try{
			String GetUrl="http://suggest3.sinajs.cn/suggest/type=11&key=";
			String[] tempstr = null;
			HttpGet hg=new HttpGet(GetUrl+s);
	        HttpResponse httpResponse = new DefaultHttpClient().execute(hg);
	        if(httpResponse.getStatusLine().getStatusCode() == 200)  
	        { 
	          String strResult = EntityUtils.toString(httpResponse.getEntity(),"GBK"); 
	          
	   //       strvalidate=new String(strvalidate.toString().getBytes(),"UTF-8");
	          int tempindex=strResult.indexOf("=")+1;
	          strResult=strResult.substring(tempindex);
	          strResult=strResult.replaceAll("\"", "");     
	          strResult=strResult.replaceAll("\n", "");  
	          tempstr=strResult.split(";");
	          return tempstr;
	        }
	        else
	        	return null;
		}catch(Exception ex)
		{
			return null;
		}
	}
}

