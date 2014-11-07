package com.liwei.prostock;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;

public class ttscheck {
		 
		Activity myMainActivity; 
		private static final int REQ_TTS_STATUS_CHECK = 0; 
		 
		public ttscheck(Activity mainActivity) { 
		     super(); 
		     this.myMainActivity=mainActivity;     
		} 

		 
		 
//		public boolean someMethod() { 
//		    Intent checkIntent = new Intent();   
//		    checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);   
//		    myMainActivity.startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
//		    
//		}
}
