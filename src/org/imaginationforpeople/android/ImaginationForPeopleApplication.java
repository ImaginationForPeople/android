package org.imaginationforpeople.android;

import org.imaginationforpeople.android.helper.LanguageHelper;

import android.app.Application;

public class ImaginationForPeopleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		LanguageHelper.setContext(this);
	}
}
