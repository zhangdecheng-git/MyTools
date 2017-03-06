package tools.zhang.com.mytools.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class BirthDay implements Parcelable{

	public int year;
	public int mouth;

	public BirthDay() {}

	public BirthDay(int year, int mouth) {
		this.year = year;
		this.mouth = mouth;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeInt(year);
		dest.writeInt(mouth);
	}

	public static final Creator<BirthDay> CREATOR = new Creator<BirthDay>() {

		@Override
		public BirthDay[] newArray(int arg0) {
			return new BirthDay[arg0];
		}

		@Override
		public BirthDay createFromParcel(Parcel source) {
			BirthDay birthDay = new BirthDay();
			birthDay.year = source.readInt();
			birthDay.mouth = source.readInt();
			return birthDay;
		}
	};
}
