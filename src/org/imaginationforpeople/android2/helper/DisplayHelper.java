package org.imaginationforpeople.android2.helper;

public class DisplayHelper extends BaseHelper {
	private final static float scale = getResources().getDisplayMetrics().density;
	
	private DisplayHelper() {}
	
	public static int dpToPx(int dp) {
		return (int) (dp * scale + 0.5f);
	}
}
