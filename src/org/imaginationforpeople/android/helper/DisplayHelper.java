package org.imaginationforpeople.android.helper;

public class DisplayHelper extends BaseHelper {
	private final static float scale = getResources().getDisplayMetrics().density;
	
	private DisplayHelper() {}
	
	public static int dpToPx(int dp) {
		return (int) (dp * scale + 0.5f);
	}
}
