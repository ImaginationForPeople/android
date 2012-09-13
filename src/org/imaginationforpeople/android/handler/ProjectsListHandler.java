package org.imaginationforpeople.android.handler;

import java.util.List;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class ProjectsListHandler extends BaseHandler implements OnClickListener, OnCancelListener {
	public final static int BEST_PROJECTS = 1001;
	public final static int LATEST_PROJECTS = 1002;
	
	private int count = 0;
	
	private Activity activity;
	private ProgressDialog progress;
	private ProjectsGridAdapter bestAdapter;
	private ProjectsGridAdapter latestAdapter;
	
	public ProjectsListHandler(Activity ac, ProgressDialog pd, ProjectsGridAdapter ba, ProjectsGridAdapter la) {
		activity = ac;
		progress = pd;
		bestAdapter = ba;
		latestAdapter = la;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {
		progress.setMessage(activity.getResources().getText(R.string.projectslist_loading));
		if(!progress.isShowing())
			progress.show();
		
		switch(arg) {
		case BEST_PROJECTS:
			bestAdapter.clearProjects();
			break;
		case LATEST_PROJECTS:
			latestAdapter.clearProjects();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccess(int arg, Object obj) {
		List<I4pProjectTranslation> projects = (List<I4pProjectTranslation>) obj;
		ProjectsGridAdapter adapter = null;
		switch(arg) {
		case BEST_PROJECTS:
			adapter = bestAdapter;
			break;
		case LATEST_PROJECTS:
			adapter = latestAdapter;
		}
		for(I4pProjectTranslation project : projects) {
			adapter.addProject(project);
		}
		
		if(++count >= 2)
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
