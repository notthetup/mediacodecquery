package com.crayonio.mediacodecquery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import android.graphics.Typeface;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CodecProfileActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	private String selectedType;
	private int codecIndex = -1;;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_codec_profile);

		if (codecIndex < 0) {
			selectedType = getIntent().getStringExtra(
					CodecDetailsActivity.SELECTED_TYPE);
			codecIndex = getIntent().getIntExtra(
					CodecDetailsActivity.CODEC_INDEX, -1);
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.codec_profile, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Log.i("Details Activity","Back pressed.");
		overridePendingTransition(R.anim.details_activity_slide_enter,
				R.anim.details_activity_slide_exit);
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

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new ColorLevelFragment();
			Bundle args = new Bundle();
			args.putInt(ColorLevelFragment.ARG_SECTION_NUMBER, position + 1);
			args.putInt(ColorLevelFragment.CODEC_INDEX, codecIndex);
			args.putString(ColorLevelFragment.SELECTED_TYPE, selectedType);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class ColorLevelFragment extends Fragment implements
			OnItemClickListener {

		public static final String ARG_SECTION_NUMBER = "sectionNum";
		public static final String SELECTED_TYPE = "selectedType";
		public static final String CODEC_INDEX = "codecIndex";
		static final int PROFILE_POSITION = 1;
		static final int COLOR_POSITION = 2;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private int[] colorFormats;
		private CodecProfileLevel[] profileLevels;
		private MediaCodecInfo thisCodecInfo = null;
		private CodecCapabilities capabalities = null;
		private int cachedCodecIndex = -1;

		private ArrayList<Boolean> profileVerbosity;
		private ArrayList<Boolean> colorVerbosity;
		private int sectionNum = -1;

		private Typeface robotoCondensedLight;
		
		private ListView myListView;

		public ColorLevelFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_codec_profile,
					container, false);

			robotoCondensedLight = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Light.ttf");
			
			myListView = (ListView) rootView.findViewById(R.id.profile_list);

			myListView.setOnItemClickListener(this);

			sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			String selectedType = getArguments().getString(SELECTED_TYPE);
			int codecIndex = getArguments().getInt(CODEC_INDEX);

			/*
			 * Log.i("Codec Profile Fragment", " Showing " + selectedType +
			 * " of codec " + codecIndex + " in " + sectionNum);
			 */

			if (cachedCodecIndex != codecIndex) {

				thisCodecInfo = MediaCodecList.getCodecInfoAt(codecIndex);
				capabalities = thisCodecInfo
						.getCapabilitiesForType(selectedType);
				colorFormats = capabalities.colorFormats;
				profileLevels = capabalities.profileLevels;
				cachedCodecIndex = codecIndex;

				Boolean[] array = new Boolean[capabalities.profileLevels.length];
				Arrays.fill(array, Boolean.FALSE);
				profileVerbosity = new ArrayList<Boolean>(Arrays.asList(array));

				array = new Boolean[capabalities.colorFormats.length];
				Arrays.fill(array, Boolean.FALSE);
				colorVerbosity = new ArrayList<Boolean>(Arrays.asList(array));
			}

			if (sectionNum == PROFILE_POSITION) {

				CodecProfileLevelTranslator profileTranslator = CodecProfileLevelTranslator
						.getInstance();

				final ArrayList<CodecProfileStrings> codecProfileStrings = new ArrayList<CodecProfileStrings>();

				for (CodecProfileLevel thisProfile : profileLevels) {
					String translatedProfile = profileTranslator
							.getProfile(thisProfile.profile);
					if (translatedProfile == null)
						translatedProfile = (String
								.valueOf(thisProfile.profile));

					String translatedLevel = profileTranslator
							.getLevel(thisProfile.level);
					if (translatedLevel == null)
						translatedLevel = ("0x" + Integer
								.toHexString(thisProfile.level));

					codecProfileStrings.add(new CodecProfileStrings(
							translatedProfile, translatedLevel));
				}

				myListView.setAdapter(new ArrayAdapter<CodecProfileStrings>(
						getActivity().getApplicationContext(),
						R.layout.codec_profile_row, R.id.profileName,
						codecProfileStrings) {

					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {

						// Must always return just a View.
						View rowView = super.getView(position, convertView,
								parent);

						CodecProfileLevelTranslator profileTranslator = CodecProfileLevelTranslator
								.getInstance();

						CodecProfileStrings thisProfile = codecProfileStrings
								.get(position);

						TextView profile = (TextView) rowView
								.findViewById(R.id.profileName);
						TextView level = (TextView) rowView
								.findViewById(R.id.levelName);
						profile.setTypeface(robotoCondensedLight);
						level.setTypeface(robotoCondensedLight);

						if (!thisProfile.getProfileName().startsWith("0x")
								|| profileVerbosity.get(position))
							profile.setText(thisProfile.getProfileName());
						else
							profile.setText(R.string.undefined);

						if (!thisProfile.getLevelName().startsWith("0x")
								|| profileVerbosity.get(position)) {
							level.setText(thisProfile.getLevelName());
							((TextView) rowView.findViewById(R.id.tapPrompt))
									.setVisibility(View.INVISIBLE);
						} else {
							level.setText(R.string.undefined);
							((TextView) rowView.findViewById(R.id.tapPrompt))
									.setVisibility(View.VISIBLE);
						}

						return rowView;
					}
				});

			} else if (sectionNum == COLOR_POSITION) {

				CodecColorFormatTranslator colorTranslator = CodecColorFormatTranslator
						.getInstance();

				final ArrayList<String> colorFormatList = new ArrayList<String>();
				for (int index = 0; index < colorFormats.length; index++) {
					String translatedProfile = colorTranslator
							.getColorFormat(colorFormats[index]);
					if (translatedProfile != null)
						colorFormatList.add(translatedProfile);
					else
						colorFormatList.add("0x"
								+ Integer.toHexString(colorFormats[index]));
				}

				myListView.setAdapter(new ArrayAdapter<String>(getActivity()
						.getApplicationContext(), R.layout.codec_color_row,
						R.id.colorName, colorFormatList) {

					public View getView(int position, View convertView,
							ViewGroup parent) {

						// Must always return just a View.
						View rowView = super.getView(position, convertView,
								parent);

						String thisColorFormat = colorFormatList.get(position);

						TextView colorField = (TextView) rowView
								.findViewById(R.id.colorName);
						colorField.setTypeface(robotoCondensedLight);

						if (!thisColorFormat.startsWith("0x")
								|| colorVerbosity.get(position)){
							colorField.setText(thisColorFormat);
							((TextView) rowView.findViewById(R.id.tapPrompt)).setVisibility(View.INVISIBLE);
						}
						else{
							colorField.setText(R.string.undefined);
							((TextView) rowView.findViewById(R.id.tapPrompt)).setVisibility(View.VISIBLE);
						}

						return rowView;
					}
				});
			} else {
				Log.w("Codec Profile Activity", "Unknown section " + sectionNum);
			}

			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			toggleVerbosity(position);
			/*
			 * Log.i("Codec Profile Activity", " Invalidating " + position +
			 * " : " + view.toString());
			 */
			if (sectionNum == PROFILE_POSITION)
				((ArrayAdapter<CodecProfileLevel>) myListView.getAdapter())
						.notifyDataSetChanged();
			else if (sectionNum == COLOR_POSITION)
				((ArrayAdapter<String>) myListView.getAdapter())
						.notifyDataSetChanged();
		}

		private void toggleVerbosity(int position) {

			if (sectionNum == PROFILE_POSITION) {
				if (profileVerbosity.get(position))
					profileVerbosity.set(position, Boolean.FALSE);
				else
					profileVerbosity.set(position, Boolean.TRUE);
			} else if (sectionNum == COLOR_POSITION) {
				if (colorVerbosity.get(position))
					colorVerbosity.set(position, Boolean.FALSE);
				else
					colorVerbosity.set(position, Boolean.TRUE);
			}

		}
	}
}

class CodecProfileStrings {

	private String profileName;
	private String levelName;

	public CodecProfileStrings(String profileName, String levelName) {
		this.profileName = profileName;
		this.levelName = levelName;
	}

	public String getLevelName() {
		return levelName;
	}

	public String getProfileName() {
		return profileName;
	}
}
