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

package pt.uturista.prspy.view.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

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
    private int mLayer;
    private String mMode;


    public GalleryDetailsActivity() {
        super(true, R.layout.gallery_details_activity, R.id.drawer_layout);
        mCompactGallery = new CompactGallery(this, this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCompactGallery.onSaveInstanceState(outState);
        outState.putInt(ARGS_LAYER, mLayer);
        outState.putString(ARGS_MODE, mMode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate( R.menu.gallery_details_menu, menu);

        // We want the menu to be displayed so we return true
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.open_layer_selector:
                mSelectorDrawer.openDrawer(Gravity.END);
                return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


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

        if (savedInstanceState == null) {
            mLayer = getIntent().getIntExtra(ARGS_LAYER, DataTypes.UNKNOWN_LAYER);
            mMode = getIntent().getStringExtra(ARGS_MODE);

        } else {
            mLayer = savedInstanceState.getInt(ARGS_LAYER, DataTypes.UNKNOWN_LAYER);
            mMode = savedInstanceState.getString(ARGS_MODE);
        }

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

        openLayout(mapName, mMode, mLayer);
    }

    @Override
    public void onLayoutReady(Layout layout) {
        Log.d(TAG, "onLayoutReady " + layout);
        mDetailsFragment.setLayout(getIntent().getStringExtra(ARGS_MAP), layout);
    }


    @Override
    public void onLayoutSelected(@DataTypes.GameModes String gameMode, @DataTypes.GameLayers int gameLayer) {

        mMode = gameMode;
        mLayer = gameLayer;

        mCompactGallery.getLayout(getIntent().getStringExtra(ARGS_MAP), mMode, mLayer);
        mSelectorDrawer.closeDrawer(Gravity.END);

    }
}
