package com.crayonio.mediacodecquery;

import java.util.ArrayList;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CodecListActivity extends ListActivity implements
OnItemClickListener {

	private final ArrayList<CodecInfo> codecInfoList = new ArrayList<CodecInfo>();
	final static String CODEC_INDEX = "codecIndex";
	private Typeface robotoCondensedLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_list);
		ListView myListView = getListView();
		myListView.setOnItemClickListener(this);
		
		robotoCondensedLight = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Light.ttf");  
		
		setListAdapter(new ArrayAdapter<CodecInfo>(this,
				R.layout.codec_list_row, R.id.codecName, codecInfoList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// Must always return just a View.
				View rowView = super.getView(position, convertView, parent);

				CodecInfo entry = codecInfoList.get(position);

				TextView name = (TextView) rowView
						.findViewById(R.id.codecName);
				TextView fullName = (TextView) rowView
						.findViewById(R.id.codecFullName);
				name.setTypeface(robotoCondensedLight);
				fullName.setTypeface(robotoCondensedLight);
				
				name.setText(entry.getCodecName());
				fullName.setText(entry.getFullName());

				//Log.i("GetView", "Getting rowView " + position + " : " + rowView.getId());

				ImageView dirImg = (ImageView) rowView
						.findViewById(R.id.directionImage);

				if (entry.isDecoder() && entry.isEncoder()) {
					dirImg.setImageDrawable(getResources().getDrawable(
							R.drawable.av_repeat));
					//Log.i("GetView", "Setting Image in " + position);
				} else {
					dirImg.setImageDrawable(null);
				}

				return rowView;
			}
		});

		//Log.i("CodecListActivity", " This device supports");
		for (int codecIndex = 0; codecIndex < MediaCodecList.getCodecCount(); ++codecIndex) {
			MediaCodecInfo thisCodec = MediaCodecList
					.getCodecInfoAt(codecIndex);
			String codecName = thisCodec.getName();
			//Log.i("CodecListActivity", "Next " + codecName);

			CodecInfo codecInfo = new CodecInfo(codecName);
			int index = codecInfoList.indexOf(codecInfo);
			if (index >= 0) {
				if (thisCodec.isEncoder() || codecInfo.isEncoder())
					codecInfoList.get(index).setEncoder();
				else if (codecInfo.isDecoder())
					codecInfoList.get(index).setDecoder();
				else {
					Log.w("CodecListActivity", codecName
							+ ": Neither Encoder/Decoder.. WTH?");
				}
			} else {
				// Log.i("CodecListActivity", "Adding " + codecInfo);
				codecInfoList.add(codecInfo);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//Log.i("OnClick", "Clicked " + arg2 + " " + codecInfoList.get(arg2));
		Intent intent = new Intent(this, CodecDetailsActivity.class);
		intent.putExtra(CODEC_INDEX, arg2);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.list_activity_slide_enter,R.anim.list_activity_slide_exit).toBundle();
		startActivity(intent,bndlanimation);
	}
}
