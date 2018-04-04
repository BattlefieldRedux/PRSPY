package pt.uturista.flags;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Country implements Parcelable{
    public final String CODE;
    public final String CODE_2;
    public final String CODE_3;
    public final String DOMAIN;
    @StringRes
    public final int NAME;
    @DrawableRes
    public final int FLAG;
    public final int CONTINENT;

    public Country(String code, String code2, String code3, String domain, @StringRes int name, @DrawableRes int flag, int continent) {
        this.CODE = code;
        this.CODE_2 = code2;
        this.CODE_3 = code3;
        this.NAME = name;
        this.DOMAIN = domain;
        this.FLAG = flag;
        this.CONTINENT = continent;
    }

    protected Country(Parcel in) {
        CODE = in.readString();
        CODE_2 = in.readString();
        CODE_3 = in.readString();
        DOMAIN = in.readString();
        NAME = in.readInt();
        FLAG = in.readInt();
        CONTINENT = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(CODE);
        parcel.writeString(CODE_2);
        parcel.writeString(CODE_3);
        parcel.writeString(DOMAIN);
        parcel.writeInt(NAME);
        parcel.writeInt(FLAG);
        parcel.writeInt(CONTINENT);
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

}
