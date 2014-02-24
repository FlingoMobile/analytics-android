package io.segment.android;

public class Constants {

	public static final String PACKAGE_NAME = Constants.class.getPackage().getName();
	
	/**
	 * Logging tag
	 */
	public static final String TAG = "analytics";
	
	/**
	 * The maximum amount of events to flush at a time
	 */
	public static final int MAX_FLUSH = 20;
	
	public static class Database {

		public static final int VERSION = 1;
		
		public static final String NAME = PACKAGE_NAME;
		
		public static class PayloadTable {
		
			public static final String NAME = "payload_table";
			
			public static final String[] FIELD_NAMES = new String[] { 
				Fields.Id.NAME,
				Fields.Payload.NAME 
			};
			
			public static class Fields {
				
				public static class Id {
					
					public static final String NAME = "id";
					
					/**
					 *  INTEGER PRIMARY KEY AUTOINCREMENT means index is monotonically
					 *  increasing, regardless of removals
					 */
					public static final String TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
				}
				
				public static class Payload {
					
					public static final String NAME = "payload";
					
					public static final String TYPE = " TEXT";
				}
				
			}
		}
	}
	
	public class SharedPreferences {
		public static final String SESSION_ID_KEY = "session.id";
		public static final String USER_ID_KEY = "user.id";
		public static final String GROUP_ID_KEY = "group.id";
	}
	
	public class Permission {

		public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
		public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
		public static final String FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
		public static final String COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
		public static final String INTERNET = "android.permission.INTERNET";
		public static final String ACCESS_NETWORK_STATE = "android.permission.ACCESS_NETWORK_STATE";
		
	}
	
}
