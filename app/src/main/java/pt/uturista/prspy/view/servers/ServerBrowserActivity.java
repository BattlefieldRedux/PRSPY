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

package pt.uturista.prspy.view.servers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.Date;

import pt.uturista.prspy.PRSPY;
import pt.uturista.prspy.R;
import pt.uturista.prspy.compact.CompactServer;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.utils.Net;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.servers.fragment.ServerBrowserFragment;

public class ServerBrowserActivity extends BaseNavigationActivity implements CompactServer.Listener, ServerBrowserFragment.Listener {

    private static final String TAG = "ServerBrowserActivity";
    private static final String SERVER_BROWSER_FRAGMENT = "SERVER_BROWSER_FRAGMENT";

    // Time span required for us to consider the PRSPY data outdated
    private static final int OUTDATED_DATA_THRESHOLD_MINUTES = 10;  // useful for the ui
    private static final long OUTDATED_DATA_THRESHOLD = OUTDATED_DATA_THRESHOLD_MINUTES * 60 * 1000;

    private CompactServer mCompactServer;
    private ServerBrowserFragment mServerBrowser;
    private CountDownTimer mTimer;

    public ServerBrowserActivity() {
        super(true);
        mCompactServer = new CompactServer(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();

        mServerBrowser = (ServerBrowserFragment) fragmentManager.findFragmentByTag(SERVER_BROWSER_FRAGMENT);


        // if there's no fragment already created, create one
        if(mServerBrowser == null){
            // Create new fragment
            mServerBrowser = new ServerBrowserFragment();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, mServerBrowser, SERVER_BROWSER_FRAGMENT);
            transaction.commit();
        }


        boolean showEmptyServers = getSharedPreferences(PRSPY.OPTIONS_PREFERENCES, MODE_PRIVATE).getBoolean(PRSPY.SHOW_EMPTY_SERVERS_PREF, false);
        mServerBrowser.showEmptyServers(showEmptyServers);
        if(mCompactServer.getServers() == null){
            mCompactServer.refresh();
        }else{
            mServerBrowser.setServers(mCompactServer.getServers());
        }
    }

    @Override
    protected void OnConnectivityChange() {
        super.OnConnectivityChange();

        // While there was no internet our data might as become old, so we need to check
        // since we have internet back
        if(Net.isInternetAvailable(this)){
            startCountdown();
        }
    }

    private void startCountdown(){
        Log.d(TAG, "startCountdown");
        // No point in informing that the data is old if we have no data
        if( mCompactServer.getTime() == null || mCompactServer.getServers() == null){
            Log.d(TAG, "No data available");
            return;
        }

        // how many milliseconds is this data old?
        long lastUpdated = new Date().getTime() - mCompactServer.getTime().getTime();
        Log.d(TAG, "Data time: " +  mCompactServer.getTime());

        if(lastUpdated >= OUTDATED_DATA_THRESHOLD){
            showSnackBar(getString(R.string.outdated_prspy, OUTDATED_DATA_THRESHOLD_MINUTES));

        }else{
            // We can hide the 'Outdated warning' (we don't care if it exists or not)
            hideSnackBar();

            // Start timer to show the 'Outdated warning'
            long dataWillBeOldIn = OUTDATED_DATA_THRESHOLD - lastUpdated;
            mTimer = new CountDownTimer(dataWillBeOldIn, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    // Do nothing;
                }

                @Override
                public void onFinish() {
                    showSnackBar(getString(R.string.outdated_prspy, OUTDATED_DATA_THRESHOLD_MINUTES));
                }
            };
            mTimer.start();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Stop the timer if it exists
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        mCompactServer.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onCreate");
        mCompactServer.onCreate(savedInstanceState);
    }

    @Override
    protected int getDrawerId() {
        return R.id.nav_servers;
    }



    @Override
    public void onDataAvailable() {

        Log.d(TAG, "onDataAvailable");
        mServerBrowser.setServers(mCompactServer.getServers());
        startCountdown();
    }

    @Override
    public void onServerSelected(Server server) {
        startActivity(ServerDetailsActivity.newIntent(this, server));
    }

    @Override
    public void onRefreshRequest() {
        mCompactServer.refresh();
    }

    @Override
    public void expandServerFilters() {
        mServerBrowser.showEmptyServers(true);
    }
}
