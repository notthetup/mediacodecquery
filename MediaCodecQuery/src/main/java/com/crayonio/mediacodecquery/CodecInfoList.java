package com.crayonio.mediacodecquery;

import java.util.ArrayList;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

class CodecInfoList {

	private static ArrayList<CodecInfo> mCodecInfoList = null;

	public CodecInfoList() {

	}

	public static ArrayList<CodecInfo> getCodecInfoList() {
		if (mCodecInfoList == null)
			mCodecInfoList = generateCodecInfoList();

		return mCodecInfoList;
	}

	private static ArrayList<CodecInfo> generateCodecInfoList() {

		ArrayList<CodecInfo> codecInfoList = new ArrayList<CodecInfo>();
        MediaCodecInfo[] allCodecs = new MediaCodecList(MediaCodecList.ALL_CODECS).getCodecInfos();


		// Log.d("CodecListActivity", " This device supports");
		for (int codecIndex = 0; codecIndex < allCodecs.length; ++codecIndex) {
			addtoCodecList(allCodecs[codecIndex], codecInfoList);
		}
		return codecInfoList;
	}

	private static void addtoCodecList(MediaCodecInfo mediaCodecInfo, ArrayList<CodecInfo> codecInfoList) {
		CodecInfo thisCodecInfo = new CodecInfo(mediaCodecInfo);
		int indexOfSimilar = codecInfoList.indexOf(thisCodecInfo);

		if (indexOfSimilar < 0) {
			codecInfoList.add(thisCodecInfo);
		} else {
			codecInfoList.get(indexOfSimilar).assimilate(mediaCodecInfo);
		}
	}
}
