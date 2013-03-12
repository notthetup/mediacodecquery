package com.crayonio.mediacodecquery;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.media.MediaCodecInfo.CodecCapabilities;
import android.util.Log;
import android.util.SparseArray;

public class CodecColorFormatTranslator {

	private static SparseArray<String> dictionary;
	private static CodecColorFormatTranslator instance = null;

	private CodecColorFormatTranslator() {
		
		dictionary = new SparseArray<String>();
		populateDictionary();
	}
	
	public static CodecColorFormatTranslator getInstance(){
		if (instance == null)
			instance = new CodecColorFormatTranslator();
		return instance;
	}

	private void populateDictionary() {

		CodecCapabilities capabilities = new CodecCapabilities();
		
		Field[] fields = capabilities.getClass().getFields();

		for (Field field : fields) {
			// Log.i("Fields", "Checking field " + field.getName());
			if (field.getType().isPrimitive()) {
				try {
					dictionary.put(field.getInt(capabilities), field.getName());
					// Log.i("Fields","Added " + field.getName() + " : " +
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
			}
		}
	}

	public String getColorFormat(int formatIndex) {
		return dictionary.get(formatIndex);
	}

}
