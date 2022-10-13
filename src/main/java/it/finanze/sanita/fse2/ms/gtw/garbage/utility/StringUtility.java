package it.finanze.sanita.fse2.ms.gtw.garbage.utility;

import com.google.gson.Gson;

public final class StringUtility {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private StringUtility() {
		// Constructor intentionally empty.
	}

	/**
	 * Transformation from Object to Json.
	 * 
	 * @param obj object to transform
	 * @return json
	 */
	public static String toJSON(final Object obj) {
		return new Gson().toJson(obj);
	}

}
