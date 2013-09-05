package org.imaginationforpeople.android2.fragment;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.helper.ErrorHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ErrorFragment extends Fragment {
	public final static String ERROR_CODE = "ERROR_CODE";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.error_black, container, false);
		TextView text1 = (TextView) view.findViewById(R.id.error_text1);
		TextView text2 = (TextView) view.findViewById(R.id.error_text2);
		switch(getArguments().getInt(ERROR_CODE)) {
		case ErrorHelper.ERROR_TIMEOUT:
			text1.setText(R.string.error_server);
			text2.setText(R.string.error_server_timeout);
			break;
		case ErrorHelper.ERROR_HTTP:
			text1.setText(R.string.error_server);
			text2.setText(R.string.error_server_badanswer);
			break;
		case ErrorHelper.ERROR_JSON:
			text1.setText(R.string.error_json);
			text2.setText(R.string.error_json_badanswer);
			break;
		case ErrorHelper.ERROR_UNKNOWN:
			text1.setText(R.string.error_unknown);
			text2.setText(R.string.error_unknown_message);
			break;
		case ErrorHelper.ERROR_LOCATION:
			text1.setText(R.string.error_location);
			text2.setText(R.string.error_location_message);
			break;
		}
		return view;
	}
}
