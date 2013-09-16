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

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
	}

	public static final Parcelable.Creator<Objective> CREATOR = new Parcelable.Creator<Objective>() {
		@Override
		public Objective createFromParcel(Parcel source) {
			return new Objective(source);
		}

		@Override
		public Objective[] newArray(int size) {
			return new Objective[size];
		}
	};

	private Objective(Parcel source) {
		name = source.readString();
	}
}
