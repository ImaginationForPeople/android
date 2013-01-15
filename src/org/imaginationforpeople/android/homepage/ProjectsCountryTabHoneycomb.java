package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class ProjectsCountryTabHoneycomb extends ProjectsTabHoneycomb {
	private boolean isUpdated;
	
	public ProjectsCountryTabHoneycomb(boolean updated) {
		isUpdated = updated;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(adapter == null)
			return null;
		
		View view;
		if(adapter.getCount() == 0) {
			view = inflater.inflate(R.layout.loading, container, false);
			TextView message = (TextView) view.findViewById(R.id.loading_text);
			if(isUpdated)
				message.setText(getResources().getString(R.string.loading_projects));
			else
				message.setText(getResources().getString(R.string.loading_location));
		} else {
			view = inflater.inflate(R.layout.projectslist, container, false);
			GridView grid = (GridView) view.findViewById(android.R.id.list);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(this);
		}
		return view;
	}
}
