package org.imaginationforpeople.android2.fragment;

import java.util.ArrayList;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.handler.GroupsListHandler;
import org.imaginationforpeople.android2.handler.ProjectsListHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.helper.ErrorHelper;
import org.imaginationforpeople.android2.thread.BaseGetJson;
import org.imaginationforpeople.android2.thread.GroupsListThread;
import org.imaginationforpeople.android2.thread.ProjectsCountryListThread;
import org.imaginationforpeople.android2.thread.ProjectsListThread;

import android.content.Context;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class LoadingFragment extends SherlockFragment {
	public interface UpdateLoadingScreenListener {
		void updateLoadingScreen();
	}
	public interface OnContentLoadedListener {
		void onContentLoaded(int contentType, Bundle bundle);
	}
	public interface OnLoadErrorListener {
		void onLoadError(int errorCode);
	}
	public static final String TEXT_RESID = "TEXT_RESID";
	public static final String CONTENT_TO_LOAD = "CONTENT_TO_LOAD";

	public static final int NOTHING_TO_LOAD = -1;
	public static final int LOAD_BESTOF_PROJECTS = 0;
	public static final int LOAD_LATEST_PROJECTS = 1;
	public static final int LOAD_MYCOUNTRY_PROJECTS = 2;
	public static final int LOAD_GROUPS = 3;

	private BaseGetJson thread;
	private LocationManager mLocationManager;
	private int contentToLoad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.loading, container, false);
		TextView text = (TextView) view.findViewById(R.id.loading_text);
		text.setText(getArguments().getInt(TEXT_RESID));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		contentToLoad = getArguments().getInt(CONTENT_TO_LOAD);
		ProjectsListHandler handler = new ProjectsListHandler(contentToLoad, updateListener, (OnContentLoadedListener) getActivity(), errorListener);
		switch(contentToLoad) {
		case LOAD_MYCOUNTRY_PROJECTS:
			ArrayList<String> providers = new ArrayList<String>();
			if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				providers.add(LocationManager.NETWORK_PROVIDER);
			if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
				providers.add(LocationManager.GPS_PROVIDER);

			if(providers.size() == 0) {
				Message msg = new Message();
				msg.arg1 = BaseHandler.STATUS_ERROR;
				msg.arg2 = ErrorHelper.ERROR_LOCATION;
				handler.sendMessage(msg);
			}

			Geocoder mGeocoder = new Geocoder(getActivity());
			thread = new ProjectsCountryListThread(handler, contentToLoad, mLocationManager, mGeocoder);

			for(String provider : providers)
				mLocationManager.requestLocationUpdates(provider, 0, 0, (LocationListener) thread);
			break;
		case LOAD_BESTOF_PROJECTS:
		case LOAD_LATEST_PROJECTS:
			thread = new ProjectsListThread(handler, contentToLoad);
			thread.start();
			break;
		case LOAD_GROUPS:
			GroupsListHandler groupHandler = new GroupsListHandler((OnContentLoadedListener) getActivity(), errorListener);
			thread = new GroupsListThread(groupHandler);
			thread.start();
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if(thread != null && contentToLoad == DataHelper.CONTENT_COUNTRY)
			mLocationManager.removeUpdates((LocationListener) thread);
	}

	@Override
	public void onStop() {
		if(thread != null && thread.isAlive())
			thread.requestStop();
		super.onStop();
	}

	private final UpdateLoadingScreenListener updateListener = new UpdateLoadingScreenListener() {
		@Override
		public void updateLoadingScreen() {
			TextView text = (TextView) getView().findViewById(R.id.loading_text);
			text.setText(R.string.loading_projects);
		}
	};

	private final OnLoadErrorListener errorListener = new OnLoadErrorListener() {
		@Override
		public void onLoadError(int errorCode) {
			Bundle data = new Bundle();
			data.putInt(ErrorFragment.ERROR_CODE, errorCode);
			SherlockFragment fragment = new ErrorFragment();
			fragment.setArguments(data);
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.homepage_content, fragment).commit();
		}
	};
}
