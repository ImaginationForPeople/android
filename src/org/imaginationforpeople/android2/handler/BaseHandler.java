package org.imaginationforpeople.android2.handler;

import android.os.Handler;
import android.os.Message;

public abstract class BaseHandler extends Handler {
	public final static int STATUS_START = 1;
	public final static int STATUS_SPECIFIC = 4;
	public final static int STATUS_SUCCESS = 2;
	public final static int STATUS_ERROR = 3;
	
	public final static int SPECIFIC_UPDATE = 20;
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		switch(msg.arg1) {
		case STATUS_START:
			onStart(msg.arg2, msg.obj);
			break;
		case STATUS_SPECIFIC:
			onSpecificEvent(msg.arg2, msg.obj);
			break;
		case STATUS_SUCCESS:
			onSuccess(msg.arg2, msg.obj);
			break;
		case STATUS_ERROR:
			onError(msg.arg2, msg.obj);
			break;
		}
	}
	
	abstract protected void onStart(int arg, Object obj);
	abstract protected void onSpecificEvent(int arg, Object obj);
	abstract protected void onSuccess(int arg, Object obj);
	abstract protected void onError(int arg, Object obj);
}
