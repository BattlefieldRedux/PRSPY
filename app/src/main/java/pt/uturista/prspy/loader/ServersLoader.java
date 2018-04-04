package pt.uturista.prspy.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.uturista.bf2.ServerDataJson;
import pt.uturista.prspy.PRSPY;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.model.ServerData;
import pt.uturista.prspy.utils.Net;


public class ServersLoader extends AsyncTaskLoader<ServerData> {
    private static final String TAG = "ServersLoader";
    private static final URL PRSPY_URL;
    private static final URL PRSPY_URL_FALLBACK;

    private final Set<String> mFriends;
    private final Set<String> mFavouriteServers;
    private ServerData mData;

    public ServersLoader(Context context) {
        super(context);
        mFriends = context.getSharedPreferences(PRSPY.FRIENDS_PREFERENCES, Context.MODE_PRIVATE).getStringSet(PRSPY.FRIENDS_SET, new HashSet<String>());
        mFavouriteServers = context.getSharedPreferences(PRSPY.FAVOURITE_SERVERS_PREFERENCES, Context.MODE_PRIVATE).getStringSet(PRSPY.FAVOURITE_SERVERS_SET, new HashSet<String>());
    }

    @Override
    public ServerData loadInBackground() {

        if(Net.isInternetAvailable(getContext())){

            Net.DownloadResult<ServerDataJson> result = Net.with(getContext()).download(ServerDataJson.class, PRSPY_URL, PRSPY_URL_FALLBACK);

            if(result.Error != null){
                // TODO:
                return  null;
            }

            ServerDataJson serversJSON = result.Result;

            ServerData data = new ServerData(serversJSON);

            Server[] servers = data.getServers();
            for (Server server : servers) {
                // For each player in the server check if its a friend
                for (Player p : server.getPlayers()) {
                    if (mFriends.contains(p.getName()))
                        p.setFriend(true);
                }
            }

            return data;
        }

        return null;
    }

    /* *********************************
     *        Loader specific handlers
     * ********************************* */


    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it;
     */
    @Override
    public void deliverResult(ServerData response) {
        mData = response;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(response);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mData != null) {
            mData = null;
        }
    }

    /* *********************************
     *        Ugly but needed
     * ********************************* */

    static {
        try {
            PRSPY_URL = new URL("https://www.realitymod.com/prspy/json/serverdata.json");
            PRSPY_URL_FALLBACK = new URL("https://projects.uturista.pt/prspy/json/serverdata.json");
        } catch (Exception ex) {
            throw new AssertionError("Should not reach here");
        }

    }
}