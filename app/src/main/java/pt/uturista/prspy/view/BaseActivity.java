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

package pt.uturista.prspy.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import pt.uturista.prspy.BuildConfig;
import pt.uturista.prspy.R;
import pt.uturista.prspy.utils.Net;


/**
 * Base activity for all activities in PRSPY
 * It contains the necessary logic to inform the user if there's not a internet connection available when needed
 * <p>
 * It also allows any subclass to display their own messages to the user, although the "No Connection"
 * message will always take precedence
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private Snackbar mSnackBar;
    private BroadcastReceiver mReceiver;
    private boolean mInternetAvailable;

    protected DrawerLayout mDrawer;

    protected abstract boolean requiresInternet();

    protected BaseActivity() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                OnConnectivityChange();
            }
        };
    }

    private Snackbar makeSnackBar() {
        View root = findViewById(R.id.root);

        Snackbar snackbar = Snackbar.make(root, "No Internet connection...", Snackbar.LENGTH_INDEFINITE);
        View snackView = snackbar.getView();
        snackView.setBackgroundResource(R.color.gray_dark);
        TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.blue));

        return snackbar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentView());

        // Set the toolbar/action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (BuildConfig.DEBUG && toolbar == null) {
            throw new AssertionError("BaseActivity requires a " + Toolbar.class.getName() + " identified with R.id.toolbar");
        }
        setSupportActionBar(toolbar);


        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.w(TAG, "HTTP response cache installation failed:" + e);
            throw new AssertionError("HTTP response cache installation failed:" + e);
        }


        // We don't need to call manually 'OnConnectivityChange', since the BroadcastReceiver
        // will always call it when its registered
    }

    @Override
    protected void onStop() {
        super.onStop();
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    final protected void showSnackBar(@NonNull String message) {
        showSnackBar(message, false);
    }

    final protected void hideSnackBar() {
        // Hide the snackbar is its visible and its not our no connection error message
        if(mInternetAvailable && mSnackBar != null && mSnackBar.isShown()){
            mSnackBar.dismiss();
        }
    }

    private void showSnackBar(@NonNull String message, boolean force) {
        if (!force && !mInternetAvailable)
            return;

        if (mSnackBar != null && mSnackBar.isShown()) {
            mSnackBar.dismiss();
        }

        mSnackBar = makeSnackBar();
        mSnackBar.setText(message).show();
    }

    /**
     * Checks if the
     */
    protected void OnConnectivityChange() {
        Log.d(TAG, "OnConnectivityChange");
        if (Net.isInternetAvailable(this)) {
            // If there was not internet, then the snackbar is sure to be showing the
            // No internet connection message - so we dismiss it
            if (!mInternetAvailable && mSnackBar != null && mSnackBar.isShown()) {
                mSnackBar.dismiss();
            }
            mInternetAvailable = true;

        } else if (!Net.isInternetAvailable(this) && requiresInternet()) {
            mInternetAvailable = false;
            showSnackBar(getString(R.string.no_internet_available), true);
        }
    }

    @LayoutRes
    public int getContentView() {
        return R.layout.base_activity;
    }
}
