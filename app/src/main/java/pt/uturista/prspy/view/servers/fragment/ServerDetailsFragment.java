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

package pt.uturista.prspy.view.servers.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.PRLibrary;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.view.widget.TabLayoutColor;
import pt.uturista.prspy.view.servers.adapter.TeamPageAdapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ServerDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PlayerListFragment.Listener {

    private static final String TAG = "ServerDetailsFragment";
    private TextView mHeaderInfo;
    private ImageView mHeaderImage;
    private TeamPageAdapter<PlayerListFragment> mViewPagerAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private Server mServer;
    private PlayerListFragment mBluforPlayersFrag;
    private PlayerListFragment mOpforPlayersFrag;


    public boolean s;
    private Listener mListener;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("s", s);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.server_details_fragment, container, false);

        mHeaderInfo = view.findViewById(R.id.map_info);
        mHeaderImage = view.findViewById(R.id.imageview);

        // Create the ViewPager
        mViewPagerAdapter = new TeamPageAdapter<>(getChildFragmentManager(), R.id.viewpager, PlayerListFragment.newInstance(DataTypes.Opfor), PlayerListFragment.newInstance(DataTypes.Blufor));
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(mViewPagerAdapter);

        // Obtain the correct references for the inner-fragments
        mOpforPlayersFrag = (PlayerListFragment) mViewPagerAdapter.getItem(0);
        mBluforPlayersFrag = (PlayerListFragment) mViewPagerAdapter.getItem(1);

        Log.d(TAG, "Setting TabLayout");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayoutColor(tabLayout));

        Log.d(TAG, "Setting SmartSwipeRefreshLayout");
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ServerDetailsFragment.Listener) context;
        } catch (ClassCastException ex) {
            throw new AssertionError(context.toString() + " must implement " + ServerDetailsFragment.Listener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateHeader() {
        Log.d(TAG, "updateHeader()");
        if (mServer == null)
            return;

        mHeaderInfo.setText(String.format("[%s %s] %s",
                getString(PRLibrary.gameMode(mServer.getGameMode(), false)),
                getString(PRLibrary.gameLayer(mServer.getGameLayer(), false)),
                mServer.getMapName()));

        final String mapWebKey = mServer.getMapName().replaceAll("\\s|_", "").toLowerCase();
        Uri imageUri = Uri.parse(String.format(Locale.US, "https://www.realitymod.com/mapgallery/images/maps/%s/tile.jpg", mapWebKey));

        Glide.with(this)
                .load(imageUri)
                .transition(withCrossFade())
                .into(mHeaderImage);
    }

    @Override
    public void onRefresh() {
        mListener.onRefreshRequest();
    }

    public void setServer(Server server) {

        mSwipeLayout.setRefreshing(false);

        if (server == null)
            return;

        Log.d(TAG, "Seeing details of: " + server.getServerName());

        this.mServer = server;

        updateFragments();
        updateHeader();
    }

    private void updateFragments() {
        Log.d(TAG, "updateFragments");
        if (mServer == null)
            return;

        if(mBluforPlayersFrag == null || mOpforPlayersFrag == null){
            Log.d(TAG, "updateFragments Fragments not set");
            return;
        }

        mOpforPlayersFrag.setPlayers(mServer.getPlayers(DataTypes.Opfor));
        mOpforPlayersFrag.setTitle(mServer.getTeamName(DataTypes.Opfor));

        mBluforPlayersFrag.setPlayers(mServer.getPlayers(DataTypes.Blufor));
        mBluforPlayersFrag.setTitle(mServer.getTeamName(DataTypes.Blufor));

        if(mViewPagerAdapter != null)
            mViewPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPlayerPressed(Player player) {
        mListener.onPlayerPressed(player);
    }

    public interface Listener {
        void onRefreshRequest();

        void onPlayerPressed(Player player);
    }
}
