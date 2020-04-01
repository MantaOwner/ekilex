package eki.ekilex.constant;

public interface SystemConstant {

	int MAX_RESULTS_LIMIT = 50;

	int DEFAULT_OFFSET = 0;

	long UPDATE_STAT_DATA_DELAY = 10 * 60 * 1000;

	long CACHE_EVICT_DELAY_5MIN = 5 * 60 * 1000;

	long CACHE_EVICT_DELAY_60MIN = 60 * 60 * 1000;

	String DELETE_FLOATING_DATA_TIME_4_AM = "0 0 4 * * *";

	String CLASSIF_LABEL_LANG_EST = "est";

	String CLASSIF_LABEL_TYPE_DESCRIP = "descrip";

	String CLASSIF_LABEL_TYPE_FULL = "full";

	String CLASSIF_LABEL_TYPE_ISO2 = "iso2";

	String CACHE_KEY_CLASSIF = "classif";

	String CACHE_KEY_DATASET = "dataset";

	String CACHE_KEY_USER = "user";

}
