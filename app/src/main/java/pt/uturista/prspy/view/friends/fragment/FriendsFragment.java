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

package pt.uturista.prspy.view.friends.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.view.friends.adapter.FriendAdapter;



public class FriendsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final static String TAG ="FriendsFragment";
    protected RecyclerView mRecyclerView;
    protected FriendAdapter mAdapter;
    private SwipeRefreshLayout mSwipeView;
    private Listener mListener;
    private View mEmptyView;


    public FriendsFragment(){
        mAdapter = new FriendAdapter(new Player[0], new Server[0]);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friends_fragment, container, false);


        mSwipeView = rootView.findViewById(R.id.swipeLayout);
        mSwipeView.setOnRefreshListener(this);

        mEmptyView = rootView.findViewById(R.id.empty_view);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(mAdapter);

        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(manager);
        }

        mRecyclerView.scrollToPosition(scrollPosition);

        if(mAdapter.getItemCount() > 0){
            mEmptyView.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (Listener) context;
        } catch (ClassCastException ex) {
            throw new AssertionError(context.toString() + " must implement " + Listener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        mListener.onRefresh();
    }

    /**
     * Setter for the information hold by this fragment.
     * It iterates all servers provided and displays all players marked as friends individually
     *
     * @param allServers a collection of all servers playing the game
     */
    public void setServers(@Nullable Server[] allServers) {
        if(allServers == null)
            return;

        ArrayList<Server> friendsservers = new ArrayList<>();
        ArrayList<Player> friends = new ArrayList<>();

        // Retrieve from all servers the individual players set has friends
        for(Server server: allServers){
            if(!server.hasFriends())
                continue;

            for(Player p : server.getPlayers())
                if(p.isFriend()){
                    friendsservers.add(server);
                    friends.add(p);
                }
        }

        Player[] players = new Player[friends.size()];
        mAdapter.setFriends(friends.toArray(players));

        // Adapter needs the servers to display additional information
        // but we can filter the servers that have no friends playing
        Server[] servers = new Server[friendsservers.size()];
        mAdapter.setServers(friendsservers.toArray(servers));


        if(mEmptyView != null)
            mEmptyView.setVisibility(View.INVISIBLE);

        if(mSwipeView!=null)
            mSwipeView.setRefreshing(false);


    }

    public interface Listener {
        void onRefresh();
        void onServerOpenRequest(Server server);
        void onPlayerUnfriend(Player player);
    }
}
