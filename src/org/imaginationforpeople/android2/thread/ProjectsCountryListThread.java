package org.imaginationforpeople.android2.thread;

import java.io.IOException;
import java.util.List;

import org.imaginationforpeople.android2.handler.BaseHandler;
import org.imaginationforpeople.android2.handler.ProjectsListHandler;
import org.imaginationforpeople.android2.helper.UriHelper;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;

public class ProjectsCountryListThread extends ProjectsListThread implements LocationListener {
	private Location location;
	private LocationManager manager;
	private Geocoder geocoder;
	
	public ProjectsCountryListThread(ProjectsListHandler h, int l, LocationManager m, Geocoder g) {
		super(h, l);
		manager = m;
		geocoder = g;
	}
	
	@Override
	protected void onStart() throws IOException {
		List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
		requestUri = UriHelper.getCurrentCountryProjectsListUri(addresses.get(0));
		Message msg = new Message();
		msg.arg1 = BaseHandler.STATUS_SPECIFIC;
		msg.arg2 = BaseHandler.SPECIFIC_UPDATE;
		handler.sendMessage(msg);
	}
	
	public void onLocationChanged(Location loc) {
		manager.removeUpdates(this);
		location = loc;
		start();                                          
	}
	
	public void onProviderDisabled(String provider) {}
	public void onProviderEnabled(String provider) {}
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
