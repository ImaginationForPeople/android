package org.imaginationforpeople.android2.handler;

import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

public class ListImageHandler extends Handler {
	private final BaseAdapter adapter;

	public ListImageHandler(BaseAdapter a) {
		adapter = a;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);

		adapter.notifyDataSetChanged();
	}

}
