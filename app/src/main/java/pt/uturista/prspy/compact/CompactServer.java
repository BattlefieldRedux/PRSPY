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

package pt.uturista.prspy.compact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pt.uturista.prspy.PRSPY;
import pt.uturista.prspy.loader.ServersLoader;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.model.ServerData;
import pt.uturista.prspy.utils.Net;


public class CompactServer implements LoaderManager.LoaderCallbacks<ServerData> {

    private static final String DATA_EXTRA = "DATA_EXTRA";
    private static final String TAG = "CompactServer";
    private static ServerData Data;

    private final AppCompatActivity mContext;
    private final Listener mListener;
    private Set<String> mFriends;

    public CompactServer(AppCompatActivity context, Listener listener) {
        if(Data == null)
            Log.d(TAG, "Creating compactServer - no data available");
        else
            Log.d(TAG, "Creating compactServer - "+Data.getServers().length+" servers available");
        mContext = context;
        mListener = listener;
    }

    /* *********************************
     *         Persistence Methods
     * ********************************* */

    public void onSaveInstanceState(Bundle outState) {
        if(outState != null)
            outState.putParcelable(DATA_EXTRA, Data);
    }


    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null)
            Data = savedInstanceState.getParcelable(DATA_EXTRA);

        if (Data != null){
            mListener.onDataAvailable();

        }else{
            mContext.getSupportLoaderManager().initLoader(0, null, this);
        }

        Log.d(TAG, "onCreate restore friends prefs");
        mFriends = mContext
                .getSharedPreferences(PRSPY.FRIENDS_PREFERENCES, Context.MODE_PRIVATE)
                .getStringSet(PRSPY.FRIENDS_SET, new HashSet<String>());

    }


    /* *********************************
     *         Handling data region
     * ********************************* */

    public void refresh(){
        if(Net.isInternetAvailable(mContext)){
            mContext.getSupportLoaderManager().restartLoader(0, null, this);
        }else {
            mListener.onDataAvailable();
        }
    }

    @Nullable
    public Server[] getServers() {
        return Data == null ? null : Data.getServers();
    }

    @Nullable
    public Date getTime() {
        return Data == null ? null : Data.getTime();
    }

    @Nullable
    public Server getServer(String id) {
        if (Data == null)
            return null;

        Server[] servers = Data.getServers();
        for (Server server : servers) {
            if (server.getID().equals(id))
                return server;
        }

        return null;
    }


    public void setPlayerFriendship(Player player, boolean newStatus) {
        Log.d(TAG, String.format("setPlayerFriendship(%s, %b)", player.getName(), newStatus));
        player.setFriend(newStatus);

        if (newStatus)
            mFriends.add(player.getName());
        else
            mFriends.remove(player.getName());

        // TODO: Change the amount of times this is called by hooking it with the onStop or onPause
        mContext
                .getSharedPreferences(PRSPY.FRIENDS_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .putStringSet(PRSPY.FRIENDS_SET, mFriends)
                .apply();
    }

    /* *********************************
     *          Loader Callbacks
     * ********************************* */

    @Override
    public Loader<ServerData> onCreateLoader(int id, Bundle args) {
        return new ServersLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<ServerData> loader, ServerData data) {
        Log.d(TAG, "onLoadFinished");
        if(data != null){
            Data = data;
        }

        mListener.onDataAvailable();
    }

    @Override
    public void onLoaderReset(Loader<ServerData> loader) {
        Log.d(TAG, "onLoaderReset");
    }


    /* *********************************
     *         Listening Interface
     * ********************************* */
    public interface Listener {
        void onDataAvailable();
    }
}
