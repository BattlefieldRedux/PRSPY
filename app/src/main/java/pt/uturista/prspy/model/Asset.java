/*
 * Copyright (c) 2018 uturista.pt
 *
 * Licensed under the Attribution-NonCommercial 4.0 International (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.uturista.prspy.model;

import android.os.Parcel;
import android.os.Parcelable;

import pt.uturista.bf2.LayoutJson;


public class Asset implements Parcelable {
    private final String Key;
    private final int InitialDelay;
    private final int RespawnDelay;
    private final int Team;
    private final Vector3 Position;
    private final Vector3 Rotation;
    private final String mIcon;
    private final String mName;

    /* =================================
     *            Constructors
     * ================================= */
    public Asset(LayoutJson.Asset asset, LayoutJson.Info info) {
        Key = asset.Key;
        InitialDelay = asset.InitialDelay;
        RespawnDelay = asset.RespawnDelay;
        Team = asset.Team;
        Position = new Vector3(asset.Position);
        Rotation = new Vector3(asset.Rotation);
        mIcon = info.Icon;
        mName = info.Name;
    }

    protected Asset(Parcel in) {
        Key = in.readString();
        InitialDelay = in.readInt();
        RespawnDelay = in.readInt();
        Team = in.readInt();
        Position = in.readParcelable(Vector3.class.getClassLoader());
        Rotation = in.readParcelable(Vector3.class.getClassLoader());
        mIcon = in.readString();
        mName =in.readString();
    }


    /* =================================
     *            Getters
     * ================================= */

    public int getTeam() {
        return Team;
    }

    public String getKey() {
        return Key;
    }

    public int getInitialDelay() {
        return InitialDelay;
    }

    public int getRespawnDelay() {
        return RespawnDelay;
    }

    public String getName() {
        return mName;
    }

    public String getIcon() {
        return mIcon;
    }

    /* =================================
    *            Serialization
    * ================================= */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Key);
        dest.writeInt(InitialDelay);
        dest.writeInt(RespawnDelay);
        dest.writeInt(Team);
        dest.writeParcelable(Position, flags);
        dest.writeParcelable(Rotation, flags);
        dest.writeString(mIcon);
        dest.writeString(mName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Asset> CREATOR = new Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };



    /* =================================
     *            Serialization
     * ================================= */



}