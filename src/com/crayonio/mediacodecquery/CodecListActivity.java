package com.crayonio.mediacodecquery;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class CodecListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_list);

		Log.i("CodecListActivity"," This device supports");
		for (int codecIndex = 0; codecIndex < MediaCodecList.getCodecCount(); ++codecIndex) {
			MediaCodecInfo thisCodec = MediaCodecList.getCodecInfoAt(codecIndex);
			
			String outputString = thisCodec.getName() + " as ";
			
			if (thisCodec.isEncoder()){
				outputString += "an encoder";
			}
			else{
				outputString += "a decoder";
			}
			Log.i("CodecListActivity", outputString);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_codec_list, menu);
		return true;
	}

}
