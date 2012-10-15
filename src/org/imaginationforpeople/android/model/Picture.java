package org.imaginationforpeople.android.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.imaginationforpeople.android.helper.DataHelper;

import com.fasterxml.jackson.annotation.JsonProperty;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Picture implements Parcelable {
	private int id;
	private String author;
	private String created;
	private String desc;
	private String license;
	private String source;
	@JsonProperty("thumb")
	private String thumbUrl;
	private Bitmap thumbBitmap;
	@JsonProperty("url")
	private String imageUrl;
	private Bitmap imageBitmap;
	
	public Picture() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public Bitmap getThumbBitmap() {
		if(thumbBitmap == null) {
			String path;
			try {
				path = new URL(thumbUrl).getPath();
				FileInputStream file = DataHelper.openFileInput(DataHelper.FILE_PREFIX_PROJECT_THUMB + path.substring(path.lastIndexOf('/') + 1));
				thumbBitmap = BitmapFactory.decodeStream(file);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return thumbBitmap;
	}
	public void setThumbBitmap(Bitmap thumbBitmap) {
		try {
			String path = new URL(thumbUrl).getPath();
			FileOutputStream file = DataHelper.openFileOutput(DataHelper.FILE_PREFIX_PROJECT_THUMB + path.substring(path.lastIndexOf('/') + 1));
			thumbBitmap.compress(Bitmap.CompressFormat.PNG, 90, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.thumbBitmap = thumbBitmap;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Bitmap getImageBitmap() {
		if(imageBitmap == null) {
			String path;
			try {
				path = new URL(imageUrl).getPath();
				FileInputStream file = DataHelper.openFileInput(DataHelper.FILE_PREFIX_PROJECT_IMAGE + path.substring(path.lastIndexOf('/') + 1));
				imageBitmap = BitmapFactory.decodeStream(file);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return imageBitmap;
	}
	public void setImageBitmap(Bitmap imageBitmap) {
		try {
			String path = new URL(imageUrl).getPath();
			FileOutputStream file = DataHelper.openFileOutput(DataHelper.FILE_PREFIX_PROJECT_IMAGE + path.substring(path.lastIndexOf('/') + 1));
			imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.imageBitmap = imageBitmap;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeStringArray(new String[] {
			author,
			created,
			desc,
			license,
			source,
			thumbUrl,
			imageUrl
		});
	}
	
	public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
		public Picture createFromParcel(Parcel source) {
			return new Picture(source);
		}
		
		public Picture[] newArray(int size) {
			return new Picture[size];
		}
	};
	
	private Picture(Parcel in) {
		id = in.readInt();
		String[] stringData = new String[7];
		in.readStringArray(stringData);
		author = stringData[0];
		created = stringData[1];
		desc = stringData[2];
		license = stringData[3];
		source = stringData[4];
		thumbUrl = stringData[5];
		imageUrl = stringData[6];
	}
}
