/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.config;

/**
 *
 */
public final class Constants {
	
	public static final String FINAL_STATUS = "EDS_WORKFLOW";

	
	public static final class Collections {

			public static final String TRANSACTION_DATA = "transaction_data";

			public static final String INI_EDS_INVOCATION = "ini_eds_invocation";

			public static final String VALIDATED_DOCUMENTS = "validated_documents";

			public static final String SCHEMA = "schema";

			public static final String SCHEMATRON = "schematron";
			
			public static final String FHIR_TRANSFORM = "transform";
			
			public static final String TERMINOLOGY = "terminology";
			
			public static final String DICTIONARY = "dictionary";

			public static final String ENGINES = "engines";
			

		private Collections() {

		}
	}


	public static final class ConfigItems {

		public static final String SUCCESS_TRANSACTION_RETENTION_HOURS = "SUCCESS";

		public static final String VALIDATED_DOCUMENT_RETENTION_DAY = "VALIDATED_DOCUMENT_RETENTION_DAY";
		
		public static final String CFG_ITEMS_RETENTION_DAY = "CFG_ITEMS_RETENTION_DAY";

		private ConfigItems() {
		}
	}

	public static final class Profile {

		public static final String TEST = "test";

		public static final String TEST_PREFIX = "test_";

		public static final String DEV = "dev";

		/**
		 * Constructor.
		 */
		private Profile() {
			// This method is intentionally left blank.
		}

	}

	/**
	 * Constants.
	 */
	private Constants() {

	}

}
