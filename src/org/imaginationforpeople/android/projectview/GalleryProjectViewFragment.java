package org.imaginationforpeople.android.projectview;

import org.imaginationforpeople.android.R;
import org.imaginationforpeople.android.adapter.ProjectGalleryGridAdapter;
import org.imaginationforpeople.android.helper.DataHelper;
import org.imaginationforpeople.android.model.I4pProjectTranslation;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GalleryProjectViewFragment extends Fragment {
	private I4pProjectTranslation project;
	
	@TargetApi(13)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		project = getArguments().getParcelable(DataHelper.PROJECT_VIEW_KEY);
		GridView layout = (GridView) inflater.inflate(R.layout.projectview_gallery, null);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int size;
		int factor;
		switch(display.getRotation()) {
		case Surface.ROTATION_90:
		case Surface.ROTATION_270:
			factor = 3;
			break;
		case Surface.ROTATION_0:
		case Surface.ROTATION_180:
		default:
			factor = 2;
			break;
		}
		if(Build.VERSION.SDK_INT >= 13) {
			Point screen = new Point();
			display.getSize(screen);
			size = (screen.x / factor) - 2;
		} else
			size = (display.getWidth() / factor) - 2;
		
		layout.setColumnWidth(size);
		layout.setVerticalSpacing(2);
		ProjectGalleryGridAdapter adapter = new ProjectGalleryGridAdapter(getActivity(), project.getProject(), size);
		layout.setAdapter(adapter);
		
		return layout;
	}
}
