package pt.uturista.prspy.view.servers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.view.servers.adapter.PlayerAdapter;
import pt.uturista.prspy.view.servers.adapter.TeamPageAdapter;
import pt.uturista.prspy.view.widget.StickyHeaderDecorator;


public class PlayerListFragment extends TeamPageAdapter.TitledFragment implements PlayerAdapter.PlayerClicked {
    private static final String TAG = "PlayerListFragment";
    private static final String ARG_TEAM = "ARG_TEAM";
    private PlayerAdapter mAdapter;
    private Listener mListener;
    private @DataTypes.Teams int mTeam;
    private String mTitle;

    public PlayerListFragment() {

        mAdapter = new PlayerAdapter(this);
    }

    public static PlayerListFragment newInstance(@DataTypes.Teams int team){

        PlayerListFragment fragment = new PlayerListFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_TEAM, team);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_TEAM, mTeam);
        Log.d(TAG, "onSaveInstanceState " + mTeam);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            int i = savedInstanceState.getInt(ARG_TEAM);
            Log.d(TAG, "onSaveInstanceState " + i);
            mTeam = DataTypes.getTeam(i);

        } else if (getArguments() != null) {


            int i = getArguments().getInt(ARG_TEAM);
            Log.d(TAG, "getArguments " + i);
            mTeam = DataTypes.getTeam(i);



        }else{
            throw new IllegalStateException("PlayerListFragment created without team assigned");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new StickyHeaderDecorator(R.layout.server_player_row));

        // recyclerView.addItemDecoration(sectionItemDecoration);
        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        } else {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);
        }

        recyclerView.scrollToPosition(scrollPosition);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");

        Fragment parentFragment = getParentFragment();
        try {
            mListener = parentFragment == null ? (Listener) context : (Listener) parentFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException((parentFragment == null ? context.toString() : parentFragment.toString())
                    + " must implement " + Listener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }





    @DataTypes.Teams
    public  int getTeam(){
        return mTeam;
    }

    public void setPlayers(Player[] players) {
        if (players == null)
            return;

        Log.d(TAG, "setPlayers: " + players.length);
        mAdapter.setPlayers(players);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerClicked(Player player) {
        mListener.onPlayerPressed(player);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        Log.d(TAG, "setTitle: " + title);
        mTitle = title;
    }

    public interface Listener {
        void onPlayerPressed(Player player);
    }
}
