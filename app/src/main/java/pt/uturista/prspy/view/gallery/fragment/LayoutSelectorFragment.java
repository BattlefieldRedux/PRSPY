package pt.uturista.prspy.view.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.Locale;

import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.model.PRLibrary;
import pt.uturista.prspy.view.RecyclerViewFragment;
import pt.uturista.prspy.view.gallery.adapter.LayoutAdapter;

public class LayoutSelectorFragment extends RecyclerViewFragment implements LayoutAdapter.Listener {
    public static final String ARGS_MAP = "args_map";
    public static final String ARGS_MODE = "args_mode";
    public static final String ARGS_LAYER = "args_layer";


    @DataTypes.GameLayers
    private int mLayer;
    @DataTypes.GameModes
    private String mMode;
    private Listener mListener;
    private LayoutAdapter mAdapter;
    private Level.LayoutIndex[] mLayouts;

    public LayoutSelectorFragment() {
        super(false);
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new LayoutAdapter(this);

        return mAdapter;
    }

    @Override
    public void onRefresh() {
        // Do nothing, cause we cant refresh
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLayer = DataTypes.getGameLayer(savedInstanceState.getInt(ARGS_MODE));
            mMode = DataTypes.getGameMode(savedInstanceState.getString(ARGS_LAYER, ""));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARGS_MODE, mLayer);
        outState.putString(ARGS_LAYER, mMode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (Listener) context;
        } catch (ClassCastException ex) {
            throw new AssertionError(context.getClass().getName() + " must implement " + Listener.class.getName());
        }
    }


    public void setCurrentLayout(@DataTypes.GameModes String mode, @DataTypes.GameLayers int layer) {
        mMode = mode;
        mLayer = layer;

        //
        for (int i = 0; i < mLayouts.length; i++) {
            if(mLayouts[i].getGameLayer() == mLayer && mLayouts[i].getGameMode().equals(mMode)){
                mAdapter.setItemChecked(i);
                break;
            }
        }
    }

    public void setAvailableLayouts(Level.LayoutIndex[] layouts) {
        mLayouts = layouts;


        String[] entries = new String[layouts.length];
        for (int i = 0; i < layouts.length; i++) {
            entries[i] = String.format(Locale.US, "%s %s",
                    getString(PRLibrary.gameMode(layouts[i].getGameMode(), true)),
                    getString(PRLibrary.gameLayer(layouts[i].getGameLayer(), true)));
        }
        mAdapter.setData(entries);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLayoutSelecter(int position) {
        if (position < mLayouts.length)
            mListener.onLayoutSelected(mLayouts[position].getGameMode(), mLayouts[position].getGameLayer());
    }

    public interface Listener {
        void onLayoutSelected(String gameMode, int gameLayer);
    }
}
