package com.crayonio.mediacodecquery;

import android.util.Log;

public class CodecInfo {

	private String fullName = "";
	private String manufacturerName = "";
	private String codecName = "";
	private boolean isEncoder = false;

	public CodecInfo(String fullname) {
		this.fullName = fullname;

		//Log.i("Codec Info","Spliting " + fullname);
		
		String[] tokens = fullname.split("\\.");

		if (tokens.length > 0)
		{
			/* Over-ride for the software?? AAC codec */
			if (tokens.length == 1 && !tokens[0].equalsIgnoreCase("OMX")) {
				this.codecName = tokens[0];
				return;
			}

			if (tokens.length > 1) {
				this.manufacturerName = tokens[1];
				//Log.i("Codec Info","Manufacturer is " + manufacturerName);
			}

			if (tokens.length > 2) {
				this.codecName = tokens[2];
				//Log.i("Codec Info","Codec is " + codecName);
			}

			if (tokens[tokens.length-1].contentEquals("encoder")){
				this.isEncoder = true;
				//Log.i("Codec Info","It is an encoder");
			}
		}
		else
		{
			Log.w("Codec Info","No tokens found?? WTH?");
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return codecName;
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

	public String getFullName() {
		return fullName;
	}
}
