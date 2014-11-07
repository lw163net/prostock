package com.liwei.rssread;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.liwei.prostock.*;

public class rsslistpage extends Activity implements OnItemClickListener{
	 ProgressDialog progressDialog=null;
	 private String RSSFEEDOFCHOICE=null;
	 private RSSFeed feed = null;
	 private ListView rsslist=null;
	 private TextView textFeedTitle=null;
	 private String rssName=null;
	 private String rssUrl=null;
	 
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
		 this.setContentView(R.layout.rsslistpage);
		 
		 rsslist=(ListView)findViewById(R.id.listViewRss);
		 rsslist.setOnItemClickListener((OnItemClickListener) this);
		 textFeedTitle=(TextView)findViewById(R.id.textViewFeedTitle);
		 //取得传入参数
		 Bundle bundle=getIntent().getExtras();
		 rssName=bundle.getString("KEY_RSSNAME");
		 rssUrl=bundle.getString("KEY_RSSURL");
		 
		 //加载rss
		 loadRss(rssUrl);
		 
		 
		 
		 
	 }
	 public void loadRss(final String url){
	       progressDialog=new ProgressDialog(this);
	       progressDialog.setTitle("加载新闻");
			progressDialog.setMessage("正在加载rss新闻....");
			progressDialog.show();
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
						case 0:
							progressDialog.dismiss();
							UpdateDisplay(url);
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
					callgetrss(url);
					handler.sendMessage(msg);
				}
			}.start();
	        
	    }
	    public void callgetrss(String rssaddress)
	    {
	    	// go get our feed!
	        feed = getFeed(rssaddress);

	        // display UI
	        
	    }
	    public void onItemClick(AdapterView parent, View v, int position, long id)
	    {
	       // Log.i(tag,"item clicked! [" + feed.getItem(position).getTitle() + "]");

	        
	        List<RSSItem> temprsslist=feed.getAllItems();
	        
	        Uri uri = Uri.parse(temprsslist.get(position).getLink());        
	        Intent it = new Intent(Intent.ACTION_VIEW, uri);        
	        startActivity(it);
	    }
	    
	    public List<Map<String, String>> fillmap(List<RSSItem> rsslist){
			List<Map<String, String>> items=new ArrayList<Map<String,String>>();
			for(int i=0;i<rsslist.size();i++){
				Map<String, String> tempmap=new HashMap<String, String>();

				tempmap.put("itemtitle", Integer.toString(i+1)+"、"+rsslist.get(i).getTitle());
				tempmap.put("itemtext", rsslist.get(i).getDescription());
				tempmap.put("itemdate",rsslist.get(i).getPubDate());
				items.add(tempmap);
			}
			return items;
		}

	    private void UpdateDisplay(String url) 
	    {
	  
	        if (feed == null)
	         {
	        	textFeedTitle.setText("No RSS Feed Available");
	           return;
	         }
	        
	        textFeedTitle.setText(feed.getTitle());
	       // feedpubdate.setText(feed.getPubDate());


	        List<Map<String, String>> items=fillmap(feed.getAllItems());
	        RssAdapter myAdapter= new RssAdapter(rsslistpage.this,items,R.layout.rsslistpageitem
										 ,new String[]{"itemtitle","itemtext","itemdate"}
										 ,new int[]{R.id.textViewRssTitle,R.id.textViewRssText,R.id.textViewRssDate});
	        rsslist.setAdapter(myAdapter);

	      //  itemlist.setAdapter(myAdapter);
	        
	       // itemlist.setOnItemClickListener(this);
	        
	       // itemlist.setSelection(0);
	        
	        
	    }
	    private RSSFeed getFeed(String urlToRssFeed)
	    {
	        try
	        {
	            // setup the url
	           URL url = new URL(urlToRssFeed);
	           CodepageDetectorProxy detector =   CodepageDetectorProxy.getInstance();
	           detector.add(JChardetFacade.getInstance());  
	           Charset charset =  detector.detectCodepage(url); 
	         //得到编码名称 
	           String encodingName = charset.name(); 

	           InputSource inputSource=null; 
	           InputStream stream = null; 



	           // create the factory
	           SAXParserFactory factory = SAXParserFactory.newInstance();
	           // create a parser
	           SAXParser parser = factory.newSAXParser();

	           // create the reader (scanner)
	           XMLReader xmlreader = parser.getXMLReader();
	           // instantiate our handler
	           RSSHandler theRssHandler = new RSSHandler();
	           // assign our handler
	           xmlreader.setContentHandler(theRssHandler);
	           // get our data through the url class
	           
	           //如果是GBK编码 
	           if("GBK".equals(encodingName)){ 
	 
	        	    stream = url.openStream(); 
	        	    //通过InputStreamReader设定编码方式 
	        	    InputStreamReader streamReader = new InputStreamReader(stream,"GBK"); 
	        	    inputSource = new InputSource(streamReader); 
	        	   }else{ 
	       	    //是utf-8编码 
	        	    inputSource = new InputSource(url.openStream()); 
	        	    inputSource.setEncoding("UTF-8"); 
	        	   } 


	           
	           // perform the synchronous parse           
	           xmlreader.parse(inputSource);
	           // get the results - should be a fully populated RSSFeed instance, 
			   // or null on error
	           return theRssHandler.getFeed();
	        }
	        catch (Exception ee)
	        {
	            // if you have a problem, simply return null
	            return null;
	        }
	    }
//	    public static Bitmap drawableToBitmap(Drawable drawable) {   
//	    	           
//	    	        Bitmap bitmap = Bitmap   
//	    	                        .createBitmap(   
//	    	                                        drawable.getIntrinsicWidth(),   
//	    	                                        drawable.getIntrinsicHeight(),   
//	                                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888   
//	    	                                                        : Bitmap.Config.RGB_565);   
//	    	        Canvas canvas = new Canvas(bitmap);   
//	    	        //canvas.setBitmap(bitmap);   
//	    	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());   
//	    	        drawable.draw(canvas);   
//	    	        return bitmap;   
//	    }  
	
}
