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

public class FundAdapter extends SimpleAdapter{
	ArrayList<Map<String,String>> items;

	public FundAdapter(Context context, List<Map<String, String>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		items=(ArrayList<Map<String,String>>)data;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = super.getView(position, convertView, parent);  
	    Map<String, String> tempmap=items.get(position);
	    
	    float tempzf=Float.parseFloat((String) tempmap.get("fundzf").replaceAll("%", ""));
	    TextView txtfundzxjz=(TextView)v.findViewById(R.id.fundzxjz);
	    TextView txtfundzf=(TextView)v.findViewById(R.id.fundzf);
	    int greencolor=Color.rgb(0, 112, 0);
	    if(tempzf==0){
	    	txtfundzxjz.setTextColor(Color.BLACK);
	    	txtfundzf.setTextColor(Color.BLACK);
	    }else{
	    	if(tempzf>0){
	    		txtfundzxjz.setTextColor(Color.RED);
	    		txtfundzf.setTextColor(Color.RED);
	    	}
	    	else
	    	{
	    		txtfundzxjz.setTextColor(greencolor);
	    		txtfundzf.setTextColor(greencolor);
	    	}
	    }
		return v;  
		
	}
}
