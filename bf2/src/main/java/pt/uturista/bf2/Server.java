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

package pt.uturista.bf2;

import java.io.Serializable;
import java.util.Map;

public class Server implements Serializable {
    public String IPAddress;
    public int QueryPort;
    public String Country;
    public String ServerName;
    public String GameName;
    public String GameVersion;
    public int GamePort;
    public String ModName;
    public String MapName;
    public String GameMode;
    public int MapSize;
    public int NumPlayers = -1;
    public int MaxPlayers;
    public int ReservedSlots;
    public String GameStatus;
    public Boolean Password;
    public int TimeLimit;
    public int NumRounds;
    public String OS;
    public Boolean Dedicated;
    public Boolean Ranked;
    public Boolean PunkBuster;
    public Boolean BattleRecorder;
    public String BRIndex;
    public String BRDownload;
    public Boolean Voip;
    public Boolean AutoBalance;
    public Boolean FriendlyFire;
    public String TKMode;
    public int StartDelay;
    public int SpawnTime;
    public String ServerText;
    public String ServerLogo;
    public String CommunityWebsite;
    public int ScoreLimit;
    public int TicketRatio;
    public int TeamRatio;
    public String Team1Name;
    public String Team2Name;
    public Boolean CoopEnabled;
    public Boolean Pure;
    public Boolean Unlocks;
    public int Fps;
    public Boolean Plasma;
    public int CoopBotRatio;
    public int CoopBotCount;
    public int CoopBotDifficulty;
    public Boolean NoVehicles;
    public Player[] Players;
}
