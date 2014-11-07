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

public class IndexSimpleAdapter extends SimpleAdapter {
	ArrayList<Map<String,Object>> items;
	public IndexSimpleAdapter( Context context, List<Map<String, Object>> data,  
			int resource, String[] from,int[] to) {  
			    super(context, data, resource, from, to);
			    items=(ArrayList<Map<String,Object>>)data;
	}
	public View getView(int position, View convertView, ViewGroup parent) {  
	    View v = super.getView(position, convertView, parent);  
	    Map<String, Object> tempmap=items.get(position);
	    float tempzde=Float.parseFloat((String) tempmap.get("zszdjs"));
	    String tempmc=(String)tempmap.get("zsmc");
	    TextView txtzsds=(TextView)v.findViewById(R.id.txtzsds);
	    TextView txtzszdjs=(TextView)v.findViewById(R.id.txtzszdjs);
	    TextView txtzszdf=(TextView)v.findViewById(R.id.txtzszdf);
	    TextView txtzssj=(TextView)v.findViewById(R.id.txtzssj);
	    TextView labzssj=(TextView)v.findViewById(R.id.lablezssj);
	    txtzssj.setTextColor(Color.BLACK);
	    if(tempmc.equals("上证指数")||tempmc.equals("深证成指")||tempmc.equals("沪深300")){
	    	labzssj.setText("成交额:");
	    }
	    else
	    	labzssj.setText("日期:");
	    
	    int greencolor=Color.rgb(0, 130, 0);
	    //int greencolor=Color.GREEN;
	    if(!(tempzde==0)){
		    if(tempzde>0.00f){
		    	txtzsds.setTextColor(Color.RED);
		    	txtzszdjs.setTextColor(Color.RED);
		    	txtzszdf.setTextColor(Color.RED);
		    }
		    else{
		    	txtzsds.setTextColor(greencolor);
		    	txtzszdjs.setTextColor(greencolor);
		    	txtzszdf.setTextColor(greencolor);
		    	

		    }
	    }
	    else{
	    	txtzsds.setTextColor(Color.BLACK);
	    	txtzszdjs.setTextColor(Color.BLACK);
	    	txtzszdf.setTextColor(Color.BLACK);
	    }
	
	    
	    return v;  
	} 
}
