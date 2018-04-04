package pt.uturista.prspy.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import pt.uturista.bf2.LevelJson;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.utils.Net;

public class GalleryLoader extends AsyncTaskLoader<Level[]> {
    private static final String TAG = "GalleryLoader";
    private static final URL GALLERY_URL;

    private Level[] mData;

    public GalleryLoader(Context context) {
        super(context);
    }

    @Override
    public Level[] loadInBackground() {
        Log.d(TAG, "loadInBackground");

         Net.DownloadResult<LevelJson[]> result = Net.with(getContext()).download(LevelJson[].class, GALLERY_URL);

         if(result.Error != null){
             // TODO: Wrap the error in a result class
             return null;
         }

        LevelJson[] galleryJson = result.Result;
        if(galleryJson == null)
            return null;

        mData = new Level[galleryJson.length];

        for (int i = 0; i < galleryJson.length; i++) {
            mData[i] = new Level(galleryJson[i]);
        }

        return mData;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading");

        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    /* *********************************
     *        Ugly but needed
     * ********************************* */
    static{
        try {
            GALLERY_URL = new URL("https://www.realitymod.com/mapgallery/json/levels.json");
        } catch (MalformedURLException e) {
            throw new AssertionError("Should not reach here");
        }
    }

}
