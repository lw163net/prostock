package com.liwei.prostock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class searchqh extends Activity {
	Spinner spinnerjys=null;
	Spinner spinnerpz=null;
	Spinner spinnerhy=null;
	Button btnselect=null;
	Button btnexit=null;
	ListView lvqhselect=null;
	ArrayList<Jys> arrayjys=new ArrayList<Jys>();
	ArrayList<Pz> arraypz=new ArrayList<Pz>();
	ArrayList<Jtqh> arrayjtqh=new ArrayList<Jtqh>();
	ArrayList<Pz> tempobjpz=new ArrayList<Pz>();
	ArrayList<Jtqh> tempobjhy=new ArrayList<Jtqh>();
	ProgressDialog progressDialog;
	String selectqh=null;
	final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchgrqh);
		progressDialog=new ProgressDialog(this);
		setTitle("国内期货字典");
		//final List<String> data = new ArrayList<String>();
		
		lvqhselect=(ListView)findViewById(R.id.lvqhselect);
		btnselect=(Button)findViewById(R.id.Btnsaveqh);
		btnexit=(Button)findViewById(R.id.Btngrqhexit);
		spinnerjys=(Spinner)findViewById(R.id.Spinnerjys);
        spinnerpz=(Spinner)findViewById(R.id.Spinnerpz);
        spinnerhy=(Spinner)findViewById(R.id.Spinnerhy);
        
		progressDialog.setTitle("加载字典");
		progressDialog.setMessage("正在加载国内期货字典....");
		progressDialog.show();
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 0:
						progressDialog.dismiss();
						break;
				default:
					super.handleMessage(msg);
				}
			}
		};
		
		new Thread(){
			public void run(){
				Message msg = new Message();
				msg.what = 0;
				downloadFile("http://finance.sina.com.cn/iframe/futures_info_cff.js","/sdcard/1.js");
				handler.sendMessage(msg);
			}
		}.start();
		

		btnselect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(selectqh.length()>0)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("check", true);
					map.put("title", selectqh);
					list.add(map);
					//lvqhselect.setAdapter(new ArrayAdapter<String>(searchqh.this, R.layout.grqhitem,data));
					  SimpleAdapter adapter = new SimpleAdapter(searchqh.this,list,R.layout.grqhitem,
							  	new String[]{"check","title"},
			  	                new int[]{R.id.checkqh,R.id.txtgrqhitem});
					  lvqhselect.setAdapter(adapter);
				}
			}});
		btnexit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
		    	Editor editor=sp.edit();
		    	String savestr=getgrqhstr();
		    	editor.putString("strgrqh",savestr);
		    	editor.commit();
		    	Bundle bundle = new Bundle();    
		    	bundle.putString("strgrqhlist", savestr); 
		    	Intent mIntent = new Intent();     
		    	mIntent.putExtras(bundle);    
		    	setResult(RESULT_OK, mIntent); 
		    	Toast.makeText(searchqh.this, "保存成功", Toast.LENGTH_SHORT)
				.show();
		    	finish();
			}});
		
		 spinnerjys.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
				//	Log.i("grqh", String.valueOf(arg2));
					ArrayList<String> temppzlist=new ArrayList<String>();
					
					temppzlist.clear();
					tempobjpz.clear();
					for(int k=0;k<arraypz.size();k++){
						//Log.i("grqh",String.valueOf(arraypz.get(k).getJysid()));
						if(arraypz.get(k).getJysid()==(arg2+1)){		
							//Log.i("grqh", arraypz.get(k).getPzname());
							temppzlist.add( arraypz.get(k).getPzname());
							tempobjpz.add(arraypz.get(k));				
						}
					}
					String[] temppz1=temppzlist.toArray(new String[temppzlist.size()]);
					
					ArrayAdapter<String> adapterpz=new ArrayAdapter<String>(searchqh.this, android.R.layout.simple_spinner_item,temppz1);
					adapterpz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerpz.setAdapter(adapterpz);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}});
         //触发品种选中事件
         spinnerpz.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					int selectpz=tempobjpz.get(arg2).getPzid();
					ArrayList<String> temphylist=new ArrayList<String>();
					temphylist.clear();
					tempobjhy.clear();
					for(int k=0;k<arrayjtqh.size();k++){
						//Log.i("grqh",String.valueOf(arraypz.get(k).getJysid()));
						if(arrayjtqh.get(k).getPzid()==selectpz){		
							//Log.i("grqh", arraypz.get(k).getPzname());
							temphylist.add( arrayjtqh.get(k).getJtqhdm()+","+arrayjtqh.get(k).getJtqhname());
							tempobjhy.add(arrayjtqh.get(k));				
						}
					}
					String[] temphy=temphylist.toArray(new String[temphylist.size()]);
					
					ArrayAdapter<String> adapterhy=new ArrayAdapter<String>(searchqh.this, android.R.layout.simple_spinner_item,temphy);
					adapterhy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerhy.setAdapter(adapterhy);
					
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}} );
         spinnerhy.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectqh=tempobjhy.get(arg2).getJtqhdm()+","+tempobjhy.get(arg2).getJtqhname();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});

		
		
	}
	public void downloadFile(String url, String filePath) {

        URL url2 = null;
        
		int intjys=0;
		int intpz=0;
		int intjtqh=0;
        try {
                url2 = new URL(url);
                StringBuffer sb = new StringBuffer(); 
                HttpURLConnection con = (HttpURLConnection) url2.openConnection();

                InputStream in = con.getInputStream();
                BufferedReader br = new BufferedReader(   
                        new InputStreamReader(in,"GB2312"));   
                String data = "";  
                while ((data = br.readLine()) != null) {   
                    sb.append(data);   
                    //Log.i("grqh", data);
                }   
                in.close();
                String tempresult = sb.toString();
                String result[]=tempresult.split(";");
                for(int i=0;i<result.length;i++){
                	String templine=result[i];
                	String a[]=templine.split("=");
                	a[0]=a[0].trim();
                	a[1]=a[1].trim();
                	if(a[0].substring(0, 3).equals("JYS")){
                		if(!(a[0].equals("JYS[0]"))){
                			Log.i("grqh",a[0]);
                			intjys++;
                			Jys tempjys=new Jys();
                			tempjys.setJysid(intjys);
                			tempjys.setJysname(a[1].replace("'", ""));
                			arrayjys.add(tempjys);
                		}
                	}
                	if(a[0].substring(0, 2).equals("PZ")){
                			if(a[0].length()==8){
                				if(!(a[0].equals("PZ[0][0]"))){
                					intpz++;
                					Pz temppz=new Pz();
                					temppz.setJysid(intjys);
                					temppz.setPzid(intpz);
                					temppz.setPzname(a[1].replace("\"", ""));
                					arraypz.add(temppz);
                				}
                			}
                			
                	}
                	if(a[0].substring(0, 2).equals("YF")){
                		if(a[0].length()==11){
                			if(!(a[0].equals("YF[0][0][0]"))){
                				intjtqh++;
                				Jtqh tempjtqh=new Jtqh();
                				tempjtqh.setJtqhid(intjtqh);
                				tempjtqh.setPzid(intpz);
                				String tempstr=a[1].substring(a[1].indexOf("(")+1,a[1].indexOf(")"));
                				tempstr=tempstr.replace("\"", "");
                				String b[]=tempstr.split(",");
                				tempjtqh.setJtqhdm(b[1]);
                				tempjtqh.setJtqhname(b[0]);
                				arrayjtqh.add(tempjtqh);
                				
                				
                			}
                		}
                	}
                	
                	
                		
           
                }
                String tempjyslist[] = new String[arrayjys.size()];
                for(int j=0;j<arrayjys.size();j++){
                	tempjyslist[j]=arrayjys.get(j).getJysname();
                }
                
                ArrayAdapter<String> adapterjys=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tempjyslist);
       		  adapterjys.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerjys.setAdapter(adapterjys);
                //触发交易所选中事件
               
                //File fileOut = new File(filePath);

                //FileOutputStream out = new FileOutputStream(fileOut);

              //  byte[] bytes = new byte[1024];
               // String tempstr=new String(bytes,"UTF-8");
                
           //     int c;

            //    while ((c = in.read(bytes)) != -1) {
            //            out.write(bytes, 0, c);

             //   }

               

                //out.close();

        } catch (Exception e) {

                e.printStackTrace();

        }
	}
	public void loaddic(String path){
		ArrayList<Jys> arrayjys=new ArrayList<Jys>();
		ArrayList<Pz> arraypz=new ArrayList<Pz>();
		ArrayList<Jtqh> arrayjtqh=new ArrayList<Jtqh>();
		int intjys=0;
		int intpz=0;
		int jtqh=0;
		File filename=null;
		FileReader fileread;
		String line;
        filename = new File(path) ;
        try
        {
              fileread = new FileReader(filename);
              BufferedReader bfr = new BufferedReader(fileread);
         try
          {
        	 while(true){
                  line = bfr.readLine() ;
                  Log.i("grqh", line);
                  String str1=new String(line.getBytes(),"UTF-8");
                 // String str1=EncodingUtils.getString(line.getBytes("ISO-8859-1"), "UTF-8");
                  Log.i("grqh",str1);
                  
        	 }
          } 
         catch (IOException e) 
          {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
          }
       }
      catch (FileNotFoundException e) 
       {
                // TODO Auto-generated catch block
                e.printStackTrace();
       }

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.grqhmenu, menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.item_grqh_exit:
	    	SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
	    	Editor editor=sp.edit();
	    	String savestr=getgrqhstr();
	    	editor.putString("strgrqh",savestr);
	    	editor.commit();
	    	Bundle bundle = new Bundle();    
	    	bundle.putString("strgrqhlist", savestr); 
	    	Intent mIntent = new Intent();     
	    	mIntent.putExtras(bundle);    
	    	setResult(RESULT_OK, mIntent); 
	    	Toast.makeText(searchqh.this, "保存成功", Toast.LENGTH_SHORT)
			.show();
	    	finish();
	        return true;


	    }
	    return false; //should never happen
	}
	public String getgrqhstr(){
		String result=null;
		for(int i=0;i<list.size();i++)
		{
			
			Map<String, Object> map =list.get(i);
			
			String tempstr=map.get("title").toString();
			String a[]=tempstr.split(",");
			if(result==null)
				result=a[0];
			else
			{
				result+=",";
				result+=a[0];
			}
			
		}
		return result;
	}

}
