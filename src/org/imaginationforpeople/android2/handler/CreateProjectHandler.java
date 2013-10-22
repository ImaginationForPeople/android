package org.imaginationforpeople.android2.handler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.activity.ProjectViewActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

public class CreateProjectHandler extends BaseHandler {
	private final Activity activity;

	public CreateProjectHandler(Activity a) {
		activity = a;
	}

	@Override
	protected void onSuccess(int arg, Object obj) {
		HttpResponse response = (HttpResponse) obj;
		if(response.getStatusLine().getStatusCode() == 201) {
			Header header = response.getFirstHeader("Location");
			if(header != null) {
				Uri projectUri = Uri.parse(header.getValue());
				Intent intent = new Intent(activity, ProjectViewActivity.class);
				intent.setData(projectUri);
				activity.startActivity(intent);
				activity.finish();
			} else {
				Log.e("error", "Server doesn't return a Location header");
				onError(arg, obj);
			}
		} else {
			StatusLine status = response.getStatusLine();
			Log.e("error", "Server doesn't return '201 CREATED'");
			Log.e("return", status.getStatusCode() + " " + status.getReasonPhrase());
			onError(arg, obj);
		}
	}

	@Override
	protected void onError(int arg, Object obj) {
		activity.setContentView(R.layout.error_white);
		TextView error1 = (TextView) activity.findViewById(R.id.error_text1);
		TextView error2 = (TextView) activity.findViewById(R.id.error_text2);
		error1.setText(R.string.error_unknown);
		error2.setText(R.string.error_unknown_message);
	}

	@Override
	protected void onStart(int arg, Object obj) {}

	@Override
	protected void onSpecificEvent(int arg, Object obj) {}
}
