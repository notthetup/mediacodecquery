package com.crayonio.mediacodecquery;

import java.util.ArrayList;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

public class CodecInfoList {

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

		// Log.i("CodecListActivity", " This device supports");
		for (int codecIndex = 0; codecIndex < MediaCodecList.getCodecCount(); ++codecIndex) {
			MediaCodecInfo thisCodec = MediaCodecList
					.getCodecInfoAt(codecIndex);
			String codecName = thisCodec.getName();
			// Log.i("CodecListActivity", "Next " + codecName);

			CodecInfo codecInfo = new CodecInfo(codecName);
			int index = codecInfoList.indexOf(codecInfo);
			if (index >= 0) {
				if (thisCodec.isEncoder() || codecInfo.isEncoder()) {
					codecInfoList.get(index).setEncoder();
				}
				else{
					codecInfoList.get(index).setDecoder();
				}
				codecInfoList.get(index).addSupportedTypes(
						thisCodec.getSupportedTypes());
			} else {
				// Log.i("CodecListActivity", "Adding " + codecInfo);
				codecInfoList.add(codecInfo);
				codecInfoList.get(codecInfoList.size()-1).addSupportedTypes(
						thisCodec.getSupportedTypes());
			}
		}
		return codecInfoList;
	}
}
