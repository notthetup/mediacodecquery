package com.crayonio.mediacodecquery;

import java.lang.reflect.Field;

import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.SparseArray;

class CodecColorFormatTranslator {

	private static SparseArray<String> dictionary;
	private static CodecColorFormatTranslator instance = null;

	private CodecColorFormatTranslator() {

		dictionary = new SparseArray<String>();
		populateDictionary();
	}

	public static CodecColorFormatTranslator getInstance() {
		if (instance == null)
			instance = new CodecColorFormatTranslator();
		return instance;
	}

	private void populateDictionary() {

		CodecCapabilities capabilities = new CodecCapabilities();

		Field[] fields = capabilities.getClass().getFields();

		// Log.d("Fields", "Checking field " + field.getName());
		for (Field field : fields)
			if (field.getType().isPrimitive()) try {
				dictionary.put(field.getInt(capabilities), field.getName());
				// Log.d("Fields","Added " + field.getName() + " : " +
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	}

	public String getColorFormat(int formatIndex) {
		return dictionary.get(formatIndex);
	}

}
