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
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pt.uturista.bf2.ServerDataJson;


public class ServerData implements Parcelable {
    private static final String TAG = "ServerData";
    // We actually want the date format to end with X instead of Z but the 'X' pattern
    // is only available for the API 24+ and we're targeting 15+
    // With 'Z' pattern we'll need to modified the data before parsing it
    private static final SimpleDateFormat DATA_FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSZ", Locale.US);

    private final Date Time;
    private final Server[] Data;


    /* *********************************
     *         Getters
     * ********************************* */
    public Date getTime() {
        return Time;
    }

    public Server[] getServers() {
        return Data;
    }



    /* *********************************
     *         Constructor
     * ********************************* */

    public ServerData(ServerDataJson json) {
        Date tm;
        try {
            // We cannot use the 'X' pattern that would allow to parse the 'Z' char
            // We use the 'Z' pattern (confusing yes I know) but to do so we need to change
            // the input. 'Z' char means its GMT timezone, so we replace the char 'Z 'that the 'X'
            // pattern expects with '+0000' that is what the 'Z' pattern expects
            final String formattedTime = json.Time.replace("Z", "+0000");

            // Parsing the formatted time with the format
            tm = DATA_FORMAT.parse(formattedTime);
            Log.d(TAG, "Date parsed correctly");

        } catch (ParseException e) {

            Log.e(TAG, e.toString());
            tm = new Date();
        }
        Time = tm;

        Data = new Server[json.Data.length];


        for (int i = 0, length = json.Data.length; i < length; i++) {
            try {
                Data[i] = new Server(json.Data[i]);
            } catch (Exception ex) {
                //Quietly skips
            }
        }
    }





    /* *********************************
     *         Parcelable Region
     * ********************************* */

    private ServerData(Parcel in) {
        Data = in.createTypedArray(Server.CREATOR);
        Time = new Date(in.readLong());
    }

    public static final Creator<ServerData> CREATOR = new Creator<ServerData>() {
        @Override
        public ServerData createFromParcel(Parcel in) {
            return new ServerData(in);
        }

        @Override
        public ServerData[] newArray(int size) {
            return new ServerData[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(Data, flags);
        dest.writeLong(Time.getTime());
    }
}
