package org.imaginationforpeople.android2.thread;

import java.util.List;

import org.imaginationforpeople.android2.handler.ListImageHandler;
import org.imaginationforpeople.android2.helper.DataHelper;
import org.imaginationforpeople.android2.model.User;

import android.graphics.drawable.Drawable;

public class UsersListImagesThread extends BaseListImageThread {
	private ListImageHandler handler;
	private List<User> users;
	
	public UsersListImagesThread(ListImageHandler h, List<User> u) {
		handler = h;
		users = u;
	}

	@Override
	public void run() {
		for(User user : users) {
			if(!"".equals(user.getAvatarUrl()) && !DataHelper.checkAvatarFile(user.getAvatarUrl())) {
				Drawable drawable = downloadDrawable(user.getAvatarUrl());
				if(stop)
					return;
				
				if(drawable != null)
					user.setAvatarDrawable(drawable);
				else
					// Unable to load drawable after 5 tries so deleting avatar
				
				handler.sendEmptyMessage(0);
			}
			if(stop)
				return;
		}
	}
}
