package tools.zhang.com.mytools.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangdecheng
 *
 *	1.序列化的时候，读写的成员变量顺序一致.
 *
 *	2.与Serializable的不同
 *	Parcelable的性能比Serializable好，在内存开销方面较小，所以在内存间数据传输时推荐使用Parcelable，如activity间传输数据，
 *	而Serializable可将数据持久化方便保存，所以在需要保存或网络传输数据时选择Serializable。
 *
 */
public class Person implements Parcelable {

	public Person(){}
	public Person(String name, int age){
		this.name = name;
		this.age = age;
	}

	public String name;
	public int age;
	public boolean isMan;
	public List<String> address;
	public String[] extra;
	public BirthDay birthDay;
	public List<Person> family;
	public Person[] groups;

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(age);
		dest.writeInt(isMan ? 1 : 0);
		dest.writeStringArray(extra);
		dest.writeStringList(address);

		// 普通的Parcelable
		dest.writeParcelable(birthDay, flags);

		// Person列表
		if (family != null && family.size() > 0) {
			dest.writeInt(family.size());
			dest.writeTypedList(family);
		} else {
			dest.writeInt(0);
		}

		// Person数组
		if (groups != null && groups.length > 0) {
			dest.writeInt(groups.length);
			dest.writeTypedArray(groups, flags);
		} else {
			dest.writeInt(0);
		}
	}

	public static final Creator<Person> CREATOR = new Creator<Person>() {

		@Override
		public Person[] newArray(int arg0) {
			return new Person[arg0];
		}

		@Override
		public Person createFromParcel(Parcel source) {
			Person person = new Person();
			person.name = source.readString();
			person.age = source.readInt();
			person.isMan = source.readInt() == 1 ? true : false;
			person.extra = source.createStringArray();
			person.address = source.createStringArrayList();

			// 普通的Parcelable
			person.birthDay = source.readParcelable(BirthDay.class.getClassLoader());

			// Person列表
			int familySize = source.readInt();
			if (familySize > 0) {
				person.family = new ArrayList<Person>(familySize);
				source.readTypedList(person.family, Person.CREATOR);
			}

			// Person数组
			int len = source.readInt();
			if (len > 0) {
				person.groups = new Person[len];
				source.readTypedArray(person.groups, Person.CREATOR);
			}

			return person;
		}
	};
}
