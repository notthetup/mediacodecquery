package com.crayonio.mediacodecquery;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.Log;

public class CodecInfo {

	private String manufacturerName = "";
	private String fullName = "";
	private String codecName = "";
	
	private boolean isEncoder = false;
	private boolean isDecoder = false;
	
	private HashSet<String> supportedTypes;
	
	private ArrayList<MediaCodecInfo> mediaCodecInfoList;

	public CodecInfo(MediaCodecInfo mediaCodecInfo) {
		supportedTypes = new HashSet<String>();
		
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

		//Log.i("Codec Info","Spliting " + fullName);

		String[] tokens = nameString.split("\\.");

		if (tokens.length > 0) {
			/* Over-ride for the software codecs*/
			if (tokens.length == 1) {
				this.codecName = tokens[0];
				this.manufacturerName = "unknown";
				return;
			}
			else{
				for (int index = 0; index < tokens.length; ++index) {
					String thisToken = tokens[index];

					if (index == 1)
						this.manufacturerName = thisToken;

					else if (index > 1 && index < tokens.length-1) {
						if (!this.codecName.equalsIgnoreCase(""))
							this.codecName += ".";

						this.codecName += thisToken;
					}

					else if (index == tokens.length-1){
						if (thisToken.equalsIgnoreCase("encoder") || thisToken.contains("encoder") || thisToken.contains("Encoder") || thisToken.contains("ENCODER")) {
							this.isEncoder = true;
							//Log.i("Codec Info","It is an encoder");
						} else if (thisToken.equalsIgnoreCase("decoder") || thisToken.contains("decoder") || thisToken.contains("Decoder") || thisToken.contains("DECODER")) {
							this.isDecoder = true;
							//Log.i("Codec Info","It is an encoder");
						}
						else{
							this.codecName += "." + thisToken;
						}
					}
				}
			}

		} else {
			Log.w("Codec Info", "No tokens found?? WTH?");
		}
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

		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		if (((CodecInfo) obj).getCodecName().equalsIgnoreCase(this.codecName)
				&& ((CodecInfo) obj).getManufacturerName().equalsIgnoreCase(
						this.manufacturerName))
			return true;
		else
			return false;
	}
	
	public String [] getSupportedTypes() {
		return (String[]) supportedTypes.toArray(new String[0]);
	}

	public CodecCapabilities getCapabilitiesForType(String selectedType) {
		for (MediaCodecInfo thisCodecInfo : mediaCodecInfoList) {
			if (Arrays.asList(thisCodecInfo.getSupportedTypes()).contains(selectedType))
				return thisCodecInfo.getCapabilitiesForType(selectedType);
		}
		
		Log.w("CodecInfo","Unable to find the selected type. Help!");
		return null;
	}


}
