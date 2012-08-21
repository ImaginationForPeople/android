package org.imaginationforpeople.android.helper;

import android.content.res.Resources;

public abstract class BaseHelper {
	protected static Resources resources;

	public static void setResources(Resources r) {
		resources = r;
	}
}
