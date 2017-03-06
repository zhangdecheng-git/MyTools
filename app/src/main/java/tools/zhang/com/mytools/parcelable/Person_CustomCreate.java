package tools.zhang.com.mytools.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zhangdecheng on 2017/2/19.
 *
 * 使用插件自动生成，（android parcelable code ）
 *
 * 按下Alt+Insert，选择Palcelable，
 */
public class Person_CustomCreate implements Parcelable {
    public Person_CustomCreate(){}

    public Person_CustomCreate(String name, int age){
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
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeByte(this.isMan ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.address);
        dest.writeStringArray(this.extra);
        dest.writeParcelable(this.birthDay, flags);
        dest.writeTypedList(this.family);
        dest.writeTypedArray(this.groups, flags);
    }

    protected Person_CustomCreate(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.isMan = in.readByte() != 0;
        this.address = in.createStringArrayList();
        this.extra = in.createStringArray();
        this.birthDay = in.readParcelable(BirthDay.class.getClassLoader());
        this.family = in.createTypedArrayList(Person.CREATOR);
        this.groups = in.createTypedArray(Person.CREATOR);
    }

    public static final Parcelable.Creator<Person_CustomCreate> CREATOR = new Parcelable.Creator<Person_CustomCreate>() {
        @Override
        public Person_CustomCreate createFromParcel(Parcel source) {
            return new Person_CustomCreate(source);
        }

        @Override
        public Person_CustomCreate[] newArray(int size) {
            return new Person_CustomCreate[size];
        }
    };
}
