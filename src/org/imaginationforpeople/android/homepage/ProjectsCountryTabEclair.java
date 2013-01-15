package org.imaginationforpeople.android.homepage;

import org.imaginationforpeople.android.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class ProjectsCountryTabEclair extends ProjectsTabEclair {
	private boolean isUpdated;
	
	public ProjectsCountryTabEclair(Activity a, boolean updated) {
		super(a);
		isUpdated = updated;
	}
	
	@Override
	public void display() {
		LinearLayout content = (LinearLayout) activity.findViewById(R.id.homepage_content);
		LayoutInflater inflater = activity.getLayoutInflater();
		View contentView;
		if(adapter.getCount() == 0) {
			contentView = inflater.inflate(R.layout.loading, null);
			contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			TextView message = (TextView) contentView.findViewById(R.id.loading_text);
			if(isUpdated)
				message.setText(activity.getResources().getString(R.string.loading_projects));
			else
				message.setText(activity.getResources().getString(R.string.loading_location));
		} else {
			contentView = inflater.inflate(R.layout.projectslist, null); 
			GridView grid = (GridView) contentView.findViewById(android.R.id.list);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(this);
		}
		content.removeAllViews();
		content.addView(contentView);
	}
}
