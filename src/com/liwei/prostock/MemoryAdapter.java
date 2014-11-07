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

public class MemoryAdapter extends SimpleAdapter {
	ArrayList<Map<String,String>> items;
	public MemoryAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		items=(ArrayList<Map<String,String>>)data;
		// TODO Auto-generated constructor stub
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);  
	    Map<String, String> tempmap=items.get(position);
	    String tempstr=(String)tempmap.get("memzde");
	    
	    float tempzde=Float.parseFloat(tempstr.substring(0,tempstr.indexOf("(")));
	    TextView txtzxj=(TextView)v.findViewById(R.id.txtmemxj);
	    TextView txtzde=(TextView)v.findViewById(R.id.txtmemzde);
	    int greencolor=Color.rgb(0, 112, 0);
	    if(tempzde==0){
	    	
	    }else{
	    	if(tempzde>0){
	    		txtzxj.setTextColor(Color.RED);
	    		txtzde.setTextColor(Color.RED);
	    	}else
	    	{
	    		txtzxj.setTextColor(greencolor);
	    		txtzde.setTextColor(greencolor);
	    	}
	    }
	    
		return v;

		
	}

}
