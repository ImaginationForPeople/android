package org.imaginationforpeople.android2.homepage;

import android.content.DialogInterface;
import android.os.Bundle;

public class SpinnerHelperEclair extends SpinnerHelper {
	private int selectedContent = 0;

	@Override
	public void init() {
		listener.onSpinnerItemSelected(0);
	}

	@Override
	public void saveCurrentSelection(Bundle outState) {
		outState.putInt(STATE_KEY, selectedContent);
	}

	@Override
	public void restoreCurrentSelection(int position) {
		selectedContent = position;
		listener.onSpinnerItemSelected(position);
	}
	
	@Override
	public void displayCurrentContent() {
		listener.onSpinnerItemSelected(selectedContent);
	}
	
	@Override
	public int getCurrentSelection() {
		return selectedContent;
	}

	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		selectedContent = which;
		listener.onSpinnerItemSelected(which);
	}
}
