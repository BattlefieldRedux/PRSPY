package pt.uturista.prspy.model;

import android.os.Parcel;
import android.os.Parcelable;

import pt.uturista.bf2.NewsJson;

public class News implements Parcelable {
    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    private static final String EVENT = "#999999";
    private static final String BLOG = "#3399ff";
    private final String text;
    private final String url;
    private final String color;

    public News(NewsJson json) {
        text = json.text;
        url = json.url;
        color = json.color;
    }

    protected News(Parcel in) {
        text = in.readString();
        url = in.readString();
        color = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(url);
        dest.writeString(color);
    }
}
