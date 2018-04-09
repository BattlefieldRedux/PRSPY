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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import pt.uturista.prspy.R;
import pt.uturista.prspy.compact.CompactServer;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.view.BaseActivity;
import pt.uturista.prspy.view.gallery.GalleryDetailsActivity;
import pt.uturista.prspy.view.servers.fragment.ServerDetailsFragment;
import pt.uturista.prspy.view.servers.fragment.ServerInformationFragment;

public class ServerDetailsActivity extends BaseActivity implements CompactServer.Listener, ServerDetailsFragment.Listener {

    private static final String ARGS_SERVER_ID = "ARGS_SERVER_ID";
    private static final String TAG = "ServerDetailsActivity";

    private CompactServer mCompactServer;
    private Server mServer;
    private ServerDetailsFragment mDetailsFragment;
    private String mServerID;
    private boolean mDataAvailable;

    public ServerDetailsActivity(){
        super(true, R.layout.servers_details_activity, R.id.server_details_fragment);
        mCompactServer = new CompactServer(this, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCompactServer.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prepare the Compact Server
        mCompactServer.onCreate(savedInstanceState);

        // Obtain Server
        mServerID = getIntent().getStringExtra(ARGS_SERVER_ID);
        mDetailsFragment = (ServerDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.server_details_fragment);

        initActivity();
    }


    private void initActivity() {

        if (!mDataAvailable || mDetailsFragment == null)
            return;

        mServer = mCompactServer.getServer(mServerID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mServer.getServerName());
        }


        mDetailsFragment.setServer(mServer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.server_details_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_see_server_information:
                ServerInformationFragment infoFragment = ServerInformationFragment.getInstance(mServer);
                infoFragment.show(getSupportFragmentManager(), ServerInformationFragment.TAG);
                break;

            case R.id.action_see_map_information:
                Log.d(TAG, "Opening GalleryDetailsActivity");
                openMapDetails(mServer.getMapName(), mServer.getGameMode(), mServer.getGameLayer());
                break;
        }

        // Default
        return super.onOptionsItemSelected(item);
    }

    private void openMapDetails(String mapName, String gameMode, int gameLayer) {
        startActivityForResult(GalleryDetailsActivity.newIntent(this, mapName, gameMode, gameLayer), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == GalleryDetailsActivity.LEVEL_NOT_FOUND_RESULT) {
            Log.e(TAG, "Level not found");
        }
    }

    public static Intent newIntent(Context context, Server server) {

        Intent intent = new Intent(context, ServerDetailsActivity.class);
        intent.putExtra(ARGS_SERVER_ID, server.getID());
        return intent;
    }

    @Override
    public void onDataAvailable() {
        mDataAvailable = true;
        initActivity();
    }

    @Override
    public void onRefreshRequest() {
        mCompactServer.refresh();
    }

    @Override
    public void onPlayerPressed(Player player) {
        mCompactServer.setPlayerFriendship(player, !player.isFriend());
    }
}
