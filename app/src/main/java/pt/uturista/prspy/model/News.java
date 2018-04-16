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

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import pt.uturista.prspy.R;

public class News implements Parcelable {
    public final static int EVENT = 1;
    public final static int TOURNAMENT = 2;
    public final static int OFFICIAL = 3;
    public static final int BLOG = 4;
    public static final int HIGHTLIGHTS = 5;


    private final String mTitle;
    private final String mContent;
    private final int mType;
    private final Date mDate;
    private final String mAuthor;

    public News(SyndEntry entry, @NewsTypes int type) {
        mTitle = entry.getTitle();
        mContent = entry.getContents().get(0).getValue();
        mDate = entry.getPublishedDate();
        mAuthor = entry.getAuthor();
        mType = type;
    }

    @Override
    public String toString() {
        return mType + " " + mTitle + " " + mContent;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getContent() {
        return mContent;
    }
    public Date getPublishedDate() {
        return mDate;
    }

    /* *********************************
     *         Parcelable Region
     * ********************************* */

    protected News(Parcel in) {
        mTitle = in.readString();
        mContent = in.readString();
        mDate = new Date(in.readLong());
        mAuthor = in.readString();
        mType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mContent);
        dest.writeLong(mDate.getTime());
        dest.writeString(mAuthor);
        dest.writeInt(mType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public String getAuthor() {
        return mAuthor;
    }

    public int getType() {
        return mType;
    }

    public String getLocalizedType(Context context) {
        switch (getType()) {
            case News.EVENT:
                return context.getString(R.string.news_type_event);

            case News.TOURNAMENT:
                return context.getString(R.string.news_type_tournament);

            case News.BLOG:
                return context.getString(R.string.news_type_blog);

            case News.HIGHTLIGHTS:
                return context.getString(R.string.news_type_highlights);

            default:
            case News.OFFICIAL:
                return context.getString(R.string.news_type_official);
        }
    }

    /* *********************************
     *         Fake Enum Region
     * ********************************* */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EVENT, TOURNAMENT, OFFICIAL,BLOG})
    public @interface NewsTypes {
    }
}
