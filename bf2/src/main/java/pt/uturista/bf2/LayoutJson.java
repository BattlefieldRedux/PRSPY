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

public class LayoutJson implements Serializable {
    public String Mode;
    public int Size;
    public Team[] Team;
    public Asset[] Assets;
    // public ControlPoint[] ControlPoints;
    // public CombatArea[] CombatAreas;

    public static class Team {
        public String Code;
        public String Name;
        public int Tickets;
    }

    public static class Asset {
        public String Key;
        public int InitialDelay;
        public int RespawnDelay;
        public int Team;
        public Vector3 Position;
        public Vector3 Rotation;
    }

    public static class Vector3 {
        public float X;
        public float Y;
        public float Z;
    }

    public static class Info {
        public String Key;
        public String Name;
        public String Icon;
    }
}
