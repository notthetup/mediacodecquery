package com.crayonio.mediacodecquery;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CodecDetailsActivity extends ListActivity implements
		OnItemClickListener {

	final static String CODEC_INDEX = "codecIndex";
	final static String SELECTED_TYPE = "selectedType";

	private MediaCodecInfo thisCodecInfo;
	private int codecIndex = -1;
	private String[] types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_details);
		
		ListView myListView = getListView();
		myListView.setOnItemClickListener(this);

		if (codecIndex < 0) {
			codecIndex = getIntent().getIntExtra(CodecListActivity.CODEC_INDEX,
					-1);
		}
		if (codecIndex >= 0 && codecIndex < MediaCodecList.getCodecCount()) {
			thisCodecInfo = MediaCodecList.getCodecInfoAt(codecIndex);

			types = thisCodecInfo.getSupportedTypes();
			// Log.i("Details"," This Codec : " + codecIndex + " :: " +
			// types.toString());

			setListAdapter(new ArrayAdapter<String>(this,
					R.layout.codec_detail_row, R.id.codecDetails, types));
		} else
			Log.w("Codec Details Activity", "No codec Index ");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(R.anim.details_activity_slide_enter,
					R.anim.details_activity_slide_exit);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Log.i("Details Activity","Back pressed.");
		overridePendingTransition(R.anim.details_activity_slide_enter,
				R.anim.details_activity_slide_exit);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/* Log.i("OnClick", "Clicked " + arg2 + " : " + types[arg2]);

		CodecCapabilities capabalities = thisCodecInfo
				.getCapabilitiesForType(types[arg2]);

		CodecColorFormatTranslator colorTranslator = CodecColorFormatTranslator
				.getInstance();
		CodecProfileLevelTranslator profileTranslator = CodecProfileLevelTranslator
				.getInstance();

		int[] colorFormats = capabalities.colorFormats;
		CodecProfileLevel[] profileLevels = capabalities.profileLevels;

		for (CodecProfileLevel codecProfileLevel : profileLevels) {
			Log.i("Profile Level",
					codecProfileLevel.profile
							+ " : "
							+ codecProfileLevel.level
							+ " <> "
							+ profileTranslator
									.getProfile(codecProfileLevel.profile)
							+ " : "
							+ profileTranslator
									.getLevel(codecProfileLevel.level));
		}

		for (int colorFormat : colorFormats) {
			Log.i("Color Format",
					colorFormat + " > "
							+ colorTranslator.getColorFormat(colorFormat));
		}*/

		Intent intent = new Intent(this, CodecProfileActivity.class);
		intent.putExtra(CODEC_INDEX, codecIndex);
		intent.putExtra(SELECTED_TYPE, types[arg2]);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.list_activity_slide_enter,
				R.anim.list_activity_slide_exit).toBundle();
		startActivity(intent, bndlanimation);

	}
}
