package org.imaginationforpeople.android2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Objective implements Parcelable {
	private String name;
	
	public Objective() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
	}
	
	public static final Parcelable.Creator<Objective> CREATOR = new Parcelable.Creator<Objective>() {
		public Objective createFromParcel(Parcel source) {
			return new Objective(source);
		}
		
		public Objective[] newArray(int size) {
			return new Objective[size];
		}
	};
	
	private Objective(Parcel source) {
		name = source.readString();
	}
}
