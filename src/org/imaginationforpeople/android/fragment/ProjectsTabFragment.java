package org.imaginationforpeople.android.fragment;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(11)
public class ProjectsTabFragment extends Fragment {
	private ArrayAdapter<I4pProjectTranslation> adapter;
	
	public void setAdapter(ArrayAdapter<I4pProjectTranslation> a) {
		adapter = a;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.projectslist, container, false);
		ListView projectsList = (ListView) view.findViewById(R.id.projectslist);
		projectsList.setAdapter(adapter);
		return view;
	}

}
