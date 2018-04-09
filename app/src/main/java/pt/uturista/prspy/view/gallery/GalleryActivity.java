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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pt.uturista.prspy.R;
import pt.uturista.prspy.compact.CompactGallery;
import pt.uturista.prspy.model.Layout;
import pt.uturista.prspy.model.Level;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.gallery.fragment.GalleryFragment;


public class GalleryActivity extends BaseNavigationActivity implements CompactGallery.Listener, GalleryFragment.Listener {
    private static final String GALLERY_FRAGMENT = "GALLERY_FRAGMENT";

    private GalleryFragment mGalleryFrag;
    private CompactGallery mCompactGallery;


    public GalleryActivity() {
        super(true);
        mCompactGallery = new CompactGallery(this, this);
    }

    @Override
    protected int getDrawerId() {
        return R.id.nav_gallery;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCompactGallery.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompactGallery.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        mGalleryFrag = (GalleryFragment) fragmentManager.findFragmentByTag(GALLERY_FRAGMENT);

        // if there's no fragment already created, create one
        if (mGalleryFrag == null) {
            // Create new fragment
            mGalleryFrag = new GalleryFragment();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, mGalleryFrag, GALLERY_FRAGMENT);
            transaction.commit();
        }

        mGalleryFrag.setMaps(mCompactGallery.getMaps());
    }


    @Override
    public void onCompactGalleryReady() {
        if (mGalleryFrag != null)
            mGalleryFrag.setMaps(mCompactGallery.getMaps());
    }

    @Override
    public void onLayoutReady(Layout layout) {
        // Do nothing
    }

    @Override
    public void onMapClick(Level map) {
        startActivity(GalleryDetailsActivity.newIntent(this, map.getName()));
    }
}
