package nl.yzaazy.contactslist;

import android.os.Parcel;
import android.os.Parcelable;

class Person implements Parcelable {
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    String firstName;
    String lastName;
    String profilePicture;

    Person() {
    }

    protected Person(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        profilePicture = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(profilePicture);

    }
}
