package org.imaginationforpeople.android.handler;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.activity.ProjectViewActivity;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;


public class ProjectViewHandler extends BaseHandler implements OnClickListener, OnCancelListener {
	private ProjectViewActivity activity;
	
	public ProjectViewHandler(ProjectViewActivity a) {
		activity = a;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {}
	
	@Override
	protected void onSuccess(int arg, Object obj) {
		I4pProjectTranslation project = (I4pProjectTranslation) obj;
		activity.setProject(project);
		activity.displayProject();
	}
	
	@Override
	protected void onError(int arg, Object obj) {
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
	
	@Override
	protected void onSpecificEvent(int arg, Object obj) {}
}
