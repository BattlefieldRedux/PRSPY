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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.uturista.bf2.LayoutJson;

public class Layout implements Parcelable {
    private final String mMode;
    private final int mSize;
    private final Team[] mTeam;
    private final Asset[] mAssets;
    // public ControlPoint[] ControlPoints;
    // public CombatArea[] CombatAreas;

    public Layout(LayoutJson json, LayoutJson.Info[] vehicles) {
        mMode = json.Mode;
        mSize = json.Size;

        mTeam = new Team[json.Team.length];
        for (int i = 0; i < json.Team.length; i++) {
            mTeam[i] = new Team(json.Team[i]);
        }


        Map<String, LayoutJson.Info> vehicleMap = new HashMap<>();
        for (LayoutJson.Info vehicle : vehicles) {
            vehicleMap.put(vehicle.Key, vehicle);
        }

        ArrayList<Asset> assetList = new ArrayList<>();
        for (int i = 0; i < json.Assets.length; i++) {
            LayoutJson.Info info = vehicleMap.get(json.Assets[i].Key);

            if (info != null) {
                assetList.add(new Asset(json.Assets[i], info));
            }
        }

        mAssets = new Asset[assetList.size()];
        assetList.toArray(mAssets);
    }

    private Layout(Parcel in) {
        mMode = in.readString();
        mSize = in.readInt();
        mTeam = in.createTypedArray(Team.CREATOR);
        mAssets = in.createTypedArray(Asset.CREATOR);
    }


    public boolean is(String mode, int layer) {
        return mode.equals(mMode) && layer == mSize;
    }

    public String getGameMode() {
        return mMode;
    }

    public int getGameLayer() {
        return mSize;
    }

    public Asset[] getAssets(@DataTypes.Teams int team) {
        team = team == DataTypes.Opfor ? 1 : 0;

        ArrayList<Asset> assets = new ArrayList<>();
        for (Asset asset : mAssets) {
            if (asset.getTeam() == team)
                assets.add(asset);
        }

        Asset[] assets1 = new Asset[assets.size()];
        assets.toArray(assets1);

        return assets1;
    }

    public Team getTeam(@DataTypes.Teams int team) {
        if (team == DataTypes.Opfor)
            return mTeam[1];

        return mTeam[0];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMode);
        dest.writeInt(mSize);
        dest.writeTypedArray(mTeam, flags);
        dest.writeTypedArray(mAssets, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Layout> CREATOR = new Creator<Layout>() {
        @Override
        public Layout createFromParcel(Parcel in) {
            return new Layout(in);
        }

        @Override
        public Layout[] newArray(int size) {
            return new Layout[size];
        }
    };
}
