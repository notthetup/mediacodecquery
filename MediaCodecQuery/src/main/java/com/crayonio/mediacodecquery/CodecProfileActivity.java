package com.crayonio.mediacodecquery;

import java.util.Locale;

import android.content.Intent;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CodecProfileActivity extends FragmentActivity {

	public static final String SELECTED_TYPE = "selectedType";
	public static final String CODEC_INDEX = "codecIndex";

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
	private int codecIndex = -1;
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
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				overridePendingTransition(R.anim.details_activity_slide_enter,
						R.anim.details_activity_slide_exit);
				return true;

			case R.id.action_email:

				StringBuilder sb = new StringBuilder();


				CodecInfo thisCodecInfo = CodecInfoList.getCodecInfoList().get(codecIndex);
				CodecProfileLevelTranslator profileTranslator = CodecProfileLevelTranslator.getInstance();
				CodecColorFormatTranslator colorTranslator = CodecColorFormatTranslator.getInstance();
				CodecCapabilities capabilities = thisCodecInfo.getCapabilitiesForType(selectedType);

				CodecProfileLevel[] profileLevels = capabilities.profileLevels;
				int[] colorFormats = capabilities.colorFormats;

				String codecName = CodecInfoList.getCodecInfoList().get(codecIndex).getCodecName();

				sb.append("Your device supports the following profiles and color format within the ")
						.append(selectedType)
						.append(" type of the ")
						.append(codecName)
						.append(" codec.")
						.append("\n\n")
						.append("\nProfiles \n\n");

				for (CodecProfileLevel thisLevel : profileLevels) {

					String translatedProfile = profileTranslator.getProfile(thisLevel.profile);
					String translatedLevel = profileTranslator.getLevel(thisLevel.level);

					if (translatedLevel == null)
						translatedLevel = "0x" + Integer.toHexString(thisLevel.level);

					if (translatedProfile == null)
						translatedProfile = "0x" + Integer.toHexString(thisLevel.profile);

					sb.append("- ").append(translatedProfile).append(" at level ").append(translatedLevel).append("\n\n");
				}

				sb.append("\nColor Formats \n\n");

				for (int thisFormat : colorFormats) {

					String translatedFormat = colorTranslator.getColorFormat(thisFormat);

					if (translatedFormat == null)
						translatedFormat = "0x" + Integer.toHexString(thisFormat);

					sb.append("- ").append(translatedFormat).append("\n\n");
				}

				sb.append("\n\nThanks for using Media Codec Query.");

				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Your Device's Media Codec Support");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
				emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Intent i = Intent.createChooser(emailIntent, "Send mail...");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(i);
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

			Fragment fragment;
			if (position == 0) {
				fragment = new LevelFragment();
			} else if (position == 1) {
				fragment = new ColorFragment();
			} else {
				Log.w("SectionsPagerAdapter", "Unknown Fragment position");
				return null;
			}
			Bundle args = new Bundle();
			args.putInt(CODEC_INDEX, codecIndex);
			args.putString(SELECTED_TYPE, selectedType);
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
}
