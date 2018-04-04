package pt.uturista.prspy.model;

import android.os.Parcel;
import android.os.Parcelable;

import pt.uturista.bf2.LayoutJson;


public class Team implements Parcelable {
    private final String mCode;
    private final String mName;
    private final int mTickets;


    public Team(LayoutJson.Team team) {
        mCode = team.Code;
        mName = team.Name;
        mTickets = team.Tickets;
    }

    protected Team(Parcel in) {
        mCode = in.readString();
        mName = in.readString();
        mTickets = in.readInt();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeString(mName);
        dest.writeInt(mTickets);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getCode() {
        return mCode;
    }
}