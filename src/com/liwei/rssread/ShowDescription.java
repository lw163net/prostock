/**
 * 
 */
package com.liwei.rssread;

import com.liwei.prostock.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author lenovo
 *
 */
public class ShowDescription extends Activity {

	public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.showdescription);
        
        String theStory = null;
        String strtitle=null;
        String strdate=null;
        String strdescription=null;
        String strlink=null;
        
        
        
        Intent startingIntent = getIntent();
        
        if (startingIntent != null)
        {
            Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
            if (b == null)
            {
                theStory = "bad bundle?";
            }
            else
            {
            	strtitle="\n"+b.getString("title")+"\n";
            	strdate=b.getString("pubdate")+"\n";
            	strdescription=b.getString("description").replace('\n',' ')+"\n";
            	strlink="更多详情:"+b.getString("link")+"\n";
                //theStory = b.getString("title") + "\n\n" + b.getString("pubdate") 
				//+ "\n\n" + b.getString("description").replace('\n',' ') 
				//+ "\n\nMore information:\n" + b.getString("link");
            }
        }
        else
        {
            theStory = "Information Not Found.";
        
        }
        
        TextView txttitle= (TextView) findViewById(R.id.textrsstitle);
        TextView txtdate=(TextView)findViewById(R.id.textrsspubdate);
        TextView txtdescription=(TextView)findViewById(R.id.textrssdescription);
        TextView txtlink=(TextView)findViewById(R.id.textrsslink);
        txttitle.setText(strtitle);
        txtdate.setText(strdate);
        txtdescription.setText(strdescription);
        txtlink.setText(strlink);
        
        Button backbutton = (Button) findViewById(R.id.back);
        
        backbutton.setOnClickListener(new Button.OnClickListener() 
        {


			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        });        
    }

}
