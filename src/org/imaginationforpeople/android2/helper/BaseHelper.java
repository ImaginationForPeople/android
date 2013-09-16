package org.imaginationforpeople.android2.helper;

import android.content.Context;
import android.content.res.Resources;

public abstract class BaseHelper {
	private static Context context;

	public static void setContext(Context c) {
		context = c;
	}

	protected static Context getContext() {
		return context;
	}
	protected static Resources getResources() {
		return context.getResources();
	}
}
