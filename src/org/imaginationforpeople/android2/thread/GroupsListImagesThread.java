package org.imaginationforpeople.android2.thread;

import java.util.List;

import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.Group;

import android.graphics.Bitmap;

public class GroupsListImagesThread extends BaseListImageThread {
	private final ListImageHandler handler;
	private final List<Group> groups;

	public GroupsListImagesThread(ListImageHandler h, List<Group> p) {
		handler = h;
		groups = p;
	}

	@Override
	public void run() {
		for(Group group : groups) {
			if(group.getThumbUrl() != null && !DataHelper.checkGroupThumbFile(group.getThumbUrl())) {
				Bitmap bitmap = downloadBitmap(group.getThumbUrl());
				if(stop)
					return;

				if(bitmap != null)
					group.setThumbBitmap(bitmap);
				else
					// Unable to load bitmap after 5 tries so deleting Thumb from the group
					group.setThumbUrl("");

				handler.sendEmptyMessage(0);
			}
			if(stop)
				return;
		}
	}
}
