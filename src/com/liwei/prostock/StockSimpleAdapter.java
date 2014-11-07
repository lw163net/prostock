package com.liwei.prostock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StockSimpleAdapter extends SimpleAdapter  {
	ArrayList<Map<String,String>> items;
	public StockSimpleAdapter( Context context, List<Map<String, String>> data,  
			int resource, String[] from,int[] to) {  
			    super(context, data, resource, from, to);
			    items=(ArrayList<Map<String,String>>)data;
	}
	public View getView(int position, View convertView, ViewGroup parent) {  
	    View v = super.getView(position, convertView, parent);  
	    Map<String, String> tempmap=items.get(position);
	    float tempzde=Float.parseFloat((String) tempmap.get("gpzde"));
	    TextView txtgpzxj=(TextView)v.findViewById(R.id.zxj);
	    TextView txtgpzde=(TextView)v.findViewById(R.id.zde);
	    TextView txtgpzdf=(TextView)v.findViewById(R.id.zdf);
	    int greencolor=Color.rgb(0, 130, 0);
	    //int greencolor=Color.GREEN;
	    if(!(tempzde==0)){
		    if(tempzde>0.00f){
		    	txtgpzxj.setTextColor(Color.RED);
		    	txtgpzde.setTextColor(Color.RED);
		    	txtgpzdf.setTextColor(Color.RED);
		    }
		    else{
		    	txtgpzxj.setTextColor(greencolor);
		    	txtgpzde.setTextColor(greencolor);
		    	txtgpzdf.setTextColor(greencolor);

		    }
	    }
	    else{
	    	txtgpzxj.setTextColor(Color.BLACK);
	    	txtgpzde.setTextColor(Color.BLACK);
	    	txtgpzdf.setTextColor(Color.BLACK);
	    }
	    
	    return v;  
	} 

}
