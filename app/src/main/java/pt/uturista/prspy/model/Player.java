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
import android.support.annotation.NonNull;
import android.text.Html;

public class Player implements Comparable<Player>, Parcelable {
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    private final String Name;
    private final int Score;
    private final int Kills;
    private final int Deaths;
    @DataTypes.Teams
    private final int Team;
    private final int Pid;
    private final int Ping;
    private final String mServerID;
    private boolean friend = false;


    public Player(pt.uturista.bf2.Player json, String serverID) {
        Name = Html.fromHtml(json.Name).toString();
        Score = json.Score;
        Kills = json.Kills;
        Deaths = json.Deaths;
        Team = DataTypes.getTeam(json.Team);
        Pid = json.Pid;
        Ping = json.Ping;
        mServerID = serverID;
    }

    protected Player(Parcel in) {
        Name = in.readString();
        Score = in.readInt();
        Kills = in.readInt();
        Deaths = in.readInt();
        Team = DataTypes.getTeam(in.readInt());
        Pid = in.readInt();
        Ping = in.readInt();
        friend = in.readByte() != 0;
        mServerID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeInt(Score);
        dest.writeInt(Kills);
        dest.writeInt(Deaths);
        dest.writeInt(Team);
        dest.writeInt(Pid);
        dest.writeInt(Ping);
        dest.writeByte((byte) (friend ? 1 : 0));
        dest.writeString(mServerID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull Player another) {
        boolean i = this.getScore() > another.getScore();
        return i ? 1 : 0;
    }

    public String getName() {
        return Name;
    }


    public int getScore() {
        return Score;
    }


    public int getKills() {
        return Kills;
    }


    public int getDeaths() {
        return Deaths;
    }

    @DataTypes.Teams
    public int getTeam() {
        return Team;
    }


    public int getPid() {
        return Pid;
    }


    public int getPing() {
        return Ping;
    }


    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean bool) {
        friend = bool;
    }

    public String getServerID() {
        return mServerID;
    }

}
