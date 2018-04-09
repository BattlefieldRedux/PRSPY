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

package pt.uturista.prspy.view.friends;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import pt.uturista.prspy.R;
import pt.uturista.prspy.compact.CompactServer;
import pt.uturista.prspy.model.Player;
import pt.uturista.prspy.model.Server;
import pt.uturista.prspy.view.BaseNavigationActivity;
import pt.uturista.prspy.view.friends.fragment.FriendsFragment;

public class FriendsActivity extends BaseNavigationActivity implements FriendsFragment.Listener, CompactServer.Listener {
    private static final String FRIENDS_FRAGMENT = "FRIENDS_FRAGMENT";
    CompactServer mCompactServer;
    private FriendsFragment mFriendsFragment;
    private boolean mDataIsAvailable = false;

    public FriendsActivity(){
        super(true);
        mCompactServer = new CompactServer(this, this);
    }

    @Override
    protected int getDrawerId() {
        return R.id.nav_friends;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCompactServer.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();

        mCompactServer.onCreate(savedInstanceState);
        mFriendsFragment = (FriendsFragment) fragmentManager.findFragmentByTag(FRIENDS_FRAGMENT);

        // if there's no fragment already created, create one
        if(mFriendsFragment == null){
            // Create new fragment
            mFriendsFragment = new FriendsFragment();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content_frame, mFriendsFragment, FRIENDS_FRAGMENT);
            transaction.commit();
        }

        // onCreate might be called after or before data is available
        // if its called after, we can set the server straight away
        // else we'll set it on 'onDataAvailable'
        if(mDataIsAvailable){
            mFriendsFragment.setServers(mCompactServer.getServers());
        }
    }



    @Override
    public void onRefresh() {
        mCompactServer.refresh();
    }

    @Override
    public void onServerOpenRequest(Server server) {

    }

    @Override
    public void onPlayerUnfriend(Player player) {
        mCompactServer.setPlayerFriendship(player, false);
    }

    @Override
    public void onDataAvailable() {
        mDataIsAvailable = true;

        if(mFriendsFragment != null)
            mFriendsFragment.setServers( mCompactServer.getServers() );
    }
}
