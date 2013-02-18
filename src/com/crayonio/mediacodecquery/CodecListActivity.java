package com.crayonio.mediacodecquery;

import java.util.ArrayList;

import android.app.ListActivity;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CodecListActivity extends ListActivity implements OnClickListener {

	private static final int NAME_CODEC_INFO_SECTIONS = 0;
	private static final int FULLNAME_CODEC_INFO_SECTIONS = 1;
	private static final int TYPE_CODEC_INFO_SECTIONS = 2;
	private static final int NUM_CODEC_INFO_SECTIONS = TYPE_CODEC_INFO_SECTIONS + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_list);
		ListView myListView = getListView();
		myListView.setOnClickListener(this);
		//int codecCount = MediaCodecList.getCodecCount();

		final ArrayList<String[]> codecInfoList = new ArrayList<String[]>();

		// setListAdapter(new ArrayAdapter<String>(this,
		// R.layout.codec_list_row, R.id.tvCodecNameFull , codecFullNames));
		setListAdapter(new ArrayAdapter<String[]>(this,
				R.layout.codec_list_row, R.id.tvCodecName, codecInfoList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// Must always return just a View.
				View view = super.getView(position, convertView, parent);

				String[] entry = codecInfoList.get(position);
				TextView name = (TextView) view.findViewById(R.id.tvCodecName);
				TextView fullName = (TextView) view
						.findViewById(R.id.tvCodecNameFull);
				name.setText(entry[NAME_CODEC_INFO_SECTIONS]);
				fullName.setText(entry[FULLNAME_CODEC_INFO_SECTIONS]);
				return view;
			}
		});

		Log.i("CodecListActivity", " This device supports");
		for (int codecIndex = 0; codecIndex < MediaCodecList.getCodecCount(); ++codecIndex) {
			MediaCodecInfo thisCodec = MediaCodecList
					.getCodecInfoAt(codecIndex);
			String codecName = thisCodec.getName();
			CodecInfo codecInfo = new CodecInfo(codecName);
			String[] codecInfoElement = new String[NUM_CODEC_INFO_SECTIONS];

			codecInfoElement[FULLNAME_CODEC_INFO_SECTIONS] = codecInfo
					.getFullName();
			codecInfoElement[NAME_CODEC_INFO_SECTIONS] = codecInfo
					.getCodecName();
			codecInfoElement[TYPE_CODEC_INFO_SECTIONS] = codecInfo.isEncoder() ? "encoder"
					: "decoder";

			codecInfoList.add(codecInfoElement);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_codec_list, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
