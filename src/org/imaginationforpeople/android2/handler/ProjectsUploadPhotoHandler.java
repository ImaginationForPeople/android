package org.imaginationforpeople.android2.handler;

import org.apache.http.HttpResponse;
import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.UploadPhotoActivity;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class ProjectsUploadPhotoHandler extends BaseHandler {
	private final UploadPhotoActivity activity;

	public ProjectsUploadPhotoHandler(UploadPhotoActivity a) {
		activity = a;
	}

	@Override
	protected void onSuccess(int arg, Object obj) {
		HttpResponse response = (HttpResponse) obj;
		if(response.getStatusLine().getStatusCode() == 201) {
			activity.setResult(Activity.RESULT_OK);
			activity.finish();
		} else {
			Log.e("error", "Server doesn't return '201 CREATED'");
			Log.e("return", (String) obj);
			onError(arg, obj);
		}
	}

	@Override
	protected void onError(int arg, Object obj) {
		activity.freeBackButton();
		activity.setContentView(R.layout.error_white);
		TextView error1 = (TextView) activity.findViewById(R.id.error_text1);
		TextView error2 = (TextView) activity.findViewById(R.id.error_text2);
		error1.setText(R.string.error_unknown);
		error2.setText(R.string.error_unknown_message);
	}

	// We do not use onStart with this handler
	@Override
	protected void onStart(int arg, Object obj) {}

	// No specific event with this handler
	@Override
	protected void onSpecificEvent(int arg, Object obj) {}
}
