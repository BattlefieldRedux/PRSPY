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

package pt.uturista.prspy.view.gallery.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.DataTypes;
import pt.uturista.prspy.model.Layout;
import pt.uturista.prspy.model.PRLibrary;
import pt.uturista.prspy.view.widget.TabLayoutColor;
import pt.uturista.prspy.view.servers.adapter.TeamPageAdapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GalleryDetailsFragment extends Fragment implements AssetsListFragment.Listener {

    private static final String TAG = "GalleryDetailsFragment";
    private static final String ARGS_MAP = "ARGS_MAP";
    private static final String ARGS_LAYOUT = "ARGS_LAYOUT";

    private Layout mLayout;
    private TextView mHeaderInfo;
    private ImageView mHeaderImage;
    private TeamPageAdapter<AssetsListFragment> mViewPagerAdapter;
    private AssetsListFragment mOpforFrag;
    private AssetsListFragment mBluforFrag;
    private String mMapName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.gallery_details_fragment, container, false);


        mHeaderInfo = view.findViewById(R.id.map_info);
        mHeaderImage = view.findViewById(R.id.imageview);

        // Create the viewpager
        mViewPagerAdapter = new TeamPageAdapter<>(getChildFragmentManager(), R.id.viewpager, AssetsListFragment.newInstance(DataTypes.Opfor), AssetsListFragment.newInstance(DataTypes.Blufor));
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(mViewPagerAdapter);

        // Obtain the correct references for the inner-fragments
        mOpforFrag = (AssetsListFragment) mViewPagerAdapter.getItem(0);
        mBluforFrag= (AssetsListFragment) mViewPagerAdapter.getItem(1);


        Log.d(TAG, "Setting TabLayout");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayoutColor(tabLayout));

        return view;
    }

    public void setLayout(String mapName, Layout layout) {
        if(layout == null)
            return;

        mMapName=mapName;
        mLayout = layout;

        updateHeader();
        updateFragments();
    }

    private void updateFragments() {
        if (mLayout == null)
            return;

        if (mBluforFrag == null || mOpforFrag == null) {
            Log.d(TAG, "updateFragments Fragments not set");
            return;
        }



        mBluforFrag.setAssets(mLayout.getAssets(DataTypes.Blufor));
        mBluforFrag.setTitle(mLayout.getTeam(DataTypes.Blufor).getCode());

        mOpforFrag.setAssets(mLayout.getAssets(DataTypes.Opfor));
        mOpforFrag.setTitle(mLayout.getTeam(DataTypes.Opfor).getCode());

        mViewPagerAdapter.notifyDataSetChanged();
    }

    private void updateHeader() {
        if (mLayout == null)
            return;

        Log.d(TAG, "updateHeader()");

        mHeaderInfo.setText(String.format("%s %s",
                getString(PRLibrary.gameMode(mLayout.getGameMode(), true)),
                getString(PRLibrary.gameLayer(mLayout.getGameLayer(), true))));

        Uri imageUri = Uri.parse(String.format(Locale.US, "https://www.realitymod.com/mapgallery/images/maps/%s/tile.jpg", mMapName.replaceAll("\\s|_",  "").toLowerCase()));

        Glide.with(this)
                .load(imageUri)
                .transition(withCrossFade())
                .into(mHeaderImage);
    }
}
