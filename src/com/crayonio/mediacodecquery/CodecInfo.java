package com.crayonio.mediacodecquery;

import android.util.Log;

public class CodecInfo {

	private String origString = "";
	private String manufacturerName = "";
	private String fullName = "";
	private String codecName = "";
	private boolean isEncoder = false;
	private boolean isDecoder = false;
	private boolean isSoftware = false;

	public CodecInfo(String nameString) {
		this.origString = nameString;

		// Log.i("Codec Info","Spliting " + fullname);

		String[] tokens = nameString.split("\\.");

		if (tokens.length > 0) {
			/* Over-ride for the software?? AAC codec */
			if (tokens.length == 1 && !tokens[0].equalsIgnoreCase("OMX")) {
				this.codecName = tokens[0];
				this.fullName = this.codecName;
				this.isSoftware = true;
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
						if (thisToken.equalsIgnoreCase("encoder")) {
							this.isEncoder = true;
							// Log.i("Codec Info","It is an encoder");
						} else if (thisToken.equalsIgnoreCase("decoder")) {
							this.isDecoder = true;
							// Log.i("Codec Info","It is an encoder");
						}
						else{
							this.codecName += "." + thisToken;
						}
					}
				}
			}

			/* if (tokens.length > 1) {
				this.manufacturerName = tokens[1];
				// Log.i("Codec Info","Manufacturer is " + manufacturerName);
			}

			if (tokens.length > 2) {
				this.codecName = tokens[2];
				if (tokens.length > 3){
					String[] subset = Arrays.copyOfRange(tokens,3,tokens.length-1);				
					for (String string : subset) {
						this.codecName += "."+string;
					}
				}
				// Log.i("Codec Info","Codec is " + codecName);
			}

			if (tokens[tokens.length - 1].contentEquals("encoder")) {
				this.isEncoder = true;
				this.fullName = nameString.replaceAll("\\.encoder", "");
				// Log.i("Codec Info","It is an encoder");
			} else if (tokens[tokens.length - 1].contentEquals("decoder")) {
				this.isDecoder = true;
				this.fullName = nameString.replaceAll("\\.decoder", "");
				// Log.i("Codec Info","It is an encoder");
			} else{
				this.fullName = nameString;
			} */

		} else {
			Log.w("Codec Info", "No tokens found?? WTH?");
		}
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
}
