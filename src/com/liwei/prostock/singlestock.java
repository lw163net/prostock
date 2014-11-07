package com.liwei.prostock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class singlestock extends Activity implements View.OnClickListener {
	stockdata tempstock;
	String StrStockList;
	String tempstr;
	boolean isin=false;
	int greencolor=Color.GREEN;
	getpicturethread thread;
	ProgressBar bar1=null;
	ImageView iv=null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		setContentView(R.layout.singlestock);
		Bundle bundle=getIntent().getExtras();
		Button btn_fs=(Button)findViewById(R.id.btn_fs);
		Button btn_rk=(Button)findViewById(R.id.btn_k);
		Button btn_zk=(Button)findViewById(R.id.btn_zk);
		Button btn_yk=(Button)findViewById(R.id.btn_yk);
		btn_fs.setBackgroundResource(R.drawable.sfb_btn_off);
		btn_rk.setBackgroundResource(R.drawable.sfb_btn_off_p);
		btn_zk.setBackgroundResource(R.drawable.sfb_btn_off_p);
		btn_yk.setBackgroundResource(R.drawable.sfb_btn_off_p);
		bar1=(ProgressBar)findViewById(R.id.ProgressBar01);
		bar1.setVisibility(View.GONE);
		btn_fs.setOnClickListener(this);
		btn_rk.setOnClickListener(this);
		btn_zk.setOnClickListener(this);
		btn_yk.setOnClickListener(this);
		
		Button btnsave=(Button)findViewById(R.id.btnsave);
		btnsave.setVisibility(View.GONE);
		btnsave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        
				SharedPreferences sp=getSharedPreferences("prostock",Context.MODE_PRIVATE);
    	    	Editor editor=sp.edit();
    	    	String savestr="";
    	    	if(!isin){
	    	    	if(!StrStockList.equals("")){
	    	    		savestr= StrStockList+","+tempstr;
	    	    		
	    	    	}
	    	    	else{
	    	    		savestr=tempstr;
	    	    	}
	    	    	editor.putString("strstock",savestr);
	    	    	editor.commit();
	    	    	Bundle bundle = new Bundle();    
	    	    	bundle.putString("strstocklist", savestr); 
	    	    	Intent mIntent = new Intent();     
	    	    	mIntent.putExtras(bundle);    
	    	    	setResult(RESULT_OK, mIntent); 
	    	    	Toast.makeText(singlestock.this, "保存成功", Toast.LENGTH_SHORT)
					.show();
    	    	}else
    	    		Toast.makeText(singlestock.this, "股票已在自选中", Toast.LENGTH_SHORT)
					.show();
    	    	finish();
			}
		});
		tempstock=(stockdata)bundle.getSerializable("KEY_STOCK");
		StrStockList=(String)bundle.getString("STRSTOCKLIST");
		tempstr=tempstock.getId();
		if((tempstr.substring(0,1).equals("0"))||(tempstr.substring(0,1).equals("3")))
    		 tempstr="sz"+tempstr;
    	else
    		 tempstr="sh"+tempstr;
		if(StrStockList.equals("")){
			btnsave.setVisibility(View.VISIBLE);
		}else{
			boolean isin=false;
			String a[]=StrStockList.split(",");
			
			for(int i=0;i<a.length;i++){
				if(tempstr.equals(a[i])){
					isin=true;
					break;
				}
			}
			if(!isin)
				btnsave.setVisibility(View.VISIBLE);
		}
		SetupView();
		bar1.setVisibility(View.VISIBLE);
		
		String picurl="http://3g.sina.com.cn/3g/static/images/finance/stock/daily/huge/"+tempstr+".png";
		thread=new getpicturethread(handerpic);
        
        thread.doStart(picurl,this); 
		
	}
	//获取图像消息
	Handler handerpic=new Handler(){
		public void handleMessage(Message m){
			switch(m.what){
			case 1:
				byte[] tempbyte=m.getData().getByteArray("picture");	
				Bitmap bm=BitmapFactory.decodeByteArray(tempbyte, 0, tempbyte.length);
				iv=(ImageView)findViewById(R.id.imagesingstock);
				iv.setImageBitmap(Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight()));
				bar1.setVisibility(View.GONE);
				break;
			case 2:
				break;
			}
		}
	};
	private void SetupView(){
		TextView txtid=(TextView)findViewById(R.id.txtid);
		TextView txtgpmc=(TextView)findViewById(R.id.txtgpmc);
		TextView txtzxj=(TextView)findViewById(R.id.txtzxj);
		TextView txtzdf=(TextView)findViewById(R.id.txtzdf);
		TextView txtzde=(TextView)findViewById(R.id.txtzde);
		TextView txtjkp=(TextView)findViewById(R.id.txtjkp);
		TextView txtzsp=(TextView)findViewById(R.id.txtzsp);
		TextView txtzd=(TextView)findViewById(R.id.txtzd);
		TextView txtzg=(TextView)findViewById(R.id.txtzg);
		TextView txtcjl=(TextView)findViewById(R.id.txtcjl);
		TextView txtcje=(TextView)findViewById(R.id.txtcje);
		TextView txtjmj=(TextView)findViewById(R.id.txtjmj);
		TextView txtjmj2=(TextView)findViewById(R.id.txtjmj2);
		TextView txtm1=(TextView)findViewById(R.id.txtm1);
		TextView txtm2=(TextView)findViewById(R.id.txtm2);
		TextView txtm3=(TextView)findViewById(R.id.txtm3);
		TextView txtm4=(TextView)findViewById(R.id.txtm4);
		TextView txtm5=(TextView)findViewById(R.id.txtm5);
		TextView txtmm1=(TextView)findViewById(R.id.txtmm1);
		TextView txtmm2=(TextView)findViewById(R.id.txtmm2);
		TextView txtmm3=(TextView)findViewById(R.id.txtmm3);
		TextView txtmm4=(TextView)findViewById(R.id.txtmm4);
		TextView txtmm5=(TextView)findViewById(R.id.txtmm5);
		TextView txthqsj=(TextView)findViewById(R.id.hqsj);
		LinearLayout layout=(LinearLayout)findViewById(R.id.LinearLayout01);
		txtid.setText(tempstock.getId());
		txtid.setTextColor(Color.BLUE);
		txtgpmc.setText(tempstock.getName());
		txtgpmc.setTextColor(Color.BLUE);
		txtzxj.setText(tempstock.getZxj());
		txtzdf.setText("("+tempstock.getZdf()+"%)");
		txtzde.setText(tempstock.getZde());
		txtjkp.setText(tempstock.getJkp());
		txtzsp.setText(tempstock.getZsp());
		txtzd.setText(tempstock.getZd());
		txtzg.setText(tempstock.getZg());
		txtcjl.setText(tempstock.getCjl().trim());
		
		txtcje.setText(tempstock.getCje().trim());
		txtjmj.setText(tempstock.getJmj());
		txtjmj2.setText(tempstock.getJmj2());
		txthqsj.setText(tempstock.getHqsj());
		//txtm.setText(tempstock.getM1()+"|"+tempstock.getM2()+"|"+tempstock.getM3()+"|"+tempstock.getM4()+"|"+tempstock.getM5());
		//txtmm.setText(tempstock.getMm1()+"|"+tempstock.getMm2()+"|"+tempstock.getMm3()+"|"+tempstock.getMm4()+"|"+tempstock.getMm5());
		txtm1.setText(tempstock.getM1());
		txtm2.setText(tempstock.getM2());
		txtm3.setText(tempstock.getM3());
		txtm4.setText(tempstock.getM4());
		txtm5.setText(tempstock.getM5());
		txtmm1.setText(tempstock.getMm1());
		txtmm2.setText(tempstock.getMm2());
		txtmm3.setText(tempstock.getMm3());
		txtmm4.setText(tempstock.getMm4());
		txtmm5.setText(tempstock.getMm5());
		
		float tempzde=Float.parseFloat(tempstock.getZde());
		if(tempzde==0){
			txtzxj.setTextColor(Color.WHITE);
			txtzdf.setTextColor(Color.WHITE);
			txtzde.setTextColor(Color.WHITE);
			layout.setBackgroundColor(Color.WHITE);
		}else{
			if(tempzde>0.00f){
				txtzxj.setTextColor(Color.RED);
				txtzdf.setTextColor(Color.RED);
				txtzde.setTextColor(Color.RED);
				layout.setBackgroundColor(Color.RED);
			}
			else{
				txtzxj.setTextColor(greencolor);
				txtzdf.setTextColor(greencolor);
				txtzde.setTextColor(greencolor);
				layout.setBackgroundColor(Color.GREEN);
			}
		}
		float jkp=Float.parseFloat(tempstock.getJkp());
		float zsp=Float.parseFloat(tempstock.getZsp());
		if((jkp-zsp)>0.00f)
			txtjkp.setTextColor(Color.RED);
		else
			if((jkp-zsp)<0.00f)
				txtjkp.setTextColor(greencolor);
			else
				txtjkp.setTextColor(Color.WHITE);
		float zd=Float.parseFloat(tempstock.getZd());
		if((zd-zsp)>0.00f)
			txtzd.setTextColor(Color.RED);
		else
			if((zd-zsp)<0.00f)
				txtzd.setTextColor(greencolor);
			else
				txtzd.setTextColor(Color.WHITE);
		float zg=Float.parseFloat(tempstock.getZg());
		if((zg-zsp)>0.00f)
			txtzg.setTextColor(Color.RED);
		else
			if((zg-zsp)<0.00f)
				txtzg.setTextColor(greencolor);
			else
				txtzg.setTextColor(Color.WHITE);
		float jmj=Float.parseFloat(tempstock.getJmj());
		if((jmj-zsp)>0.00f)
			txtjmj.setTextColor(Color.RED);
		else
			if((jmj-zsp)<0.00f)
				txtjmj.setTextColor(greencolor);
			else
				txtjmj.setTextColor(Color.WHITE);
		float jmj2=Float.parseFloat(tempstock.getJmj2());
		if((jmj2-zsp)>0.00f)
			txtjmj2.setTextColor(Color.RED);
		else
			if((jmj2-zsp)<0.00f)
				txtjmj2.setTextColor(greencolor);
			else
				txtjmj2.setTextColor(Color.WHITE);
		txtcjl.setTextColor(Color.YELLOW);
		txtcje.setTextColor(Color.YELLOW);
		txthqsj.setTextColor(Color.YELLOW);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String picurl=null;
		Button btnfs=(Button)findViewById(R.id.btn_fs);
		Button btnk=(Button)findViewById(R.id.btn_k);
		Button btnzk=(Button)findViewById(R.id.btn_zk);
		Button btnyk=(Button)findViewById(R.id.btn_yk);
		switch(v.getId()){
		case R.id.btn_fs:
			btnfs.setBackgroundResource(R.drawable.sfb_btn_off);
			btnk.setBackgroundResource(R.drawable.sfb_btn_off_p);
			btnzk.setBackgroundResource(R.drawable.sfb_btn_off_p);
			btnyk.setBackgroundResource(R.drawable.sfb_btn_off_p);
			bar1.setVisibility(View.VISIBLE);
			picurl="http://3g.sina.com.cn/3g/static/images/finance/stock/daily/huge/"+tempstr+".png";
			thread=new getpicturethread(handerpic);
			thread.doStart(picurl,this); 
			break;
		case R.id.btn_k:
			btnfs.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off));
			btnzk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnyk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			bar1.setVisibility(View.VISIBLE);
			picurl="http://3g.sina.com.cn/3g/static/images/finance/stock/k/daily/huge/"+tempstr+".gif";
			thread=new getpicturethread(handerpic);
			thread.doStart(picurl,this); 
			break;
		case R.id.btn_zk:
			btnfs.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnzk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off));
			btnyk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			bar1.setVisibility(View.VISIBLE);
			picurl="http://3g.sina.com.cn/3g/static/images/finance/stock/k/week/huge/"+tempstr+".gif";
			thread=new getpicturethread(handerpic);
			thread.doStart(picurl,this); 
			break;
		case R.id.btn_yk:
			btnfs.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnzk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off_p));
			btnyk.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.sfb_btn_off));
			bar1.setVisibility(View.VISIBLE);
			picurl="http://3g.sina.com.cn/3g/static/images/finance/stock/k/month/huge/"+tempstr+".gif";
			thread=new getpicturethread(handerpic);
			thread.doStart(picurl,this); 
			break;
		}
	}
	
}
