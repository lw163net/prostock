package com.liwei.prostock;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class search_fund extends Activity {
	private static final String TAG="search_fund"; 
	String[] arrayfundtype;
	String[] tempa=null;
	String[] tempb=null;
	String selectfundtype=null;
	ArrayAdapter<String> adapterfundtype=null;
	GetFundList thread=null;
	Spinner spinnertype=null;
	String[] a=null;
	ListView listviewfund=null; 
	Button btnsearch=null;
	EditText edit=null; 
	ProgressBar bar1=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setTheme(R.style.Translucent);  
		 setContentView(R.layout.search_fund);
		 //取得操作组件
		 spinnertype=(Spinner)findViewById(R.id.spinner_jjrx);
		 btnsearch=(Button)findViewById(R.id.button_jjsearch);
		 listviewfund=(ListView)findViewById(R.id.ListViewSearchFund);
		 edit=(EditText)findViewById(R.id.editText_fund);
		 bar1=(ProgressBar)findViewById(R.id.barsearch);
		 
		 //取得基金类型字典
		 arrayfundtype=this.getResources().getStringArray(R.array.stringfundtype);
		 int fundtypelength=arrayfundtype.length;
		 tempa=new String[fundtypelength];
		 tempb=new String[fundtypelength];
		 for(int i=0;i<fundtypelength;i++){
			 String tempfund=arrayfundtype[i];
			 String[] tempc=tempfund.split(",");
			 tempa[i]=tempc[0];
			 tempb[i]=tempc[1];
		 }
		 adapterfundtype=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempb);
		 adapterfundtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinnertype.setAdapter(adapterfundtype);
		 //选取基金类型事件
		 spinnertype.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectfundtype=tempa[arg2];												
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		 btnsearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bar1.setVisibility(View.VISIBLE);
				String tempstr=edit.getText().toString().trim();
				String strsearch="type="+selectfundtype+"&key="+tempstr;
				if(!tempstr.equals("")){
			        thread=new GetFundList(handlerSearch);     
			        thread.doStart(strsearch);   
				}
			}});
		 listviewfund.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String	b[]=arg0.getItemAtPosition(arg2).toString().split(",");
				String tempstr=b[1];
				Bundle bundle = new Bundle();    
    	    	bundle.putString("search_fund",tempstr); 
    	    	Intent mIntent = new Intent();     
    	    	mIntent.putExtras(bundle);    
    	    	setResult(RESULT_OK, mIntent); 
    	    	finish();
				
			}});
		
		  
	}
	
	
	
	Handler handlerSearch=new Handler(){
		public void handleMessage(Message m){
			bar1.setVisibility(View.GONE);
			switch(m.what){
			case 1://列表取得数据成功的消息
				a=(String[])m.getData().getStringArray("search_fund");	
				
				List<String> items = new ArrayList<String>(); 
				
				if(a.length==0){
					items.add("没有找到基金!");
				}else{
					for(int i=0;i<a.length;i++){
	
						String tempstr=a[i];
						String b[]=tempstr.split(",");
						tempstr=b[0]+","+b[2]+","+b[4];
						items.add(tempstr);
					}
				}
				ArrayAdapter<String> tempArray=new ArrayAdapter<String>(search_fund.this, android.R.layout.simple_list_item_1, items);
				listviewfund.setAdapter(tempArray);				
				break;
				
			}
		}
	};
}


class GetFundList extends Thread
{
	String[] tempdata=null;
	String inputchar =null;
	private Handler handle=null;
	public GetFundList(Handler hander){
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
	            bd.putStringArray("search_fund", tempdata);
	            message.setData(bd);
	            handle.sendMessage(message);
			}
	}
	public String[] getstocklist(String s){
		try{
			String GetUrl="http://suggest3.sinajs.cn/suggest/";
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


