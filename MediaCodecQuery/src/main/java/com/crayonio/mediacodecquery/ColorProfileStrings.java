package com.crayonio.mediacodecquery;

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