package com.liwei.prostock;

import java.util.Locale;


import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.google.tts.TextToSpeechBeta; 
import com.liwei.prostock.ShakeDetector.OnShakeListener;


public class speakService extends Service{
	
	private static final String TAG = "TextToSpeech";
	//private TextToSpeech mTts;
	private String str_speak_list;  

	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public void onInit(int status) {
//		// TODO Auto-generated method stub
//		if (status == TextToSpeech.SUCCESS) {
//			int result = mTts.setLanguage(Locale.FRANCE);
//
//			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//				// Lanuage data is missing or the language is not supported.
//				Log.e(TAG, "Language is not available.");
//			}
//		} else {
//			// Initialization failed.
//			Log.e(TAG, "Could not initialize TextToSpeech.");
//		}
//	}
    @Override  
    public void onCreate() {   
    //	android.os.Debug.waitForDebugger();
        
    }  
	@Override  
    public void onStart(Intent intent, int startId) { 
		
        super.onStart(intent, startId);   
        str_speak_list=stockwidget4.str_speak_list;
        Context context = getApplicationContext(); 
        TtsProviderFactory ttsProviderImpl = TtsProviderFactory.getInstance(); 
        if (ttsProviderImpl != null) {     
        	ttsProviderImpl.init(context);     
        	ttsProviderImpl.say(str_speak_list); 
        	stopSelf();
        	//ttsProviderImpl.say("你摇晃了手机,摇晃值大于3000"); 
        }
        
        
//        mTts = new TextToSpeech(this, this);
//   //     Bundle bundle=intent.getExtras();
//        //str_speak_list=(String)bundle.getString("STR_STOCK_SPEAK");
//        str_speak_list=stockwidget4.str_speak_list;
//        Log.d(TAG, str_speak_list);
//        mTts.speak(str_speak_list, TextToSpeech.QUEUE_FLUSH, null);
    }   


	
}

