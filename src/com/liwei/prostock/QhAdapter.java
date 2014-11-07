package com.liwei.prostock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class QhAdapter extends SimpleAdapter {
	ArrayList<Map<String,String>> items;
	public QhAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to){
		super(context, data, resource, from, to);
		
		items=(ArrayList<Map<String,String>>)data;
		// TODO Auto-generated constructor stub
	}
	public View getView(int position, View convertView, ViewGroup parent) {
			
			View v = super.getView(position, convertView, parent);  
		    Map<String, String> tempmap=items.get(position);
		    
		    float tempzde=Float.parseFloat((String) tempmap.get("qh_zde"));
		    TextView qh_txt_zxj=(TextView)v.findViewById(R.id.qh_txt_zxj);
		    TextView qh_txt_zde=(TextView)v.findViewById(R.id.qh_txt_zde);
		    TextView qh_txt_zf=(TextView)v.findViewById(R.id.qh_txt_zf);
		    TextView qh_txt_mc=(TextView)v.findViewById(R.id.qh_txt_mc);
		    int greencolor=Color.rgb(0, 112, 0);
		    qh_txt_mc.setTextColor(Color.YELLOW);
		    if(tempzde==0){
		    	
		    }else{
		    	if(tempzde>0){
		    		qh_txt_zxj.setTextColor(Color.RED);
		    		qh_txt_zde.setTextColor(Color.RED);
		    		qh_txt_zf.setTextColor(Color.RED);
		    		
		    	}
		    	else
		    	{
		    		qh_txt_zxj.setTextColor(Color.GREEN);
		    		qh_txt_zde.setTextColor(Color.GREEN);
		    		qh_txt_zf.setTextColor(Color.GREEN);
		    	}
		    }
			return v;  
			
		}
}
