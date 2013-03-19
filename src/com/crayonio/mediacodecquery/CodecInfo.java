package com.crayonio.mediacodecquery;


import java.util.Arrays;
import java.util.HashSet;

import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.Log;

public class CodecInfo {

	private String origString = "";
	private String manufacturerName = "";
	private String fullName = "";
	private String codecName = "";
	private boolean isEncoder = false;
	private boolean isDecoder = false;
	private boolean isSoftware = false;
	private HashSet<String> supportedTypes ;

	public CodecInfo(String nameString) {
		this.origString = nameString;
		supportedTypes = new HashSet<String>();

		Log.i("Codec Info","Spliting " + origString);

		String[] tokens = nameString.split("\\.");

		if (tokens.length > 0) {
			/* Over-ride for the software?? AAC codec */
			if (tokens.length == 1 && !tokens[0].equalsIgnoreCase("OMX")) {
				this.codecName = tokens[0];
				this.fullName = this.codecName;
				this.isSoftware = true;
				this.isEncoder = true;
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
						this.fullName = nameString;
						if (thisToken.equalsIgnoreCase("encoder") || thisToken.contains("encoder") || thisToken.contains("Encoder") || thisToken.contains("ENCODER")) {
							this.isEncoder = true;
							Log.i("Codec Info","It is an encoder");
						} else if (thisToken.equalsIgnoreCase("decoder") || thisToken.contains("decoder") || thisToken.contains("Decoder") || thisToken.contains("DECODER")) {
							this.isDecoder = true;
							Log.i("Codec Info","It is an encoder");
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
		
		if (!isEncoder)
			isDecoder = true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return origString;
	}

	public String getCodecName() {
		return codecName;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public boolean isEncoder() {
		return isEncoder;
	}

	public boolean isDecoder() {
		return isDecoder;
	}

	public String getFullName() {
		return fullName;
	}

	public boolean isSoftware() {
		return isSoftware;
	}

	public void setEncoder() {
		isEncoder = true;
	}

	public void setDecoder() {
		isDecoder = true;
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

	public void addSupportedTypes(String[] supportedTypes) {
		this.supportedTypes.addAll(Arrays.asList(supportedTypes));
	}
	
	public String [] getSupportedTypes() {
		return (String[]) supportedTypes.toArray(new String[0]);
	}

	public CodecCapabilities getCapabilitiesForType(String selectedType) {
		// TODO Auto-generated method stub
		return null;
	}
}
