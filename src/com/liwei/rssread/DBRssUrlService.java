/**
 * 收藏RSS 地址数据库操作
 */
package com.liwei.rssread;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import com.liwei.message.messagedata;
import com.liwei.prostock.R;
import com.liwei.message.*;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author lenovo
 *
 */
public class DBRssUrlService extends SQLiteOpenHelper{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private final static String TAG="DBRssUrlService";
	private final static int  DATBASE_VERSION=1;
	private final static String DATABASE_NAME="prostock.db";
	public DBRssUrlService(Context context) {
		super(context, DATABASE_NAME, null, DATBASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//没有数据库时运行
		
		String sql="CREATE TABLE [tabrssurl] ("
					+"[_id] INT NOT NULL," 
					+"[rssname] VARCHAR(10)," 
					+"[rssurl] VARCHAR(50), "
					+"[rssimage] BINARY)";

		
		String sqlmessage="CREATE TABLE [tabmessage] ("
							+"[messageid] integer PRIMARY KEY autoincrement," 
							+"[stockid] varchar(6) NOT NULL,"
							+"[stocktj] int DEFAULT 0 NOT NULL," 
							+"[stockdx] int DEFAULT 0 NOT NULL, "
							+"[stockvalue] float DEFAULT 0 NOT NULL,"
							+"[stockbool] BOOLEAN DEFAULT '0' NOT NULL,"
							+"[stocktime] datetime default datetime('now')";
		
		try{
			db.execSQL(sqlmessage);
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
		}
		
		try{
			db.execSQL(sql);
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
		}
		String sql2="insert into tabrssurl(_id,rssname,rssurl,rssimage) values(?,?,?,?)";
		ClassRssUrl rssurl1=new ClassRssUrl();
		rssurl1.set_id(1);
		rssurl1.setRssname("财经要闻");
		rssurl1.setRssurl("http://rss.sina.com.cn/roll/finance/hot_roll.xml");
		rssurl1.setRssimage(null);
		Object[] args1=new Object[]{rssurl1.get_id(),rssurl1.getRssname(),rssurl1.getRssurl(),rssurl1.getRssimage()};
		
		try{
			db.execSQL(sql2,args1);
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
		}
		
		rssurl1.set_id(2);
		rssurl1.setRssname("股市要闻");
		rssurl1.setRssurl("http://rss.sina.com.cn/roll/stock/hot_roll.xml");
		rssurl1.setRssimage(null);
		args1=new Object[]{rssurl1.get_id(),rssurl1.getRssname(),rssurl1.getRssurl(),rssurl1.getRssimage()};
		
		http://news.hexun.com/rss/stock_rss.xml
		try{
			db.execSQL(sql2,args1);
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
		}
		
		rssurl1.set_id(3);
		rssurl1.setRssname("凤凰要闻");
		rssurl1.setRssurl("http://finance.ifeng.com/rss/stocknews.xml");
		rssurl1.setRssimage(null);
		args1=new Object[]{rssurl1.get_id(),rssurl1.getRssname(),rssurl1.getRssurl(),rssurl1.getRssimage()};
		
		http://news.hexun.com/rss/stock_rss.xml
		try{
			db.execSQL(sql2,args1);
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
		}
		
		for(int i=3;i<8;i++){
			
			ClassRssUrl rssurl=new ClassRssUrl();
			rssurl.set_id(i+1);
			rssurl.setRssname("添加");
			rssurl.setRssurl(null);
			rssurl.setRssimage(null);
			Object[] argstemp=new Object[]{rssurl.get_id(),rssurl.getRssname(),rssurl.getRssurl(),rssurl.getRssimage()};
			try{
				db.execSQL(sql2,argstemp);
			}catch(Exception ex){
				Log.d("TAG", ex.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	
		db.execSQL("DROP TABLE IF EXISTS " + "tabrssurl");   
		onCreate(db);   
		
	}

	public ArrayList<ClassRssUrl> getRssUrlList(){
		try{
			ArrayList<ClassRssUrl> templist=new ArrayList<ClassRssUrl>();
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cursor=db.rawQuery("select *from tabrssurl", null);
			cursor.moveToFirst();
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				ClassRssUrl tempurl=new ClassRssUrl();
				tempurl.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
				tempurl.setRssname(cursor.getString(cursor.getColumnIndex("rssname")));
				tempurl.setRssurl(cursor.getString(cursor.getColumnIndex("rssurl")));
				tempurl.setRssimage(cursor.getBlob(cursor.getColumnIndex("rssimage")));
				templist.add(tempurl);
			}
			if(templist.isEmpty())
				return null;
			else
				return templist;	
		}catch(Exception ex){
			return null;
		}
	}
	
	public boolean editRssUrl(ClassRssUrl rssurl){
		try{
			//String sql="insert into tabrssurl(rssname,rssurl,rssimage) values(?,?,?)";
			String sql="update tabrssurl set rssname=?,rssurl=?,rssimage=? where _id=?";
			SQLiteDatabase db=this.getReadableDatabase();
			Object[] args=new Object[]{rssurl.getRssname(),rssurl.getRssurl(),rssurl.getRssimage(),rssurl.get_id()};
			db.execSQL(sql,args);
			return true;
		}catch(Exception ex){
			Log.d(TAG, ex.toString());
			return false;
		}
		
	}
	//插入提醒数据
	public boolean insertmessage(messagedata data){
		String strsql="insert into tabmessage"
			          +"(stockid,stocktj,stockdx,stockvalue,stockbool,stocktime) values(?,?,?,?,?,?)"; 
		Object[] args1=new Object[]{
					data.getStockid(),
					data.getStocktj(),
					data.getStockdx(),
					data.getStockvalue(),
					data.isStockbool(),
					data.getStocktime().toString()
					};
		try{
			SQLiteDatabase db=this.getReadableDatabase();
			db.execSQL(strsql,args1);
			return true;
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
			return false;
		}
	}
	//修改提醒数据
	public boolean editmessage(messagedata data){
		String strsql="update tabmessage set stockid=?,stocktj=?,stockdx=?,"
									+"stockvalue=?,stockbool=?,stocktime=? where messageid=?";
		Object[] args1=new Object[]{
				data.getStockid(),
				data.getStocktj(),
				data.getStockdx(),
				data.getStockvalue(),
				data.isStockbool(),
				data.getStocktime().toString(),
				data.getMessageid()
				};
		try{
			SQLiteDatabase db=this.getReadableDatabase();
			db.execSQL(strsql,args1);
			return true;
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
			return false;
		}
	}
	//取列表
	public ArrayList<messagedata> selectmessage(String strsql,String[] args){
		
		ArrayList<messagedata> result=new ArrayList<messagedata>();
		try{
			SQLiteDatabase db=this.getReadableDatabase();
			Cursor cursor=db.rawQuery(strsql, args);
			//int selectcount=cursor.getCount();
			cursor.moveToFirst();
			while(!cursor.isLast()){
				messagedata data=new messagedata();
				data.setMessageid(cursor.getInt(cursor.getColumnIndex("messageid")));
				data.setStockid(cursor.getString(cursor.getColumnIndex("stockid")));
				data.setStocktj(cursor.getInt(cursor.getColumnIndex("stocktj")));
				data.setStockdx(cursor.getInt(cursor.getColumnIndex("stockdx")));
				data.setStockvalue(cursor.getFloat(cursor.getColumnIndex("stockvalue")));
				data.setStockbool(cursor.getInt(cursor.getColumnIndex("stockbool"))>0);
				Date tempdate=new Date(cursor.getString(cursor.getColumnIndex("stocktime")));
				data.setStocktime(tempdate);
				result.add(data);
				cursor.moveToNext();		
			}
			return result;
		}catch(Exception ex){
			Log.d("TAG", ex.getMessage());
			return null;
		}
		
	}

}
