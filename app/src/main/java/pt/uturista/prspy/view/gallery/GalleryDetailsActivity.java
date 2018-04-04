package pt.uturista.prspy.view.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;

import pt.uturista.prspy.R;
import pt.uturista.prspy.compact.CompactGallery;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Layout;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.view.BaseActivity;
import pt.uturista.prspy.view.gallery.fragment.GalleryDetailsFragment;
import pt.uturista.prspy.view.gallery.fragment.LayoutSelectorFragment;

public class GalleryDetailsActivity extends BaseActivity implements CompactGallery.Listener, LayoutSelectorFragment.Listener {
    public static final int LEVEL_NOT_FOUND_RESULT = -2;

    private static final String TAG = "GalleryDetailsActivity";
    private static final String ARGS_MAP = "ARGS_MAP";
    private static final String ARGS_MODE = "ARGS_MODE";
    private static final String ARGS_LAYER = "ARGS_LAYER";

    private CompactGallery mCompactGallery;
    private GalleryDetailsFragment mDetailsFragment;
    private LayoutSelectorFragment mSelectorFragment;
    private DrawerLayout mSelectorDrawer;


    public GalleryDetailsActivity() {
        mCompactGallery = new CompactGallery(this, this);
    }


    @Override
    public int getContentView() {
        return R.layout.gallery_details_activity;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCompactGallery.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onRestoreInstanceState(savedInstanceState);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain Map
        String mapName = getIntent().getStringExtra(ARGS_MAP);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mapName);
        }

        // No need to create the fragments since they're defined in the xml
        mDetailsFragment = (GalleryDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_details_fragment);
        mSelectorFragment = (LayoutSelectorFragment) getSupportFragmentManager().findFragmentById(R.id.layout_selector_fragment);
        mSelectorDrawer = findViewById(R.id.drawer_layout);

        mCompactGallery.onCreate(savedInstanceState);
    }

    private void openLayout(String mapName, String mode, int layer) {
        Log.d(TAG, "openLayout");

        Level level = mCompactGallery.getMap(mapName);
        if (level == null || level.getLayouts().length == 0) {
            Log.d(TAG, "level not found");
            setResult(LEVEL_NOT_FOUND_RESULT);
            finish();
            return;
        }


        // Obtain the layout and layer
        if (mode == null || layer == DataTypes.UNKNOWN_LAYER) {
            mode = level.getLayouts()[0].getGameMode();
            layer = level.getLayouts()[0].getGameLayer();
        }

        mSelectorFragment.setAvailableLayouts(level.getLayouts());
        mSelectorFragment.setCurrentLayout(mode, layer);
        mCompactGallery.getLayout(mapName, mode, layer);
    }

    @Override
    protected boolean requiresInternet() {
        return false;
    }

    public static Intent newIntent(Context context, String mapName) {
        return newIntent(context, mapName, null, DataTypes.UNKNOWN_LAYER);
    }

    public static Intent newIntent(Context context, String mapName, @DataTypes.GameModes String mode, @DataTypes.GameLayers int layer) {
        Intent intent = new Intent(context, GalleryDetailsActivity.class);
        intent.putExtra(ARGS_MAP, mapName);
        intent.putExtra(ARGS_MODE, mode);
        intent.putExtra(ARGS_LAYER, layer);
        return intent;
    }

    @Override
    public void onCompactGalleryReady() {
        Log.d(TAG, "onCompactGalleryReady");
        String mapName = getIntent().getStringExtra(ARGS_MAP);
        String mode = getIntent().getStringExtra(ARGS_MODE);
        int layer = getIntent().getIntExtra(ARGS_LAYER, DataTypes.UNKNOWN_LAYER);
        openLayout(mapName, mode, layer);
    }

    @Override
    public void onLayoutReady(Layout layout) {
        Log.d(TAG, "onLayoutReady " + layout);
        mDetailsFragment.setLayout(getIntent().getStringExtra(ARGS_MAP), layout);
    }


    @Override
    public void onLayoutSelected(@DataTypes.GameModes String gameMode, @DataTypes.GameLayers int gameLayer) {
        mCompactGallery.getLayout(getIntent().getStringExtra(ARGS_MAP), gameMode, gameLayer);
        mSelectorDrawer.closeDrawer(Gravity.RIGHT);

    }
}