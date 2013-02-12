package org.imaginationforpeople.android2.handler;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.HomepageActivity;
import org.imaginationforpeople.android2.adapter.ProjectsGridAdapter;
import org.imaginationforpeople.android2.homepage.SpinnerHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class ProjectsListHandler extends BaseHandler implements OnClickListener, OnCancelListener {
	public final static int BEST_PROJECTS = 1001;
	public final static int LATEST_PROJECTS = 1002;
	
	private HomepageActivity activity;
	private SpinnerHelper helper;
	private ProjectsGridAdapter adapter;
	
	public ProjectsListHandler(HomepageActivity ac, SpinnerHelper h, ProjectsGridAdapter a) {
		activity = ac;
		helper = h;
		adapter = a;
	}
	
	@Override
	protected void onStart(int arg, Object obj) {
		adapter.clearProjects();
	}
	
	@Override
	protected void onSpecificEvent(int arg, Object obj) {
		switch(arg) {
		case SPECIFIC_UPDATE:
			activity.updateContent();
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSuccess(int arg, Object obj) {
		List<I4pProjectTranslation> projects = (List<I4pProjectTranslation>) obj;
		for(I4pProjectTranslation project : projects) {
			adapter.addProject(project);
		}
		
		helper.displayContent(adapter);
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
		case ERROR_LOCATION:
			alert.setTitle(R.string.error_location);
			alert.setMessage(activity.getResources().getText(R.string.error_location_message));
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
