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

package pt.uturista.prspy.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import pt.uturista.bf2.LayoutJson;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Layout;
import pt.uturista.prspy.utils.Net;


public class LayoutLoader extends AsyncTaskLoader<Layout> {


    private static final String TAG = "LayoutLoader";
    private static LayoutJson.Info[] vehicles;

    private Layout mData;
    private String mGameMode;
    private int mGameLayer;
    private String mLevelName;

    public LayoutLoader(Context context, String mapName, @DataTypes.GameModes String gamemode, @DataTypes.GameLayers int layer) {
        super(context);
        mGameMode = gamemode;
        mGameLayer = layer;
        mLevelName = mapName;
    }

    @Override
    public Layout loadInBackground() {
        Log.d(TAG, "loadInBackground");

        try {

            // Lets download the vehicle data info
            if (vehicles == null) {
                Log.d(TAG, "Downloading vehicles info");
                URL url = new URL("https://www.realitymod.com/mapgallery/json/vehicles.json");
                Net.DownloadResult<LayoutJson.Info[]> result = Net.with(getContext()).download(LayoutJson.Info[].class, url);

                if (result.Error != null) {
                    // TODO: something bad happen
                    Log.e(TAG, "Unable to download the vehicles info: " + result.Error);
                    return null;
                }
                vehicles = result.Result;
            }else{
                Log.d(TAG, "Using cached vehicles info");
            }

            Log.d(TAG, String.format(Locale.US, "Layout URL: https://www.realitymod.com/mapgallery/json/%s/%s_%d.json", mLevelName, mGameMode, mGameLayer));
            URL url = new URL(String.format(Locale.US, "https://www.realitymod.com/mapgallery/json/%s/%s_%d.json", mLevelName, mGameMode, mGameLayer));
            Net.DownloadResult<LayoutJson> result = Net.with(getContext()).download(LayoutJson.class, url);
            if (result.Error != null) {
                // TODO: something bad happen
                Log.e(TAG, "Error downloading the layout: " + result.Error);
                return null;
            }

            LayoutJson galleryJson = result.Result;
            mData = new Layout(galleryJson, vehicles);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return mData;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");

        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }






    /* *********************************
     *        Ugly but needed
     * ********************************* */
   /* static {
        try {
            GALLERY_URL = new URL("https://www.realitymod.com/mapgallery/json/levels.json");
        } catch (MalformedURLException e) {
            throw new AssertionError("Should not reach here");
        }
    }*/
}
