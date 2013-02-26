package org.imaginationforpeople.android2.handler;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.SearchActivity;
import org.imaginationforpeople.android2.helper.ErrorHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class SearchHandler extends BaseHandler implements OnCancelListener, OnClickListener {
	private SearchActivity activity;
	
	public SearchHandler(SearchActivity ac) {
		activity = ac;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {}
	
	@Override
	protected void onSuccess(int arg, Object obj) {
		activity.displayResults();
	}
	
	@Override
	protected void onError(int arg, Object obj) {
		AlertDialog alert = new AlertDialog.Builder(activity).create();
		switch(arg) {
		case ErrorHelper.ERROR_TIMEOUT:
			alert.setTitle(R.string.error_server);
			alert.setMessage(activity.getResources().getText(R.string.error_server_timeout));
			break;
		case ErrorHelper.ERROR_HTTP:
			alert.setTitle(R.string.error_server);
			alert.setMessage(activity.getResources().getText(R.string.error_server_badanswer));
			break;
		case ErrorHelper.ERROR_JSON:
			alert.setTitle(R.string.error_json);
			alert.setMessage(activity.getResources().getText(R.string.error_json_badanswer));
			break;
		case ErrorHelper.ERROR_UNKNOWN:
			alert.setTitle(R.string.error_unknown);
			alert.setMessage(activity.getResources().getText(R.string.error_unknown_message));
			break;
		}
		alert.setOnCancelListener(this);
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getResources().getText(R.string.close), this);
		alert.show();
	}
	
	public void onCancel(DialogInterface arg0) {
		activity.finish();
	}

	public void onClick(DialogInterface arg0, int arg1) {
		activity.finish();
	}

    @Override
    protected void onSpecificEvent(int arg, Object obj) {}
}
