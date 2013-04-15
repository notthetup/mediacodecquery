package com.crayonio.mediacodecquery;

import java.lang.reflect.Field;

import android.media.MediaCodecInfo.CodecProfileLevel;
import android.util.SparseArray;

public class CodecProfileLevelTranslator {

	private static SparseArray<String> profileDictionary;
	private static SparseArray<String> levelDictionary;
	private static CodecProfileLevelTranslator instance = null;

	private CodecProfileLevelTranslator() {

		profileDictionary = new SparseArray<String>();
		levelDictionary = new SparseArray<String>();
		
		populateDictionaries();
	}
	
	public static CodecProfileLevelTranslator getInstance(){
		if (instance == null)
			instance = new CodecProfileLevelTranslator();
		
		return instance;
	}


	private void populateDictionaries() {
		
		CodecProfileLevel profileLevel = new CodecProfileLevel();

		Field[] fields = profileLevel.getClass().getFields();

		for (Field field : fields) {
			// Log.d("Fields", "Checking field " + field.getName());
			if (field.getType().isPrimitive()) {
				String fieldName = field.getName();
				try {
					if (fieldName.contains("Level"))
						levelDictionary.put(field.getInt(profileLevel),
								fieldName);
					else if (fieldName.contains("Profile"))
						profileDictionary.put(field.getInt(profileLevel),
								fieldName);
					// Log.d("Fields","Added " + field.getName() + " : " +
					// field.getInt(capabilities));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	public String getProfile(int profileIndex) {
		return profileDictionary.get(profileIndex);
	}

	public String getLevel(int levelIndex) {
		return levelDictionary.get(levelIndex);
	}

}
