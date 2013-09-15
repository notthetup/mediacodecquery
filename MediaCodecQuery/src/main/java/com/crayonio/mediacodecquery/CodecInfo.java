package com.crayonio.mediacodecquery;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.Log;

class CodecInfo {

	private String manufacturerName = "";
	private String fullName = "";
	private String codecName = "";

	private boolean isEncoder = false;
	private boolean isDecoder = false;

	private HashSet<String> supportedTypes = new HashSet<String>();

	private ArrayList<MediaCodecInfo> mediaCodecInfoList;

	public CodecInfo(MediaCodecInfo mediaCodecInfo) {

		mediaCodecInfoList = new ArrayList<MediaCodecInfo>(2);
		mediaCodecInfoList.add(mediaCodecInfo);
		supportedTypes.addAll(Arrays.asList(mediaCodecInfo.getSupportedTypes()));

		this.splitName(mediaCodecInfo.getName());
		if (mediaCodecInfo.isEncoder())
			this.isEncoder = true;
		else
			this.isDecoder = true;
	}

	private void splitName(String nameString) {
		this.fullName = nameString;

		//Log.d("Codec Info","Spliting " + fullName);

		String[] tokens = nameString.split("\\.");

		/* Over-ride for the software codecs*/
		if (tokens.length > 0) {
			if (tokens.length == 1) {
				this.codecName = tokens[0];
				this.manufacturerName = "unknown";
			} else {
				for (int index = 0; index < tokens.length; ++index) {
					String thisToken = tokens[index];

					if (index == 1)
						this.manufacturerName = thisToken;

					else if (index > 1 && index < tokens.length - 1) {
						if (!this.codecName.equalsIgnoreCase(""))
							this.codecName += ".";

						this.codecName += thisToken;
					} else if (index == tokens.length - 1) {
						if (thisToken.equalsIgnoreCase("encoder") || thisToken.contains("encoder") || thisToken.contains("Encoder") || thisToken.contains("ENCODER")) {
							this.isEncoder = true;
							//Log.d("Codec Info","It is an encoder");
						} else if (thisToken.equalsIgnoreCase("decoder") || thisToken.contains("decoder") || thisToken.contains("Decoder") || thisToken.contains("DECODER")) {
							this.isDecoder = true;
							//Log.d("Codec Info","It is an encoder");
						} else {
							this.codecName += "." + thisToken;
						}
					}
				}
			}
		} else Log.w("Codec Info", "No tokens found?? WTH?");
	}

	public void assimilate(MediaCodecInfo mediaCodecInfo) {
		if (mediaCodecInfo.isEncoder())
			this.isEncoder = true;
		else
			this.isDecoder = true;

		mediaCodecInfoList.add(mediaCodecInfo);
		supportedTypes.addAll(Arrays.asList(mediaCodecInfo.getSupportedTypes()));
	}


	public String getCodecName() {
		return codecName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public boolean isEncoder() {
		return isEncoder;
	}

	public boolean isDecoder() {
		return isDecoder;
	}

	@Override
	public String toString() {
		return fullName;
	}

	@Override
	public boolean equals(Object obj) {

		return (obj != null) && ((obj == this) || ((obj.getClass() == getClass()) && ((CodecInfo) obj).getCodecName().equalsIgnoreCase(this.codecName) && ((CodecInfo) obj).getManufacturerName().equalsIgnoreCase(this.manufacturerName)));

	}

	public String[] getSupportedTypes() {
		return supportedTypes.toArray(new String[supportedTypes.size()]);
	}

	public CodecCapabilities getCapabilitiesForType(String selectedType) {

		CodecCapabilities selectedCapability = null;

		for (MediaCodecInfo thisCodecInfo : mediaCodecInfoList)
			if (Arrays.asList(thisCodecInfo.getSupportedTypes()).contains(selectedType)){
				try{
					selectedCapability = thisCodecInfo.getCapabilitiesForType(selectedType);
				}catch (IllegalArgumentException e){
					Log.w("CodecInfo", "Error getting capability for the type " + selectedType);
				}
				break;
			}

		if (selectedCapability == null){
			Log.w("CodecInfo", "Unable to find the selected type. Making up an empty one!");
			selectedCapability = new CodecCapabilities();
			selectedCapability.profileLevels = new MediaCodecInfo.CodecProfileLevel[0];
			selectedCapability.colorFormats = new int[0];
		}


		return selectedCapability;
	}


}
