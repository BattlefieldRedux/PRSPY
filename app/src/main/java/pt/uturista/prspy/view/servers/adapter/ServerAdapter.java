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

package pt.uturista.prspy.view.servers.adapter;


import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.PRLibrary;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.utils.filter.Filter;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    // Constants
    private static final String TAG = "ServerListViewAdapter";
    private final static int ROW_LAYOUT_ID = R.layout.server_browser_row;
    private final static int FOOTER_LAYOUT_ID = R.layout.server_browser_footer_row;
    private static final int ROW_FOOTER = 1;
    private static final int ROW_SERVER = 2;
    // Fields
    private Server[] mServers;
    private Server[] mDisplayedServers;
    private Filter<Server> mFilter;
    private Listener mListener;

    public ServerAdapter(Listener listener) {
        mDisplayedServers = mServers = new Server[0];
        mListener = listener;
    }


    public void filter(Filter<Server> filter) {
        Log.d(TAG, "filter");

        if (filter != null){
            mFilter = filter;
            List<Server> newCollection = new ArrayList<>(mServers.length);
            for (Server server : mServers) {
                Log.d(TAG, "Filtering: " + server.getServerName());
                if (mFilter.filter(server)) {
                    Log.d(TAG, ">True");
                    newCollection.add(server);
                }else {
                    Log.d(TAG, ">False");
                }
            }
            mDisplayedServers = new Server[newCollection.size()];
            newCollection.toArray(mDisplayedServers);

        }else{
            mDisplayedServers = mServers;
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = viewType == ROW_FOOTER ? FOOTER_LAYOUT_ID : ROW_LAYOUT_ID;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ViewHolder(v, viewType == ROW_FOOTER, mListener);
    }


    @Override
    public int getItemViewType(int position) {

        if (position < mDisplayedServers.length)
            return ROW_SERVER;

        return ROW_FOOTER;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(position < mDisplayedServers.length)
            holder.setData(mDisplayedServers[position]);
        else
            holder.setFooter(mDisplayedServers.length, mServers.length);

    }

    @Override
    public int getItemCount() {
        // If we have servers to show, also show the footer
        if(mDisplayedServers.length != 0)
            return mDisplayedServers.length + 1;

        // else, completely empty view
        return 0;
    }

    public void setData(Server[] servers) {
        Log.d(TAG, "setData");
        mServers = servers;
        // Reset the filter
        filter(mFilter);
    }


    public interface Listener{
        void onServerClicked(Server server);
        void onFilterClicked();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final boolean mIsFooter;
        private final Listener mListener;
        private TextView mFooterText;
        private TextView friendIndicator;
        private ImageView passwordIcon;
        private TextView serverName;
        private ImageView serverFlag;
        private TextView serverMap;
        private TextView serverPlayerCount;
        private Server mServer;


        ViewHolder(View view, boolean isFooter, Listener listener) {
            super(view);
            mIsFooter = isFooter;
            mListener = listener;

            if (mIsFooter) {
                mFooterText = view.findViewById(R.id.footer);

            } else {
                serverName = view.findViewById(R.id.serverName);
                serverPlayerCount = view.findViewById(R.id.serverPlayerCount);
                serverMap = view.findViewById(R.id.serverMap);
                serverFlag = view.findViewById(R.id.serverFlag);
                friendIndicator = view.findViewById(R.id.serverFriendIndicator);
                passwordIcon = view.findViewById(R.id.server_locked);
            }

            view.setOnClickListener(this);
        }

        void setData(Server data) {
            mServer = data;
            Log.d(TAG, "setFriends: " + mIsFooter);
            if (mIsFooter)
                return;

            Log.d(TAG, " server name : " + mServer.getServerName());
            serverName.setText(mServer.getServerName());

            // Player Count Text View
            serverPlayerCount.setText(String.format(Locale.UK, "%d/%d (%d)",
                    mServer.getNumPlayers(),
                    mServer.getMaxPlayers(),
                    mServer.getReservedSlots()
            ));

            // Server's Map Name
            Resources res = serverMap.getContext().getResources();
            String gameSize = res.getString(PRLibrary.gameLayer(mServer.getGameLayer(), false));
            String gameMode = res.getString(PRLibrary.gameMode(mServer.getGameMode(), false));

            serverMap.setText(String.format("[%s %s] %s", gameMode, gameSize, mServer.getMapName()));

            // Server Flag
            Glide
                    .with(serverFlag.getContext())
                    .load(mServer.getCountry().FLAG)
                    .into(serverFlag);


            // Checks if Friend Image is needed
            friendIndicator.setVisibility(mServer.hasFriends() ? View.VISIBLE : View.INVISIBLE);


            // Checks if Password Image is needed
            passwordIcon.setVisibility(mServer.hasPassword() ? View.VISIBLE : View.INVISIBLE);
        }

        void setFooter(int displaying, int total) {

            if (mIsFooter) {
                mFooterText.setText(String.format(Locale.US, "Showing %d of %d servers", displaying, total));
                //mRoot.setOnClickListener(mOnFooterClick);
            }
        }
        @Override
        public void onClick(View view) {
            if(mIsFooter)
                mListener.onFilterClicked();
            else
                mListener.onServerClicked(mServer);
        }
    }
}
