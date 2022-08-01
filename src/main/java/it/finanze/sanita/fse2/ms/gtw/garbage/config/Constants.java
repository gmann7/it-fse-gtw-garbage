/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.config;

/**
 * @author AndreaPerquoti
 *
 */
public final class Constants {

	/**
	 *	Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.sanita.fse2.ms.gtw.garbage";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.sanita.fse2.ms.gtw.garbage.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.sanita.fse2.ms.gtw.garbage.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.sanita.fse2.ms.gtw.garbage.config";
		
		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.sanita.fse2.ms.gtw.garbage.config.mongo";
		
		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.sanita.fse2.ms.gtw.garbage.repository";

		public static final class Collections {

			public static final String TRANSACTION_DATA = "transaction_data";

			public static final String INI_EDS_INVOCATION = "ini_eds_invocation";

			private Collections() {

			}
		}
		
		private ComponentScan() {
			//This method is intentionally left blank.
		}

	}

	public static final class ConfigItems {

		public static final String SUCCESS_TRANSACTION_RETENTION_HOURS = "SUCCESS_TRANSACTION_RETENTION_HOURS";

		public static final String ERROR_TRANSACTION_RETENTION_HOURS = "ERROR_TRANSACTION_RETENTION_HOURS";

		private ConfigItems() {}
	}
 
	public static final class Profile {

		public static final String TEST = "test";

		public static final String TEST_PREFIX = "test_";

		public static final String DEV = "dev";

		/** 
		 * Constructor.
		 */
		private Profile() {
			//This method is intentionally left blank.
		}

	}
  
	/**
	 *	Constants.
	 */
	private Constants() {

	}

}
