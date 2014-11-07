/**
 * 
 */
package com.liwei.rssread;



import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.parser.EncodingParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.liwei.prostock.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author lenovo
 *
 */
public class rssnews extends Activity{


	//private String RSSFEEDOFCHOICE="http://rss.sina.com.cn/roll/finance/hot_roll.xml";
	//private String RSSFEEDOFCHOICE="http://rss.sina.com.cn/roll/stock/hot_roll.xml";
	//private String RSSFEEDOFCHOICE="http://rss.sina.com.cn/news/allnews/finance.xml";
	//private String RSSFEEDOFCHOICE="http://rss.sina.com.cn/sina_finance_opml.xml";

	ProgressDialog progressDialog;
	private GridView gridView;
	private GridView gridView2=null;
	private TextView ceshi;
	private Drawable[] menuImagesRss = null;
	private Drawable[] backImages=null;
	private String[] menuStrs = null;
	private String[] menuId=null;
	ArrayList<ClassRssUrl> arrayurl=null;
	private String[] longclickmenu=new String[]{"删除"};
	String[] netnodelist=null;
	String[] strtest={"111","222","333","444","555","666","777","888","999"};
	String[] txttitle=null;
	String[] txturl=null;
	int netnodecount=0;
	RelativeLayout lay1=null;
	DisplayMetrics dm=null;
	
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.rssnews);
        netnodelist=this.getResources().getStringArray(R.array.string_rss_all);
        netnodecount=netnodelist.length;
        menuImagesRss=new Drawable[netnodecount];
        menuStrs=new String[netnodecount];
        menuId=new String[netnodecount];
        backImages=new Drawable[4];
        loadRssList();
 
    }
	public void loadRssList(){
			setArray();
	       dm= new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);//取得屏幕参数

			gridView = (GridView) findViewById(R.id.gridView);
			gridView.setNumColumns(menuImagesRss.length);//设置gridview个数
			gridView.setColumnWidth((dm.widthPixels) / 3);//每列宽度
			LinearLayout.LayoutParams liner = (LinearLayout.LayoutParams) gridView
					.getLayoutParams();//取得gridview参数
			
			liner.width = menuImagesRss.length*(dm.widthPixels) / 3;//设置gridview 宽度
			gridView.setLayoutParams(liner);
			class ItemView extends BaseAdapter {
				public int position;
				private Context context;

				public ItemView(Context context) {
					this.context = context;
				}

				public int getCount() {
					// TODO Auto-generated method stub
					return menuImagesRss.length;
				}

				public Object getItem(int position) {
					// TODO Auto-generated method stub
					this.position = position;
					return this.position;
				}

				public long getItemId(int position) {
					// TODO Auto-generated method stub
					this.position = position;
					return this.position;
				}

				public View getView(int position, View convertView, ViewGroup parent) {
					// TODO Auto-generated method stub
					this.position = position;
					convertView = LayoutInflater.from(context).inflate(
							R.layout.rssgriditem, null);
					ImageView itemIcon = (ImageView) convertView
							.findViewById(R.id.imageitem);
					//itemIcon.setBackgroundResource(menuImagesRss[position]);
					//itemIcon.setAdjustViewBounds(true);
					//itemIcon.setMaxHeight(48);
					//itemIcon.setMaxWidth(64);
					
					itemIcon.setBackgroundDrawable(menuImagesRss[position]);
					
					itemIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
					//itemIcon.setImageDrawable(menuImagesRss[position]);
					//itemIcon.setImageBitmap(drawableToBitmap(menuImagesRss[position]));
					//
					itemIcon.setPadding(8, 8, 8, 8);
					TextView itemText = (TextView) convertView
							.findViewById(R.id.textitem);
					itemText.setText(menuStrs[position]);

					return convertView;
				}

				public int getPosition() {
					return (int) getItemId(position);
				}

			}
			ItemView itemView = new ItemView(this);
			gridView.setAdapter(itemView);
			gridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String tempid=menuId[arg2];
					int resId=getResources().getIdentifier(tempid, "array", "com.liwei.prostock");
					String[] tempstr=getResources().getStringArray(resId);
					txttitle=new String[tempstr.length];
					txturl=new String[tempstr.length];
					for(int i=0;i<tempstr.length;i++){
						String[] temptext=tempstr[i].split(",");
						txttitle[i]=temptext[0];
						txturl[i]=temptext[1];			
					}
					setgridview2(txttitle);
					
				}});
			
			

	}
	public void setgridview2(final String[] strtextlist)
	{
		gridView2 = (GridView) findViewById(R.id.gridViewItem);
		gridView2.setNumColumns(2);//设置gridview个数
		gridView2.setColumnWidth((dm.widthPixels) / 2);//每列宽度
		class ItemView2 extends BaseAdapter {
			public int position;
			private Context context;

			public ItemView2(Context context) {
				this.context = context;
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return strtextlist.length;
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				this.position = position;
				return this.position;
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				this.position = position;
				return this.position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				this.position = position;
				convertView = LayoutInflater.from(context).inflate(
						R.layout.rssgriditem2, null);
				lay1=(RelativeLayout)convertView.findViewById(R.id.relativeLayout_gridview);
				TextView textviewtitle=(TextView)convertView.findViewById(R.id.textViewrssgriditem2);
				textviewtitle.setText(strtextlist[position]);
				int max=4,min=0;
				Random random = new Random();
				int ind = random.nextInt(max)%(max-min+1) + min;
				lay1.setBackgroundDrawable(backImages[ind]);
				
				convertView.setPadding(2, 2, 2, 2);
				return convertView;
			}

			public int getPosition() {
				return (int) getItemId(position);
			}

		}
		ItemView2 itemView2 = new ItemView2(this);
		gridView2.setAdapter(itemView2);
		gridView2.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
			  	intent.setClass(rssnews.this, rsslistpage.class);
			  	intent.putExtra("KEY_RSSNAME", txttitle[arg2]);
			  	intent.putExtra("KEY_RSSURL", txturl[arg2]);
			  	startActivity(intent);
				
			}});
	}
	public void setArray(){
		int resID1 = getResources().getIdentifier("back_box_black", "drawable", "com.liwei.prostock"); 
		backImages[0]=getResources().getDrawable(resID1);
		resID1 = getResources().getIdentifier("back_box_blue", "drawable", "com.liwei.prostock"); 
		backImages[1]=getResources().getDrawable(resID1);
		resID1 = getResources().getIdentifier("back_box_red", "drawable", "com.liwei.prostock"); 
		backImages[2]=getResources().getDrawable(resID1);
		resID1 = getResources().getIdentifier("back_box_green", "drawable", "com.liwei.prostock"); 
		backImages[3]=getResources().getDrawable(resID1);
		for(int i=0;i<netnodecount;i++){
			String strtemp=netnodelist[i];
			String[] tempa=strtemp.split(",");
			menuId[i]=tempa[0];
			menuStrs[i]=tempa[1];
			String logoimagename=tempa[2];
			//获得logo在drawable上的id
			int resID = getResources().getIdentifier(logoimagename, "drawable", "com.liwei.prostock"); 
			menuImagesRss[i]=getResources().getDrawable(resID);	
			
			
			
		}
	}
//    public void setArray(){
//    	DBRssUrlService rssdb=new DBRssUrlService(this);
//    	arrayurl=rssdb.getRssUrlList();
//    	for (int i=0;i<8;i++){
//    		Drawable tempdraw=null;
//    		ClassRssUrl tempurl=arrayurl.get(i);
//    		menuStrs[i]=tempurl.getRssname();
//    		if(tempurl.getRssimage()==null){
//    			if(tempurl.getRssname().equals("添加"))
//    				tempdraw=getResources().getDrawable(R.drawable.netvibes);
//    			else
//    				tempdraw=getResources().getDrawable(R.drawable.repeat);
//    		}else{
//    			ByteArrayInputStream bais=new ByteArrayInputStream(tempurl.getRssimage());
//    			tempdraw=Drawable.createFromStream(bais, null);
//    			
//    		}
//    		menuImagesRss[i]=tempdraw;
//    		
//    	}
//    
//    }
   




 


}
