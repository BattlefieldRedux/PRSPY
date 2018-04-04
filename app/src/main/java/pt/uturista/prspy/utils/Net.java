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

package pt.uturista.prspy.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;


public class Net {
    private static final String TAG = "Net";
    private static final int TIMEOUT = 1000;
    private final Context mContext;

    private Net(Context context) {
        mContext = context;
    }

    public static Net with(Context context) {
        return new Net(context);
    }

    @RequiresPermission(Manifest.permission.INTERNET)
    public <T> DownloadResult<T> download(Type type, URL... urls) {
        DownloadResult<T> result = null;

        for (URL url : urls) {
            result = doDownload(type, url);

            // If we have a good result return it, else keep trying
            if (result.Error == null && result.Result != null)
                return result;

        }

        return result;
    }


    private <T> DownloadResult<T> doDownload(Type type, URL url) {
        T result = null;
        String error = null;

        // Even with a cached version there might be a recent version available
        HttpURLConnection connection = null;
        try {
            // Prepare the connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);

            if (Net.isInternetAvailable(mContext)) {
                connection.setConnectTimeout(TIMEOUT);
            }else{

                connection.addRequestProperty("Cache-Control", "only-if-cached");
            }
            connection.connect();

            // Server as new stuff
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                result = gson.fromJson(new JsonReader(new InputStreamReader(connection.getInputStream())), type);

                // Close the input stream - Extremely necessary as HttpResponseCache will not
                // cache the request if its open (https://stackoverflow.com/a/47771100)
                connection.getInputStream().close();
            } else {
                error = "Expected 200 from server got " + connection.getResponseCode();
            }
        } catch (Exception e) {
            error = e.toString();

        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }

        return new DownloadResult<>(result, error);
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static class DownloadResult<T> {
        public final T Result;
        public final String Error;

        DownloadResult(T result, String error) {
            Result = result;
            Error = error;
        }
    }
}