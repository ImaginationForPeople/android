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
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(desc);
	}
	
	public static final Parcelable.Creator<Reference> CREATOR = new Parcelable.Creator<Reference>() {
		public Reference createFromParcel(Parcel source) {
			return new Reference(source);
		}
		
		public Reference[] newArray(int size) {
			return new Reference[size];
		}
	};
	
	private Reference(Parcel source) {
		id = source.readInt();
		desc = source.readString();
	}
}
