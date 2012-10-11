package org.imaginationforpeople.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
	private int id;
	private String address;
	private String country;
	
	public Location() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeStringArray(new String[] {
			address,
			country
		});
	}
	
	public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
		public Location createFromParcel(Parcel source) {
			return new Location(source);
		}
		
		public Location[] newArray(int size) {
			return new Location[size];
		}
	};
	
	private Location(Parcel source) {
		id = source.readInt();
		String[] stringData = new String[2];
		source.readStringArray(stringData);
		address = stringData[0];
		country = stringData[1];
	}
}
