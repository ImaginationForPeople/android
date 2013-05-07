package org.imaginationforpeople.android2.adapter;

import java.util.List;

import org.imaginationforpeople.android2.R;
import org.imaginationforpeople.android2.model.User;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UsersGridAdapter extends BaseAdapter {
	private Activity activity;
	private List<User> users;
	
	public UsersGridAdapter(Activity a, List<User> u) {
		activity = a;
		users = u;
	}
	
	public int getCount() {
		return users.size();
	}

	public User getItem(int position) {
		return users.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.userslist_item, parent, false);
		
		User user = getItem(position);
		TextView userTitle = (TextView) convertView.findViewById(R.id.userslist_item_text);
		ImageView userImage = (ImageView) convertView.findViewById(R.id.userslist_item_image);
		ProgressBar userLoading = (ProgressBar) convertView.findViewById(R.id.userslist_item_loading);
		userTitle.setText(user.getFullname());
		userTitle.getBackground().setAlpha(127);
		if(!"".equals(user.getAvatarUrl())) {
			Drawable avatar = user.getAvatarDrawable();
			if(avatar == null) {
				userImage.setVisibility(View.GONE);
				userLoading.setVisibility(View.VISIBLE);
			} else {
				userImage.setImageDrawable(avatar);
				userLoading.setVisibility(View.GONE);
				userImage.setVisibility(View.VISIBLE);
			}
		} else {
			userLoading.setVisibility(View.GONE);
			userImage.setImageResource(R.drawable.user_nophoto);
			userImage.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
}
