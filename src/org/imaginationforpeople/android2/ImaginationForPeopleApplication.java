package org.imaginationforpeople.android2;

import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.LanguageHelper;

import android.app.Application;

public class ImaginationForPeopleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		LanguageHelper.setContext(this);
		DataHelper.removeOldFiles();
	}
}
