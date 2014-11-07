package com.liwei.prostock;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class stocksetup extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 所的的值将会自动保存到SharePreferences
		addPreferencesFromResource(R.xml.stocksetup);
	}

	
}
