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

package pt.uturista.prspy.view.servers.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import pt.uturista.prspy.R;
import pt.uturista.prspy.model.PRLibrary;
import pt.uturista.prspy.model.Server;

public class ServerInformationFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ServerInformationFrag";
    public static final String ARG_SERVER = "ARG_SERVER_ID";
    private Server mServer;

    public static ServerInformationFragment getInstance(Server server) {
        ServerInformationFragment fragment = new ServerInformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SERVER, server);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mServer = getArguments().getParcelable(ARG_SERVER);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.server_info_fragment, container);

        TextView serverName = (TextView) view.findViewById(R.id.server_name);
        serverName.setText(mServer.getServerName());

        TextView mapName = (TextView) view.findViewById(R.id.server_map_name);
        mapName.setText(mServer.getMapName());

        TextView gameMode = (TextView) view.findViewById(R.id.server_gamemode);
        gameMode.setText(PRLibrary.gameMode(mServer.getGameMode(), true));

        TextView gameLayout = (TextView) view.findViewById(R.id.server_gamelayout);
        gameLayout.setText(PRLibrary.gameLayer(mServer.getGameLayer(), true));

        TextView players = (TextView) view.findViewById(R.id.server_players);
        players.setText(String.format(Locale.US, "%d/%d (%d)", mServer.getNumPlayers(), mServer.getMaxPlayers(), mServer.getReservedSlots()));

        TextView country = (TextView) view.findViewById(R.id.server_country);
        country.setText(mServer.getCountry().NAME);

        TextView os = (TextView) view.findViewById(R.id.server_os);
        os.setText(mServer.getOS());

        TextView battleRecorder = (TextView) view.findViewById(R.id.server_battlerecorder);
        battleRecorder.setText(mServer.hasBattleRecorder() ? "Yes" : "No");

        TextView description = (TextView) view.findViewById(R.id.server_description);
        description.setText(mServer.getDescription());
        return view;
    }


}
