package org.imaginationforpeople.android2.homepage;

import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public abstract class SpinnerHelper implements OnClickListener {
	public final static String STATE_KEY = "selected";
	
	public interface OnSpinnerItemSelectedListener {
		public void onSpinnerItemSelected(int itemId);
	}
	
	protected OnSpinnerItemSelectedListener listener;
	
	public void setListener(OnSpinnerItemSelectedListener l) {
		listener = l;
	}
	
	public abstract void init();
	public abstract void saveCurrentSelection(Bundle outState);
	public abstract void restoreCurrentSelection(int position);
	public abstract void displayCurrentContent();
	public abstract int getCurrentSelection();
}
