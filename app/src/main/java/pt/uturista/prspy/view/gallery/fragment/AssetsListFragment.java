package pt.uturista.prspy.view.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Asset;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Spawner;
import pt.uturista.prspy.view.gallery.adapter.AssetAdapter;
import pt.uturista.prspy.view.servers.adapter.TeamPageAdapter;


public class AssetsListFragment extends TeamPageAdapter.TitledFragment {

    private static final String TAG = "AssetsListFragment";
    private static final String ARG_TEAM = "ARG_TEAM";
    private AssetAdapter mAdapter;
    private @DataTypes.Teams int mTeam;
    private Listener mListener;
    private String mTitle;

    public AssetsListFragment() {
        mAdapter = new AssetAdapter();
    }


    public static AssetsListFragment newInstance(@DataTypes.Teams int team) {
        AssetsListFragment fragment = new AssetsListFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_TEAM, team);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_TEAM, mTeam);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mTeam = DataTypes.getTeam(savedInstanceState.getInt(ARG_TEAM));
        } else if (getArguments() != null) {
            mTeam = DataTypes.getTeam(getArguments().getInt(ARG_TEAM));
        } else {
            throw new IllegalStateException("PlayerListFragment created without team assigned");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setAdapter(mAdapter);
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

    @Override
    public String getTitle() {
        return mTitle;
    }


    public void setTitle(String title) {
        Log.d(TAG, "setTitle: " + title);
        mTitle = title;
    }

    public void setAssets(Asset[] assets) {
        mAdapter.setAssets(assets);
        mAdapter.notifyDataSetChanged();
    }

    @DataTypes.Teams
    public int getTeam() {
        return mTeam;
    }


    public interface Listener {}
}
