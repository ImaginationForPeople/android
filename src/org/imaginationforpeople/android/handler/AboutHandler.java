package org.imaginationforpeople.android.handler;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.activity.HomepageActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class AboutHandler extends BaseHandler implements OnCancelListener, OnClickListener {
	private HomepageActivity activity;
	
	private ProgressDialog progress;
	
	public AboutHandler(HomepageActivity a, ProgressDialog p) {
		activity = a;
		progress = p;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {
		progress.setMessage(activity.getResources().getText(R.string.projectslist_api_verify));
		progress.show();
	}
	
	@Override
	protected void onSuccess(int arg, Object obj) {
		if((Boolean) obj) {
			progress.dismiss();
			AlertDialog alert = new AlertDialog.Builder(activity).create();
			alert.setTitle(R.string.error_server);
			alert.setMessage(activity.getResources().getString(R.string.error_api));
			alert.setOnCancelListener(this);
			alert.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getResources().getText(R.string.close), this);
			alert.show();
		} else {
			activity.loadProjects();
		}
	}
	
	@Override
	protected void onError(int arg, Object obj) {
		progress.dismiss();
		
		AlertDialog alert = new AlertDialog.Builder(activity).create();
		switch(arg) {
		case ERROR_TIMEOUT:
			alert.setTitle(R.string.error_server);
			alert.setMessage(activity.getResources().getText(R.string.error_server_timeout));
			break;
		case ERROR_HTTP:
			alert.setTitle(R.string.error_server);
			alert.setMessage(activity.getResources().getText(R.string.error_server_badanswer));
			break;
		case ERROR_JSON:
			alert.setTitle(R.string.error_json);
			alert.setMessage(activity.getResources().getText(R.string.error_json_badanswer));
			break;
		case ERROR_UNKNOWN:
			alert.setTitle(R.string.error_unknown);
			alert.setMessage(activity.getResources().getText(R.string.error_unknown_message));
			break;
		}
		alert.setOnCancelListener(this);
		alert.setButton(DialogInterface.BUTTON_NEUTRAL, activity.getResources().getText(R.string.close), this);
		alert.show();
	}

	public void onClick(DialogInterface arg0, int arg1) {
		activity.finish();
	}

	public void onCancel(DialogInterface arg0) {
		activity.finish();
	}
}
