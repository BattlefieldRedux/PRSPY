package pt.uturista.prspy.compact;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pt.uturista.prspy.loader.GalleryLoader;
import pt.uturista.prspy.loader.LayoutLoader;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Layout;
import pt.uturista.prspy.model.Level;

public class CompactGallery {

    private static final String TAG = "CompactGallery";
    private static final String DATA_EXTRA = "DATA_EXTRA";
    private static final int LEVELS_LOADER_ID = 0;
    private static final int LAYOUT_LOADER_ID = 1;

    private static Level[] Data = null;

    private final AppCompatActivity mContext;
    private final Listener mListener;
    private boolean mIsLoaded;

    private LoaderManager.LoaderCallbacks<Level[]> mLevelsCallback = new LoaderManager.LoaderCallbacks<Level[]>() {
        @NonNull
        @Override
        public Loader<Level[]> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, "Levels onCreateLoader");
            return new GalleryLoader(mContext);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Level[]> loader, Level[] data) {
            Log.d(TAG, "Levels onLoadFinished");
            Data = data;
            mIsLoaded = true;
            mListener.onCompactGalleryReady();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Level[]> loader) {
            Log.d(TAG, "onLoaderReset");
        }
    };
    private LoaderManager.LoaderCallbacks<Layout> mLayoutCallback = new LoaderManager.LoaderCallbacks<Layout>() {
        @NonNull
        @Override
        public Loader<Layout> onCreateLoader(int id, @Nullable Bundle args) {
            return new LayoutLoader(mContext, args.getString("MapName"), args.getString("MapMode"), args.getInt("MapLayer"));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Layout> loader, Layout data) {

            mListener.onLayoutReady(data);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Layout> loader) {
            // Do nothing...
        }
    };

    public CompactGallery(AppCompatActivity context, Listener listener) {
        mContext = context;
        mListener = listener;
        mIsLoaded = false;
    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(DATA_EXTRA, Data);
    }


    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        if(Data != null){
            mIsLoaded = true;

        }else if (savedInstanceState == null) {
            Log.d(TAG, "init loader");
            mContext.getSupportLoaderManager().initLoader(LEVELS_LOADER_ID, null, mLevelsCallback);

        } else {
            Parcelable[] data = savedInstanceState.getParcelableArray(DATA_EXTRA);
            if (data != null) {
                Data = new Level[data.length];
                for (int i = 0; i < data.length; i++) {
                    Data[i] = (Level) data[i];
                }
            }

            mIsLoaded = true;
        }

        if(mIsLoaded)
            mListener.onCompactGalleryReady();
    }


    public Level[] getMaps() {
        return Data;
    }


    public Level getMap(String name) {
        if (Data == null)
            return null;


        for (Level level : Data) {
            if (level.getName().equals(name))
                return level;
        }

        return null;
    }

    public void getLayout(String mapName, @DataTypes.GameModes String mode, @DataTypes.GameLayers int layer) {

        if (mapName == null || mode == null || layer == DataTypes.UNKNOWN_LAYER){
            Log.e(TAG, "Called getLayout with invalid params");
            return;
        }

        // Init the loader to load this new layout
        Bundle args = new Bundle();
        args.putString("MapName", mapName.replaceAll("\\s|_", "").toLowerCase());
        args.putString("MapMode", mode);
        args.putInt("MapLayer", layer);

        mContext.getSupportLoaderManager().restartLoader(LAYOUT_LOADER_ID, args, mLayoutCallback);
    }

    public interface Listener {
        void onCompactGalleryReady();

        void onLayoutReady(Layout layout);
    }
}
