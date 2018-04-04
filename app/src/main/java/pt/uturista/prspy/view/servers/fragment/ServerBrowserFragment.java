package pt.uturista.prspy.view.servers.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.utils.filter.Filter;
import pt.uturista.prspy.view.RecyclerViewFragment;
import pt.uturista.prspy.view.servers.adapter.ServerAdapter;


public class ServerBrowserFragment extends RecyclerViewFragment implements ServerAdapter.Listener {
    public final static String TAG = "ServerListFragment";
    private Listener mListener;
    private ServerAdapter mAdapter;
    private boolean mHideEmptyServers;
    private boolean mRefreshing;


    public ServerBrowserFragment() {
        super(true);
        setHasOptionsMenu(true);
        mAdapter = new ServerAdapter( this);

        // Will always start this fragment trying to load servers
        mRefreshing = true;
    }

    public void setServers(Server[] servers) {
        Log.d(TAG, "setServers");

        // Stop the refreshing animation
        mRefreshing = setRefreshing(false);

        if (mAdapter == null || servers == null)
            return;

        // mServers = servers;
        mAdapter.setData(servers);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onRefresh() {
        mRefreshing = true;
        mListener.onRefreshRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // By contract the activity will automatically refresh if there's no servers
        // so, we make the visual representation of it
        if (mRefreshing){
            Log.d(TAG, "setRefreshing(true);");
            setRefreshing(true);
        }

        return view;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.browser_server_menu, menu);
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
                Log.d(TAG, "onQueryTextChange: " + newText);
                Filter<Server> filter = new Filter<Server>() {
                    @Override
                    public boolean filter(Server server) {
                        // show all if query is null or empty || show servers that match query
                        return !(mHideEmptyServers && server.getNumPlayers() == 0) && (newText == null || newText.equals("") || server.getServerName().toLowerCase().contains(newText.toLowerCase()) || server.getMapName().toLowerCase().contains(newText.toLowerCase()));

                    }
                };
                mAdapter.filter(filter);
                return true;
            }
        });
    }

    @Override
    public void onServerClicked(Server server) {
        mListener.onServerSelected(server);
    }

    @Override
    public void onFilterClicked() {
        mListener.expandServerFilters();
    }

    public void showEmptyServers(boolean showEmptyServers) {
        Log.d(TAG, "showEmptyServers " + showEmptyServers);
        mHideEmptyServers = !showEmptyServers;

        if(mAdapter == null){
            return;
        }

        if (mHideEmptyServers) {
            Filter<Server> filter = new Filter<Server>() {
                @Override
                public boolean filter(@NonNull Server server) {
                    return server.getNumPlayers() != 0;
                }
            };
            mAdapter.filter(filter);
        }

        mAdapter.notifyDataSetChanged();
    }

    public interface Listener {
        void onServerSelected(Server server);

        void onRefreshRequest();

        void expandServerFilters();
    }
}
