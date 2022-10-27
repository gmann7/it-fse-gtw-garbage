/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.config;

/**
 * @author AndreaPerquoti
 *
 */
public final class Constants {

	/**
	 * Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.finanze.sanita.fse2.ms.gtw.garbage";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.finanze.sanita.fse2.ms.gtw.garbage.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.finanze.sanita.fse2.ms.gtw.garbage.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.finanze.sanita.fse2.ms.gtw.garbage.config";
		
		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.finanze.sanita.fse2.ms.gtw.garbage.config.mongo";
		
		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.finanze.sanita.fse2.ms.gtw.garbage.repository";

				
		private ComponentScan() {
			// This method is intentionally left blank.
		}

	}
	
	public static final class Collections {

		public static final String TRANSACTION_DATA = "transaction_data";

			public static final String INI_EDS_INVOCATION = "ini_eds_invocation";

			public static final String VALIDATED_DOCUMENTS = "validated_documents";

			public static final String SCHEMA = "schema";

			public static final String SCHEMATRON = "schematron";
			
			public static final String FHIR_TRANSFORM = "transform";

		private Collections() {

		}
	}


	public static final class ConfigItems {

		public static final String SUCCESS_TRANSACTION_RETENTION_HOURS = "SUCCESS";

		public static final String BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS = "BLOCKING_ERROR";

		public static final String NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS = "NON_BLOCKING_ERROR";

		public static final String VALIDATED_DOCUMENT_RETENTION_DAY = "VALIDATED_DOCUMENT_RETENTION_DAY";

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
