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

public class Vector3 implements Parcelable {
    private final float mX;
    private final float mY;
    private final float mZ;

    public Vector3(LayoutJson.Vector3 vector) {
        mX = vector.X;
        mY = vector.Y;
        mZ = vector.Z;
    }

    protected Vector3(Parcel in) {
        mX = in.readFloat();
        mY = in.readFloat();
        mZ = in.readFloat();
    }

    public static final Creator<Vector3> CREATOR = new Creator<Vector3>() {
        @Override
        public Vector3 createFromParcel(Parcel in) {
            return new Vector3(in);
        }

        @Override
        public Vector3[] newArray(int size) {
            return new Vector3[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mX);
        dest.writeFloat(mY);
        dest.writeFloat(mZ);
    }
}