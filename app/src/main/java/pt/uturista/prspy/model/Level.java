package pt.uturista.prspy.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.uturista.bf2.LevelJson;

public class Level implements Parcelable {
    private final String mName;
    private final String mKey;
    private final int mResolution;
    private final int mSize;
    private final int mColor;
    private final LayoutIndex[] mLayoutsIndex;

    /* =================================
     *            Constructors
     * ================================= */
    public Level(LevelJson levelJson) {
        mName = levelJson.Name;
        mKey = levelJson.Name;
        mResolution = levelJson.Resolution;
        mSize = levelJson.Size;
        mColor = Color.parseColor(levelJson.Color);

        final LayoutIndex[] layouts = new LayoutIndex[levelJson.Layouts.length];
        for (int i = 0; i < levelJson.Layouts.length; i++) {

            layouts[i] = new LayoutIndex(levelJson.Layouts[i].Key, levelJson.Layouts[i].Value);
        }
        mLayoutsIndex = layouts;
    }

    public Level(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
        mResolution = in.readInt();
        mSize = in.readInt();
        mColor = in.readInt();

        // We now handle the deserialization of our LayoutIndex<string, int>[]
        // first we read the length
        // followed by the string and int of each index
        int length = in.readInt();
        LayoutIndex[] layouts = new LayoutIndex[length];

        for (int i = 0; i < length; i++) {
            String key = in.readString();
            int value = in.readInt();
            layouts[i] = new LayoutIndex(key, value);
        }

        mLayoutsIndex = layouts;
    }

    /* =================================
     *            Getters
     * ================================= */

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    public String getWebKey() {
        return mName.replaceAll("\\s|_", "").toLowerCase();
    }

    public int getResolution() {
        return mResolution;
    }

    public int getSize() {
        return mSize;
    }

    public int getColor() {
        return mColor;
    }

    public LayoutIndex[] getLayouts() {
        return mLayoutsIndex;
    }


    /* =================================
     *            Serialization
     * ================================= */

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mKey);
        dest.writeInt(mResolution);
        dest.writeInt(mSize);
        dest.writeInt(mColor);

        // We now handle the serialization of our LayoutIndex<string, int>[]
        // first we write the length
        // followed by the string and int of each index
        dest.writeInt(mLayoutsIndex.length);
        for (LayoutIndex layout : mLayoutsIndex) {
            dest.writeString(layout.getGameMode());
            dest.writeInt(layout.getGameLayer());
        }
    }

    public static class LayoutIndex {
        private final String mGamemode;
        private final int mGameLayer;

        LayoutIndex(String key, int value) {
            mGamemode = key;
            mGameLayer = value;
        }


        @DataTypes.GameLayers
        public int getGameLayer() {
            return mGameLayer;
        }

        @DataTypes.GameModes
        public String getGameMode() {
            return mGamemode;
        }
    }
}
