package org.imaginationforpeople.android2.thread;

import java.util.ArrayList;
import java.util.List;

import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.I4pProjectTranslation;
import org.imaginationforpeople.android2.model.Picture;

import android.graphics.Bitmap;

public class ProjectsListImagesThread extends BaseListImageThread {
	private ListImageHandler handler;
	private List<I4pProjectTranslation> projects;
	
	public ProjectsListImagesThread(ListImageHandler h, List<I4pProjectTranslation> p) {
		handler = h;
		projects = p;
	}

	@Override
	public void run() {
		for(I4pProjectTranslation project : projects) {
			if(project.getProject().getPictures().size() > 0 && !DataHelper.checkThumbFile(project.getProject().getPictures().get(0).getThumbUrl())) {
				Bitmap bitmap = downloadBitmap(project.getProject().getPictures().get(0).getThumbUrl());
				if(stop)
					return;
				
				if(bitmap != null)
					project.getProject().getPictures().get(0).setThumbBitmap(bitmap);
				else
					// Unable to load bitmap after 5 tries so deleting images from the project
					project.getProject().setPictures(new ArrayList<Picture>());
				
				handler.sendEmptyMessage(0);
			}
			if(stop)
				return;
		}
	}
}
