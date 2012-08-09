package org.imaginationforpeople.android.handler;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.ArrayAdapter;

public class ProjectsListHandler extends BaseHandler implements OnClickListener, OnCancelListener {
	private Activity activity;
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
	private ProgressDialog progress;
	
	public ProjectsListHandler(Activity ac, ArrayAdapter<I4pProjectTranslation> ad) {
		activity = ac;
		adapter = ad;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {
		progress = new ProgressDialog(activity);
		progress.setMessage(activity.getResources().getText(R.string.projectslist_loading));
		progress.setOnCancelListener(this);
		progress.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getResources().getText(R.string.cancel), this);
		progress.show();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccess(int arg, Object obj) {
		List<I4pProjectTranslation> projects = (List<I4pProjectTranslation>) obj;
		for(I4pProjectTranslation project : projects) {
			adapter.add(project);
		}
		
		progress.dismiss();
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
