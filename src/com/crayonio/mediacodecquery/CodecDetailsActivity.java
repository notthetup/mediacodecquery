package com.crayonio.mediacodecquery;

import java.util.ArrayList;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CodecDetailsActivity extends ListActivity implements
		OnItemClickListener {

	final static String CODEC_INDEX = "codecIndex";
	final static String SELECTED_TYPE = "selectedType";

	private CodecInfo thisCodecInfo;
	private int codecIndex = -1;
	private String[] types;

	private Typeface robotoCondensedLight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_details);
		
		robotoCondensedLight = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Light.ttf");  

		ListView myListView = getListView();
		myListView.setOnItemClickListener(this);

		if (savedInstanceState != null) {
			int newCodecState = savedInstanceState.getInt(CODEC_INDEX, -1);
			if (newCodecState >= 0)
				codecIndex = newCodecState;
		}

		if (codecIndex < 0) {
			codecIndex = getIntent().getIntExtra(CodecListActivity.CODEC_INDEX,
					-1);
		}
		
		ArrayList<CodecInfo> codecInfoList = CodecInfoList.getCodecInfoList();
		
		if (codecIndex >= 0 && codecIndex < codecInfoList.size()) {
			thisCodecInfo = codecInfoList.get(codecIndex);
			types = thisCodecInfo.getSupportedTypes();
			setListAdapter(new ArrayAdapter<String>(this,
					R.layout.codec_detail_row, R.id.codecDetails, types){
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {

					// Must always return just a View.
					View rowView = super.getView(position, convertView, parent);

					String entry = types[position];

					TextView details = (TextView) rowView
							.findViewById(R.id.codecDetails);
					details.setTypeface(robotoCondensedLight);
					
					details.setText(entry);

					return rowView;
				}
				
			});
		} else
			Log.w("Codec Details Activity", "No codec Index ");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.details_activity_slide_enter,
					R.anim.details_activity_slide_exit);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Log.d("Details Activity","Back pressed.");
		overridePendingTransition(R.anim.details_activity_slide_enter,
				R.anim.details_activity_slide_exit);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CODEC_INDEX, codecIndex);
		Log.d("Codec Details", "Saving State!");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/*
		 * Log.d("OnClick", "Clicked " + arg2 + " : " + types[arg2]);
		 * 
		 * CodecCapabilities capabalities = thisCodecInfo
		 * .getCapabilitiesForType(types[arg2]);
		 * 
		 * CodecColorFormatTranslator colorTranslator =
		 * CodecColorFormatTranslator .getInstance();
		 * CodecProfileLevelTranslator profileTranslator =
		 * CodecProfileLevelTranslator .getInstance();
		 * 
		 * int[] colorFormats = capabalities.colorFormats; CodecProfileLevel[]
		 * profileLevels = capabalities.profileLevels;
		 * 
		 * for (CodecProfileLevel codecProfileLevel : profileLevels) {
		 * Log.d("Profile Level", codecProfileLevel.profile + " : " +
		 * codecProfileLevel.level + " <> " + profileTranslator
		 * .getProfile(codecProfileLevel.profile) + " : " + profileTranslator
		 * .getLevel(codecProfileLevel.level)); }
		 * 
		 * for (int colorFormat : colorFormats) { Log.d("Color Format",
		 * colorFormat + " > " + colorTranslator.getColorFormat(colorFormat)); }
		 */

		Intent intent = new Intent(this, CodecProfileActivity.class);
		intent.putExtra(CODEC_INDEX, codecIndex);
		intent.putExtra(SELECTED_TYPE, types[arg2]);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.list_activity_slide_enter,
				R.anim.list_activity_slide_exit).toBundle();
		startActivity(intent, bndlanimation);

	}
}
