package pt.uturista.prspy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import pt.uturista.prspy.R;
import pt.uturista.prspy.view.friends.FriendsActivity;
import pt.uturista.prspy.view.gallery.GalleryActivity;
import pt.uturista.prspy.view.servers.ServerBrowserActivity;
import pt.uturista.prspy.view.settings.SettingsActivity;

/**
 * Created by Vasco on 26-02-2018.
 */

public abstract class BaseNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "BaseNavigationActivity";

    private static final int BACK = 1;
    private static final int MENU = 2;
    private static final String TUNE_PREFERENCES = "SERVER_TUNE_OPTIONS_PREFERENCES";
    private static final String ARGS_SERVER = "ARGS_SERVER";
    private static final String ARGS_GALLERY = "ARGS_GALLERY";

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedItem;


    protected abstract @IdRes
    int getDrawerId();


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onPostCreate");
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onCreate has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initDrawer(toolbar, getDrawerId());
    }

    private void initDrawer(Toolbar toolbar, int pos) {
        NavigationView mNav = (NavigationView) findViewById(R.id.navigation_view);
        if (mNav == null)
            throw new AssertionError("BaseActivity requires a " + NavigationView.class.getName() + " identified with R.id.navigation_view");

        mNav.setNavigationItemSelectedListener(this);
        mNav.setCheckedItem(pos);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer == null)
            throw new AssertionError("BaseActivity requires a " + DrawerLayout.class.getName() + " identified with R.id.drawer_layout");

        mDrawerToggle = new PRSPYNavigationManager(toolbar, this);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");
        mSelectedItem = item.getItemId();
        mDrawer.closeDrawers();
        return true;
    }


    protected void setHomeButton(@BaseNavigationActivity.State int state) {
        switch (state) {
            case BACK:
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
                break;
            case MENU:
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
                break;
            default:
                throw new AssertionError("Case not defined");
        }
        animateButton(state);
    }

    private void animateButton(@BaseNavigationActivity.State final int state) {
        int start = state == BACK ? 0 : 1;
        int end = state == BACK ? 1 : 0;

        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerToggle.onDrawerSlide(null, slideOffset);

                if (state == BACK && slideOffset == 1)
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        anim.start();
        if (state == MENU)
            mDrawerToggle.setDrawerIndicatorEnabled(true);

    }



    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BACK, MENU})
    public @interface State {
    }


    private class PRSPYNavigationManager extends ActionBarDrawerToggle {

        PRSPYNavigationManager(Toolbar toolbar, Context mContext) {
            super(BaseNavigationActivity.this, BaseNavigationActivity.this.mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);

            if (drawerView != null && drawerView.getId() != R.id.navigation_view)
                return;

            Intent intent = null;
            //  Check if we're not attempting to select the activity that is already open
            //  or 0 which is not a valid Identifier
            if (mSelectedItem != getDrawerId() && mSelectedItem != 0) {
                switch (mSelectedItem) {
                    case R.id.nav_servers:
                        intent = new Intent(BaseNavigationActivity.this, ServerBrowserActivity.class);
                        break;

                    case R.id.nav_friends:
                        intent = new Intent(BaseNavigationActivity.this, FriendsActivity.class);
                        break;

                    case R.id.nav_gallery:
                        intent = new Intent(BaseNavigationActivity.this, GalleryActivity.class);
                        break;

                 /*   case R.id.nav_events:
                        intent = new Intent(mContext, EventsActivity.class);
                        break;*/

                    case R.id.nav_settings:
                     intent = new Intent(BaseNavigationActivity.this, SettingsActivity.class);

                    default:
                        // do nothing
                }
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        }
    }

}
