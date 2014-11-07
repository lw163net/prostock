package com.liwei.prostock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

public class classqg {
	public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
        	mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
        = activityManager.getRunningServices(30);
       if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {

            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
	
	public  static void WriteSettings(String content){  
		String filePath = "/sdcard/prostock.txt";
		SimpleDateFormat  formatter=new    SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");      
       Date    curDate=new    Date(System.currentTimeMillis());//获取当前时间      
       String    str=formatter.format(curDate); 
       
            	//如果filePath是传递过来的参数，可以做一个后缀名称判断； 没有指定的文件名没有后缀，则自动保存为.txt格式
            	if(!filePath.endsWith(".txt") && !filePath.endsWith(".log")) 
            		filePath += ".txt";
            	//保存文件
            	File file = new File(filePath); 
            	try {
            		OutputStream outstream = new FileOutputStream(file,true);
            		OutputStreamWriter out = new OutputStreamWriter(outstream);
            		out.write(str+":"+content+"\r\n");
            		out.close();
            	} catch (java.io.IOException e) {
            		e.printStackTrace();
        	}
	     }  

}

