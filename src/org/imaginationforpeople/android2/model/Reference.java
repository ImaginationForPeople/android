package org.imaginationforpeople.android2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reference implements Parcelable {
	private int id;
	private String desc;

	public Reference() {}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(desc);
	}

	public static final Parcelable.Creator<Reference> CREATOR = new Parcelable.Creator<Reference>() {
		@Override
		public Reference createFromParcel(Parcel source) {
			return new Reference(source);
		}

		@Override
		public Reference[] newArray(int size) {
			return new Reference[size];
		}
	};

	private Reference(Parcel source) {
		id = source.readInt();
		desc = source.readString();
	}
}
