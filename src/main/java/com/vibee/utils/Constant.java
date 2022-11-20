package com.vibee.utils;

public class Constant {
	public final static String GLOBAL_INCOMPLETE_QUEUE = "com.mbbank.queue.incomplete";
	public final static String USER_SYSTEM = "SYSTEM";
	public static final int DEFAULT_PAGE_SIZE_MAX = 10;
	// Redis
	public static final String ACCESS_TOKEN_KEY = "access_token";

	public static class Email {
		public static final String TPL_REPORT_ERROR = "TPL_REPORT_ERROR";
		public static final String TPL_BEAUTY_ACC_SUSPEND = "TPL_BEAUTY_ACC_SUSPEND";
	}

	public static class Sdf {
		public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		public static final String DEFAULT_TIME_ZONE = "GMT+7";
	}

	public static class Job {
		public final static String STATUS_SUCCESS = "S";
		public final static String RESULT_SUCCESS = "Success";
	}

	public static class Scope {
		public final static String OPN = "SMARTCHANNEL.OPN";
	}

	public static class RightTraditional {
		public final static String INFO = "TRADITIONAL";
//		public final static String EXPORT = "TRADITIONAL_EXPORT";
		public final static String CREATE = "TRADITIONAL_CREATE";
//		public final static String PRINT = "TRADITIONAL_PRINT";
//		public final static String VIEW = "TRADITIONAL_VIEW";
//		public final static String COPY = "TRADITIONAL_COPY";
		public final static String EDIT = "TRADITIONAL_EDIT";
		public final static String APPROVE = "TRADITIONAL_APPROVE";
//		public final static String REJECT = "TRADITIONAL_REJECT";
//		public final static String CANCEL = "TRADITIONAL_CANCEL";
//		public final static String DONE = "TRADITIONAL_DONE";
		public final static String SEARCH = "TRADITIONAL_SEARCH";
	}

	public static class RightCorpCustomer {
		public final static String EDIT = "CORP_PROCESS_EDIT";
		public final static String CANCEL = "CORP_PROCESS_CANCEL";
		public final static String APPROVE = "CORP_PROCESS_APPROVE";
//		public final static String TEMPLATE_PRINT = "CORP_PROCESS_TEMPLATE_PRINT";
//		public final static String INFO_CHANGE = "CORP_PROCESS_INFO_CHANGE";
//		public final static String VIEW = "CORP_PROCESS_VIEW";
//		public final static String LOAN = "CORP_PROCESS_LOAN";
		public final static String PROCESS_UPDATE = "CORP_PROCESS_UPDATE";
		public final static String RECALL = "CORP_PROCESS_RECALL";
		public final static String CREATE = "CORP_CUSTOMER_CREATE";
		public final static String ACCOUNT_EDIT = "CORP_PROCESS_ACCOUNT_EDIT";
		public final static String ACCOUNT_VIEW = "CORP_PROCESS_ACCOUNT_VIEW";
		public final static String ACCOUNT_CLOSE = "CORP_PROCESS_ACCOUNT_CLOSE";
		public final static String ACCOUNT_CREATE = "CORP_PROCESS_ACCOUNT_CREATE";
		public final static String ACCOUNT_REMOVE = "CORP_PROCESS_ACCOUNT_REMOVE";
		public final static String CARD_CREATE = "CORP_PROCESS_CARD_CREATE";
		public final static String CARD_EDIT = "CORP_PROCESS_CARD_EDIT";
//		public final static String CARD_VIEW = "CORP_PROCESS_CARD_VIEW";
//		public final static String CARD_DELETE = "CORP_PROCESS_CARD_DELETE";
		public final static String SURPLUS = "CORP_PROCESS_SURPLUS";
		public final static String SURPLUS_CREATE = "CORP_PROCESS_SURPLUS_CREATE";
		public final static String SURPLUS_VIEW = "CORP_PROCESS_SURPLUS_VIEW";
		public final static String SURPLUS_EDIT = "CORP_PROCESS_SURPLUS_EDIT";
		public final static String SURPLUS_DELETE = "CORP_PROCESS_SURPLUS_DELETE";
//		public final static String E_BANKING = "CORP_PROCESS_E_BANKING";
		public final static String E_BANKING_CREATE = "CORP_PROCESS_E_BANKING_CREATE";
		public final static String E_BANKING_EDIT = "CORP_PROCESS_E_BANKING_EDIT";
//		public final static String E_BANKING_ADD_USER = "CORP_PROCESS_E_BANKING_ADD_USER";
		public final static String E_BANKING_VIEW = "CORP_PROCESS_E_BANKING_VIEW";
//		public final static String E_BANKING_CANCEL = "CORP_PROCESS_E_BANKING_CANCEL";
//		public final static String MANAGER = "CORP_PROCESS_MANAGER";
//		public final static String MANAGER_EXPORT = "CORP_PROCESS_MANAGER_EXPORT";
		public final static String MANAGER_VIEW = "CORP_PROCESS_MANAGER_VIEW";
		public final static String MANAGER_SEARCH = "CORP_PROCESS_MANAGER_SEARCH";
//		public final static String UPLOAD = "CORP_PROFILE_UPLOAD";
		public final static String UPLOAD_OVERVIEW = "CORP_PROFILE_UPLOAD_OVERVIEW";
		public final static String UPLOAD_VIEW = "CORP_PROFILE_UPLOAD_VIEW";
		public final static String UPLOAD_UPDATE = "CORP_PROFILE_UPLOAD_UPDATE";
//		public final static String UPLOAD_DELETE = "CORP_PROFILE_UPLOAD_DELETE";
//		public final static String UPLOAD_SEARCH = "CORP_PROFILE_UPLOAD_SEARCH";
		public final static String UPLOAD_UPLOAD = "CORP_PROFILE_UPLOAD_UPLOAD";
		public final static String UPLOAD_CREATE = "CORP_PROFILE_UPLOAD_CREATE";
		public final static String UPLOAD_UNLOCK = "CORP_PROFILE_UPLOAD_UNLOCK";
		public final static String UPLOAD_LOCK = "CORP_PROFILE_UPLOAD_LOCK";
		public final static String UPLOAD_CANCEL = "CORP_PROFILE_UPLOAD_CANCEL";
		public final static String REJECT = "CORP_PROCESS_REJECT";
		
	}

	public static class RightCustomer {
		public final static String VIEW = "PERSONAL_PROCESS_VIEW";
		public final static String EDIT = "PERSONAL_PROCESS_EDIT";
		public final static String CANCEL = "PERSONAL_PROCESS_CANCEL";
		public final static String APPROVE = "PERSONAL_PROCESS_APPROVE";
		public final static String LOAN = "PERSONAL_PROCESS_LOAN";
		public final static String REJECT = "PERSONAL_PROCESS_REJECT";
		public final static String ACCOUNT_EDIT = "PERSONAL_PROCESS_ACCOUNT_EDIT";
		public final static String ACCOUNT_VIEW = "PERSONAL_PROCESS_ACCOUNT_VIEW";
		public final static String ACCOUNT_CLOSE = "PERSONAL_PROCESS_ACCOUNT_CLOSE";
		public final static String ACCOUNT_CREATE = "PERSONAL_PROCESS_ACCOUNT_CREATE";
		public final static String ACCOUNT_REMOVE = "PERSONAL_PROCESS_ACCOUNT_REMOVE";
		public final static String CARD_CREATE = "PERSONAL_PROCESS_CARD_CREATE";
		public final static String CARD_EDIT = "PERSONAL_PROCESS_CARD_EDIT";
//		public final static String CARD_VIEW = "PERSONAL_PROCESS_CARD_VIEW";
//		public final static String CARD_DELETE = "PERSONAL_PROCESS_CARD_DELETE";
		public final static String SURPLUS = "PERSONAL_PROCESS_SURPLUS";
		public final static String SURPLUS_CREATE = "PERSONAL_PROCESS_SURPLUS_CREATE";
		public final static String SURPLUS_VIEW = "PERSONAL_PROCESS_SURPLUS_VIEW";
		public final static String SURPLUS_EDIT = "PERSONAL_PROCESS_SURPLUS_EDIT";
		public final static String SURPLUS_DELETE = "PERSONAL_PROCESS_SURPLUS_DELETE";
//		public final static String GUARDIAN = "PERSONAL_PROCESS_GUARDIAN";
		public final static String GUARDIAN_CREATE = "PERSONAL_PROCESS_GUARDIAN_CREATE";
//		public final static String GUARDIAN_VIEW = "PERSONAL_PROCESS_GUARDIAN_VIEW";
		public final static String GUARDIAN_EDIT = "PERSONAL_PROCESS_GUARDIAN_EDIT";
//		public final static String GUARDIAN_DELETE = "PERSONAL_PROCESS_GUARDIAN_DELETE";
//		public final static String E_BANKING = "PERSONAL_PROCESS_E_BANKING";
		public final static String E_BANKING_CREATE = "PERSONAL_PROCESS_E_BANKING_CREATE";
		public final static String E_BANKING_EDIT = "PERSONAL_PROCESS_E_BANKING_EDIT";
		public final static String E_BANKING_VIEW = "PERSONAL_PROCESS_E_BANKING_VIEW";
//		public final static String E_BANKING_CANCEL = "PERSONAL_PROCESS_E_BANKING_CANCEL";
//		public final static String E_BANKING_AUTHENTICATION = "PERSONAL_PROCESS_E_BANKING_AUTHENTICATION_METHOD";
//		public final static String E_BANKING_CANCEL_AUTHENTICATION = "PERSONAL_PROCESS_E_BANKING_CANCEL_AUTHENTICATION_METHOD";
//		public final static String E_BANKING_DEVICES = "PERSONAL_PROCESS_E_BANKING_DEVICE_LIST";
//		public final static String E_BANKING_ACTIVE = "PERSONAL_PROCESS_E_BANKING_ACTIVE_DEVICE";
//		public final static String E_BANKING_UNLOCK = "PERSONAL_PROCESS_E_BANKING_USER_BANKING_UNLOCK";
//		public final static String E_BANKING_STATUS = "PERSONAL_PROCESS_E_BANKING_USER_BANKING_STATUS";
//		public final static String E_BANKING_RESET_PASSWORD = "PERSONAL_PROCESS_E_BANKING_RESET_PASSWORD";
//		public final static String E_BANKING_RESET_D_OTP = "PERSONAL_PROCESS_E_BANKING_RESET_D_OTP";
		public final static String MANAGER = "PERSONAL_PROCESS_MANAGER";
//		public final static String MANAGER_EXPORT = "PERSONAL_PROCESS_MANAGER_EXPORT";
		public final static String MANAGER_VIEW = "PERSONAL_PROCESS_MANAGER_VIEW";
		public final static String MANAGER_SEARCH = "PERSONAL_PROCESS_MANAGER_SEARCH";
		public final static String CREATE = "PERSONAL_CUSTOMER_CREATE";
		public final static String UPDATE = "PERSONAL_PROCESS_UPDATE";
//		public final static String UPLOAD = "PERSONAL_PROFILE_UPLOAD";
		public final static String UPLOAD_OVERVIEW = "PERSONAL_PROFILE_UPLOAD_OVERVIEW";
		public final static String UPLOAD_VIEW = "PERSONAL_PROFILE_UPLOAD_VIEW";
		public final static String UPLOAD_UPDATE = "PERSONAL_PROFILE_UPLOAD_UPDATE";
//		public final static String UPLOAD_DELETE = "PERSONAL_PROFILE_UPLOAD_DELETE";
//		public final static String UPLOAD_SEARCH = "PERSONAL_PROFILE_UPLOAD_SEARCH";
		public final static String UPLOAD_UPLOAD = "PERSONAL_PROFILE_UPLOAD_UPLOAD";
//		public final static String UPLOAD_CREATE = "PERSONAL_PROFILE_UPLOAD_CREATE";
		public final static String UPLOAD_UNLOCK = "PERSONAL_PROFILE_UPLOAD_UNLOCK";
		public final static String UPLOAD_LOCK = "PERSONAL_PROFILE_UPLOAD_LOCK";
		public final static String UPLOAD_CANCEL = "PERSONAL_PROFILE_UPLOAD_CANCEL";
	}

	public static class RightBeautyAccount {
		public final static String UPDATE_INFO = "BEAUTY_ACCOUNT_UPDATE_INFO";
		public final static String UPDATE = "BEAUTY_ACCOUNT_UPDATE";
		public final static String APPROVE = "BEAUTY_ACCOUNT_APPROVE";
		public final static String APPROVES = "BEAUTY_ACCOUNT_APPROVE_LIST";
//		public final static String CORP_CREATE = "CORP_PROCESS_BEAUTY_ACCOUNT_CREATE";
//		public final static String PERSONAL_CREATE = "PERSONAL_PROCESS_BEAUTY_ACCOUNT_CREATE";
//		public final static String PREFERENTIAL = "PREFERENTIAL_BEAUTY_ACCOUNT";
		public final static String PREFERENTIAL_IMPORT = "PREFERENTIAL_BEAUTY_ACCOUNT_IMPORT";
		public final static String PREFERENTIAL_EXPORT = "PREFERENTIAL_BEAUTY_ACCOUNT_EXPORT";
		public final static String PREFERENTIAL_EDIT = "PREFERENTIAL_BEAUTY_ACCOUNT_EDIT";
		public final static String PREFERENTIAL_VIEW = "PREFERENTIAL_BEAUTY_ACCOUNT_VIEW";
		public final static String PREFERENTIAL_DELETE = "PREFERENTIAL_BEAUTY_ACCOUNT_DELETE";
		public final static String PREFERENTIAL_REJECT = "BEAUTY_ACCOUNT_REJECT";
		public final static String PREFERENTIAL_APPROVE  = "PREFERENTIAL_BEAUTY_ACCOUNT_APPROVE";

	}

	public static class Right {
//		public final static String DASHBOARD = "DASHBOARD";
//		public final static String EXPORT = "EXPORT_PROCESS";
//		public final static String MANAGER_USER = "MANAGER_USER";
//		public final static String CREATE_UPLOAD = "CORP_CUSTOMER_CREATE_UPLOAD";
		public final static String TEMPLATE_PRINT = "PERSONAL_PROCESS_TEMPLATE_PRINT";
//		public final static String INFO_CHANGE = "PERSONAL_PROCESS_INFO_CHANGE";
	}

	public static class RightO2o {
//		public final static String INFO = "O2O";
		public final static String EXPORT = "O2O_EXPORT";
		public final static String SEARCH = "O2O_SEARCH";
		public final static String CREATE = "O2O_CREATE";
//		public final static String PRINT = "O2O_PRINT";
		public final static String VIEW = "O2O_VIEW";
		public final static String COPY = "O2O_COPY";
//		public final static String EDIT = "O2O_EDIT";
		public final static String APPROVE = "O2O_APPROVE";
		public final static String REJECT = "O2O_REJECT";
		public final static String CANCEL = "O2O_CANCEL";
		public final static String DONE = "O2O_DONE";
	}
}
