package org.imaginationforpeople.android2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
	private String answer;
	private String question;
	
	public Question() {}
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {
			answer,
			question
		});
	}
	
	public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
		public Question createFromParcel(Parcel source) {
			return new Question(source);
		}
		
		public Question[] newArray(int size) {
			return new Question[size];
		}
	};
	
	private Question(Parcel source) {
		String[] stringData = new String[2];
		source.readStringArray(stringData);
		answer = stringData[0];
		question = stringData[1];
	}
}
