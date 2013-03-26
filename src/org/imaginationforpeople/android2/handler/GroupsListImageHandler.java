package org.imaginationforpeople.android2.handler;

import org.imaginationforpeople.android2.adapter.GroupsGridAdapter;

import android.os.Handler;
import android.os.Message;

public class GroupsListImageHandler extends Handler {
	private GroupsGridAdapter adapter;
	
	public GroupsListImageHandler(GroupsGridAdapter a) {
		adapter = a;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		adapter.notifyDataSetChanged();
	}
}
