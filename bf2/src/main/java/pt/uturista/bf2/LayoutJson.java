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
