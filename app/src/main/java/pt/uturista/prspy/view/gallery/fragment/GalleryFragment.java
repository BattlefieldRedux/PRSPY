package pt.uturista.prspy.view.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.utils.filter.Filter;
import pt.uturista.prspy.view.gallery.adapter.MapAdapter;

public class GalleryFragment extends Fragment implements MapAdapter.Listener {

    private Listener mListener;
    private MapAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    public GalleryFragment() {
        setHasOptionsMenu(true);
        mAdapter = new MapAdapter(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.gallery_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        if (searchView == null)
            return;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                mAdapter.filter(new Filter<Level>() {
                    @Override
                    public boolean filter(Level map) {

                        return newText == null || newText.equals("") || map.getName().toLowerCase().contains(newText.toLowerCase());
                    }
                });
                return true;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
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

        // We want to have the ability to inform the user we're updating the maps
        // but only if there's no maps currently available
        mSwipeLayout = rootView.findViewById(R.id.swipeLayout);
        mSwipeLayout.setEnabled(false);
        if (mAdapter.getItemCount() <= 0) {
            mSwipeLayout.setRefreshing(true);
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

    public void setMaps(@Nullable Level[] maps) {
        if(maps == null)
            return;

        mAdapter.setMaps(maps);
        mAdapter.notifyDataSetChanged();

        if (mSwipeLayout != null)
            mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onMapClick(Level map) {
        mListener.onMapClick(map);
    }

    public interface Listener {
        void onMapClick(Level map);
    }
}
